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
            //this.source.reduceUnits(num);
            this.numUnits = num;
        }
        else {
            promptFail();
            this.numUnits = 0;
        }
    }

    /**
    * @Description: This function promptFail is to prompt to server when construction of order fails.
    * @Param: []
    * @return: void
    * @Author: Leo
    * @Date: 2020/4/13
    */
    private void promptFail() {
        throw new IllegalArgumentException("Attack order creation failed: not enough units.\nPlayer: " + player.getPlayerName() + "; sourceTerritory: " + source.getTerritoryName() + "; aimTerritory: " + aim.getTerritoryName() + "; demand units: " + numUnits + " / available units: " + source.getTerritoryUnits());
    }

    /**
    * @Description: This function runOrder is to run attack orders as instruction requires.
    * @Param: []
    * @return: void
    * @Author: Leo
    * @Date: 2020/4/13
    */
    private void runOrder() {
        //the place has been occupied
        if (aim.getTerritoryOwner() == source.getTerritoryOwner()) {
            aim.increaseUnits(this.numUnits);
            return;
        }

        //attack
        Random dice = new Random();
        int attackUnits = numUnits;
        int defendUnits = aim.getTerritoryUnits();
        while (true) {
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

    /**
    * @Description: This function execute is to execute the order with some checks to ensure it executable.
    * @Param: []
    * @return: void
    * @Author: Leo
    * @Date: 2020/4/13
    */
    @Override
    public void execute() {
        //if (checkLegal())
        runOrder();
        //if the territory is taken by self's another army from another territory, then just add the numUnits.
        //else if (source.checkNeighbor(aim) && aim.getTerritoryOwner().equals(player))
        //    aim.increaseUnits(numUnits);
    }

    /**
    * @Description: This function checkLegal is to check whether the attack order is constructed correctly by checking whether attacking neighbor and its owner.
    * @Param: []
    * @return: boolean
    * @Author: Leo
    * @Date: 2020/4/13
    */
    @Override
    public boolean checkLegal() {
        return this.numUnits >= 0 &&
               this.numUnits <= this.source.getTerritoryUnits() &&
               this.source.checkNeighbor(aim) &&
               this.source.getTerritoryOwner() == this.player &&
               this.aim.getTerritoryOwner() != this.player;
    }

    /**
    * @Description: This function deserialize is to generate new order based on info received by server from clients.
    * @Param: [moveObject, playerMap, territoryMap]
    * @return: edu.duke651.wlt.models.AttackOrder
    * @Author: Will
    * @Date: 2020/4/13
    */
    public static AttackOrder deserialize(JSONObject moveObject, Map<String, Player> playerMap, Map<String, Territory> territoryMap) {
        return new AttackOrder(playerMap.get(moveObject.getString("player")),
                territoryMap.get(moveObject.getString("source")),
                territoryMap.get(moveObject.getString("aim")),
                moveObject.getInt("num"));
    }
}
