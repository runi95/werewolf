var stompClient = null;
var playerlist = {}; // Initialize an empty object that will contain players
var gamecode = null;
var ready = false; // Player is not ready upon load
var owner = null; // ID of the user

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
        stompClient.subscribe('/action/broadcast/' + gamecode, function (messageOutput) {
            receiveMessage(JSON.parse(messageOutput.body));
        });
        stompClient.subscribe('/user/action/private', function (messageOutput) {
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
    stompClient.send('/app/broadcast/' + gamecode, {}, JSON.stringify(message));
}
// This is the message that will be sent to the server
function sendPrivateMessage(message) {
    stompClient.send('/app/private/' + gamecode, {}, JSON.stringify(message));
}

function receiveMessage(message) {
    for(var i = 0; i < message.length; i++) {
            var action = message[i].action;
            
            switch(action) {
                case "join":
                    addPlayer(message[i].playerid, message[i].nickname);
                    break;
                case "updatereadystatus":
                	someoneClickedReady(message[i].playerid, message[i].readyplayercount, message[i].lobbyplayercount);
                	break;
                case "owner":
                	owner = message[i].playerid;
                	addPlayer(message[i].playerid, message[i].nickname);
                	document.getElementById(message[i].playerid).setAttribute("class", "list-group-item list-group-item-success");
                	break;
                case "leave":
                	removePlayer(message[i].playerid, message[i].nickname);
                    break;
                case "gamerequestgranted":
                	window.location.href = "/game/" + gamecode;
                	break;
                case "gamerequestdenied":
                	// Something went wrong! Should request the real numbers then
                	break;
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
	document.getElementById("btnready").disabled = true;
	if(ready) {
		broadcastMessage({"action":"ready"});
	}else{
		broadcastMessage({"action":"unready"});
	}
}

function someoneClickedReady(playerid, readyplayercount, lobbyplayercount) {
	var elem = document.getElementById("btnready");
	if(playerid === owner) {
		elem.disabled = false;
		if(ready) {
			elem.setAttribute("class", "btn btn-danger btn-block");
		} else {
			elem.setAttribute("class", "btn btn-success btn-block");
		}
	}
	if(ready) {
		elem.innerHTML = "Unready (" + readyplayercount + "/" + Math.max(lobbyplayercount, 3) + ")";
	} else {
		elem.innerHTML = "Ready (" + readyplayercount + "/" + Math.max(lobbyplayercount, 3) + ")";
	}
	
	if(readyplayercount === lobbyplayercount) {
		sendPrivateMessage({"action":"requestgame"}); //Ask server if everything is ready
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