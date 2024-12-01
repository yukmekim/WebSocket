let socket;

function connect() {
    socket = new WebSocket('ws://localhost:8090/chat');

    socket.onopen = function() {
        console.log('WebSocket connection opened');

        // WebSocket에 username을 저장 (optional)
        socket.send(JSON.stringify({
            type: 'JOIN',
            memberId: document.getElementById("username").value,
            content: document.getElementById("username").value + ' has joined the chat',
            roomId: 'room1'
        }));
    };

    socket.onmessage = function(event) {
        const chatContainer = document.getElementById('chat');

        let message = JSON.parse(event.data);
        if (message.type === 'INFO') {
            // JOIN 메시지 처리
            alert(message.content);
        } else {
            // 일반 채팅 메시지 처리
            createMessageCon(chatContainer, message);
        }
    };

    socket.onerror = function(error) {
        console.log('WebSocket error: ' + error);
    };

    socket.onclose = function() {
        console.log('WebSocket connection closed');
    };

}

function sendMessage() {
    let message = document.getElementById('message').value;
    socket.send(JSON.stringify({
        type: 'MESSAGE',
        sender: document.getElementById("username").value,
        content: message,
        roomId: 'room1'
    }));

    document.getElementById('message').value = '';
    document.getElementById('send-btn').disabled = true; // 전송 버튼 비활성화
}

function closeConnection() {
    let leaveUser = document.getElementById("username").value;

    // 서버에 퇴장 메시지 전송
    socket.send(JSON.stringify({
        type: 'LEAVE',
        sender: leaveUser,
        content: leaveUser + ' 님이 나갔습니다.',
        roomId: 'room1'
    }));

    // 서버로 퇴장 메시지를 전송한 후 연결 종료
    socket.close();
}


// 내가 보낸 메세지에는 보낸사람 이름이 필요없음
// 같은 사람이 여러 메세지를 보냈을때 보낸사람 이름을 한 번만 출력
function createMessageCon(el, message) {
    const div = document.createElement('div');
    div.classList.add('chat-wrap');

    // 일반 채팅 메시지 처리
    const p = document.createElement('p');
    p.textContent = message.content;
    if (message.type === 'JOIN' || message.type === 'LEAVE') {
        p.classList.add('middle');
        div.append(p);
        el.append(div);
    } else {
        div.dataset.username = message.sender;

        // 메세지 수신자 구분
        if (document.getElementById("username").value !== message.sender) {
            p.classList.add('left');

            // 발신자가 연속해서 메세지를 보냈을 경우 프로필을 한 번만 출력
            if (el.lastChild.dataset.username !== message.sender) {
                // 발신자 사람 프로필
                const span = document.createElement('span');
                span.textContent = message.sender;
                span.classList.add('sender');

                div.append(span);
                div.append(p);
                el.append(div);
            } else {
                el.lastChild.append(p);
            }
        } else {
            p.classList.add('right');
            // 본인이 연속해서 메세지를 보냈을 경우 같은 메세지 그룹에 채팅을 추가
            if (el.lastChild.dataset.username !== message.sender) {
                div.append(p);
                el.append(div);
            } else {
                el.lastChild.append(p);
            }
        }
    }
}