package com.werewolf.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.werewolf.entities.LobbyPlayer;
import com.werewolf.entities.User;
import com.werewolf.services.AccountService;
import com.werewolf.services.JoinLobbyService;

@org.springframework.web.bind.annotation.RestController
public class RestController {
	
	@Autowired
	AccountService accountService;
	
	@Autowired
	JoinLobbyService lobbyPlayerService;
	
	@RequestMapping(value = "/lobby/gamecoderequest", method = RequestMethod.GET)
	public String gamecoderequest(Principal principal) {
		String username = principal.getName();
		User loggedinuser = accountService.findByUsername(username);
        LobbyPlayer lobbyPlayer = lobbyPlayerService.getPlayer(loggedinuser.getId());
        
        return lobbyPlayer.getLobby().getGameId();
	}
	
}
