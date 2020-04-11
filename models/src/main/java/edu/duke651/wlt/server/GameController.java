package edu.duke651.wlt.server;

import edu.duke651.wlt.models.Player;
import edu.duke651.wlt.models.Territory;
import edu.duke651.wlt.models.TerritoryMapInit;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: wlt-risc
 * @description: This is the game controller class which facilities the game in turn.
 * @author: Leo
 * @create: 2020-04-11 11:20
 **/
public class GameController {
    private HashMap<String, Player> players;
    private HashMap<String, Territory> territoryMap;

    public GameController() {
        territoryMapInit();
        assignTerritory();
    }

    private void gameOver() {
        //Todo
    }

    private void gameStart() {
        while(!isGameOver()) {
            takeTurn();
            deletePlayers();
        }

    }

    private void takeTurn() {
        //TODO
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
        //TODO
    }

    private void territoryMapInit() {
        TerritoryMapInit map = new TerritoryMapInit();
        this.territoryMap = map.getMap();
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
