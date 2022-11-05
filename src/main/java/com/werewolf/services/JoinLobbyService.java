package com.werewolf.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.werewolf.Messages.LobbyMessage;
import com.werewolf.Messages.PlayerMessage;
import com.werewolf.Messages.PlayerMessageType;
import com.werewolf.data.JoinLobbyForm;
import com.werewolf.data.NameDictionaryRepository;
import com.werewolf.entities.*;
import com.werewolf.gameplay.*;
import com.werewolf.gameplay.rules.OneNight.OneNightUltimateWerewolfRuleSet;
import com.werewolf.gameplay.rules.RuleSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class JoinLobbyService {

    private final ConcurrentHashMap<Long, LobbyPlayer> playerMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, LobbyEntity> lobbyMap = new ConcurrentHashMap<>();

    @Autowired
    SimpMessagingTemplate simpTemplate;

    @Autowired
    NameDictionaryRepository nameDictionaryRepository;

    @Autowired
    AccountService accountService;

    @Autowired
    UserStatisticService userStatisticService;

    @Autowired
    Game game;

    ArrayList<RuleSet> gameModes = new ArrayList<>();
    String[] gameModesArr;

    public JoinLobbyService() {
        gameModes.add(new OneNightUltimateWerewolfRuleSet());
        gameModesArr = new String[gameModes.size()];

        for (int i = 0; i < gameModes.size(); i++)
            gameModesArr[i] = gameModes.get(i).getName();
    }

    public String[] getGameModesAsStrings() {
        return gameModesArr;
    }

    public static String convertObjectToJson(Object message) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        String arrayToJson = null;
        try {
            arrayToJson = objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return arrayToJson;
    }

    public LobbyPlayer getPlayer(long userid) {
        return playerMap.get(userid);
    }

    public void sendChatMessage(String actionName, String username, String message) {
        LobbyPlayer chatSourcePlayer = getPlayerFromUsername(username);

        if (chatSourcePlayer == null)
            return;

        LobbyEntity lobbyEntity = chatSourcePlayer.getLobby();

        if (lobbyEntity == null)
            return;

        lobbyEntity.getGame().chat(lobbyEntity, chatSourcePlayer, message, lobbyEntity.getRuleModel());
    }

    public void join(String username, String gamemode, String privatelobby, String maxplayers, String nickname) {
        JoinLobbyForm joinLobbyForm = new JoinLobbyForm();

        final String gameid = generateNewGameid();
        LobbyEntity lobbyEntity = null;
        switch (gamemode) {
            case "One Night Ultimate Werewolf":
                lobbyEntity = new LobbyEntity(gameid, readInt(maxplayers), game,
                        new OneNightUltimateWerewolfRuleSet().getRuleModel());
                break;
            /*
             * case "Advanced":
             * lobbyEntity.setGameMode(advancedGameMode);
             * break;
             * case "One Night":
             * lobbyEntity.setGameMode(oneNightMode);
             * break;
             * case "Basic":
             * lobbyEntity.setGameMode(basicMode);
             * break;
             * default:
             * lobbyEntity.setGameMode(advancedGameMode);
             * break;
             */
        }
        if (lobbyEntity == null)
            return;

        if (privatelobby.equals("true"))
            lobbyEntity.setPrivate(true);

        lobbyMap.put(gameid, lobbyEntity);
        joinLobbyForm.setNickname(nickname);
        joinLobbyForm.setGameid(gameid);

        boolean priv = !lobbyEntity.getPrivate();
        if (priv) {
            Map<String, Object> messageMap = new HashMap<>();
            messageMap.put("action", "openlobby");
            messageMap.put("gameid", gameid);
            messageMap.put("name", lobbyEntity.getRuleModel().getName());
            messageMap.put("players", "0");

            sendMessages(Arrays.asList(new PlayerMessage(PlayerMessageType.BROADCAST, "public", messageMap)));
            // broadcastPublicMessage(JoinLobbyService.convertObjectToJson(new
            // LobbyMessage[]{new LobbyMessage("openlobby", gameid,
            // lobbyEntity.getRuleModel().getName(), "0")}));
        }

        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                    if (lobbyMap.containsKey(gameid)) {
                        LobbyEntity gameLobby = lobbyMap.get(gameid);
                        if (gameLobby.getPlayerSize() == 0) {
                            lobbyMap.remove(gameLobby);
                            if (priv) {
                                Map<String, Object> messageMap = new HashMap<>();
                                messageMap.put("action", "removeopenlobby");
                                messageMap.put("gameid", gameid);

                                sendMessages(Arrays
                                        .asList(new PlayerMessage(PlayerMessageType.BROADCAST, "public", messageMap)));
                                // broadcastPublicMessage(JoinLobbyService.convertObjectToJson(new
                                // LobbyMessage[]{new LobbyMessage("removeopenlobby", gameid)}));
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        join(username, gameid, nickname);
    }

    public void join(String username, String gameid, String nickname) {
        User user = accountService.findByUsername(username);
        gameid = gameid.toUpperCase();

        LobbyPlayer oldLobby = playerMap.get(user.getId());
        if (oldLobby != null)
            leave(playerMap.get(user.getId()));

        LobbyEntity lobbyEntity = lobbyMap.get(gameid);
        if (lobbyEntity == null)
            return;

        if (nickname == null || nickname.equals(""))
            nickname = generateNewNickname();
        else if (invalidNickname(nickname))
            return;

        LobbyPlayer lobbyPlayer = lobbyEntity.addPlayer(user, nickname);
        playerMap.put(user.getId(), lobbyPlayer);
        if (!lobbyEntity.getStartedState() && !lobbyEntity.getPrivate()) {
            Map<String, Object> messageMap = new HashMap<>();
            messageMap.put("action", "openlobby");
            messageMap.put("gameid", gameid);
            messageMap.put("name", lobbyEntity.getRuleModel().getName());
            messageMap.put("players", Integer.toString(lobbyEntity.getPlayerSize()));

            sendMessages(Arrays.asList(new PlayerMessage(PlayerMessageType.BROADCAST, "public", messageMap)));
        }

        Map<String, Object> lobbyInfoMap = new HashMap<>();
        lobbyInfoMap.put("action", "lobbyinfo");
        lobbyInfoMap.put("gameid", gameid);
        lobbyInfoMap.put("gamemode", lobbyEntity.getRuleModel().getName());
        lobbyInfoMap.put("ispublic", (lobbyEntity.getPrivate() ? "false" : "true"));
        lobbyInfoMap.put("palyerreadycount", Integer.toString(lobbyEntity.getReadyPlayerCount()));
        lobbyInfoMap.put("players", Integer.toString(lobbyEntity.getPlayerSize()));
        lobbyInfoMap.put("maxplayers", Integer.toString(lobbyEntity.getMaxPlayers()));

        Map<String, Object> messageMap = new HashMap<>();
        messageMap.put("action", "join");
        messageMap.put("playerid", lobbyPlayer.getId());
        messageMap.put("nickname", nickname);

        sendMessages(Arrays.asList(new PlayerMessage[] {
                new PlayerMessage(PlayerMessageType.BROADCAST, lobbyPlayer.getLobby().getGameId(), messageMap),
                new PlayerMessage(PlayerMessageType.PRIVATE, username, lobbyInfoMap) }));
    }

    public void getProfile(String username) {
        UserStatistics userStatistics = userStatisticService.getUserStatistics(username);
        Map<String, Object> messageMap = new HashMap<>();
        messageMap.put("action", "profile");
        messageMap.put("username", username);
        messageMap.put("gameswon", Long.toString(userStatistics.getGameswon()));
        messageMap.put("gamesplayed", Long.toString(userStatistics.getGamesplayed()));

        sendMessages(Arrays.asList(new PlayerMessage(PlayerMessageType.PRIVATE, username, messageMap)));
    }

    // Unsure if this is going to be needed, but keeping it anyway
    public void leave(String username) {
        LobbyPlayer lobbyPlayer = getPlayerFromUsername(username);

        leave(lobbyPlayer);
    }

    public void setReadyStatus(String username, boolean ready) {
        LobbyPlayer lobbyPlayer = getPlayerFromUsername(username);
        if (lobbyPlayer == null)
            return;

        LobbyEntity lobbyEntity = lobbyPlayer.getLobby();
        if (lobbyEntity.getStartedState())
            return;

        if (ready != lobbyPlayer.ready()) {
            lobbyPlayer.setReady(ready);

            if (ready)
                lobbyEntity.setReadyPlayerCount(lobbyEntity.getReadyPlayerCount() + 1);
            else
                lobbyEntity.setReadyPlayerCount(lobbyEntity.getReadyPlayerCount() - 1);
        }

        if (lobbyEntity.getReadyPlayerCount() == lobbyEntity.getPlayerSize()
                && lobbyEntity.getPlayerSize() <= lobbyEntity.getMaxPlayers()
                && lobbyEntity.getPlayerSize() >= lobbyEntity.getMinPlayers()) {
            loadGame(lobbyEntity);
        } else {
            Map<String, Object> messageMap = new HashMap<>();
            messageMap.put("action", "updatereadystatus");
            messageMap.put("playerid", lobbyPlayer.getId());
            messageMap.put("votecounter", Integer.toString(lobbyEntity.getReadyPlayerCount()));
            messageMap.put("players", Integer.toString(lobbyEntity.getPlayerSize()));

            sendMessages(Arrays.asList(
                    new PlayerMessage(PlayerMessageType.BROADCAST, lobbyPlayer.getLobby().getGameId(), messageMap)));
        }
    }

    public LobbyEntity findByGameId(String gameId) {
        return lobbyMap.get(gameId);
    }

    public JoinLobbyForm getEditForm(LobbyEntity lobbyEntity) {
        JoinLobbyForm joinLobbyForm = new JoinLobbyForm();
        joinLobbyForm.setGameid(lobbyEntity.getGameId());
        return joinLobbyForm;
    }

    public boolean gameidIsPresent(String gameid) {
        return lobbyMap.containsKey(gameid);
    }

    public void vote(String username, String voteon, boolean flag) {
        LobbyPlayer voter = getPlayerFromUsername(username);
        if (voter == null)
            return;

        /*
         * LobbyPlayer voteonPlayer = voter.getLobby().getAlivePlayer(voteon);
         * if (voteonPlayer == null || (voter.getVoted() != null &&
         * voter.getVoted().equals(voteon) && status)
         * || voter.getLobby().getDeadPlayer(voter.getId()) != null ||
         * voter.getId().equals(voteon) || voter.getLobby().getDeadPlayer(voteon) !=
         * null)
         * return;
         * 
         * LobbyPlayer oldVoteTarget = null;
         * if (voter.getVoted() != null) {
         * oldVoteTarget = voter.getLobby().getAlivePlayer(voter.getVoted());
         * }
         * 
         * lobbyVote(voter.getLobby(), voter, voteonPlayer, oldVoteTarget, status);
         */

        LobbyEntity lobbyEntity = voter.getLobby();
        if (lobbyEntity == null)
            return;

        LobbyPlayer target = lobbyEntity.getPlayer(voteon);
        if (target == null)
            return;

        LobbyPlayer oldTarget = null;
        String voted = voter.getVoted();
        if (voted != null)
            oldTarget = lobbyEntity.getPlayer(voted);

        voter.getLobby().getGame().vote(lobbyEntity, voter, target, oldTarget, flag);
    }

    public void getPlayers(String username) {
        List<PlayerMessage> messages = new ArrayList<>();

        LobbyPlayer lobbyPlayer = getPlayerFromUsername(username);
        if (lobbyPlayer == null)
            return;

        LobbyEntity lobbyEntity = lobbyPlayer.getLobby();

        if (lobbyEntity != null) {
            for (LobbyPlayer lp : lobbyEntity.getPlayers()) {
                Map<String, Object> messageMap = new HashMap<>();
                messageMap.put("action", "join");
                messageMap.put("playerid", lp.getId());
                messageMap.put("nickname", lp.getNickname());
                messageMap.put("isowner", lp.getId() == lobbyPlayer.getId() ? "true" : "false");
                messages.add(new PlayerMessage(PlayerMessageType.PRIVATE, username, messageMap));
            }
            /*
             * if (lobbyEntity.getStartedState())
             * messageMap.put("action", "lobbyready");
             */
        }

        // TODO: FIX

        /*
         * messageMap.put("action", "updatereadystatus");
         * messageMap.put("playerid", lobbyPlayer.getId());
         * messageMap.put("votecounter",
         * Integer.toString(lobbyEntity.getReadyPlayerCount()));
         * messageMap.put("players", Integer.toString(lobbyEntity.getPlayerSize()));
         */

        sendMessages(messages);
    }

    public void initializeLobby(String username) {
        Map<String, Object> messageMap = new HashMap<>();

        LobbyPlayer lobbyPlayer = getPlayerFromUsername(username);
        if (lobbyPlayer == null)
            return;

        LobbyEntity lobbyEntity = lobbyPlayer.getLobby();
        if (lobbyEntity == null)
            return;

        if (lobbyEntity.getStartedState())
            messageMap.put("action", "lobbyready");
        else
            messageMap.put("action", "lobby");

        sendMessages(Arrays.asList(new PlayerMessage(PlayerMessageType.PRIVATE, username, messageMap)));
    }

    public void initializePlayer(String username) {
        LobbyPlayer lobbyPlayer = getPlayerFromUsername(username);
        if (lobbyPlayer == null)
            return;

        LobbyEntity lobbyEntity = lobbyPlayer.getLobby();
        if (lobbyEntity == null)
            return;

        lobbyEntity.getGame().initializePlayer(lobbyEntity, lobbyPlayer);
    }

    /*
     * public void initializeGame(String username) {
     * List<PlayerMessage> messageList = new ArrayList<>();
     * 
     * LobbyPlayer lobbyPlayer = getPlayerFromUsername(username);
     * if (lobbyPlayer == null)
     * return;
     * 
     * LobbyEntity lobbyEntity = lobbyPlayer.getLobby();
     * 
     * if (lobbyEntity != null) {
     * if (lobbyPlayer.getAlignment().equals("Evil")) {
     * for(LobbyPlayer p : lobbyEntity.getEvilTeam()) {
     * Map<String, Object> messageMap = new HashMap<>();
     * messageMap.put("action", "addinvalidtarget");
     * messageMap.put("playerid", p.getId());
     * messageMap.put("rolename", p.getRole().getName());
     * messageMap.put("align", p.getRole().getAlignment().getAlignmentName());
     * messageList.add(new PlayerMessage(PlayerMessageType.PRIVATE, username,
     * messageMap));
     * }
     * }
     * 
     * Map<String, Object> messageMap = new HashMap<>();
     * messageMap.put("action", "addinvalidtarget");
     * messageMap.put("playerid", lobbyPlayer.getId());
     * //messageMap.put("rolename", lobbyPlayer.getRole().getName());
     * //messageMap.put("align",
     * lobbyPlayer.getRole().getAlignment().getAlignmentName());
     * messageList.add(new PlayerMessage(PlayerMessageType.PRIVATE, username,
     * messageMap));
     * 
     * for(LobbyPlayer p : lobbyEntity.getAlivePlayers()) {
     * Map<String, Object> aliveMap = new HashMap<>();
     * messageMap.put("action", "joinalive");
     * messageMap.put("playerid", p.getId());
     * messageMap.put("nickname", p.getNickname());
     * messageMap.put("votes", Integer.toString(p.getVotes()));
     * messageList.add(new PlayerMessage(PlayerMessageType.PRIVATE, username,
     * aliveMap));
     * }
     * 
     * for(LobbyPlayer p : lobbyEntity.getDeadPlayers()) {
     * Map<String, Object> deadMap = new HashMap<>();
     * messageMap.put("action", "joindead");
     * messageMap.put("playerid", p.getId());
     * messageMap.put("nickname", p.getNickname());
     * messageMap.put("role", p.getRole().getName());
     * messageMap.put("align", p.getAlignment());
     * messageList.add(new PlayerMessage(PlayerMessageType.PRIVATE, username,
     * deadMap));
     * }
     * 
     * Map<String, Object> userMap = new HashMap<>();
     * userMap.put("action", "initrole");
     * userMap.put("rolename", lobbyPlayer.getRole().getName());
     * userMap.put("align",
     * lobbyPlayer.getRole().getAlignment().getAlignmentName());
     * userMap.put("goal", lobbyPlayer.getRole().getAlignment().getGoal());
     * userMap.put("desc", lobbyPlayer.getRole().getDescription());
     * messageList.add(new PlayerMessage(PlayerMessageType.PRIVATE, username,
     * userMap));
     * 
     * sendMessages(messageList);
     * }
     * }
     */

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

    public void getOpenLobbies(String username) {
        List<PlayerMessage> messageList = new ArrayList<>();

        lobbyMap.values().forEach((lobby) -> {
            if (!lobby.getStartedState() && !lobby.getPrivate()) {
                Map<String, Object> messageMap = new HashMap<>();
                messageMap.put("action", "openlobby");
                messageMap.put("gameid", lobby.getGameId());
                messageMap.put("name", lobby.getRuleModel().getName());
                messageMap.put("players", Integer.toString(lobby.getPlayerSize()));
                messageList.add(new PlayerMessage(PlayerMessageType.PRIVATE, username, messageMap));
            }
        });

        sendMessages(messageList);
    }

    public void getGamePhase(String username) {
        LobbyPlayer lobbyPlayer = getPlayerFromUsername(username);
        if (lobbyPlayer == null)
            return;

        Map<String, Object> messageMap = new HashMap<>();
        messageMap.put("action", "gamephase");
        messageMap.put("name", lobbyPlayer.getLobby().getPhase().getName());

        sendMessages(Arrays.asList(new PlayerMessage(PlayerMessageType.PRIVATE, username, messageMap)));
    }

    public void getRole(String username) {
        LobbyPlayer lobbyPlayer = getPlayerFromUsername(username);
        if (lobbyPlayer == null)
            return;

        if (lobbyPlayer.getRole() == null)
            return;

        Map<String, Object> messageMap = new HashMap<>();
        messageMap.put("action", "initrole");
        messageMap.put("rolename", lobbyPlayer.getRole().getName());
        messageMap.put("align", lobbyPlayer.getRole().getAlignment().getAlignmentName());
        messageMap.put("goal", lobbyPlayer.getRole().getAlignment().getGoal());
        messageMap.put("desc", lobbyPlayer.getRole().getDescription());

        /*
         * messageList
         * .add(new LobbyMessage("initrole", lobbyPlayer.getRole().getName(),
         * lobbyPlayer.getRole().getAlignment().getAlignmentName(),
         * lobbyPlayer.getRole().getAlignment().getGoal(),
         * lobbyPlayer.getRole().getDescription()));
         * messageList
         * .add(new LobbyMessage("role", lobbyPlayer.getRole().getName(),
         * lobbyPlayer.getRole().getAlignment().getAlignmentName(),
         * lobbyPlayer.getRole().getAlignment().getGoal(),
         * lobbyPlayer.getRole().getDescription()));
         */

        sendMessages(Arrays.asList(new PlayerMessage(PlayerMessageType.PRIVATE, username, messageMap)));
    }

    private int readInt(String integer) {
        int retint = -1;

        try {
            retint = Integer.parseInt(integer);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return retint;
    }

    // TODO: Possibly make this failsafe
    private synchronized String generateNewGameid() {
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
        NameDictionary name = nameDictionaryRepository.findById(num).get();
        num = random.nextInt(repositorySize) + 1;
        NameDictionary surname = nameDictionaryRepository.findById(num).get();

        return name.getName() + " " + surname.getName();
    }

    /*
     * private void createPlayers(LobbyEntity lobbyEntity) {
     * Collection<LobbyPlayer> lobbyPlayers = lobbyEntity.getPlayers();
     * List<RoleInterface> lottery = lobbyEntity.getGame().setRoles(lobbyEntity);
     * 
     * for (LobbyPlayer lobbyPlayer : lobbyPlayers) {
     * Random random = new Random();
     * int sz = lottery.size();
     * int rng = random.nextInt(sz);
     * RoleInterface role = lottery.get(rng);
     * lottery.remove(rng);
     * 
     * lobbyPlayer.setRole(role);
     * lobbyPlayer.setAlignment(role.getAlignment().getAlignmentName());
     * if (role.getAlignment() == Alignments.Evil)
     * lobbyEntity.addToTeamEvil(lobbyPlayer);
     * }
     * }
     */

    private void leave(LobbyPlayer lobbyPlayer) {
        List<LobbyMessage> messageList = new ArrayList<LobbyMessage>();

        if (lobbyPlayer == null)
            return;

        // TODO: FIX THIS MESS

        LobbyEntity lobbyEntity = lobbyPlayer.getLobby();
        lobbyEntity.removePlayer(lobbyPlayer);
        playerMap.remove(lobbyPlayer.getUser().getId());

        if (lobbyEntity.getPlayerSize() == 0) {
            Map<String, Object> messageMap = new HashMap<>();
            messageMap.put("action", "removeopenlobby");
            messageMap.put("gameid", lobbyEntity.getGameId());

            sendMessages(Arrays.asList(new PlayerMessage(PlayerMessageType.BROADCAST, "public", messageMap)));
            lobbyMap.remove(lobbyEntity.getGameId());
        } else {
            if (!lobbyEntity.getStartedState()) {
                Map<String, Object> messageMap = new HashMap<>();
                messageMap.put("action", "openlobby");
                messageMap.put("gameid", lobbyEntity.getGameId());
                messageMap.put("players", Integer.toString(lobbyEntity.getPlayerSize()));
                sendMessages(Arrays.asList(new PlayerMessage(PlayerMessageType.BROADCAST, "public", messageMap)));
                messageList.add(new LobbyMessage("leave", lobbyPlayer.getId(), lobbyPlayer.getNickname()));
                if (lobbyPlayer.ready()) {
                    lobbyEntity.setReadyPlayerCount(lobbyEntity.getReadyPlayerCount() - 1);
                    messageList.add(new LobbyMessage("updatereadystatus", lobbyPlayer.getId(),
                            Integer.toString(lobbyEntity.getReadyPlayerCount()),
                            Integer.toString(lobbyEntity.getPlayerSize())));
                }
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

        if (lobbyEntity.getReadyPlayerCount() != lobbyEntity.getReadyPlayerCount())
            return;

        Map<String, Object> messageMap = new HashMap<>();
        messageMap.put("action", "removeopenlobby");
        messageMap.put("gameid", lobbyEntity.getGameId());
        sendMessages(Arrays.asList(new PlayerMessage(PlayerMessageType.BROADCAST, "public", messageMap)));

        lobbyEntity.getPlayers().forEach((p) -> lobbyEntity.addAlivePlayer(p));

        // createPlayers(lobbyEntity);
        lobbyEntity.getGame().initializeGame(lobbyEntity);

        /*
         * for(LobbyPlayer p : lobbyEntity.getPlayers())
         * initializeGame(p.getUser().getUsername());
         */
    }

    private void lobbyNightAction(LobbyEntity lobbyEntity, LobbyPlayer acter, LobbyPlayer oldTarget, LobbyPlayer target,
            boolean act) {
        lobbyEntity.getGame().nightAction(lobbyEntity, acter, oldTarget, target, act);
    }

    private void lobbyVote(LobbyEntity lobbyEntity, LobbyPlayer voter, LobbyPlayer voteTarget,
            LobbyPlayer oldVoteTarget, boolean status) {

        lobbyEntity.getGame().vote(lobbyEntity, voter, voteTarget, oldVoteTarget, status);
    }

    protected void broadcastMessage(String gameid, String message) {
        simpTemplate.convertAndSend("/action/broadcast/" + gameid, message);
    }

    protected void privateMessage(String user, String message) {
        simpTemplate.convertAndSendToUser(user, "/action/private", message);
    }

    protected void sendMessages(List<PlayerMessage> messageList) {
        if (messageList == null)
            return;

        for (PlayerMessage pm : messageList) {
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
}
