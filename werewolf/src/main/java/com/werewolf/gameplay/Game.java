package com.werewolf.gameplay;

import com.werewolf.Messages.PlayerMessage;
import com.werewolf.entities.GamePhase;
import com.werewolf.entities.LobbyEntity;
import com.werewolf.entities.LobbyPlayer;
import com.werewolf.gameplay.rules.*;
import com.werewolf.services.JoinLobbyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Game {

    @Autowired
    protected SimpMessagingTemplate simpTemplate;

    protected void broadcastMessage(String gameid, String message) {
        simpTemplate.convertAndSend("/action/broadcast/" + gameid, message);
    }

    protected void privateMessage(String user, String message) {
        simpTemplate.convertAndSendToUser(user, "/action/private", message);
    }

    protected void sendMessages(List<PlayerMessage> messageList) {
        if(messageList == null)
            return;

        for(PlayerMessage pm : messageList) {
            switch (pm.getPlayerMessageType()) {
                case PRIVATE:
                    privateMessage(pm.getReceiverId(), JoinLobbyService.convertObjectToJson(pm.getMessageMap()));
                    break;
                case BROADCAST:
                    broadcastMessage(pm.getReceiverId(), JoinLobbyService.convertObjectToJson(pm.getMessageMap()));
                    break;
            }
        }
    }

    /**
     * Adds all the rules to the game and then
     * runs the initialization rule(s).
     *
     * @param lobbyEntity
     */
    public void initializeGame(LobbyEntity lobbyEntity) {
        RuleModel ruleModel = lobbyEntity.getRuleModel();
        if (lobbyEntity.getStartedState())
            return;
        else
            for (LobbyInitializationRule r : ruleModel.getLobbyInitializationRules())
                sendMessages(r.initializeLobby(lobbyEntity));
    }

    public void initializePlayer(LobbyEntity lobbyEntity, LobbyPlayer lobbyPlayer) {
        RuleModel ruleModel = lobbyEntity.getRuleModel();

        for(PlayerInitializationRule r : ruleModel.getPlayerInitializationRules())
            sendMessages(r.initializePlayer(lobbyEntity, lobbyPlayer));
    }

    public void gamePhaseChanges(GamePhase oldGamePhase, GamePhase newGamePhase, LobbyEntity lobbyEntity) {
        RuleModel ruleModel = lobbyEntity.getRuleModel();

        for (GamePhaseRule r : ruleModel.getGamePhaseRules())
            sendMessages(r.gamePhaseChanged(oldGamePhase, newGamePhase, lobbyEntity));

        if (newGamePhase == GamePhase.DAY)
            for (DayRule r : ruleModel.getDayRules())
                sendMessages(r.dayStarted(lobbyEntity));

        if (newGamePhase == GamePhase.NIGHT)
            for (NightRule r : ruleModel.getNightRules())
                sendMessages(r.nightStarted(lobbyEntity));

        for (WinConditionRule r : ruleModel.getWinConditionRules())
            if (r.checkWinCondition(lobbyEntity))
                r.getWinners(lobbyEntity);
                // TODO: This has to be changed!
    }

    public void vote(LobbyEntity lobbyEntity, LobbyPlayer voter, LobbyPlayer target, LobbyPlayer oldTarget, boolean flag) {
        RuleModel ruleModel = lobbyEntity.getRuleModel();
        for (VoteRule r : ruleModel.getVoteRules())
            sendMessages(r.vote(lobbyEntity, voter, target, oldTarget, flag));
    }

    public void nightAction(LobbyEntity lobbyEntity, LobbyPlayer actor, LobbyPlayer target, LobbyPlayer oldTarget, boolean flag) {
        RuleModel ruleModel = lobbyEntity.getRuleModel();
        for (ActionRule r : ruleModel.getActionRules())
            sendMessages(r.nightAction(lobbyEntity, actor, target, oldTarget, flag));
    }

    public void chat(LobbyEntity lobbyEntity, LobbyPlayer chatSourcePlayer, String message, RuleModel ruleModel) {
        for (ChatRule r : ruleModel.getChatRules())
            sendMessages(r.chat(lobbyEntity, chatSourcePlayer, message));
    }
}
