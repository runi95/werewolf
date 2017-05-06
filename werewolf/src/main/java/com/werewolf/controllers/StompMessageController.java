package com.werewolf.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.werewolf.Messages.JoinLobbyMessage;
import com.werewolf.Messages.LobbyMessage;
import com.werewolf.services.JoinLobbyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.security.Principal;
import java.util.List;

@Controller
public class StompMessageController {
	
	@Autowired
	SimpMessagingTemplate simpTemplate;
	
	@Autowired
	JoinLobbyService joinLobbyService;
	
	@MessageMapping("/broadcast/{gameid}")
	public void send(@DestinationVariable String gameid, JoinLobbyMessage message, Principal principal) {
		String username = principal.getName();
		List<LobbyMessage> messageList = null;
		
		switch(message.getAction()) {
		case "leave":
			messageList = joinLobbyService.leave(username);
			break;
		case "join":
			messageList = joinLobbyService.join(username);
			break;
		case "ready":
			messageList = joinLobbyService.setReadyStatus(username, true);
			break;
		case "unready":
			messageList = joinLobbyService.setReadyStatus(username, true);
			break;
		case "vote":
			messageList = joinLobbyService.vote(username, message.getPlayerid(), true);
			break;
		case "unvote":
			messageList = joinLobbyService.vote(username, message.getPlayerid(), false);
			break;
		}

		if(messageList != null && !messageList.isEmpty())
			broadcastMessage(gameid, convertObjectToJson(messageList));
	}

	@MessageMapping("/private/{gameid}")
	public void reply(JoinLobbyMessage message, Principal principal) {
		String username = principal.getName();
		List<LobbyMessage> messageList = null;

		switch (message.getAction()) {
		case "getplayers":
			messageList = joinLobbyService.getPlayers(username);
			break;
		case "requestgame":
			messageList = joinLobbyService.gameRequest(username);	
			break;
		case "initializegame":
			messageList = joinLobbyService.initializeGame(username);
			break;
		}
		
		if(messageList != null && !messageList.isEmpty())
			privateMessage(principal.getName(), convertObjectToJson(messageList));
	}

	private void broadcastMessage(String gameid, String message) {
		simpTemplate.convertAndSend("/action/broadcast/" + gameid, message);
	}
	
	private void privateMessage(String user, String message) {
		simpTemplate.convertAndSendToUser(user, "/action/private", message);
	}
	
	public static String convertObjectToJson(Object message) {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

		String arrayToJson = null;
		try {
			arrayToJson = objectMapper.writeValueAsString(message);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return arrayToJson;
	}
}
