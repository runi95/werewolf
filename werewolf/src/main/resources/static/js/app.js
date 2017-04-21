var stompClient = null;
var playerlist = {}; // Initialize an empty object that will contain players
var gamecode = null;
var ready = false; // Player is not ready upon load

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
        console.log('Connected: ' + frame);
        stompClient.subscribe('/action/lobbymessages/' + gamecode, function (messageOutput) {
            receiveMessage(JSON.parse(messageOutput.body));
        });
        stompClient.subscribe('/user/action/joinlobby', function (messageOutput) {
            receiveMessage(JSON.parse(messageOutput.body));
        });
        setConnected(true);
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
    stompClient.send('/app/lobbymessages/' + gamecode, {}, JSON.stringify(message))
}
// This is the message that will be sent to the server
function sendPrivateMessage(message) {
    stompClient.send('/app/joinlobby/' + gamecode, {}, JSON.stringify(message));
}

function receiveMessage(message) {
    for(var i = 0; i < message.length; i++) {
            var action = message[i].action;

            switch(action) {
                case "leave":
                	removePlayer(message[i].playerid, message[i].nickname);
                    break;
                case "join":
                    addPlayer(message[i].playerid, message[i].nickname);
                    break;
                case "owner":
                	addPlayer(message[i].playerid, message[i].nickname);
                	document.getElementById(message[i].playerid).setAttribute("class", "list-group-item list-group-item-success");
                default:
                    break;
            }
    }
}

function addPlayer(playerid, nickname) {
	if (!playerlist.hasOwnProperty(playerid)) {
		playerlist[playerid] = nickname;
		var player = document.createElement("li");
    	var text = document.createTextNode(nickname);
    	player.appendChild(text);
    	player.setAttribute("id", playerid);
    	player.setAttribute("class", "list-group-item list-group-item-default");
    	document.getElementById("plist").appendChild(player);
	}
}

function removePlayer(playerid, nickname) {
	if(playerlist.hasOwnProperty(playerid)) {
		delete playerlist[playerid];
		document.getElementById(playerid).remove();
	}
}

function changeReadyState() {
	ready = !ready;
	var elem = document.getElementById("btnready");
	if(ready) {
		//sendPrivateMessage({"action":"ready"});
		elem.setAttribute("class", "btn btn-danger btn-block");
		elem.innerHTML = "Unready";
	} else {
		//sendPrivateMessage({"action":"unready"});
		elem.setAttribute("class", "btn btn-success btn-block");
		elem.innerHTML = "Ready";
	}
}

// This function runs on initialization
$.ajax({
    url: '/lobby/gamecoderequest',
    type: 'GET',
    datatype: 'json',
    success: function(data) { 
    	gamecode = data;
    	connect();
    }
});