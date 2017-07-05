package com.werewolf.gameplay;

import com.werewolf.Messages.LobbyMessage;
import com.werewolf.entities.GamePhase;
import com.werewolf.entities.LobbyEntity;
import com.werewolf.entities.LobbyPlayer;
import com.werewolf.services.JoinLobbyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.*;

public abstract class GameModeMasterClass implements GameMode {
	
	@Autowired
	protected SimpMessagingTemplate simpTemplate;
	
	protected void broadcastMessage(String gameid, String message) {
		simpTemplate.convertAndSend("/action/broadcast/" + gameid, message);
	}
	
	protected void privateMessage(String user, String message) {
		simpTemplate.convertAndSendToUser(user, "/action/private", message);
	}

	protected boolean checkWinCondition(LobbyEntity lobbyEntity) {
        LinkedList<LobbyPlayer>[] alignmentLists = splitPlayerListIntoAlignments(lobbyEntity);

        if(alignmentLists[0].size() > 1 && alignmentLists[1].isEmpty() && alignmentLists[2].isEmpty()) {
            goodWins(alignmentLists[0], alignmentLists[1], alignmentLists[2], alignmentLists[3], lobbyEntity);
            return true;
        } else if (!alignmentLists[1].isEmpty() && alignmentLists[0].size() < 2 && alignmentLists[2].isEmpty()) {
            evilWins(alignmentLists[0], alignmentLists[1], alignmentLists[3], alignmentLists[2], lobbyEntity);
            return true;
        } else if (alignmentLists[0].size() < 2 && alignmentLists[1].isEmpty()){
            neutralWins(alignmentLists[0], alignmentLists[1], alignmentLists[3], alignmentLists[2], lobbyEntity);
            return true;
        } else
            return false;
    }

    protected LinkedList<LobbyPlayer>[] splitPlayerListIntoAlignments(LobbyEntity lobbyEntity) {
        LinkedList<LobbyPlayer> goodList = new LinkedList<>(), evilList = new LinkedList<>(), neutralEvilList = new LinkedList<>(), neutralList = new LinkedList<>();
        for(LobbyPlayer lp : lobbyEntity.getAlivePlayers()) {
            switch(lp.getRole().getAlignment()) {
                case Good:
                case ChaoticGood:
                    goodList.add(lp);
                    break;
                case Evil:
                case ChaoticEvil:
                    evilList.add(lp);
                    break;
                case NeutralEvil:
                    neutralEvilList.add(lp);
                    break;
                case Neutral:
                    neutralList.add(lp);
                    break;
            }
        }

        return new LinkedList[] {goodList, evilList, neutralEvilList, neutralList};
    }

    @Override
    public void nightAction(LobbyEntity lobbyEntity, LobbyPlayer acter, LobbyPlayer oldTarget, LobbyPlayer target, boolean act) {
        if(lobbyEntity.getPhase() != GamePhase.NIGHT)
            return;

        List<LobbyMessage> messageList = new ArrayList<>();

        if(oldTarget != null) {
            messageList.add(new LobbyMessage("unnightaction", oldTarget.getId()));
        }

        if(act) {
            acter.setTarget(target.getId());
            messageList.add(new LobbyMessage("nightaction", target.getId()));
        } else {
            acter.setTarget(null);
            messageList.add(new LobbyMessage("unnightaction", target.getId()));
        }

        if(!messageList.isEmpty())
            privateMessage(acter.getUser().getUsername(), JoinLobbyService.convertObjectToJson(messageList));
    }

    protected void neutralWins(LinkedList<LobbyPlayer> goodPlayers, LinkedList<LobbyPlayer> evilPlayers, LinkedList<LobbyPlayer> neutralPlayers, LinkedList<LobbyPlayer> neutralEvilPlayers, LobbyEntity lobbyEntity) {
        goodPlayers.forEach((p) -> privateMessage(p.getUser().getUsername(), JoinLobbyService.convertObjectToJson(new ArrayList<>(Arrays.asList(new LobbyMessage[]{new LobbyMessage("lost")})))));
        evilPlayers.forEach((p) -> privateMessage(p.getUser().getUsername(), JoinLobbyService.convertObjectToJson(new ArrayList<>(Arrays.asList(new LobbyMessage[]{new LobbyMessage("lost")})))));
        neutralPlayers.forEach((p) -> privateMessage(p.getUser().getUsername(), JoinLobbyService.convertObjectToJson(new ArrayList<>(Arrays.asList(new LobbyMessage[]{new LobbyMessage("won")})))));
        neutralEvilPlayers.forEach((p) -> privateMessage(p.getUser().getUsername(), JoinLobbyService.convertObjectToJson(new ArrayList<>(Arrays.asList(new LobbyMessage[]{new LobbyMessage("won")})))));

        lobbyEntity.getDeadPlayers().forEach((p) -> privateMessage(p.getUser().getUsername(), JoinLobbyService.convertObjectToJson(new ArrayList<>(Arrays.asList(new LobbyMessage[]{new LobbyMessage("lost")})))));
        broadcastMessage(lobbyEntity.getGameId(), JoinLobbyService.convertObjectToJson(new ArrayList<>(Arrays.asList(new LobbyMessage[]{new LobbyMessage("endgame")}))));
    }

