package com.werewolf.configurations;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.werewolf.services.JoinLobbyService;

public class PresenceChannelInterceptor extends ChannelInterceptorAdapter {

	@Autowired
	private JoinLobbyService joinLobbyService;
	
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
            	disconnect(message, channel);
                logger.debug("STOMP Disconnect [sessionId: " + sessionId + "]");
                break;
            default:
                break;
 
        }
    }
    
    private void disconnect(Message<?> message, MessageChannel channel) {
    	if(message != null && message.getHeaders().containsKey("simpUser")) {
    		UsernamePasswordAuthenticationToken userToken = (UsernamePasswordAuthenticationToken) message.getHeaders().get("simpUser");
    		joinLobbyService.leave(userToken.getName());
    	}
    }
}