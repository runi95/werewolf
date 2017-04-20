var stompClient = null;
var gamecode = undefined;

function setConnected(connected) {
    if(connected) {
        broadcastMessage({"action":"join"});
        sendPrivateMessage({"action":"getplayers"});
    } else {
        broadcastMessage({"action":"leave"});
    }
}

// Runs when client connects to messagebroker
function connect() {
    var socket = new SockJS('/werewolf-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/action/lobbymessages/' + gamecode, function (messageOutput) {
            receiveMessage(JSON.parse(messageOutput.body));
        });
        stompClient.subscribe('/action/joinlobby/' + gamecode, function (messageOutput) {
            receiveMessage(JSON.parse(messageOutput.body));
        });
        sendMessage();
    });
}

// Runs when client disconnects from messagebroker
function disconnect() {
    if (stompClient != null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function broadcastMessage(message) {
    stompClient.send('/app/lobbymessages/', {}, JSON.stringify(message))
}
// This is the message that will be sent to the server
function sendPrivateMessage(message) {
    stompClient.send('/app/joinlobby/', {}, JSON.stringify(message));
}

function receiveMessage(message) {
    for(var i = 0; i < message.length; i++) {
            var action = message[i].action;

            switch(action) {
                case "leave":
                    document.getElementById(message[i].nickname).remove();
                    break;
                case "join":
                    var player = document.createElement("p");
                    var text = document.createTextNode(message[i].nickname);
                    player.appendChild(text);
                    player.setAttribute("id", message[i].nickname);
                    document.getElementById("plist").appendChild(player);
                    break;
                default:
                    break;
            }
    }
}

// This function runs on initialization
$.ajax({
    url: '/lobby/gamecoderequest',
    type: 'GET',
    datatype: 'json',
    success: function(data) { gamecode = data; connect();}
});