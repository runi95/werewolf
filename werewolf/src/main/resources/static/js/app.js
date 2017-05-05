var stompClient = null;
var playerlist = {}; // Initialize an empty object that will contain players
var deadplayers = {};
var aliveplayers= {};
var gamecode = null;
var ready = false; // Player is not ready upon load
var voted = null;
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
                    addPlayer(message[i].playerid, message[i].info);
                    break;
                case "joinalive":
                	addToVoteList(message[i].playerid, message[i].info, message[i].additionalinfo);
                	break;
                case "joindead":
                	addToGraveyard(message[i].playerid, message[i].info, message[i].additionalinfo, message[i].variable);
                	break;
                case "updatevotestatus":
                	someoneVoted(message[i].playerid, message[i].info, message[i].additionalinfo);
                	break;
                case "updatereadystatus":
                	someoneClickedReady(message[i].playerid, message[i].info, message[i].additionalinfo);
                	break;
                case "lynch":
                	lynchPlayer(message[i].playerid, message[i].info, message[i].additionalinfo, message[i].variable);
                	break;
                case "kill":
                	killPlayer(message[i].playerid, message[i].info, message[i].additionalinfo, message[i].variable);
                	break;
                case "owner":
                	owner = message[i].playerid;
                	addPlayer(message[i].playerid, message[i].info);
                	document.getElementById(message[i].playerid).setAttribute("class", "list-group-item list-group-item-success");
                	break;
                case "leave":
                	removePlayer(message[i].playerid, message[i].info);
                    break;
                case "gamerequestgranted":
                	loadGame();
                	break;
                case "gamerequestdenied":
                	// Something went wrong! Should request the real numbers then
                	break;
                default:
                    break;
            }
    }
}

function dayPhase() {
	
}

function lynchPlayer(playerid, playername, playerrole, alignment) {
	addToLog(nickname + " was lynched by an angry mob!");
	addToGraveyard(playerid, playername, playerrole, alignment);
}

function nightPhase() {
	
}

function killPlayer(playerid, playername, playerrole, alignment) {
	addToLog(nickname + " was murdered during the deepest, darkest hours of the night.");
	addToGraveyard(playerid, playername, playerrole, alignment);
}

function addToVoteList(playerid, playername, votes) {
	if (!aliveplayers.hasOwnProperty(playerid)) {
		if(deadplayers.hasOwnProperty(playerid)) {
			removeFromGraveyard(playerid);
		}
		
		aliveplayers[playerid] = {"name":playername, "votes":votes};
		var votelist = document.getElementById("votelist");
		var row = document.createElement("tr");
		var name = document.createElement("th");
		var vote = document.createElement("th");
		var votebtn = document.createElement("button");
		name.innerHTML = playername;
		name.setAttribute("class", "text-center");
		row.setAttribute("id", "v" + playerid);
		votebtn.innerHTML = "Vote(" + votes + ")";
		votebtn.setAttribute("id", "vb" + playerid);
		votebtn.setAttribute("class", "btn btn-info btn-block");
		votebtn.setAttribute("onclick", "voteon(" + playerid + ")");
		vote.appendChild(votebtn);
		row.appendChild(name);
		row.appendChild(vote);
		votelist.appendChild(row);
	}
}

function removeFromVoteList(playerid) {
	if (aliveplayers.hasOwnProperty(playerid)) {
		delete aliveplayers[playerid];
		document.getElementById("v" + playerid).remove();
	}
}

function addToLog(message) {
	var loglist = document.getElementById("loglist");
	var msg = document.createTextNode(message);
	loglist.appendChild(msg);
}

function addToGraveyard(playerid, playername, playerrole, alignment) {
	if (!deadplayers.hasOwnProperty(playerid)) {
		if(aliveplayers.hasOwnProperty(playerid)) {
			removeFromVoteList(playerid);
		}
		
		deadplayers[playerid] = {"name":playername, "role":playerrole};
		var gravelist = document.getElementById("gravelist");
		var row = document.createElement("tr");
		var name = document.createElement("th");
		var role = document.createElement("th");
		name.innerHTML = playername;
		row.setAttribute("id", "g" + playerid);
		role.innerHTML = playerrole;
    	switch(alignment) {
    	case "Good":
    		name.setAttribute("class", "text-center label-success");
    		role.setAttribute("class", "text-center label-success");
    		break;
    	case "ChaoticGood":
    		name.setAttribute("class", "text-center label-success");
			role.setAttribute("class", "text-center label-success");
    		break;
    	case "Evil":
    		name.setAttribute("class", "text-center label-danger");
			role.setAttribute("class", "text-center label-danger");
    		break;
    	case "ChaoticEvil":
    		name.setAttribute("class", "text-center label-danger");
			role.setAttribute("class", "text-center label-danger");
    		break;
    	case "Neutral":
    		name.setAttribute("class", "text-center");
			role.setAttribute("class", "text-center");
    		break;
    	case "NeutralEvil":
    		name.setAttribute("class", "text-center label-warning");
			role.setAttribute("class", "text-center label-warning");
    		break;
    	default:
    		name.setAttribute("class", "text-center");
			role.setAttribute("class", "text-center");
    		break;
    	}
    	row.appendChild(name);
    	row.appendChild(role);
    	gravelist.appendChild(row);
	}
}

