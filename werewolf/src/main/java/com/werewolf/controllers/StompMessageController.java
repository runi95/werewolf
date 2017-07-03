package com.werewolf.controllers;

import com.werewolf.Messages.JoinLobbyMessage;
import com.werewolf.services.JoinLobbyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;

@Controller
public class StompMessageController {
	
	@Autowired
	JoinLobbyService joinLobbyService;
	
	@MessageMapping("/broadcast/{gameid}")
	public void broadcastToLobby(@DestinationVariable String gameid, JoinLobbyMessage message, Principal principal) {
		String username = principal.getName();

		switch(message.getAction()) {
        case "lobbychat":
            joinLobbyService.sendChatMessage("lobbychat", username, message.getInfo());
            break;
        case "chat":
            joinLobbyService.sendChatMessage("chat", username, message.getInfo());
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
			joinLobbyService.vote(username, message.getPlayerid(), true);
			break;
		case "unvote":
			joinLobbyService.vote(username, message.getPlayerid(), false);
			break;
		}
	}

	@MessageMapping("/private")
	public void replyToUser(JoinLobbyMessage message, Principal principal) {
		String username = principal.getName();

		switch (message.getAction()) {
		case "gamephase":
			joinLobbyService.getGamePhase(username);
			break;
		case "nightaction":
			joinLobbyService.nightAction(username, message.getPlayerid(), true);
			break;
		case "unnightaction":
			joinLobbyService.nightAction(username, message.getPlayerid(), false);
			break;
		case "getplayers":
			joinLobbyService.getPlayers(username);
			break;
		case "initializelobby":
			joinLobbyService.initializeLobby(username);
			break;	
		case "initializegame":
			joinLobbyService.initializeGame(username);
			break;
		case "getgamephase":
			joinLobbyService.getGamePhase(username);
			break;
		case "getopenlobbies":
			joinLobbyService.getOpenLobbies(username);
			break;
		}
	}
}
