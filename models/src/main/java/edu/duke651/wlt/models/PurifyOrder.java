package edu.duke651.wlt.models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

/**
 * @program: wlt-risc
 * @description: This is the order to purify a territory from plague by paying tech resource.
 * @author: Leo
 * @create: 2020-04-29 14:33
 **/
public class PurifyOrder extends Order{
    private int techCost = 5;
    private Territory source;

    public PurifyOrder(Territory source) {
        this.player = source.getTerritoryOwner();
        this.source = source;
        this.type = "purify";
    }

    @Override
    public void execute() {
        //did not deduct the tech resource when constructed, as other resource consuming orders.
        this.source.getTerritoryOwner().consumeTechResource(techCost);
        Plague.forceDisappear(this.source);
    }

    @Override
    public boolean checkLegal() { //no illegal order. if the territory is healthy, then overwriting the healthy state with healthy state does not matter.
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
        //TODO
        JSONObject orderObject = new JSONObject();
        orderObject.put("type", this.type);
        orderObject.put("player", this.player.getPlayerName());
        orderObject.put("source", this.source.getTerritoryName());

        return orderObject;
    }

    public static PurifyOrder deserialize(JSONObject orderObject, Map<String, Territory> territoryMap) {
        return new PurifyOrder(territoryMap.get(orderObject.getString("source")));
    }
}
