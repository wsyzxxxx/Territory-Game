package edu.duke651.wlt.models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

/**
 * @program: wlt-risc
 * @description: This is the order to quarantine a territory.
 * @author: Leo
 * @create: 2020-04-29 14:39
 **/
public class QuarantineOrder extends Order {
    private Territory source;

    public QuarantineOrder(Territory source) {
        this.source = source;
        this.type = "quarantine";
        this.player = source.getTerritoryOwner();
    }

    public Territory getSource() {
        return source;
    }

    public void setSource(Territory source) {
        this.source = source;
    }

    @Override
    public void execute() {
        source.setQuarantineMode(true);
    }

    @Override
    public boolean checkLegal() {
        return true;
    }

    @Override
    public Player getPlayer() {
        return super.getPlayer();
    }

    @Override
    public String getType() {
        return super.getType();
    }

    @Override
    public JSONObject serialize() {
        JSONObject orderObject = new JSONObject();
        orderObject.put("type", this.type);
        orderObject.put("player", this.player.getPlayerName());
        orderObject.put("source", this.source.getTerritoryName());

        return orderObject;
    }

    public static QuarantineOrder deserialize(JSONObject quarantineOrderObject, Map<String, Territory> territoryMap) {
        return new QuarantineOrder(territoryMap.get(quarantineOrderObject.getString("source")));
    }
}
