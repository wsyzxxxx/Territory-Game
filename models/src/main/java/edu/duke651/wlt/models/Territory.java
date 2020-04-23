package edu.duke651.wlt.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * @program: wlt-risc
 * @description: This is one territory which contains its neighbors, owner, and name.
 * @author: Leo
 * @create: 2020-04-09 11:57
 **/
public class Territory {
    //fields:
    private String territoryName;
    private Player territoryOwner;
    private int territoryUnits;
    private Map<String, Territory> territoryNeighbors = new HashMap<>();
    private int resourceGenerate = 20;
    private int size = 5;
    //This is an array of numbers of different level of units. [0] is number of units of level 0.
    private ArrayList<Integer> territoryUnitsInLevel = new ArrayList<>(Collections.nCopies(7, 0));

    /**
    * @Description: This is the constructor with only territory name.
    * @Param: [territoryName]
    * @return:
    * @Author: Leo
    * @Date: 2020/4/13
    */
    public Territory(String territoryName) {
        this.territoryName = territoryName;
        this.territoryUnits = 0;
    }

    /**
    * @Description: This is the full constructor.
    * @Param: [name, neighbors]
    * @return:
    * @Author: Leo
    * @Date: 2020/4/13
    */
    public Territory(String name, Map<String, Territory> neighbors) {
        this.territoryName = name;
        this.territoryUnits = 0;
        this.territoryNeighbors = neighbors;
    }

    //methods:

    public void setResourceGenerate(int resourceGenerate) {
        this.resourceGenerate = resourceGenerate;
    }

    public ArrayList<Integer> getTerritoryUnitsInLevel() {
        return territoryUnitsInLevel;
    }

    public void setTerritoryUnitsInLevel(ArrayList<Integer> territoryUnitsInLevel) {
        this.territoryUnitsInLevel = territoryUnitsInLevel;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void increaseUnits(int num, ArrayList<Integer> unitsArray) {
        this.territoryUnits += num;
        for (int i = 0; i < this.territoryUnitsInLevel.size(); ++i) {
            this.territoryUnitsInLevel.set(i, this.territoryUnitsInLevel.get(i) + unitsArray.get(i));
        }
    }

    public void reduceUnits(int num, ArrayList<Integer> unitsArray) {
        this.territoryUnits -= num;
        for (int i = 0; i < this.territoryUnitsInLevel.size(); ++i) {
            this.territoryUnitsInLevel.set(i, this.territoryUnitsInLevel.get(i) - unitsArray.get(i));
        }
    }

    public void incrementUnits() {
        this.territoryUnitsInLevel.set(0, this.territoryUnitsInLevel.get(0) + 1);
        ++this.territoryUnits;
    }

    public String getTerritoryName() {
        return territoryName;
    }

    public void setTerritoryName(String territoryName) {
        this.territoryName = territoryName;
    }

    public void setTerritoryNeighbors(Map<String, Territory> territoryNeighbors) {
        this.territoryNeighbors = territoryNeighbors;
    }

    public void addTerritoryNeighbors(Territory neighbour) {
        this.territoryNeighbors.put(neighbour.getTerritoryName(), neighbour);
    }

    public Map<String, Territory> getTerritoryNeighbors() {
        return territoryNeighbors;
    }

    public Player getTerritoryOwner() {
        return territoryOwner;
    }

    public int getTerritoryUnits() {
        return territoryUnits;
    }

    public void setTerritoryOwner(Player territoryOwner) {
        this.territoryOwner = territoryOwner;
    }

    public void setTerritoryUnits(int territoryUnits) {
        this.territoryUnits = territoryUnits;
    }

    public int getResourceGenerate() {
        return resourceGenerate;
    }

    /**
    * @Description: This function checkNeighbor is to check whether one territory is neighbor of aim.
    * @Param: [aim]
    * @return: boolean
    * @Author: Leo
    * @Date: 2020/4/13
    */
    public boolean checkNeighbor(Territory aim) {
        return territoryNeighbors.containsValue(aim);
    }

    /**
     * @Description: This function serialize is to serialize the player in order to send over network.
     * @Param: []
     * @return: org.json.JSONObject
     * @Author: Will
     * @Date: 2020/4/13
     */
    public JSONObject serialize() {
        JSONObject territoryItem = new JSONObject();
        territoryItem.put("name", this.territoryName);
        territoryItem.put("units", this.territoryUnits);
        JSONArray neighbourList = new JSONArray();
        this.territoryNeighbors.keySet().forEach(neighbourList::put);
        territoryItem.put("neighbours", neighbourList);

        return territoryItem;
    }

    /**
     * @Description: This function deserialize is to deserialize the player after receiving the message.
     * @Param: [playerObject, territoryMap]
     * @return: edu.duke651.wlt.models.Player
     * @Author: Will
     * @Date: 2020/4/13
     */
    public static Territory deserialize(JSONObject territoryObject) throws JSONException {
        Territory territory = new Territory(territoryObject.getString("name"));
        territory.setTerritoryUnits(territoryObject.getInt("units"));

        return territory;
    }
}
