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
    protected String type;

    //methods:
    public abstract void execute();
    public abstract boolean checkLegal();
    public Player getPlayer() {
        return this.player;
    }
    public String getType() {
        return type;
    }
    public abstract JSONObject serialize();
}
