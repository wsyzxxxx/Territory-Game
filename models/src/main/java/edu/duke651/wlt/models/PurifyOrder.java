package edu.duke651.wlt.models;

import org.json.JSONObject;

/**
 * @program: wlt-risc
 * @description: This is the order to purify a territory from plague by paying tech resource.
 * @author: Leo
 * @create: 2020-04-29 14:33
 **/
public class PurifyOrder extends Order{

    private Territory source;

    public PurifyOrder(Territory source) {
        this.player = source.getTerritoryOwner();
        this.source = source;
        this.type = "purify";
    }

    @Override
    public void execute() {

    }

    @Override
    public boolean checkLegal() {
        return false;
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
        return null;
    }
}
