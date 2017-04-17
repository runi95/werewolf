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

    @MessageMapping("/lobbymessages")
    @SendTo("/action/lobbymessages")
    public String send(JoinLobbyMessage message, Principal principal) {
        String username = principal.getName();
        User loggedinuser = accountService.findByUsername(username);
        LobbyPlayer lobbyPlayer = lobbyPlayerService.findByUser(loggedinuser);

        if(message.getAction() != null && message.getAction().equals("leave")) {
            lobbyPlayer.getLobby().getPlayers().remove(lobbyPlayer);
        }

        List<LobbyMessage> lml = new ArrayList<>();

        lml.add(new LobbyMessage(Long.toString(loggedinuser.getId()), lobbyPlayer.getNickname(), message.getAction()));

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        String arrayToJson = null;
        try {
            arrayToJson = objectMapper.writeValueAsString(lml);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return arrayToJson;
    }

    @MessageMapping("/joinlobby")
    @SendToUser("/action/joiblobby")
    public String reply(JoinLobbyMessage message, Principal principal) {
        List<LobbyMessage> lml = new ArrayList<>();
        String username = principal.getName();
        User loggedinuser = accountService.findByUsername(username);
        LobbyPlayer lobbyPlayer = lobbyPlayerService.findByUser(loggedinuser);
        LobbyEntity lobby = lobbyPlayer.getLobby();

        for(LobbyPlayer lp : lobby.getPlayers()) {
            lml.add(new LobbyMessage(Long.toString(lp.getUser().getId()),lp.getNickname(), "join"));
        }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        String arrayToJson = null;
        try {
            arrayToJson = objectMapper.writeValueAsString(lml);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return arrayToJson;
    }
}
