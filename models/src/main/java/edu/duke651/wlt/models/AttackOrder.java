package edu.duke651.wlt.models;

import org.json.JSONObject;

import java.util.Map;
import java.util.Random;

/**
 * @program: wlt-risc
 * @description: This is an inheritance of Order Class
 * @author: Leo
 * @create: 2020-04-09 12:16
 **/
public class AttackOrder extends Order {
    public AttackOrder(Player player, Territory source, Territory aim, int num) {
        this.player = player;
        this.source = source;
        this.aim = aim;
        this.type = "attack";
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
        System.out.println("Attack order creation failed: not enough units.\nPlayer: " + player.getPlayerName() + "; sourceTerritory: " + source.getTerritoryName() + "; aimTerritory: " + aim.getTerritoryName() + "; demand units: " + numUnits + " / available units: " + source.getTerritoryUnits());
    }

    private void runOrder() {
        Random dice = new Random();
        int attackUnits = numUnits;
        int defendUnits = aim.getTerritoryUnits();
        while(true) {
            System.out.println(player.getPlayerName() + ": from " + source.getTerritoryName() + " to " + aim.getTerritoryName() + "| AttackUnits: " + attackUnits + "; DefendUnits: " + defendUnits);
            if (attackUnits == 0) {
                aim.setTerritoryUnits(defendUnits);
                break;
            }
            if (defendUnits == 0) {
                aim.setTerritoryUnits(attackUnits);
                aim.getTerritoryOwner().removeTerritory(aim);
                aim.setTerritoryOwner(player);
                player.addTerritory(aim);
                break;
            }
            int attackDice = dice.nextInt(20);
            int defendDice = dice.nextInt(20);
            if (attackDice > defendDice) --defendUnits;
            else if (attackDice < defendDice) --attackUnits;
        }
    }

    @Override
    public void execute() {
        if (checkLegal())
            runOrder();
        else if (source.checkNeighbor(aim) && aim.getTerritoryOwner().equals(player))
            aim.increaseUnits(numUnits);
    }

    @Override
    public boolean checkLegal() {
        return source.checkNeighbor(aim) && !aim.getTerritoryOwner().equals(player);
    }

    public static AttackOrder deserialize(JSONObject moveObject, Map<String, Player> playerMap, Map<String, Territory> territoryMap) {
        return new AttackOrder(playerMap.get(moveObject.getString("player")),
                territoryMap.get(moveObject.getString("source")),
                territoryMap.get(moveObject.getString("aim")),
                moveObject.getInt("num"));
    }
}
