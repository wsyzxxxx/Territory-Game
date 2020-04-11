package edu.duke651.wlt.models;

import org.json.JSONObject;

import java.util.Map;

/**
 * @program: wlt-risc
 * @description: This is an inheritance of Order Class.
 * @author: Leo
 * @create: 2020-04-09 12:15
 **/
public class MoveOrder extends Order {

<<<<<<< HEAD
    public MoveOrder(Player player, Territory source, Territory aim, int num) {
=======
    public MoveOrder(Player player,Territory source, Territory aim, int num) {
>>>>>>> adf0c466f6c79205bbd154ca374fecfe4ba6823b
        this.player = player;
        this.source = source;
        this.aim = aim;
        this.type = "move";
        if (source.getTerritoryUnits() >= num) {
            this.source.reduceUnits(num);
            this.numUnits = num;
        }
        else {
            promptFail();
            this.numUnits = 0;
        }
    }

    private void promptFail() {
        System.out.println("Move order creation failed: not enough units.\nPlayer: " + player.getPlayerName() + "; sourceTerritory: " + source.getTerritoryName() + "; aimTerritory: " + aim.getTerritoryName() + "; demand units: " + numUnits + " / available units: " + source.getTerritoryUnits());
    }

    private void runOrder() {
        source.reduceUnits(numUnits);
        aim.increaseUnits(numUnits);
    }

    @Override
    public void execute() {
        if (checkLegal())
            runOrder();

    }

    @Override
    public boolean checkLegal() {
        return player.checkReachable(source, aim) && source.getTerritoryUnits() >= numUnits;
    }

    public static MoveOrder deserialize(JSONObject moveObject, Map<String, Player> playerMap, Map<String, Territory> territoryMap) {
        return new MoveOrder(playerMap.get(moveObject.getString("player")),
                territoryMap.get(moveObject.getString("source")),
                territoryMap.get(moveObject.getString("aim")),
                moveObject.getInt("num"));
    }
}