    protected void goodWins(LinkedList<LobbyPlayer> goodPlayers, LinkedList<LobbyPlayer> evilPlayers, LinkedList<LobbyPlayer> neutralPlayers, LinkedList<LobbyPlayer> neutralEvilPlayers, LobbyEntity lobbyEntity) {
        goodPlayers.forEach((p) -> privateMessage(p.getUser().getUsername(), JoinLobbyService.convertObjectToJson(new ArrayList<>(Arrays.asList(new LobbyMessage[]{new LobbyMessage("won")})))));
        evilPlayers.forEach((p) -> privateMessage(p.getUser().getUsername(), JoinLobbyService.convertObjectToJson(new ArrayList<>(Arrays.asList(new LobbyMessage[]{new LobbyMessage("lost")})))));
        neutralPlayers.forEach((p) -> privateMessage(p.getUser().getUsername(), JoinLobbyService.convertObjectToJson(new ArrayList<>(Arrays.asList(new LobbyMessage[]{new LobbyMessage("won")})))));
        neutralEvilPlayers.forEach((p) -> privateMessage(p.getUser().getUsername(), JoinLobbyService.convertObjectToJson(new ArrayList<>(Arrays.asList(new LobbyMessage[]{new LobbyMessage("lost")})))));

        lobbyEntity.getDeadPlayers().forEach((p) -> privateMessage(p.getUser().getUsername(), JoinLobbyService.convertObjectToJson(new ArrayList<>(Arrays.asList(new LobbyMessage[]{new LobbyMessage("lost")})))));
        broadcastMessage(lobbyEntity.getGameId(), JoinLobbyService.convertObjectToJson(new ArrayList<>(Arrays.asList(new LobbyMessage[]{new LobbyMessage("endgame")}))));
    }

    protected void evilWins(LinkedList<LobbyPlayer> goodPlayers, LinkedList<LobbyPlayer> evilPlayers, LinkedList<LobbyPlayer> neutralPlayers, LinkedList<LobbyPlayer> neutralEvilPlayers, LobbyEntity lobbyEntity) {
        goodPlayers.forEach((p) -> privateMessage(p.getUser().getUsername(), JoinLobbyService.convertObjectToJson(new ArrayList<>(Arrays.asList(new LobbyMessage[]{new LobbyMessage("lost")})))));
        evilPlayers.forEach((p) -> privateMessage(p.getUser().getUsername(), JoinLobbyService.convertObjectToJson(new ArrayList<>(Arrays.asList(new LobbyMessage[]{new LobbyMessage("won")})))));
        neutralPlayers.forEach((p) -> privateMessage(p.getUser().getUsername(), JoinLobbyService.convertObjectToJson(new ArrayList<>(Arrays.asList(new LobbyMessage[]{new LobbyMessage("won")})))));
        neutralEvilPlayers.forEach((p) -> privateMessage(p.getUser().getUsername(), JoinLobbyService.convertObjectToJson(new ArrayList<>(Arrays.asList(new LobbyMessage[]{new LobbyMessage("lost")})))));

        lobbyEntity.getDeadPlayers().forEach((p) -> privateMessage(p.getUser().getUsername(), JoinLobbyService.convertObjectToJson(new ArrayList<>(Arrays.asList(new LobbyMessage[]{new LobbyMessage("lost")})))));
        broadcastMessage(lobbyEntity.getGameId(), JoinLobbyService.convertObjectToJson(new ArrayList<>(Arrays.asList(new LobbyMessage[]{new LobbyMessage("endgame")}))));
    }

    protected void dayPhase(LobbyEntity lobbyEntity) {
        List<LobbyMessage> messageList = new ArrayList<>();
        lobbyEntity.setPhase(GamePhase.DAY);

        messageList.add(new LobbyMessage("dayphase"));

        if(!messageList.isEmpty())
            broadcastMessage(lobbyEntity.getGameId(), JoinLobbyService.convertObjectToJson(messageList));

        startDay(lobbyEntity);
    }

    protected void nightPhase(LobbyEntity lobbyEntity) {
        List<LobbyMessage> messageList = new ArrayList<>();
        lobbyEntity.setPhase(GamePhase.NIGHT);

        messageList.add(new LobbyMessage("nightphase"));

        if(!messageList.isEmpty())
            broadcastMessage(lobbyEntity.getGameId(), JoinLobbyService.convertObjectToJson(messageList));

        startNight(lobbyEntity);
    }

    protected void waitPhase(LobbyEntity lobbyEntity, String nextPhase) {
        List<LobbyMessage> messageList = new ArrayList<>();

        lobbyEntity.setPhase(GamePhase.WAIT);
        lobbyEntity.setPhaseTime(3);

        messageList.add(new LobbyMessage("waitphase"));

        if(!messageList.isEmpty())
            broadcastMessage(lobbyEntity.getGameId(), JoinLobbyService.convertObjectToJson(messageList));

        if(!checkWinCondition(lobbyEntity))
            startWaitingPhase(lobbyEntity, nextPhase);
    }

