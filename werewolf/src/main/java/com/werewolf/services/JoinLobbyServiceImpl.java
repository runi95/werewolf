package com.werewolf.services;

import com.werewolf.Messages.LobbyMessage;
import com.werewolf.data.CreateLobbyForm;
import com.werewolf.data.JoinLobbyForm;
import com.werewolf.data.NameDictionaryRepository;
import com.werewolf.entities.*;
import com.werewolf.gameplay.AdvancedMode;
import com.werewolf.gameplay.OneNightMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class JoinLobbyServiceImpl implements JoinLobbyService {

	private final ConcurrentHashMap<Long, LobbyPlayer> playerMap = new ConcurrentHashMap<>();
	private final ConcurrentHashMap<String, LobbyEntity> lobbyMap = new ConcurrentHashMap<>();

	@Autowired
	SimpMessagingTemplate simpTemplate;

	@Autowired
	AdvancedMode advancedGameMode;

    @Autowired
    OneNightMode oneNightMode;

	@Autowired
	NameDictionaryRepository nameDictionaryRepository;

	@Autowired
	AccountService accountService;

	@Autowired
    UserStatisticService userStatisticService;

	@Override
	public LobbyPlayer getPlayer(long userid) {
		return playerMap.get(userid);
	}

	@Override
    public void sendChatMessage(String actionName, String username, String message) {
        LobbyPlayer chatSourcePlayer = getPlayerFromUsername(username);

        if(chatSourcePlayer == null)
            return;

        LobbyEntity lobbyEntity = chatSourcePlayer.getLobby();

        if(lobbyEntity == null || (!lobbyEntity.getPhase().equals("dayphase") && !lobbyEntity.getPhase().equals("lobby")))
            return;

        List<LobbyMessage> lobbyMessages = new ArrayList<>();

        lobbyMessages.add(new LobbyMessage(actionName, chatSourcePlayer.getNickname(), message));

        broadcastMessage(lobbyEntity.getGameId(), JoinLobbyService.convertObjectToJson(lobbyMessages));
    }

	@Override
    public List<LobbyMessage> join(String username, CreateLobbyForm createLobbyForm) {
        JoinLobbyForm joinLobbyForm = new JoinLobbyForm();

	    final String gameid = generateNewGameid();
        LobbyEntity lobbyEntity = new LobbyEntity(gameid, readInt(createLobbyForm.getMaxplayers()));
        switch(createLobbyForm.getGamemode()) {
            case "Advanced":
                lobbyEntity.setGameMode(advancedGameMode);
                break;
            case "One Night":
                lobbyEntity.setGameMode(oneNightMode);
                break;
            default:
                lobbyEntity.setGameMode(advancedGameMode);
                System.out.println("Mode: " + createLobbyForm.getGamemode());
                break;
        }
        if(createLobbyForm.getPrivatelobby().equals("true"))
            lobbyEntity.setPrivate(true);

        lobbyMap.put(gameid, lobbyEntity);
        joinLobbyForm.setNickname(createLobbyForm.getNickname());
        joinLobbyForm.setGameid(gameid);

        if(!lobbyEntity.getPrivate())
            broadcastPublicMessage(JoinLobbyService.convertObjectToJson(new LobbyMessage[] {new LobbyMessage("openlobby", gameid, lobbyEntity.getGameMode().getName(),"0")}));

        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                    if(lobbyMap.containsKey(gameid)) {
                        LobbyEntity gameLobby = lobbyMap.get(gameid);
                        if (gameLobby.getPlayerSize() == 0) {
                            lobbyMap.remove(gameLobby);
                            if(!lobbyEntity.getPrivate())
                                broadcastPublicMessage(JoinLobbyService.convertObjectToJson(new LobbyMessage[]{new LobbyMessage("removeopenlobby", gameid)}));
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        return join(username, joinLobbyForm);
    }

	@Override
	public List<LobbyMessage> join(String username, JoinLobbyForm joinLobbyForm) {
		List<LobbyMessage> messageList = new ArrayList<>();
        List<LobbyMessage> privateMessageList = new ArrayList<>();

		User user = accountService.findByUsername(username);
		String gameid = joinLobbyForm.getGameid().toUpperCase();
		String nickname = joinLobbyForm.getNickname();

		LobbyPlayer oldLobby = playerMap.get(user.getId());
		if (oldLobby != null)
			leave(playerMap.get(user.getId()));

		LobbyEntity lobbyEntity = lobbyMap.get(gameid);
		if (lobbyEntity == null) {
            privateMessageList.add(new LobbyMessage("error", "404"));
            return privateMessageList;
        }

        if(nickname == null || nickname.equals(""))
            nickname = generateNewNickname();
		else if (invalidNickname(joinLobbyForm.getNickname())) {
            privateMessageList.add(new LobbyMessage("error", "100"));
            return privateMessageList;
        }


		LobbyPlayer lobbyPlayer = lobbyEntity.addPlayer(user, nickname);
		playerMap.put(user.getId(), lobbyPlayer);
		if(!lobbyEntity.getStartedState() && !lobbyEntity.getPrivate())
            broadcastPublicMessage(JoinLobbyService.convertObjectToJson(new LobbyMessage[] {new LobbyMessage("openlobby", gameid, lobbyEntity.getGameMode().getName(), Integer.toString(lobbyEntity.getPlayerSize()))}));

		messageList.add(new LobbyMessage("join", lobbyPlayer.getId(), nickname));
        privateMessageList.add(new LobbyMessage("lobbyinfo", gameid, lobbyEntity.getGameMode().getName(), (lobbyEntity.getPrivate() ? "Private" : "Public"), Integer.toString(lobbyEntity.getMaxPlayers())));
		privateMessageList.add(new LobbyMessage("playerreadycount", null,
				Integer.toString(lobbyEntity.getReadyPlayerCount()), Integer.toString(lobbyEntity.getPlayerSize())));

		if (!messageList.isEmpty())
			broadcastMessage(lobbyPlayer.getLobby().getGameId(), JoinLobbyService.convertObjectToJson(messageList));

		return privateMessageList;
	}

	@Override
	public String getProfile(String username) {
		List<LobbyMessage> messageList = new ArrayList<>();

        UserStatistics userStatistics = userStatisticService.getUserStatistics(username);
		messageList.add(new LobbyMessage("profile", username, Long.toString(userStatistics.getGameswon()), Long.toString(userStatistics.getGamesplayed())));

		return JoinLobbyService.convertObjectToJson(messageList);
	}

	// Unsure if this is going to be needed, but keeping it anyway
	@Override
	public void leave(String username) {
		LobbyPlayer lobbyPlayer = getPlayerFromUsername(username);

		leave(lobbyPlayer);
	}

	@Override
	public void setReadyStatus(String username, boolean ready) {
		List<LobbyMessage> messageList = new ArrayList<LobbyMessage>();

		LobbyPlayer lobbyPlayer = getPlayerFromUsername(username);
		if (lobbyPlayer == null)
			return;

		LobbyEntity lobbyEntity = lobbyPlayer.getLobby();
		if (ready != lobbyPlayer.ready()) {
			lobbyPlayer.setReady(ready);

			if (ready)
				lobbyEntity.setReadyPlayerCount(lobbyEntity.getReadyPlayerCount() + 1);
			else
				lobbyEntity.setReadyPlayerCount(lobbyEntity.getReadyPlayerCount() - 1);
		}

		messageList.add(new LobbyMessage("updatereadystatus", lobbyPlayer.getId(),
				Integer.toString(lobbyEntity.getReadyPlayerCount()), Integer.toString(lobbyEntity.getPlayerSize())));

		if (lobbyEntity.getReadyPlayerCount() == lobbyEntity.getPlayerSize() && lobbyEntity.getPlayerSize() >= 2 && lobbyEntity.getPlayerSize() <= lobbyEntity.getMaxPlayers())
            loadGame(lobbyEntity);

		if (!messageList.isEmpty())
			broadcastMessage(lobbyPlayer.getLobby().getGameId(), JoinLobbyService.convertObjectToJson(messageList));
	}

	@Override
	public LobbyEntity findByGameId(String gameId) {
		return lobbyMap.get(gameId);
	}

	@Override
	public JoinLobbyForm getEditForm(LobbyEntity lobbyEntity) {
		JoinLobbyForm joinLobbyForm = new JoinLobbyForm();
		joinLobbyForm.setGameid(lobbyEntity.getGameId());
		return joinLobbyForm;
	}

	@Override
	public boolean gameidIsPresent(String gameid) {
		return lobbyMap.containsKey(gameid);
	}

	@Override
	public void vote(String username, String voteon, boolean status) {
		LobbyPlayer voter = getPlayerFromUsername(username);
		if (voter == null)
			return;

		LobbyPlayer voteonPlayer = voter.getLobby().getAlivePlayer(voteon);
		if (voteonPlayer == null || (voter.getVoted() != null && voter.getVoted().equals(voteon) && status)
				|| voter.getLobby().getDeadPlayer(voter.getId()) != null || voter.getId().equals(voteon) || voter.getLobby().getDeadPlayer(voteon) != null)
			return;

		LobbyPlayer oldVoteTarget = null;
		if (voter.getVoted() != null) {
			oldVoteTarget = voter.getLobby().getAlivePlayer(voter.getVoted());
		}

		lobbyVote(voter.getLobby(), voter, voteonPlayer, oldVoteTarget, status);
	}

	@Override
	public void getPlayers(String username) {
		List<LobbyMessage> messageList = new ArrayList<>();

		LobbyPlayer lobbyPlayer = getPlayerFromUsername(username);
		if (lobbyPlayer == null)
			return;

		LobbyEntity lobbyEntity = lobbyPlayer.getLobby();

		if (lobbyEntity != null) {
			for (LobbyPlayer lp : lobbyEntity.getPlayers()) {
				if (lp.getId() == lobbyPlayer.getId())
					messageList.add(new LobbyMessage("owner", lp.getId(), lp.getNickname()));
				else
					messageList.add(new LobbyMessage("join", lp.getId(), lp.getNickname()));
			}
			if(lobbyEntity.getStartedState())
				messageList.add(new LobbyMessage("lobbyready"));
		}

		if (!messageList.isEmpty())
			privateMessage(username, JoinLobbyService.convertObjectToJson(messageList));
	}
	
	@Override
	public void initializeLobby(String username) {
		List<LobbyMessage> messageList = new ArrayList<>();

		LobbyPlayer lobbyPlayer = getPlayerFromUsername(username);
		if (lobbyPlayer == null)
			return;
		
		LobbyEntity lobbyEntity = lobbyPlayer.getLobby();
		if(lobbyEntity == null)
			return;
		
		if(lobbyEntity.getStartedState())
			messageList.add(new LobbyMessage("lobbyready"));
		else
			messageList.add(new LobbyMessage("lobby"));
		
		if (!messageList.isEmpty())
			privateMessage(username, JoinLobbyService.convertObjectToJson(messageList));
	}

	@Override
	public void initializeGame(String username) {
		List<LobbyMessage> messageList = new ArrayList<>();

		LobbyPlayer lobbyPlayer = getPlayerFromUsername(username);
		if (lobbyPlayer == null)
			return;

		LobbyEntity lobbyEntity = lobbyPlayer.getLobby();

		if (lobbyEntity != null) {
			if(lobbyPlayer.getAlignment().equals("Evil"))
				lobbyEntity.getEvilTeam().forEach((p) -> messageList.add(new LobbyMessage("addinvalidtarget", p.getId(), p.getRole().getName(), p.getRole().getAlignment().getAlignmentName())));
			
			lobbyEntity.getAlivePlayers().forEach((lp) -> messageList
					.add(new LobbyMessage("joinalive", lp.getId(), lp.getNickname(), Integer.toString(lp.getVotes()))));
			lobbyEntity.getDeadPlayers().forEach((lp) -> messageList.add(new LobbyMessage("joindead", lp.getId(),
					lp.getNickname(), lp.getRole().getName(), lp.getAlignment())));

			messageList
					.add(new LobbyMessage("initrole", lobbyPlayer.getRole().getName()));
		}

		if (!messageList.isEmpty())
			privateMessage(username, JoinLobbyService.convertObjectToJson(messageList));
	}

	@Override
	public void nightAction(String username, String target, boolean act) {
		LobbyPlayer lobbyPlayer = getPlayerFromUsername(username);
		if (lobbyPlayer == null)
			return;

		LobbyPlayer targetPlayer = lobbyPlayer.getLobby().getAlivePlayer(target);
		if (targetPlayer == null)
			return;

		LobbyPlayer oldTargetPlayer = null;
		if (lobbyPlayer.getTarget() != null) {
			oldTargetPlayer = lobbyPlayer.getLobby().getAlivePlayer(lobbyPlayer.getTarget());
		}
		lobbyNightAction(lobbyPlayer.getLobby(), lobbyPlayer, oldTargetPlayer, targetPlayer, act);
	}

	@Override
	public void getOpenLobbies(String username) {
		List<LobbyMessage> messageList = new ArrayList<LobbyMessage>();

		lobbyMap.values().forEach((lobby) -> { if(!lobby.getStartedState() && !lobby.getPrivate()) messageList.add(new LobbyMessage("openlobby", lobby.getGameId(), lobby.getGameMode().getName(), Integer.toString(lobby.getPlayerSize()))); });

        if (!messageList.isEmpty())
            privateMessage(username, JoinLobbyService.convertObjectToJson(messageList));
	}

	public void getGamePhase(String username) {
		List<LobbyMessage> messageList = new ArrayList<>();

		LobbyPlayer lobbyPlayer = getPlayerFromUsername(username);
		if (lobbyPlayer == null)
			return;

		messageList.add(new LobbyMessage("gamephase", lobbyPlayer.getLobby().getPhase()));

		if (!messageList.isEmpty())
			privateMessage(username, JoinLobbyService.convertObjectToJson(messageList));
	}

	public void getRole(String username) {
		List<LobbyMessage> messageList = new ArrayList<>();

		LobbyPlayer lobbyPlayer = getPlayerFromUsername(username);
		if (lobbyPlayer == null)
			return;

		if (lobbyPlayer.getRole() == null)
			return;

		messageList
				.add(new LobbyMessage("initrole", lobbyPlayer.getRole().getName(), lobbyPlayer.getRole().getDescription(),
						lobbyPlayer.getRole().getAlignment().getAlignmentName(), lobbyPlayer.getRole().getAlignment().getGoal()));
		messageList
				.add(new LobbyMessage("role", lobbyPlayer.getRole().getName(), lobbyPlayer.getRole().getDescription(),
						lobbyPlayer.getRole().getAlignment().getAlignmentName(), lobbyPlayer.getRole().getAlignment().getGoal()));

		if (!messageList.isEmpty())
			privateMessage(username, JoinLobbyService.convertObjectToJson(messageList));
	}

	private int readInt(String integer) {
	    int retint = -1;

	    try {
            retint = Integer.parseInt(integer);
        } catch(NumberFormatException e) {
	        e.printStackTrace();
        }

        return retint;
    }

	// TODO: Possibly make this failsafe
	private String generateNewGameid() {
		SecureRandom random = new SecureRandom();
		int num = random.nextInt(58786560) + 1679616;
		String generatedGameid = Integer.toUnsignedString(num, 36).toUpperCase();

		if (gameidIsPresent(generatedGameid))
			return generateNewGameid();
		else
			return generatedGameid;
	}

	private boolean invalidNickname(String nickname) {
		if (nickname != null) {
			nickname = nickname.replaceAll("\\s+", "");
			if (nickname.equals(""))
				return true;
			else
				return false;
		} else
			return true;
	}

	private String generateNewNickname() {
		SecureRandom random = new SecureRandom();
		int repositorySize = (int) nameDictionaryRepository.count();
		int num = random.nextInt(repositorySize) + 1;
		NameDictionary name = nameDictionaryRepository.findOne(num);
		num = random.nextInt(repositorySize) + 1;
		NameDictionary surname = nameDictionaryRepository.findOne(num);

		return name.getName() + " " + surname.getName();
	}

	private void createPlayers(LobbyEntity lobbyEntity) {
		lobbyEntity.getGameMode().setRoles(lobbyEntity);
	}

	private void leave(LobbyPlayer lobbyPlayer) {
		List<LobbyMessage> messageList = new ArrayList<LobbyMessage>();

		if (lobbyPlayer == null)
			return;

		LobbyEntity lobbyEntity = lobbyPlayer.getLobby();
		lobbyEntity.removePlayer(lobbyPlayer);
		playerMap.remove(lobbyPlayer.getUser().getId());

		if (lobbyEntity.getPlayerSize() == 0) {
            broadcastPublicMessage(JoinLobbyService.convertObjectToJson(new LobbyMessage[] {new LobbyMessage("removeopenlobby", lobbyEntity.getGameId())}));
			lobbyMap.remove(lobbyEntity.getGameId());
		} else {
			if(!lobbyEntity.getStartedState()) {
                broadcastPublicMessage(JoinLobbyService.convertObjectToJson(new LobbyMessage[] {new LobbyMessage("openlobby", lobbyEntity.getGameId(), Integer.toString(lobbyEntity.getPlayerSize()))}));
                messageList.add(new LobbyMessage("leave", lobbyPlayer.getId(), lobbyPlayer.getNickname()));
            }
		}

		if (!messageList.isEmpty())
			broadcastMessage(lobbyPlayer.getLobby().getGameId(), JoinLobbyService.convertObjectToJson(messageList));
	}

	private LobbyPlayer getPlayerFromUsername(String username) {
		User loggedinuser = accountService.findByUsername(username);
		LobbyPlayer lobbyPlayer = getPlayer(loggedinuser.getId());

		return lobbyPlayer;
	}

	private void loadGame(LobbyEntity lobbyEntity) {
		if (lobbyEntity == null)
			return;

		List<LobbyMessage> messageList = new ArrayList<LobbyMessage>();

		if (lobbyEntity.getReadyPlayerCount() != lobbyEntity.getReadyPlayerCount())
			return;

		lobbyEntity.getPlayers().forEach((p) -> lobbyEntity.addAlivePlayer(p));

		createPlayers(lobbyEntity);
		initializeLobby(lobbyEntity);

        broadcastPublicMessage(JoinLobbyService.convertObjectToJson(new LobbyMessage[] {new LobbyMessage("removeopenlobby", lobbyEntity.getGameId())}));
		messageList.add(new LobbyMessage("lobbyready"));
		lobbyEntity.getPlayers().forEach((p) -> { messageList.add(new LobbyMessage("addtorolelist", p.getRole().getName(), p.getRole().getAlignment().getAlignmentName(), p.getRole().getAlignment().getGoal(), p.getRole().getDescription())); });

		if (!messageList.isEmpty())
			broadcastMessage(lobbyEntity.getGameId(), JoinLobbyService.convertObjectToJson(messageList));
	}

	private void initializeLobby(LobbyEntity lobbyEntity) {
		lobbyEntity.getGameMode().initalizeGame(lobbyEntity);
	}

	private void lobbyNightAction(LobbyEntity lobbyEntity, LobbyPlayer acter, LobbyPlayer oldTarget, LobbyPlayer target,
			boolean act) {
		lobbyEntity.getGameMode().nightAction(lobbyEntity, acter, oldTarget, target, act);
	}

	private void lobbyVote(LobbyEntity lobbyEntity, LobbyPlayer voter, LobbyPlayer voteTarget,
			LobbyPlayer oldVoteTarget, boolean status) {

		lobbyEntity.getGameMode().vote(lobbyEntity, voter, voteTarget, oldVoteTarget, status);
	}

	private void broadcastMessage(String gameid, String message) {
		simpTemplate.convertAndSend("/action/broadcast/" + gameid, message);
	}

	private void broadcastPublicMessage(String message) {
	    simpTemplate.convertAndSend("/action/broadcast/public", message);
    }

	private void privateMessage(String user, String message) {
		simpTemplate.convertAndSendToUser(user, "/action/private", message);
	}
}
