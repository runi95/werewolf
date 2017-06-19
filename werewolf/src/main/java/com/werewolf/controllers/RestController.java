package com.werewolf.controllers;

import com.werewolf.Messages.LobbyMessage;
import com.werewolf.data.CreateLobbyForm;
import com.werewolf.data.JoinLobbyForm;
import com.werewolf.services.AccountService;
import com.werewolf.services.JoinLobbyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;
import java.util.List;

@org.springframework.web.bind.annotation.RestController
public class RestController {
	
	@Autowired
	AccountService accountService;
	
	@Autowired
	JoinLobbyService lobbyPlayerService;

	@GetMapping(value = "/lobby/getprofile")
	public String getuserprofile(Principal principal) {
		String username = principal.getName();

		return lobbyPlayerService.getProfile(username);
	}

	@PostMapping(value = "/lobby/joinlobbyrequest")
	public List<LobbyMessage> joinlobbyrequest(@RequestBody JoinLobbyForm joinLobbyForm, Principal principal) {
        String username = principal.getName();
        return lobbyPlayerService.join(username, joinLobbyForm);
    }

	@PostMapping(value = "/lobby/createlobbyrequest")
	public List<LobbyMessage> createlobbyrequest(@RequestBody CreateLobbyForm createLobbyForm, Principal principal) {
		String username = principal.getName();
		return lobbyPlayerService.join(username, createLobbyForm);
	}
}
