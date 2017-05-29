package com.werewolf.gameplay;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

public abstract class GameModeMasterClass implements GameMode {
	
	@Autowired
	protected SimpMessagingTemplate simpTemplate;
	
	protected void broadcastMessage(String gameid, String message) {
		simpTemplate.convertAndSend("/action/broadcast/" + gameid, message);
	}
	
	protected void privateMessage(String user, String message) {
		simpTemplate.convertAndSendToUser(user, "/action/private", message);
	}

}
