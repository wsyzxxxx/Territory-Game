package edu.duke651.wlt.models;

import javafx.util.Pair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

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
    private int techResources = 0;
    private int foodResources = 0;
    private int techLevel = 1;
    private String colorOnMap;

    //constructors
    public Player(String playerName) {
        this.playerName = playerName;
    }

    public Player(String playerName, HashMap<String, Territory> territories) {
        this.playerName = playerName;
        this.territories = territories;
    }

    //methods:
    public String getColor() {
        return this.colorOnMap;
    }

    public void setColor(String color) {
        this.colorOnMap = color;
    }

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

    public int getTechResources() {
        return techResources;
    }

    public int getTechLevel() {
        return techLevel;
    }

    public void setTechLevel(int techLevel) {
        this.techLevel = techLevel;
    }

    public int getFoodResources() {
        return foodResources;
    }

    public void setFoodResources(int foodResources) {
        this.foodResources = foodResources;
    }

    public int getMinimumMoveSize(Territory source, Territory aim) {
        if (source.getTerritoryOwner() != this || aim.getTerritoryOwner() != this)
            return -1;
        //BFS to search the destination
        Queue<Pair<Territory, Integer>> territoryQueue = new LinkedList<>();
        Map<Territory, Integer> visitedTerritory = new HashMap<>();
        territoryQueue.add(new Pair<>(source, source.getSize()));
        visitedTerritory.put(source, source.getSize());

        while (!territoryQueue.isEmpty()) {
            Pair<Territory, Integer> curr = territoryQueue.poll();

            for (Territory territory : curr.getKey().getTerritoryNeighbors().values()) {
                if (territory.getTerritoryOwner() == this &&
                    (!visitedTerritory.containsKey(territory) || visitedTerritory.get(territory) > curr.getValue() + territory.getSize())) {
                    visitedTerritory.put(territory, curr.getValue() + territory.getSize());
                    territoryQueue.add(new Pair<>(territory, curr.getValue() + territory.getSize()));
                }
            }
        }

        return visitedTerritory.getOrDefault(aim, -1);
    }

    /**
    * @Description: This function collectResource is for a player to collect his tech resource from his territories.
    * @Param: []
    * @return: void
    * @Author: Leo
    * @Date: 2020/4/20
    */
    public void collectTechResource() {
        for (Territory territory : this.territories.values()) {
            this.techResources += territory.getTechResourceGenerate();
        }
    }

    /**
    * @Description: This function collectFoodResource is to collect food resource from all territories.
    * @Param: []
    * @return: void
    * @Author: Leo
    * @Date: 2020/4/24
    */
    public void collectFoodResource() {
        for (Territory territory : this.territories.values()) {
            this.foodResources += territory.getFoodResourceGenerate();
        }
    }

    /**
    * @Description: This function reduceFoodResource is to consume food resource.
    * @Param: [consume]
    * @return: void
    * @Author: Leo
    * @Date: 2020/4/24
    */
    public void consumeFoodResource(int consume) {
        this.foodResources -= consume;
    }

    /**
    * @Description: This function reduceTechResource is to consume tech resource.
    * @Param: [consume]
    * @return: void
    * @Author: Leo
    * @Date: 2020/4/24
    */
    public void consumeTechResource(int consume) {
        this.techResources -= consume;
    }

    /**
     * @Description: This function setTerritories is to replace existing territories with updatedList.
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

    /**
    * @Description: This function checkReachable is to use recursion on self's neighbors to check whether two territories are connected to enable move orders.
    * @Param: [source, aim]
    * @return: boolean
    * @Author: Leo
    * @Date: 2020/4/9
    */
    public boolean checkReachable(Territory source, Territory aim) {
        if (source.getTerritoryOwner() != this || aim.getTerritoryOwner() != this)
            return false;
        //BFS to search the destination
        Queue<Territory> territoryQueue = new LinkedList<>();
        Set<Territory> visitedTerritory = new HashSet<>();
        territoryQueue.add(source);
        visitedTerritory.add(source);

        while (!territoryQueue.isEmpty()) {
            Territory curr = territoryQueue.poll();
            if (curr == aim) {
                return true;
            }
            for (Territory territory : curr.getTerritoryNeighbors().values()) {
                if (territory.getTerritoryOwner() == this && !visitedTerritory.contains(territory)) {
                    visitedTerritory.add(territory);
                    territoryQueue.add(territory);
                }
            }
        }

        return false;
    }

    /**
    * @Description: This function checkLose is to check whether this player loses by checking whether his territory list is empty.
    * @Param: []
    * @return: boolean
    * @Author: Leo
    * @Date: 2020/4/13
    */
    public boolean checkLose() {
        return territories == null || territories.isEmpty();
    }

    /**
    * @Description: This function serialize is to serialize the player in order to send over network.
    * @Param: []
    * @return: org.json.JSONObject
    * @Author: Will
    * @Date: 2020/4/13
    */
    public JSONObject serialize() {
        JSONObject playerObject = new JSONObject();
        playerObject.put("playerName", this.playerName);
        playerObject.put("food", this.foodResources);
        playerObject.put("tech", this.techResources);
        playerObject.put("techLevel", this.techLevel);
        playerObject.put("color", this.colorOnMap);

        JSONArray territoryList = new JSONArray();
        this.territories.forEach((name, territory) -> {
            JSONObject territoryObject = new JSONObject();
            territoryObject.put("name", name);
            territoryList.put(territoryObject);
        });
        playerObject.put("territories", territoryList);

        return playerObject;
    }

    /**
    * @Description: This function deserialize is to deserialize the player after receiving the message.
    * @Param: [playerObject, territoryMap]
    * @return: edu.duke651.wlt.models.Player
    * @Author: Will
    * @Date: 2020/4/13
    */
    public static Player deserialize(JSONObject playerObject, Map<String, Territory> territoryMap) throws JSONException {
        Player player = new Player(playerObject.getString("playerName"));
        player.foodResources = playerObject.getInt("food");
        player.techResources = playerObject.getInt("tech");
        player.techLevel = playerObject.getInt("techLevel");
        player.colorOnMap = playerObject.getString("color");

        playerObject.getJSONArray("territories").forEach(element -> {
            String territoryName = ((JSONObject)element).getString("name");

            if (!territoryMap.containsKey(territoryName)) {
                throw new IllegalArgumentException("territory " + territoryName + " does not exist!");
            }
            player.addTerritory(territoryMap.get(territoryName));
            territoryMap.get(territoryName).setTerritoryOwner(player);
        });

        return player;
    }
}
