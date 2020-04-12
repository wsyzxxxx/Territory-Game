package edu.duke651.wlt.server;

import edu.duke651.wlt.models.*;
import org.json.JSONObject;
import org.json.JSONString;

import java.io.IOException;
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
    private HashMap<String, Player> players = new HashMap<>();
    private HashMap<Player, LinkInfo> playerLinkInfoHashMap = new HashMap<>(); //TODO
    private HashMap<String, Territory> territoryMap;
    private MessageSender messageSender = new MessageSender();
    private MessageReceiver messageReceiver = new MessageReceiver();
    private ArrayList<ArrayList<Territory>> territoryGroups;

    public GameController() {
        territoryMapInit();
        assignTerritory();
    }

    private void gameStart() throws IOException {
        while(!isGameOver()) {
            takeTurn();
            deletePlayers();
        }
        gameOver();
    }

    private void gameOver() {
        //
        //send finish message
       for (LinkInfo linkInfo : playerLinkInfoHashMap.values())
            messageSender.sendFinishMessage(linkInfo, players.values().iterator().next().getPlayerName());
    }

    private void takeTurn() throws IOException {
        //
        //broadcast the map and player setting
        messageSender.sendResults(playerLinkInfoHashMap, territoryMap);

        //request new orders
//        for (LinkInfo linkInfo : playerLinkInfoHashMap.values())
//        messageSender.sendMessage(linkInfo, oneJSONObject);

        //receive new orders
        ArrayList<Order> moveOrders = new ArrayList<>();
        ArrayList<Order> attackOrders = new ArrayList<>();
        for (LinkInfo linkInfo : playerLinkInfoHashMap.values()) {
            moveOrders.addAll(messageReceiver.receiveNewTurn(linkInfo, players, territoryMap));
            attackOrders.addAll(messageReceiver.receiveNewTurn(linkInfo, players, territoryMap));
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
    }

    private void deletePlayers() {
        for (Player player : players.values()) {
            if (player.checkLose()) players.remove(player.getPlayerName());
        }
    }

    private boolean isGameOver() {
        return players.size() == 1;
    }

    private void assignTerritory() {
        //
        //randomly assign territories to players
        Random r = new Random();
        for (Player player: players.values()) {
            ArrayList<Territory> territoryArrayList = this.territoryGroups.get(r.nextInt(territoryGroups.size()));
            for (Territory territory: territoryArrayList) {
                territory.setTerritoryOwner(player);
                player.addTerritory(territory);
            }
//            messageSender.sendTerritoryList(playerLinkInfoHashMap.get(player), territoryArrayList);
            territoryGroups.remove(territoryArrayList);
        }

    }

    private void territoryMapInit() {
        TerritoryMapInit map = new TerritoryMapInit();
        this.territoryMap = map.getMap();
        this.territoryGroups = map.getGroups();
    }

    public Map<String, Player> getPlayers() {
        return players;
    }

    public void setPlayers(HashMap<String, Player> players) {
        this.players = players;
    }

    public HashMap<String, Territory> getTerritoryMap() {
        return territoryMap;
    }

    public void setTerritoryMap(HashMap<String, Territory> territoryMap) {
        this.territoryMap = territoryMap;
    }
}
