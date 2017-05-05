package com.werewolf.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.werewolf.Messages.JoinLobbyMessage;
import com.werewolf.Messages.LobbyMessage;
import com.werewolf.entities.LobbyEntity;
import com.werewolf.entities.LobbyPlayer;
import com.werewolf.entities.User;
import com.werewolf.services.AccountService;
import com.werewolf.services.JoinLobbyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class StompMessageController {
	
	@Autowired
	SimpMessagingTemplate simpTemplate;
	
	@Autowired
	AccountService accountService;

	@Autowired
	JoinLobbyService joinLobbyService;
	
	@MessageMapping("/broadcast/{gameid}")
	@SendTo("/action/broadcast/{gameid}")
	public String send(@DestinationVariable String gameid, JoinLobbyMessage message, Principal principal) {
		LobbyPlayer lobbyPlayer = getPlayerFromPrincipal(principal);
		List<LobbyMessage> messageList = new ArrayList<>();
		int readyPlayerCount;
		int playerCount;
		int votes;
		
		switch(message.getAction()) {
		case "leave":
			joinLobbyService.leave(lobbyPlayer);
		case "join":
			messageList.add(new LobbyMessage(message.getAction(), lobbyPlayer.getId(), lobbyPlayer.getNickname()));
			break;
		case "ready":
			readyPlayerCount = joinLobbyService.setReadyStatus(lobbyPlayer, true);
			playerCount = joinLobbyService.getPlayerCount(lobbyPlayer);
			if(readyPlayerCount == playerCount) {
				joinLobbyService.loadGame(lobbyPlayer.getLobby());
			}
			messageList.add(new LobbyMessage("updatereadystatus", lobbyPlayer.getId(), Integer.toString(readyPlayerCount), Integer.toString(playerCount)));
			break;
		case "unready":
			readyPlayerCount = joinLobbyService.setReadyStatus(lobbyPlayer, false);
			playerCount = joinLobbyService.getPlayerCount(lobbyPlayer);
			messageList.add(new LobbyMessage("updatereadystatus", lobbyPlayer.getId(), Integer.toString(readyPlayerCount), Integer.toString(playerCount)));
			break;
		case "vote":
			votes = joinLobbyService.vote(lobbyPlayer, message.getPlayerid());
			messageList.add(new LobbyMessage("updatevotestatus", lobbyPlayer.getId(), message.getPlayerid(), Integer.toString(votes)));
			break;
		case "unvote":
			votes = joinLobbyService.removeVote(lobbyPlayer, message.getPlayerid());
			messageList.add(new LobbyMessage("updatevotestatus", message.getPlayerid(), Integer.toString(votes)));
			break;
		}

		return convertObjectToJson(messageList);
	}

	@MessageMapping("/private/{gameid}")
	@SendToUser("/action/private")
	public String reply(@DestinationVariable String gameid, JoinLobbyMessage message, Principal principal) {
		List<LobbyMessage> lml = new ArrayList<>();
		LobbyPlayer lobbyPlayer = getPlayerFromPrincipal(principal);
		LobbyEntity lobby = lobbyPlayer.getLobby();

		switch (message.getAction()) {
		case "getplayers":
			for (LobbyPlayer lp : lobby.getPlayers()) {
				if (lp.getId() == lobbyPlayer.getId())
					lml.add(new LobbyMessage("owner", lp.getId(), lp.getNickname()));
				else
					lml.add(new LobbyMessage("join", lp.getId(), lp.getNickname()));
			}
			break;
		case "requestgame":
			if(lobby.getReadyPlayerCount() == lobby.getPlayers().size()) {
				lml.add(new LobbyMessage("gamerequestgranted", lobbyPlayer.getId(), lobbyPlayer.getNickname()));
			}else{
				lml.add(new LobbyMessage("gamerequestdenied", lobbyPlayer.getId(), lobbyPlayer.getNickname()));
			}
			break;
		case "initializegame":
			lobby.getAlivePlayers().forEach((lp) -> lml.add(new LobbyMessage("joinalive", lp.getId(), lp.getNickname(), Integer.toString(lp.getVotes()))));
			lobby.getDeadPlayers().forEach((lp) -> lml.add(new LobbyMessage("joindead", lp.getId(), lp.getNickname(), lp.getRole(), lp.getAlignment())));
			break;
		}

		return convertObjectToJson(lml);
	}

	private void broadcastMessage(String message) {
		simpTemplate.convertAndSend("/action/broadcast/{gameid}", message);
	}
	
	private void privateMessage(String message) {
		simpTemplate.convertAndSend("/action/private", message);
	}
	
	private LobbyPlayer getPlayerFromPrincipal(Principal principal) {
		String username = principal.getName();
		User loggedinuser = accountService.findByUsername(username);
		LobbyPlayer lobbyPlayer = joinLobbyService.getPlayer(loggedinuser.getId());

		return lobbyPlayer;
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
