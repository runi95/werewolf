package com.werewolf.configurations;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.werewolf.Messages.LobbyMessage;
import com.werewolf.controllers.StompMessageController;
import com.werewolf.entities.LobbyPlayer;
import com.werewolf.entities.User;
import com.werewolf.services.AccountService;
import com.werewolf.services.JoinLobbyService;
import com.werewolf.services.LobbyPlayerService;

public class PresenceChannelInterceptor extends ChannelInterceptorAdapter {

	@Autowired
    private SimpMessagingTemplate simpTemplate;
	
	@Autowired
	private JoinLobbyService joinLobbyService;
	
	@Autowired
	private LobbyPlayerService lobbyPlayerService;
	
	@Autowired
	private AccountService accountService;
	
    private final Log logger = LogFactory.getLog(PresenceChannelInterceptor.class);
 
    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
 
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(message);
 
        // ignore non-STOMP messages like heartbeat messages
        if(sha.getCommand() == null) {
            return;
        }
 
        String sessionId = sha.getSessionId();
 
        switch(sha.getCommand()) {
            case CONNECT:
                logger.debug("STOMP Connect [sessionId: " + sessionId + "]");
                break;
            case CONNECTED:
                logger.debug("STOMP Connected [sessionId: " + sessionId + "]");
                break;
            case DISCONNECT:
            	disconnect(message);
                logger.debug("STOMP Disconnect [sessionId: " + sessionId + "]");
                break;
            default:
                break;
 
        }
    }
    
    private void disconnect(Message<?> message) {
    	if(message != null && message.getHeaders().containsKey("simpUser")) {
    		UsernamePasswordAuthenticationToken userToken = (UsernamePasswordAuthenticationToken) message.getHeaders().get("simpUser");
    		User user = accountService.findByUsername(userToken.getName());
    		LobbyPlayer lobbyPlayer = lobbyPlayerService.findByUser(user);
    		
    		List<LobbyMessage> lobbyMessages = new ArrayList<>();
    		lobbyMessages.add(new LobbyMessage(Long.toString(lobbyPlayer.getUser().getId()), lobbyPlayer.getNickname(), "leave"));
    		
    		joinLobbyService.leave(lobbyPlayer);
    		
    		simpTemplate.convertAndSend("/action/broadcast/" + lobbyPlayer.getLobby().getGameId(), StompMessageController.convertObjectToJson(lobbyMessages));
    	}
    }
}