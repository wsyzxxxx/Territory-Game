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

    public void startGame() throws IOException {
        while (!isGameOver()) {
            takeTurn();
            deletePlayers();
        }
        endGame();
    }

    private void endGame() throws IOException {
        //send finish message
       for (LinkInfo linkInfo : playerLinkInfoHashMap.values()) {
           messageSender.sendFinishMessage(linkInfo, players.values().iterator().next().getPlayerName());
           linkInfo.closeLink();
       }
    }

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

    private void deletePlayers() {
        ArrayList<String> removeList = new ArrayList<>();
        for (Player player : players.values()) {
            if (player.checkLose()) removeList.add(player.getPlayerName());
        }

        for (String playerName : removeList) {
            players.remove(playerName);
        }
    }

    private boolean isGameOver() {
        return players.size() == 1;
    }

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

    private void territoryMapInit() {
        TerritoryMapInit map = new TerritoryMapInit();
        this.territoryMap = map.getMap();
        this.territoryGroups = map.getGroups();
    }

}
