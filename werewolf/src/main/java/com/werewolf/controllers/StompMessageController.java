package com.werewolf.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.werewolf.Messages.JoinLobbyMessage;
import com.werewolf.Messages.LobbyMessage;
import com.werewolf.Messages.ReadyAndUnreadyLobbyMessage;
import com.werewolf.entities.LobbyEntity;
import com.werewolf.entities.LobbyPlayer;
import com.werewolf.entities.User;
import com.werewolf.services.AccountService;
import com.werewolf.services.JoinLobbyService;
import com.werewolf.services.LobbyPlayerService;
import org.jboss.logging.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.simp.annotation.SendToUser;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class StompMessageController {

	@Autowired
	AccountService accountService;

	@Autowired
	JoinLobbyService joinLobbyService;

	@Autowired
	LobbyPlayerService lobbyPlayerService;

	@SuppressWarnings("unchecked")
	@MessageMapping("/lobbymessages/{gameid}")
	@SendTo("/action/lobbymessages/{gameid}")
	public String send(@DestinationVariable String gameid, JoinLobbyMessage message, Principal principal) {
		LobbyPlayer lobbyPlayer = getPlayerFromPrincipal(principal);
		Object messageList = null;
		int readyPlayerCount;
		int playerCount;
		
		switch(message.getAction()) {
		case "leave":
			joinLobbyService.leave(lobbyPlayer);
		case "join":
			messageList = new ArrayList<LobbyMessage>();
			((ArrayList<LobbyMessage>)messageList).add(new LobbyMessage(Long.toString(lobbyPlayer.getUser().getId()), lobbyPlayer.getNickname(),
					message.getAction()));
			break;
		case "ready":
			messageList = new ArrayList<ReadyAndUnreadyLobbyMessage>();
			readyPlayerCount = joinLobbyService.setReadyStatus(lobbyPlayer, true);
			playerCount = joinLobbyService.getPlayerCount(lobbyPlayer);
			((ArrayList<ReadyAndUnreadyLobbyMessage>)messageList).add(new ReadyAndUnreadyLobbyMessage(Long.toString(lobbyPlayer.getUser().getId()), Integer.toString(readyPlayerCount), Integer.toString(playerCount)));
			break;
		case "unready":
			messageList = new ArrayList<ReadyAndUnreadyLobbyMessage>();
			readyPlayerCount = joinLobbyService.setReadyStatus(lobbyPlayer, false);
			playerCount = joinLobbyService.getPlayerCount(lobbyPlayer);
			((ArrayList<ReadyAndUnreadyLobbyMessage>)messageList).add(new ReadyAndUnreadyLobbyMessage(Long.toString(lobbyPlayer.getUser().getId()), Integer.toString(readyPlayerCount), Integer.toString(playerCount)));
			break;
		}

		return convertObjectToJson(messageList);
	}

	@MessageMapping("/joinlobby//{gameid}")
	@SendToUser("/action/joinlobby")
	public String reply(@DestinationVariable String gameid, JoinLobbyMessage message, Principal principal) {
		List<LobbyMessage> lml = new ArrayList<>();
		LobbyPlayer lobbyPlayer = getPlayerFromPrincipal(principal);
		LobbyEntity lobby = lobbyPlayer.getLobby();

		switch (message.getAction()) {
		case "getplayers":
			for (LobbyPlayer lp : lobby.getPlayers()) {
				if (lp.getId() == lobbyPlayer.getId())
					lml.add(new LobbyMessage(Long.toString(lp.getUser().getId()), lp.getNickname(), "owner"));
				else
					lml.add(new LobbyMessage(Long.toString(lp.getUser().getId()), lp.getNickname(), "join"));
			}
			break;
		}

		return convertObjectToJson(lml);
	}

	@MessageMapping("/game/{gameid}")
	@SendToUser("/action/game")
	public String gamereply(@DestinationVariable String gameid, JoinLobbyMessage message, Principal principal) {
		switch(message.getAction()) {
            case "initializegame":
                // Return alive and dead players
                break;
        }
		return "";
	}

	private LobbyPlayer getPlayerFromPrincipal(Principal principal) {
		String username = principal.getName();
		User loggedinuser = accountService.findByUsername(username);
		LobbyPlayer lobbyPlayer = lobbyPlayerService.findByUser(loggedinuser);

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
