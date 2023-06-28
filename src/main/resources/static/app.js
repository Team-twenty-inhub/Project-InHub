let stompClient = null;
let fromId = 0;
let ChatMessageUl = null;

function getChatMessages() {
    console.log("fromId : " + fromId);
    fetch(`/rooms/${chatRoomId}/messages?fromId=${fromId}`, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json"
        }})
        .then(response => response.json())
        .then(body => {
            drawMessages(body);
        });
}

function drawMessages(messages) {
    if (messages.length > 0) {
        fromId = messages[messages.length - 1].message_id;
    }

    messages.forEach((message) => {

        const avatar = document.createElement("div");
        avatar.classList.add("chat-image");
        avatar.classList.add("avatar");

        const imageFrame = document.createElement("div");
        imageFrame.classList.add("w-10");
        imageFrame.classList.add("rounded-full");

        const image = document.createElement("img");
        image.src = message.sender.profile_img ? message.sender.profile_img : '/images/profile/no-image.png';

        avatar.appendChild(imageFrame).appendChild(image);

        const username = document.createElement("div");
        username.classList.add("chat-header");
        username.textContent = `${message.sender.username}`;

        const time = document.createElement("time");
        time.classList.add("text-xs");
        time.classList.add("opacity-50");
        time.classList.add("ml-1")
        const createdAt = new Date(message.created_at);
        const hours = createdAt.getHours();
        const minutes = createdAt.getMinutes();
        const formattedHours = hours < 10 ? `0${hours}` : hours;
        const formattedMinutes = minutes < 10 ? `0${minutes}` : minutes;
        time.textContent = `${formattedHours}:${formattedMinutes}`;

        username.appendChild(time);

        const content = document.createElement("div");
        content.classList.add("chat-bubble");
        content.textContent = `${message.content}`;

        const all = document.createElement("div");
        all.classList.add("chat");
        if (message.sender.username === myUsername) {
            all.classList.add("chat-end");
        } else {
            all.classList.add("chat-start");
        }
        all.appendChild(avatar);
        all.appendChild(username);
        all.appendChild(content);

        ChatMessageUl.appendChild(all);
    });
}

function ChatWriteMessage(form) {

    form.content.value = form.content.value.trim();

    stompClient.send(`/app/chats/${chatRoomId}/sendMessage`, {}, JSON.stringify({content: form.content.value}));

    form.content.value = '';
    form.content.focus();
}

function connect() {
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    const headers = {
        'X-CSRF-TOKEN': token,
    };

    stompClient.connect(headers, function (frame) {
        console.log('Connected: ' + frame);

        stompClient.subscribe(`/topic/chats/${chatRoomId}`, function (data) {
            getChatMessages();
        });
    });
}

document.addEventListener("DOMContentLoaded", function() {
    ChatMessageUl = document.querySelector('.chat__message-ul');
    getChatMessages();
    connect();
});