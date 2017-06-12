package com.werewolf.controllers;

import java.security.Principal;
import java.util.List;

import com.werewolf.Messages.LobbyMessage;
import com.werewolf.Messages.RoleRequestMessages;
import com.werewolf.data.JoinLobbyForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping(value = "/lobby/rolerequest")
	public List<LobbyMessage> rolerequest(@RequestBody RoleRequestMessages roleRequestMessages) {
		return null;
	}
}
