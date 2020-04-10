package edu.duke651.wlt.models;

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

    Territory(String territoryName) {
        this.territoryName = territoryName;
        territoryUnits = 0;
    }

    Territory(String name, Map<String, Territory> neighbors) {
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
}
