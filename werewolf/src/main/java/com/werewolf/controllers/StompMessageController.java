package com.werewolf.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.werewolf.Messages.JoinLobbyMessage;
import com.werewolf.Messages.LobbyMessage;
import com.werewolf.data.LobbyEntityRepository;
import com.werewolf.data.LobbyPlayerRepository;
import com.werewolf.entities.LobbyEntity;
import com.werewolf.entities.LobbyPlayer;
import com.werewolf.entities.User;
import com.werewolf.services.AccountService;
import com.werewolf.services.JoinLobbyService;
import com.werewolf.services.LobbyPlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.core.MessageSendingOperations;
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

	@Autowired
	LobbyPlayerRepository lobbyPlayerRepository;

	@Autowired
	LobbyEntityRepository lobbyEntityRepository;

	@MessageMapping("/lobbymessages/{gameid}")
	@SendTo("/action/lobbymessages/{gameid}")
	public String send(@DestinationVariable String gameid, JoinLobbyMessage message, Principal principal) {
		LobbyPlayer lobbyPlayer = getPlayerFromPrincipal(principal);

		if (message.getAction() != null && message.getAction().equals("leave")) {
			joinLobbyService.leave(lobbyPlayer);
		}

		List<LobbyMessage> lml = new ArrayList<>();

		lml.add(new LobbyMessage(Long.toString(lobbyPlayer.getUser().getId()), lobbyPlayer.getNickname(),
				message.getAction()));

		return convertArrayToJson(lml);
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
		case "ready":
			//TODO:
			break;
		case "unready":
			//TODO:
			break;
		}

		return convertArrayToJson(lml);
	}

	private LobbyPlayer getPlayerFromPrincipal(Principal principal) {
		String username = principal.getName();
		User loggedinuser = accountService.findByUsername(username);
		LobbyPlayer lobbyPlayer = lobbyPlayerService.findByUser(loggedinuser);

		return lobbyPlayer;
	}

	public static String convertArrayToJson(List<LobbyMessage> messageArray) {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

		String arrayToJson = null;
		try {
			arrayToJson = objectMapper.writeValueAsString(messageArray);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return arrayToJson;
	}
}
