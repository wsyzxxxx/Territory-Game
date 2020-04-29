package edu.duke651.wlt.models;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

/**
 * @program: wlt-risc
 * @description: This is an inheritance of ActionOrder Class, moving units from one territory to another reachable one.
 * @author: Leo
 * @create: 2020-04-09 12:15
 **/
public class MoveActionOrder extends ActionOrder {
    public MoveActionOrder(Player player, Territory source, Territory aim, ArrayList<Integer> unitList) {
        this.player = player;
        this.source = source;
        this.aim = aim;
        this.type = "move";
        this.units = unitList;
    }

    public int calculateFoodCost() {
        return this.player.getMinimumMoveSize(source, aim) * sumUnits();
    }

    /**
     * @Description: This function promptFail is to prompt to server when construction of order fails.
     * @Param: []
     * @return: void
     * @Author: Leo
     * @Date: 2020/4/13
     */
    private void promptFail() {
        throw new IllegalArgumentException("Move order creation failed: not enough units.\nPlayer: " + player.getPlayerName() + "; sourceTerritory: " + source.getTerritoryName() + "; aimTerritory: " + aim.getTerritoryName() + "; demand units: " + sumUnits() + " / available units: " + source.getTerritoryUnits());
    }

    /**
    * @Description: This function execute is to execute the move order.
    * @Param: []
    * @return: void
    * @Author: Leo
    * @Date: 2020/4/13
    */
    public void execute() {
        //EVO2
        this.player.consumeFoodResource(calculateFoodCost());
        this.aim.increaseUnits(this.units);

        //EVO3
        if (source.isPlagueMode()) {
            Plague.actionOrderPropagate(aim);
        }
    }

    /**
     * @Description: This function checkLegal is to check whether the attack order is constructed correctly by checking whether moving to reachable territory.
     * @Param: []
     * @return: boolean
     * @Author: Leo
     * @Date: 2020/4/13
     */
    @Override
    public boolean checkLegal() {
        return player.getMinimumMoveSize(this.source, this.aim) != -1 &&
               player.getFoodResources() >= this.calculateFoodCost() &&
               source.hasEnoughUnits(this.units);
    }

    /**
     * @Description: This function deserialize is to generate new order based on info received by server from clients.
     * @Param: [moveObject, playerMap, territoryMap]
     * @return: edu.duke651.wlt.models.AttackActionOrder
     * @Author: Will
     * @Date: 2020/4/13
     */
    public static MoveActionOrder deserialize(JSONObject moveObject, Map<String, Player> playerMap, Map<String, Territory> territoryMap) {
        ArrayList<Integer> unitList = new ArrayList<>();
        moveObject.getJSONArray("unitList").forEach(e -> unitList.add((Integer)e));

        if (unitList.size() != 7) {
            throw new IllegalArgumentException("The unit size is not correct!");
        }

        return new MoveActionOrder(playerMap.get(moveObject.getString("player")),
                territoryMap.get(moveObject.getString("source")),
                territoryMap.get(moveObject.getString("aim")),
                unitList);
    }
}
