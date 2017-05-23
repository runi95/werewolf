var stompClient = null;
navigator.vibrate = navigator.vibrate || navigator.webkitVibrate || navigator.mozVibrate || navigator.msVibrate;
var playerlist = {}; // Initialize an empty object that will contain players
var deadplayers = {};
var aliveplayers = {};
var invalidtargets = {};
var gamecode = null;
var ready = false; // Player is not ready upon load
var voted = null;
var nightact = null;
var owner = null; // ID of the user
var phase = null;
var alive = true;

function setConnected(connected) {
    if(connected) {
    	sendPrivateMessage({"action":"getplayers"});
        sendPrivateMessage({"action":"initializelobby"});
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
            receiveBroadcastMessage(JSON.parse(messageOutput.body));
        });
        stompClient.subscribe('/user/action/private', function (messageOutput) {
            receivePrivateMessage(JSON.parse(messageOutput.body));
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

function receiveBroadcastMessage(message) {
    for(var i = 0; i < message.length; i++) {
            var action = message[i].action;
            
            switch(action) {
            	case "waitphase":
            		waitPhase();
            	break;
            	case "dayphase":
            		dayPhase();
            		break;
            	case "nightphase":
            		nightPhase();
            		break;
                case "join":
                    addPlayer(message[i].playerid, message[i].info);
                    break;
                case "updatevotestatus":
                	someoneVoted(message[i].playerid, message[i].info, message[i].additionalinfo, message[i].variable);
                	break;
                case "updatereadystatus":
                	someoneClickedReady(message[i].playerid, message[i].info, message[i].additionalinfo);
                	break;
                case "kinglynch":
                	lynchPlayer(message[i].playerid, message[i].info, message[i].additionalinfo, message[i].variable, true);
                	break;
                case "lynch":
                	lynchPlayer(message[i].playerid, message[i].info, message[i].additionalinfo, message[i].variable, false);
                	break;
                case "kill":
                	killPlayer(message[i].playerid, message[i].info, message[i].additionalinfo, message[i].variable, false);
                	break;
                case "jesterkill":
                	killPlayer(message[i].playerid, message[i].info, message[i].additionalinfo, message[i].variable, true);
                	break;
                case "leave":
                	removePlayer(message[i].playerid, message[i].info);
                    break;
                case "lobbyready":
                	loadGame();
                	break;
                default:
                    break;
            }
    }
}

function receivePrivateMessage(message) {
    for(var i = 0; i < message.length; i++) {
            var action = message[i].action;
            
            switch(action) {
            	case "nightaction":
            		updateNightAction(message[i].info, true);
            		break;
            	case "unnightaction":
            		updateNightAction(message[i].info, false);
            		break;
            	case "nightmessage":
                 	addToLog(message[i].info);
                 	break;
                case "join":
                    addPlayer(message[i].playerid, message[i].info);
                    break;
                case "addinvalidtarget":
                	addToInvalidTargets(message[i].playerid, message[i].info, message[i].additionalinfo);
                	break;
                case "joinalive":
                	addToActionList(message[i].playerid, message[i].info, message[i].additionalinfo);
                	break;
                case "joindead":
                	addToGraveyard(message[i].playerid, message[i].info, message[i].additionalinfo, message[i].variable);
                	break;
                case "lobby":
                	prepareLobby();
                	break;
                case "lobbyready":
                	loadGame();
                	break;
                case "owner":
                	owner = message[i].playerid;
                	addPlayer(message[i].playerid, message[i].info);
                	document.getElementById(message[i].playerid).setAttribute("class", "list-group-item list-group-item-success");
                	break;
                case "won":
                	won();
                	break;
                case "lost":
                	lost();
                	break;
                case "role":
                	setRole(message[i].playerid, message[i].info, message[i].additionalinfo, message[i].variable);
                	break;
                default:
                    break;
            }
    }
}

function prepareLobby() {
	var lobby = document.getElementById("lobby");
	lobby.setAttribute("class", "show");
	broadcastMessage({"action":"join"});
}

function addToInvalidTargets(playerid, rolename, alignment) {
	if (!invalidtargets.hasOwnProperty(playerid)) {
		invalidtargets[playerid] = {"role":rolename, "alignment":alignment};
	}
}

function waitPhase() {
	phase = "wait";
	loadNoAction();
}

function dayPhase() {
	phase = "day";
	if ("vibrate" in navigator  && alive) {
		navigator.vibrate(500);
	}
	
	var elem = document.getElementById("dayphase");
	elem.setAttribute("class", "phase phase-in");
	phaseout(elem, 2);
	loadDayAction();
}

function lynchPlayer(playerid, playername, playerrole, alignment, kinged) {
	if(kinged) {
		addToLog(playerlist[playerid] + " executed by the king himself!");
	} else {
		addToLog(playerlist[playerid] + " was lynched by an angry mob!");
	}
	addToGraveyard(playerid, playername, playerrole, alignment);
}

function dead() {
	alive = false;
	
	if ("vibrate" in navigator) {
		navigator.vibrate(500);
	}
	
	var elem = document.getElementById("deadphase");
	elem.setAttribute("class", "phase phase-in");
	phaseout(elem, 2);
}

function won() {
	var elem = document.getElementById("wonphase");
	elem.setAttribute("class", "phase phase-in");
}

function lost() {
	var elem = document.getElementById("lostphase");
	elem.setAttribute("class", "phase phase-in");
}

function nightPhase() {
	phase = "night";
	if ("vibrate" in navigator && alive) {
		navigator.vibrate(500);
	}
	
	var elem = document.getElementById("nightphase");
	elem.setAttribute("class", "phase phase-in");
	phaseout(elem, 2);
	loadNightAction();
}

function phaseout(elem, delay) {
	setTimeout(function() {elem.setAttribute("class", "phase phase-out"); }, delay*1000);
}

function killPlayer(playerid, playername, playerrole, alignment, jester) {
	if(playerid == owner)
		dead();
	
	if(jester) {
		addToLog(playername + " was haunted to death by a jester.");
	} else {
		addToLog(playername + " was murdered during the deepest, darkest hours of the night.");
	}
	addToGraveyard(playerid, playername, playerrole, alignment);
}

function addToActionList(playerid, playername, votes) {
	if (!aliveplayers.hasOwnProperty(playerid)) {
		if(deadplayers.hasOwnProperty(playerid)) {
			removeFromGraveyard(playerid);
		}
		
		aliveplayers[playerid] = {"name":playername, "votes":votes};
		
		var votelist = document.getElementById("votelist");
		var voterow = document.createElement("tr");
		var votename = document.createElement("th");
		var vote = document.createElement("th");
		var votebtn;
		votename.innerHTML = playername;
		votename.setAttribute("class", "text-center");
		voterow.setAttribute("id", "av" + playerid);
		if(playerid == owner) {
			votebtn = document.createElement("b");
			votebtn.innerHTML = votes;
			votebtn.setAttribute("id", "vb" + playerid);
			votebtn.setAttribute("class", "text-center center-block");
		} else {
			votebtn = document.createElement("button");
			votebtn.innerHTML = "Vote(" + votes + ")";
			votebtn.setAttribute("id", "vb" + playerid);
			votebtn.setAttribute("class", "btn btn-info btn-block");
			votebtn.setAttribute("onclick", "voteon(" + playerid + ")");
		}
		vote.appendChild(votebtn);
		voterow.appendChild(votename);
		voterow.appendChild(vote);
		votelist.appendChild(voterow);
		
		var nightlist = document.getElementById("nightactionlist");
		var nightrow = document.createElement("tr");
		var nightname = document.createElement("th");
		var night = document.createElement("th");
		var nightbtn;
		nightname.innerHTML = playername;
		nightname.setAttribute("class", "text-center");
		nightrow.setAttribute("id", "an" + playerid);
		if(playerid != owner) {
			if (!invalidtargets.hasOwnProperty(playerid)) {
				nightbtn = document.createElement("button");
				nightbtn.setAttribute("id", "ab" + playerid);
				nightbtn.setAttribute("class", "btn btn-night btn-nightact btn-block");
				nightbtn.setAttribute("onclick", "nightaction(" + playerid + ")");
				night.appendChild(nightbtn);
			} else {
				nightbtn = document.createElement("b");
				nightbtn.setAttribute("id", "ab" + playerid);
				nightbtn.setAttribute("class", "text-center center-block text-info");
				nightbtn.innerHTML = "Ally";
				night.appendChild(nightbtn);
			}
		} else {
			nightbtn = document.createElement("b");
			nightbtn.setAttribute("id", "ab" + playerid);
			nightbtn.setAttribute("class", "text-center center-block text-success");
			nightbtn.innerHTML = "Yourself";
			night.appendChild(nightbtn);
		}
		nightrow.appendChild(nightname);
		nightrow.appendChild(night);
		nightlist.appendChild(nightrow);
	}
}

function removeFromActionList(playerid) {
	if (aliveplayers.hasOwnProperty(playerid)) {
		delete aliveplayers[playerid];
		document.getElementById("av" + playerid).remove();
		document.getElementById("an" + playerid).remove();
	}
}

function addToLog(message) {
	var loglist = document.getElementById("loglist");
	var msg = document.createElement("div");
	msg.innerHTML = message;
	loglist.appendChild(msg);
}

function addToGraveyard(playerid, playername, playerrole, alignment) {
	if (!deadplayers.hasOwnProperty(playerid)) {
		if(aliveplayers.hasOwnProperty(playerid)) {
			removeFromActionList(playerid);
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
    	case "Chaotic Good":
    		name.setAttribute("class", "text-center label-success");
			role.setAttribute("class", "text-center label-success");
    		break;
    	case "Evil":
    		name.setAttribute("class", "text-center label-danger");
			role.setAttribute("class", "text-center label-danger");
    		break;
    	case "Chaotic Evil":
    		name.setAttribute("class", "text-center label-danger");
			role.setAttribute("class", "text-center label-danger");
    		break;
    	case "Neutral":
    		name.setAttribute("class", "text-center");
			role.setAttribute("class", "text-center");
    		break;
    	case "Neutral Evil":
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
}

function loadLobby() {
	var lobbyref = document.getElementById("lobbyref");
	var lobbydiv = document.getElementById("lobbydiv");
	var optionsref = document.getElementById("optionsref");
	var optionsdiv = document.getElementById("optionsdiv");
	lobbyref.setAttribute("class", "active");
	lobbydiv.setAttribute("class", "show");
	optionsref.setAttribute("class", "");
	optionsdiv.setAttribute("class", "hide");
}

function loadOptions() {
	var lobbyref = document.getElementById("lobbyref");
	var lobbydiv = document.getElementById("lobbydiv");
	var optionsref = document.getElementById("optionsref");
	var optionsdiv = document.getElementById("optionsdiv");
	lobbyref.setAttribute("class", "");
	lobbydiv.setAttribute("class", "hide");
	optionsref.setAttribute("class", "active");
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
	var votebtn = document.getElementById("vb" + playerid).disabled = true;
	
	if(voted == playerid) {
		broadcastMessage({"action":"unvote", "playerid":playerid});
		voted = null;
	} else {
		broadcastMessage({"action":"vote", "playerid":playerid});
	}
}

function nightaction(playerid) {
	var nightbtn = document.getElementById("ab" + playerid).disabled = true;
	
	if(nightact == playerid) {
		sendPrivateMessage({"action":"unnightaction", "playerid":playerid});
	} else {
		sendPrivateMessage({"action":"nightaction", "playerid":playerid});
	}
}

function updateNightAction(target, act) {
	var elem = document.getElementById("ab" + target);
	elem.disabled = false;
	
	if(act) {
		nightact = target;
		elem.setAttribute("class", "btn btn-night btn-default");
	} else {
		nightact = null;
		elem.setAttribute("class", "btn btn-night btn-nightact");
	}
}

function someoneVoted(playerid, votedon, votes, status) {
	if(aliveplayers.hasOwnProperty(playerid)) {
	var elem = document.getElementById("vb" + votedon);
	
	if(playerid === owner) {
		elem.disabled = false;
		
		if(status === "+") { // Means they voted on this player
			voted = votedon;
			elem.setAttribute("class", "btn btn-default btn-block");
			addToLog("You voted on " + playerlist[votedon]);
		} else if(status === "-") { // Means they removed their vote from this player			
			elem.setAttribute("class", "btn btn-info btn-block");
			addToLog("You removed your vote from " + playerlist[votedon]);
		} else if(status === "x") {
			voted = null;
			elem.setAttribute("class", "btn btn-info btn-block");
		}
	} else {
		if(status === "+") {
			addToLog(playerlist[playerid] + " has voted on " + playerlist[votedon]);
		} else if(status === "-") {
			addToLog(playerlist[playerid] + " has removed thier vote from " + playerlist[votedon]);
		}
	}
	
	if (votedon == owner) {
		elem.innerHTML = votes;
	} else if(votedon == voted) {
		elem.innerHTML = "Remove Vote(" + votes + ")";
	} else {
		elem.innerHTML = "Vote(" + votes + ")";
	}
	}
}

// function 

function loadAction() {
	if(phase === "night") {
		loadNightAction();
	}else if(phase === "day") {
		loadDayAction();
	}else {
		loadNoAction();
	}
	
	var actionref = document.getElementById("actionref");
	var actiondiv = document.getElementById("actiondiv");
	var logref = document.getElementById("logref");
	var logdiv = document.getElementById("logdiv");
	var graveref = document.getElementById("graveref");
	var gravediv = document.getElementById("gravediv");
	var roleref = document.getElementById("roleref");
	var rolediv = document.getElementById("rolediv");
	actionref.setAttribute("class", "breadcrumbs-text breadcrumbs-text-current");
	actiondiv.setAttribute("class", "show");
	logref.setAttribute("class", "breadcrumbs-text");
	logdiv.setAttribute("class", "hide");
	graveref.setAttribute("class", "breadcrumbs-text");
	gravediv.setAttribute("class", "hide");
	roleref.setAttribute("class", "breadcrumbs-text");
	rolediv.setAttribute("class", "hide");
}

function loadNightAction() {
	var nightdiv = document.getElementById("nightactiondiv");
	var daydiv = document.getElementById("dayactiondiv");
	var nodiv = document.getElementById("noactiondiv");
	nightdiv.setAttribute("class", "show");
	daydiv.setAttribute("class", "hide");
	nodiv.setAttribute("class", "hide");
}

function loadDayAction() {
	var nightdiv = document.getElementById("nightactiondiv");
	var daydiv = document.getElementById("dayactiondiv");
	var nodiv = document.getElementById("noactiondiv");
	nightdiv.setAttribute("class", "hide");
	daydiv.setAttribute("class", "show");
	nodiv.setAttribute("class", "hide");
}

function loadNoAction() {
	var nightdiv = document.getElementById("nightactiondiv");
	var daydiv = document.getElementById("dayactiondiv");
	var nodiv = document.getElementById("noactiondiv");
	nightdiv.setAttribute("class", "hide");
	daydiv.setAttribute("class", "hide");
	nodiv.setAttribute("class", "show");
}

function loadLog() {
	var actionref = document.getElementById("actionref");
	var actiondiv = document.getElementById("actiondiv");
	var logref = document.getElementById("logref");
	var logdiv = document.getElementById("logdiv");
	var graveref = document.getElementById("graveref");
	var gravediv = document.getElementById("gravediv");
	var roleref = document.getElementById("roleref");
	var rolediv = document.getElementById("rolediv");
	actionref.setAttribute("class", "breadcrumbs-text");
	actiondiv.setAttribute("class", "hide");
	logref.setAttribute("class", "breadcrumbs-text breadcrumbs-text-current");
	logdiv.setAttribute("class", "show");
	graveref.setAttribute("class", "breadcrumbs-text");
	gravediv.setAttribute("class", "hide");
	roleref.setAttribute("class", "breadcrumbs-text");
	rolediv.setAttribute("class", "hide");
}

function loadGraveyard() {
	var actionref = document.getElementById("actionref");
	var actiondiv = document.getElementById("actiondiv");
	var logref = document.getElementById("logref");
	var logdiv = document.getElementById("logdiv");
	var graveref = document.getElementById("graveref");
	var gravediv = document.getElementById("gravediv");
	var roleref = document.getElementById("roleref");
	var rolediv = document.getElementById("rolediv");
	actionref.setAttribute("class", "breadcrumbs-text");
	actiondiv.setAttribute("class", "hide");
	logref.setAttribute("class", "breadcrumbs-text");
	logdiv.setAttribute("class", "hide");
	graveref.setAttribute("class", "breadcrumbs-text breadcrumbs-text-current");
	gravediv.setAttribute("class", "show");
	roleref.setAttribute("class", "breadcrumbs-text");
	rolediv.setAttribute("class", "hide");
}

function loadRole() {
	var actionref = document.getElementById("actionref");
	var actiondiv = document.getElementById("actiondiv");
	var logref = document.getElementById("logref");
	var logdiv = document.getElementById("logdiv");
	var graveref = document.getElementById("graveref");
	var gravediv = document.getElementById("gravediv");
	var roleref = document.getElementById("roleref");
	var rolediv = document.getElementById("rolediv");
	actionref.setAttribute("class", "breadcrumbs-text");
	actiondiv.setAttribute("class", "hide");
	logref.setAttribute("class", "breadcrumbs-text");
	logdiv.setAttribute("class", "hide");
	graveref.setAttribute("class", "breadcrumbs-text");
	gravediv.setAttribute("class", "hide");
	roleref.setAttribute("class", "breadcrumbs-text breadcrumbs-text-current");
	rolediv.setAttribute("class", "show");
}

function setRole(name, alignment, goal, description) {
	var rolename = document.getElementById("rolename");
	rolename.innerHTML = name;
	
	var roletable = document.getElementById("roletable");
	var rolelist = null;
	
	rolelist = document.getElementById("rolelist");
	if(rolelist != null) {
		rolelist.remove();
	}
	
	rolelist = document.createElement("tbody");
	rolelist.setAttribute("id", "rolelist");
	
	var alignmentrow = document.createElement("tr");
	var goalrow = document.createElement("tr");
	var descriptionrow = document.createElement("tr");
	
	var alignmentcol = document.createElement("th");
	var alignmentbold = document.createElement("b");
	var alignmentcontent = document.createElement("th");
	alignmentbold.innerHTML = "Alignment";
	alignmentcontent.innerHTML = alignment;
	
	var goalcol = document.createElement("th");
	var goalbold = document.createElement("b");
	var goalcontent = document.createElement("th");
	goalbold.innerHTML = "Goal";
	goalcontent.innerHTML = goal;
	
	var descriptioncol = document.createElement("th");
	var descriptionbold = document.createElement("b");
	var descriptioncontent = document.createElement("th");
	descriptionbold.innerHTML = "Description";
	descriptioncontent.innerHTML = description;
	
	switch(alignment) {
	case "Good":
		alignmentcontent.setAttribute("class", "text-success");
		break;
	case "Chaotic Good":
		alignmentcontent.setAttribute("class", "text-success");
		break;
	case "Evil":
		alignmentcontent.setAttribute("class", "text-danger");
		break;
	case "Chaotic Evil":
		alignmentcontent.setAttribute("class", "text-danger");
		break;
	case "Neutral Evil":
		alignmentcontent.setAttribute("class", "text-warning");
		break;
	case "Neutral":
	default:
		break;
	}
	
	alignmentcol.appendChild(alignmentbold);
	alignmentrow.appendChild(alignmentcol);
	alignmentrow.appendChild(alignmentcontent);
	goalcol.appendChild(goalbold);
	goalrow.appendChild(goalcol);
	goalrow.appendChild(goalcontent);
	descriptioncol.appendChild(descriptionbold);
	descriptionrow.appendChild(descriptioncol);
	descriptionrow.appendChild(descriptioncontent);
	
	rolelist.appendChild(alignmentrow);
	rolelist.appendChild(goalrow);
	rolelist.appendChild(descriptionrow);
	
	roletable.appendChild(rolelist);
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