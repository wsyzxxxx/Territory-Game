package edu.duke651.wlt.server;

import edu.duke651.wlt.models.*;
import org.json.JSONObject;
import org.json.JSONString;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @program: wlt-risc
 * @description: This is the game controller class which facilities the game in turn.
 * @author: Leo
 * @create: 2020-04-11 11:20
 **/
public class GameController {
    private HashMap<String, Player> players;
    private HashMap<Player, LinkInfo> playerLinkInfoHashMap;
    private HashMap<String, Territory> territoryMap;
    private MessageSender messageSender;
    private MessageReceiver messageReceiver;
    private ArrayList<ArrayList<Territory>> territoryGroups;

    /**
    * @Description: This function GameController is to initialize (thus build) a list of players based on client connections and their inputs, a list of connection info, a messageReceiver and a messageSender to communicate with all the clients. The first might throw IOException.
    * @Param: []
    * @return: 
    * @Author: Leo
    * @Date: 2020/4/13
    */
    public GameController() throws IOException {
        this.players = new HashMap<>();
        this.playerLinkInfoHashMap = new HashMap<>();
        this.messageReceiver = new MessageReceiver();
        this.messageSender = new MessageSender();

        System.out.println("Init map");
        territoryMapInit();
        System.out.println("Create Link!");
        createLink();
        System.out.println("Assign map");
        assignTerritory();
    }

    /**
    * @Description: This function startGame is to start the game which turns into a loop, ending when there is only one player left is the winner.
    * @Param: []
    * @return: void
    * @Author: Leo
    * @Date: 2020/4/13
    */
    public void startGame() throws IOException {
        while (!isGameOver()) {
            takeTurn();
            deletePlayers();
        }
        endGame();
    }

    /**
    * @Description: This function endGame is to send finish message to all the clients connected, indicating a closure of them.
    * @Param: []
    * @return: void
    * @Author: Leo
    * @Date: 2020/4/13
    */
    private void endGame() throws IOException {
        //send finish message
       for (LinkInfo linkInfo : playerLinkInfoHashMap.values()) {
           messageSender.sendFinishMessage(linkInfo, players.values().iterator().next().getPlayerName());
           linkInfo.closeLink();
       }
    }

    /**
    * @Description: This function createLink is to create links between clients and server.
    * @Param: []
    * @return: void
    * @Author: Will
    * @Date: 2020/4/13
    */
    private void createLink() throws IOException {
        ServerSocket serverSocket = new ServerSocket(ServerSetting.PORT);
        while (playerLinkInfoHashMap.size() < ServerSetting.PLAYER_NUM) {
            LinkInfo linkInfo = new LinkInfo(serverSocket.accept());
            linkInfo.setPlayerName(linkInfo.readMessage());
            Player player = new Player(linkInfo.getPlayerName());
            playerLinkInfoHashMap.put(player, linkInfo);
            players.put(player.getPlayerName(), player);
            System.out.println("link: " + playerLinkInfoHashMap.size());
        }
        serverSocket.close();
    }

    /**
    * @Description: This function takeTurn is the turns in the game loop where players receive game info and then make move and attack decisions and send to server. Server is responsible for sending game info to every player at the start of every turn, and then receive orders, and then execute them. At the end of each turn, there should be a check whether one or more players lose the game and thus should be ignored of their incoming turns.
    * @Param: []
    * @return: void
    * @Author: Leo
    * @Date: 2020/4/13
    */
    private void takeTurn() throws IOException {
        //broadcast the map and player setting
        messageSender.sendResults(playerLinkInfoHashMap, territoryMap);

        //request new orders
        //for (LinkInfo linkInfo : playerLinkInfoHashMap.values())
        //messageSender.sendMessage(linkInfo, oneJSONObject);

        //receive new orders
        ArrayList<Order> moveOrders = new ArrayList<>();
        ArrayList<Order> attackOrders = new ArrayList<>();
        for (LinkInfo linkInfo : playerLinkInfoHashMap.values()) {
            if (players.containsKey(linkInfo.getPlayerName())) {
                moveOrders.addAll(messageReceiver.receiveNewTurn(linkInfo, players, territoryMap, "move"));
                attackOrders.addAll(messageReceiver.receiveNewTurn(linkInfo, players, territoryMap, "attack"));
            }
        }

        //miss simulation execution here. This seems OK with current layout.
        //execute new orders
        //move orders first
        for(Order order: moveOrders) {
            order.execute();
        }
        //attack order second
        for(Order order: attackOrders) {
            order.execute();
        }
        //increment territory unit by 1
        for (Territory territory : territoryMap.values()) {
            territory.incrementUnits();
        }
    }

    /**
    * @Description: This function deletePlayers is to delete players from the player map who have lost all the territories.
    * @Param: []
    * @return: void
    * @Author: Leo
    * @Date: 2020/4/13
    */
    private void deletePlayers() {
        ArrayList<String> removeList = new ArrayList<>();
        for (Player player : players.values()) {
            if (player.checkLose()) removeList.add(player.getPlayerName());
        }

        for (String playerName : removeList) {
            players.remove(playerName);
        }
    }

    /**
    * @Description: This function isGameOver is to check whether the game is over by checking the size of player map.
    * @Param: []
    * @return: boolean
    * @Author: Leo
    * @Date: 2020/4/13
    */
    private boolean isGameOver() {
        return players.size() == 1;
    }

    /**
    * @Description: This function assignTerritory is to assign territory groups randomly to all the players connected.
    * @Param: []
    * @return: void
    * @Author: Leo
    * @Date: 2020/4/13
    */
    private void assignTerritory() {
        //randomly assign territories to players
        Random random = new Random();
        for (Player player: players.values()) {
            ArrayList<Territory> territoryArrayList = this.territoryGroups.get(random.nextInt(territoryGroups.size()));
            for (Territory territory: territoryArrayList) {
                territory.setTerritoryOwner(player);
                player.addTerritory(territory);
                territoryMap.put(territory.getTerritoryName(), territory);
            }
            //messageSender.sendTerritoryList(playerLinkInfoHashMap.get(player), territoryArrayList);
            territoryGroups.remove(territoryArrayList);
        }

    }

    /**
    * @Description: This function territoryMapInit is to get a new map layout from the class TerritoryMapInit. This class can be inherited for more different maps. 
    * @Param: []
    * @return: void
    * @Author: Leo
    * @Date: 2020/4/13
    */
    private void territoryMapInit() {
        TerritoryMapInit map = new TerritoryMapInit();
        this.territoryMap = map.getMap();
        this.territoryGroups = map.getGroups();
    }

}
