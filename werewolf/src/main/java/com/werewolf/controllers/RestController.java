package com.werewolf.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.werewolf.entities.LobbyPlayer;
import com.werewolf.entities.User;
import com.werewolf.services.AccountService;
import com.werewolf.services.LobbyPlayerService;

@org.springframework.web.bind.annotation.RestController
public class RestController {
	
	@Autowired
	AccountService accountService;
	
	@Autowired
	LobbyPlayerService lobbyPlayerService;
	
	@RequestMapping(value = "/lobby/gamecoderequest", method = RequestMethod.GET)
	public String gamecoderequest(Principal principal) {
		String username = principal.getName();
		User loggedinuser = accountService.findByUsername(username);
        LobbyPlayer lobbyPlayer = lobbyPlayerService.findByUser(loggedinuser);
        
        return lobbyPlayer.getLobby().getGameId();
	}
	
	
}
