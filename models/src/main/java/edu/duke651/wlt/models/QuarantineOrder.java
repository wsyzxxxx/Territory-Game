package edu.duke651.wlt.models;

import org.json.JSONObject;

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
        return null;
    }
}
