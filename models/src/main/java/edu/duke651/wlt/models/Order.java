package edu.duke651.wlt.models;

import org.json.JSONObject;

import java.util.Map;

/**
 * @program: wlt-risc
 * @description:
 * @author: Leo
 * @create: 2020-04-09 11:58
 **/
public abstract class Order {
    //fields:
    protected Player player;
    protected Territory source;
    protected Territory aim;
    protected int numUnits;
    protected String type;

    //methods:
    public abstract void execute();
    public abstract boolean checkLegal();
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
