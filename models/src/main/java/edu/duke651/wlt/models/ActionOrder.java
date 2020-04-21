package edu.duke651.wlt.models;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @program: wlt-risc
 * @description: This is the order class which is abstract class of all orders
 * @author: Leo
 * @create: 2020-04-21 20:21
 **/
public abstract class ActionOrder extends Order {
    //fields:
    protected Territory source;
    protected Territory aim;
    protected int numUnits;
    protected int cost;
    //This is for EVO2
    protected ArrayList<Integer> units;

    //methods:
    public abstract int getCost();

    public ArrayList<Integer> getUnits() {
        return units;
    }

    public void setUnits(ArrayList<Integer> units) {
        this.units = units;
    }

    public Territory getSource() {
        return this.source;
    }
    public int getNumUnits() {
        return this.numUnits;
    }
    public void moveOut() {
        this.source.reduceUnits(this.numUnits);
    }
    //public abstract boolean checkSourceAndAim();
    public JSONObject serialize() {
        JSONObject orderObject = new JSONObject();
        orderObject.put("type", this.type);
        orderObject.put("player", this.player.getPlayerName());
        orderObject.put("source", this.source.getTerritoryName());
        orderObject.put("aim", this.aim.getTerritoryName());
        orderObject.put("num", this.numUnits);

        return orderObject;
    }
}
