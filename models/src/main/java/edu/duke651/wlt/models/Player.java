package edu.duke651.wlt.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: wlt-risc
 * @description: This is Player class which contains all the information for one player
 * @author: Leo
 * @create: 2020-04-09 11:55
 **/
public class Player {
    //fields:
    private String playerName;
    private HashMap<String, Territory> territories = new HashMap<>();

    //constructors
    public Player(String playerName) {
        this.playerName = playerName;
    }

    public Player(String playerName, HashMap<String, Territory> territories) {
        this.playerName = playerName;
        this.territories = territories;
    }
//    private ArrayList<Order> orders; //this is to be sent to server.

    //methods:
    public void addTerritory(Territory t){
        territories.put(t.getTerritoryName(), t);
    }

    public String getPlayerName() {
        return playerName;
    }

    public void removeTerritory(Territory t) {
        territories.remove(t.getTerritoryName(), t);
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public Map<String, Territory> getTerritories() {
        return territories;
    }

    /**
     * @Description: This function updateTerritories is to replace existing territories with updatedList.
     * @Param: [updatedList]
     * @return: void
     * @Author: Leo
     * @Date: 2020/4/9
     */
    public void setTerritories(HashMap<String, Territory> territories) {
        this.territories = territories;
    }

    /**
    * @Description: This function transferTerritory is to transfer one territory t from this player to aim player by remove t from this player's territories, set t's new owner as aim, and add t to aim's territories.
    * @Param: [aim, t]
    * @return: void
    * @Author: Leo
    * @Date: 2020/4/11
    */
    void transferTerritory(Player aim, Territory t) {
        removeTerritory(t);
        t.setTerritoryOwner(aim);
        aim.addTerritory(t);
    }

    //    void createOrder(Territory source, Territory aim, int num) {
//
//    }

    /**
    * @Description: This function checkReachable is to use recursion on self's neighbors to check whether two territories are connected to enable move orders.
    * @Param: [source, aim]
    * @return: boolean
    * @Author: Leo
    * @Date: 2020/4/9
    */
    boolean checkReachable(Territory source, Territory aim) {

        if (!source.getTerritoryOwner().equals(aim.getTerritoryOwner()))
            return false;
        Territory curr = source;
        ArrayList<Territory> checked = new ArrayList<>();
        ArrayList<Territory> toCheck = new ArrayList<>();
        toCheck.add(source);
        while (!source.checkNeighbor(aim)) {
            checked.add(source);
            for (Territory t : source.getTerritoryNeighbors().values()) {
                if (!checked.contains(t)) toCheck.add(t);
            }
            toCheck.remove(source);
            if (toCheck.isEmpty()) return false;
            source = toCheck.get(0);
        }
        return true;
    }

    public boolean checkLose() {
        return territories.isEmpty();
    }

    public JSONObject serialize() {
        JSONObject playerObject = new JSONObject();
        playerObject.put("playerName", this.playerName);

        JSONArray territoryList = new JSONArray();
        this.territories.forEach((name, territory) -> {
            JSONObject territoryObject = new JSONObject();
            territoryObject.put("name", name);
            territoryObject.put("unit", territory.getTerritoryUnits());
            territoryList.put(territoryObject);
        });
        playerObject.put("territories", territoryList);

        return playerObject;
    }

    public static Player deserialize(JSONObject playerObject, Map<String, Territory> territoryMap) throws JSONException {
        Player player = new Player(playerObject.getString("playerName"));
        playerObject.getJSONArray("territories").forEach(element -> {
            player.addTerritory(territoryMap.get(element));
            territoryMap.get(element).setTerritoryOwner(player);
        });

        return player;
    }
}