function removeFromGraveyard(playerid) {
	if(deadplayers.hasOwnProperty(playerid)) {
		delete deadplayers[playerid];
		document.getElementById("g" + playerid).remove();
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
		elem.innerHTML = "Unready (" + readyplayercount + "/" + Math.max(lobbyplayercount, 4) + ")";
	} else {
		elem.innerHTML = "Ready (" + readyplayercount + "/" + Math.max(lobbyplayercount, 4) + ")";
	}
	
	if(readyplayercount === lobbyplayercount) {
		sendPrivateMessage({"action":"requestgame"}); //Ask server if everything is ready
	}
}

function loadLobby() {
	var lobbyref = document.getElementById("lobbyref");
	var lobbydiv = document.getElementById("lobbydiv");
	var optionsref = document.getElementById("optionsref");
	var optionsdiv = document.getElementById("optionsdiv");
	lobbyref.setAttribute("class", "breadcrumbs-text breadcrumbs-text-current");
	lobbydiv.setAttribute("class", "show");
	optionsref.setAttribute("class", "breadcrumbs-text");
	optionsdiv.setAttribute("class", "hide");
}

function loadOptions() {
	var lobbyref = document.getElementById("lobbyref");
	var lobbydiv = document.getElementById("lobbydiv");
	var optionsref = document.getElementById("optionsref");
	var optionsdiv = document.getElementById("optionsdiv");
	lobbyref.setAttribute("class", "breadcrumbs-text");
	lobbydiv.setAttribute("class", "hide");
	optionsref.setAttribute("class", "breadcrumbs-text breadcrumbs-text-current");
	optionsdiv.setAttribute("class", "show");
}

function loadGame() {
	var lobby = document.getElementById("lobby");
	var game = document.getElementById("game");
	lobby.setAttribute("class", "hide");
	game.setAttribute("class", "show");
	sendPrivateMessage({"action":"initializegame"});
}

function voteon(playerid) {
	voted = playerid;
	var votebtn = document.getElementById("vb" + playerid).disabled = true;
	broadcastMessage({"action":"vote", "playerid":playerid});
}

function someoneVoted(playerid, votedon, votes) {
	var elem = document.getElementById("vb" + playerid);
	if(playerid === owner) {
		elem.disabled = false;
		if(voted != null) {
			elem.setAttribute("class", "btn btn-default btn-block");
		} else {
			elem.setAttribute("class", "btn btn-info btn-block");
		}
		addToLog("You voted on " + votedon);
	} else {
		addToLog(playerid + " has voted on " + votedon);
	}
	if(votedon == voted) {
		elem.innerHTML = "Remove Vote(" + votes + ")";
	} else {
		elem.innerHTML = "Vote(" + votes + ")";
	}
	
	//if(readyplayercount === lobbyplayercount) {
		//sendPrivateMessage({"action":"requestgame"}); //Ask server if everything is ready
	//}
}

function loadVote() {
	var voteref = document.getElementById("voteref");
	var votediv = document.getElementById("votediv");
	var logref = document.getElementById("logref");
	var logdiv = document.getElementById("logdiv");
	var graveref = document.getElementById("graveref");
	var gravediv = document.getElementById("gravediv");
	voteref.setAttribute("class", "breadcrumbs-text breadcrumbs-text-current");
	votediv.setAttribute("class", "show");
	logref.setAttribute("class", "breadcrumbs-text");
	logdiv.setAttribute("class", "hide");
	graveref.setAttribute("class", "breadcrumbs-text");
	gravediv.setAttribute("class", "hide");
}

function loadLog() {
	var voteref = document.getElementById("voteref");
	var votediv = document.getElementById("votediv");
	var logref = document.getElementById("logref");
	var logdiv = document.getElementById("logdiv");
	var graveref = document.getElementById("graveref");
	var gravediv = document.getElementById("gravediv");
	voteref.setAttribute("class", "breadcrumbs-text");
	votediv.setAttribute("class", "hide");
	logref.setAttribute("class", "breadcrumbs-text breadcrumbs-text-current");
	logdiv.setAttribute("class", "show");
	graveref.setAttribute("class", "breadcrumbs-text");
	gravediv.setAttribute("class", "hide");
}

function loadGraveyard() {
	var voteref = document.getElementById("voteref");
	var votediv = document.getElementById("votediv");
	var logref = document.getElementById("logref");
	var logdiv = document.getElementById("logdiv");
	var graveref = document.getElementById("graveref");
	var gravediv = document.getElementById("gravediv");
	logref.setAttribute("class", "breadcrumbs-text");
	logdiv.setAttribute("class", "hide");
	voteref.setAttribute("class", "breadcrumbs-text");
	votediv.setAttribute("class", "hide");
	graveref.setAttribute("class", "breadcrumbs-text breadcrumbs-text-current");
	gravediv.setAttribute("class", "show");
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