    protected void initializeGameWait(LobbyEntity lobbyEntity, String nextPhase) {
        List<LobbyMessage> messageList = new ArrayList<>();

        lobbyEntity.setPhase(GamePhase.WAIT);
        lobbyEntity.setPhaseTime(15);

        messageList.add(new LobbyMessage("waitphase"));

        if(!messageList.isEmpty())
            broadcastMessage(lobbyEntity.getGameId(), JoinLobbyService.convertObjectToJson(messageList));

        if(!checkWinCondition(lobbyEntity))
            startWaitingPhase(lobbyEntity, nextPhase);
    }

    protected void startWaitingPhase(LobbyEntity lobbyEntity, String nextPhase) {
        new Thread() {
            public void run() {
                try {
                    if(lobbyEntity.getPhaseTime() > 0) {
                        Thread.sleep(1000);
                        lobbyEntity.setPhaseTime(lobbyEntity.getPhaseTime() - 1);
                        run();
                    } else {
                        runNextPhase(lobbyEntity, nextPhase);
                    }
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }.start();
    }

    protected void runNextPhase(LobbyEntity lobbyEntity, String nextPhase) {
        switch(nextPhase) {
            case "nightphase":
                nightPhase(lobbyEntity);
                break;
            case "dayphase":
                dayPhase(lobbyEntity);
                break;
        }
    }

    protected void startNight(LobbyEntity lobbyEntity) {
        lobbyEntity.setPhaseTime(30);
        new Thread() {
            public void run() {
                try {
                    if(lobbyEntity.getPhaseTime() > 0) {
                        Thread.sleep(1000);
                        lobbyEntity.setPhaseTime(lobbyEntity.getPhaseTime() - 1);
                        run();
                    } else {
                        endNight(lobbyEntity);
                    }
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    protected void endNight(LobbyEntity lobbyEntity) {
        GameEmulator game = new GameEmulator(lobbyEntity.getRounds());

        Map<String, EmulationCharacter> emulationCharacters = setUpCharacters(lobbyEntity);

        for(EmulationCharacter emulationChar : emulationCharacters.values()) {
            emulationChar.getRole().doAction(game, emulationChar, emulationCharacters.get(emulationChar.getTargetid()));
        }

        game.emulate();

        for(EmulationCharacter emulationChar : emulationCharacters.values()) {
            List<LobbyMessage> messageList = new ArrayList<>();

            messageList.addAll(emulationChar.getMessageList());

            privateMessage(emulationChar.getLobbyPlayer().getUser().getUsername(), JoinLobbyService.convertObjectToJson(messageList));
        }

        game.getDeadPlayers().forEach((p) -> { broadcastMessage(lobbyEntity.getGameId(), JoinLobbyService.convertObjectToJson(new ArrayList<>(Arrays.asList(new LobbyMessage[]{new LobbyMessage("kill", p.getLobbyPlayer().getId(), p.getLobbyPlayer().getNickname(), p.getLobbyPlayer().getRole().getName(), p.getLobbyPlayer().getAlignment())})))); lobbyEntity.addDeadPlayer(p.getLobbyPlayer()); } );

        waitPhase(lobbyEntity, "dayphase");

        // Setting all targets to null and telling clients about the change
        lobbyEntity.getAlivePlayers().forEach((p) -> { if(p.getTarget() != null) { privateMessage(p.getUser().getUsername(), JoinLobbyService.convertObjectToJson(new ArrayList<>(Arrays.asList(new LobbyMessage[]{new LobbyMessage("unnightaction", p.getTarget())})))); p.setTarget(null);}});

        lobbyEntity.setRounds(lobbyEntity.getRounds() + 1);
    }

    protected Map<String, EmulationCharacter> setUpCharacters(LobbyEntity lobbyEntity) {
        Map<String, EmulationCharacter> emulationCharacters = new HashMap<>();

        lobbyEntity.getAlivePlayers().forEach((p) -> emulationCharacters.put(p.getId(), new EmulationCharacter(p, p.getRole(), p.getTarget())));

        return emulationCharacters;
    }

    protected void startDay(LobbyEntity lobbyEntity) {
        lobbyEntity.setPhaseTime(60);
        new Thread() {
            public void run() {
                try {
                    if(lobbyEntity.getPhaseTime() > 0) {
                        Thread.sleep(1000);
                        lobbyEntity.setPhaseTime(lobbyEntity.getPhaseTime() - 1);
                        run();
                    } else {
                        endDay(lobbyEntity);
                    }
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    protected void endDay(LobbyEntity lobbyEntity) {
        waitPhase(lobbyEntity, "nightphase");

        // Setting all votes to 0 and telling clients about the change
        lobbyEntity.getAlivePlayers().forEach((p) -> { if(p.getVotes() != 0) p.setVotes(0); if(p.getVoted() != null) { broadcastMessage(lobbyEntity.getGameId(), JoinLobbyService.convertObjectToJson(new ArrayList<>(Arrays.asList(new LobbyMessage[]{new LobbyMessage("updatevotestatus", p.getId(), p.getVoted(), "0", "x")})))); p.setVoted(null);}});
    }
}
