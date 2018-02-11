package com.werewolf.controllers;

import com.werewolf.services.JoinLobbyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Map;

@Controller
public class StompMessageController {
	
	@Autowired
	JoinLobbyService joinLobbyService;
	
	@MessageMapping("/broadcast/{gameid}")
	public void broadcastToLobby(@DestinationVariable String gameid, Map<String, String> message, Principal principal) {
	    String action = message.get("action");

	    if(action == null)
	        return;

		String username = principal.getName();

		switch(action) {
        case "lobbychat":
            joinLobbyService.sendChatMessage("lobbychat", username, message.get("info"));
            break;
        case "chat":
            joinLobbyService.sendChatMessage("chat", username, message.get("info"));
            break;
		case "leave":
			joinLobbyService.leave(username);
			break;
		case "ready":
			joinLobbyService.setReadyStatus(username, true);
			break;
		case "unready":
			joinLobbyService.setReadyStatus(username, false);
			break;
		case "vote":
			joinLobbyService.vote(username, message.get("playerid"), true);
			break;
		case "unvote":
			joinLobbyService.vote(username, message.get("playerid"), false);
			break;
		}
	}

	@MessageMapping("/private")
	public void replyToUser(Map<String, String> message, Principal principal) {
	    String action = message.get("action");
	    if(action == null)
	        return;

		String username = principal.getName();

		switch (action) {
		case "gamephase":
			joinLobbyService.getGamePhase(username);
			break;
		case "nightaction":
			joinLobbyService.nightAction(username, message.get("playerid"), true);
			break;
		case "unnightaction":
			joinLobbyService.nightAction(username, message.get("playerid"), false);
			break;
		case "getplayers":
			joinLobbyService.getPlayers(username);
			break;
		case "initializelobby":
			joinLobbyService.initializeLobby(username);
			break;
        case "initializeplayer":
            joinLobbyService.initializePlayer(username);
            break;
		case "getgamephase":
			joinLobbyService.getGamePhase(username);
			break;
		case "getopenlobbies":
			joinLobbyService.getOpenLobbies(username);
			break;
        case "getprofile":
            joinLobbyService.getProfile(username);
            break;
        case "joinlobby":
            joinLobbyService.join(username, message.get("gameid"), message.get("info"));
		    break;
        case "createlobby":
            joinLobbyService.join(username, message.get("gamemode"), message.get("privatelobby"), message.get("maxplayers"), message.get("nickname"));
            break;
		}
	}
}
