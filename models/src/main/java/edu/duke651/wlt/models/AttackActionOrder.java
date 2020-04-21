package edu.duke651.wlt.models;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

/**
 * @program: wlt-risc
 * @description: This is an inheritance of ActionOrder Class
 * @author: Leo
 * @create: 2020-04-09 12:16
 **/
public class AttackActionOrder extends ActionOrder {
    public AttackActionOrder(Player player, Territory source, Territory aim, int num) {
        this.player = player;
        this.source = source;
        this.aim = aim;
        this.type = "attack";
        this.cost = getCost();
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
        if (aim.getTerritoryOwner() == this.player) {
            aim.increaseUnits(this.numUnits);
            return;
        }

        //attack
        Random dice = new Random();
        int attackUnits = numUnits;
        int defendUnits = aim.getTerritoryUnits();
        //for EVO2
        boolean isAttackTurn = true;
        int attackerUpperBond = 6;
        int defenderUpperBond = 6;
        int attackerLowerBond = 0;
        int defenderLowerBond = 0;
        ArrayList<Integer> attackers = this.units;
        ArrayList<Integer> defenders = aim.getTerritoryUnitsInLevel();
        while (true) {
            System.out.println(player.getPlayerName() + ": from " + source.getTerritoryName() + " to " + aim.getTerritoryName() + "| AttackUnits: " + attackUnits + "; DefendUnits: " + defendUnits);
            if (attackUnits == 0) {
                aim.setTerritoryUnits(defendUnits);
                //for EVO2
                aim.setTerritoryUnitsInLevel(defenders);
                break;
            }
            if (defendUnits == 0) {
                aim.setTerritoryUnits(attackUnits);
                aim.getTerritoryOwner().removeTerritory(aim);
                aim.setTerritoryOwner(player);
                player.addTerritory(aim);
                //for EVO2
                aim.setTerritoryUnitsInLevel(attackers);
                break;
            }
            int attackDice = dice.nextInt(20);
            int defendDice = dice.nextInt(20);
            //for EVO2
            if (isAttackTurn) {
                //find the right units to fight
                while(attackers.get(attackerUpperBond) == 0) attackerUpperBond--;
                while(defenders.get(defenderLowerBond) == 0) defenderLowerBond++;
                attackDice += ServerSetting.UNIT_LEVEL_COST_MAP.get(attackerUpperBond).getBonus();
                defendDice += ServerSetting.UNIT_LEVEL_COST_MAP.get(defenderLowerBond).getBonus();
                if (attackDice > defendDice) {
                    --defendUnits;
                    defenders.set(defenderLowerBond, defenders.get(defenderLowerBond) - 1);
                }
                else if (attackDice < defendDice) {
                    --attackUnits;
                    attackers.set(attackerUpperBond, attackers.get(attackerUpperBond) - 1);
                }
                isAttackTurn = false;
            } else {
                while(attackers.get(attackerLowerBond) == 0) attackerLowerBond++;
                while(defenders.get(defenderUpperBond) == 0) defenderUpperBond--;
                attackDice += ServerSetting.UNIT_LEVEL_COST_MAP.get(attackerLowerBond).getBonus();
                defendDice += ServerSetting.UNIT_LEVEL_COST_MAP.get(defenderUpperBond).getBonus();
                if (attackDice > defendDice) {
                    --defendUnits;
                    defenders.set(defenderUpperBond, defenders.get(defenderUpperBond) - 1);
                }
                else if (attackDice < defendDice) {
                    --attackUnits;
                    attackers.set(attackerLowerBond, attackers.get(attackerLowerBond) - 1);
                }
                isAttackTurn = true;
            }
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
    * @return: edu.duke651.wlt.models.AttackActionOrder
    * @Author: Will
    * @Date: 2020/4/13
    */
    public static AttackActionOrder deserialize(JSONObject moveObject, Map<String, Player> playerMap, Map<String, Territory> territoryMap) {
        return new AttackActionOrder(playerMap.get(moveObject.getString("player")),
                territoryMap.get(moveObject.getString("source")),
                territoryMap.get(moveObject.getString("aim")),
                moveObject.getInt("num"));
    }

    @Override
    public int getCost() {
        return numUnits;
    }
}
