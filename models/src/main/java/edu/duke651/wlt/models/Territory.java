package edu.duke651.wlt.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

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
    private Map<String, Territory> territoryNeighbors;

    public Territory(String territoryName) {
        this.territoryName = territoryName;
        this.territoryUnits = 0;
    }

    public Territory(String name, Map<String, Territory> neighbors) {
        this.territoryName = name;
        this.territoryUnits = 0;
        this.territoryNeighbors = neighbors;
    }

    //methods:

    public void increaseUnits(int num) {
        this.territoryUnits += num;
    }

    public void reduceUnits(int num) {
        this.territoryUnits -= num;
    }

    public void incrementUnits() {
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

    public boolean checkNeighbor(Territory aim) {
        return territoryNeighbors.containsValue(aim);
    }

    public JSONObject serialize() {
        JSONObject territoryItem = new JSONObject();
        territoryItem.put("name", this.territoryName);
        territoryItem.put("units", this.territoryUnits);
        JSONArray neighbourList = new JSONArray();
        this.territoryNeighbors.keySet().forEach(neighbourList::put);
        territoryItem.put("neighbours", neighbourList);

        return territoryItem;
    }

    public static Territory deserialize(JSONObject territoryObject) throws JSONException {
        Territory territory = new Territory(territoryObject.getString("name"));
        territory.setTerritoryUnits(territoryObject.getInt("units"));

        return territory;
    }
}