package edu.duke651.wlt.models;

import org.json.JSONObject;

import java.util.Map;

/**
 * @program: wlt-risc
 * @description: This is an inheritance of Order Class, moving units from one territory to another reachable one.
 * @author: Leo
 * @create: 2020-04-09 12:15
 **/
public class MoveOrder extends Order {
    public MoveOrder(Player player, Territory source, Territory aim, int num) {
        this.player = player;
        this.source = source;
        this.aim = aim;
        this.type = "move";
        if (source.getTerritoryUnits() >= num) {
            this.source.reduceUnits(num);
            this.numUnits = num;
        } else {
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
        System.out.println("Move order creation failed: not enough units.\nPlayer: " + player.getPlayerName() + "; sourceTerritory: " + source.getTerritoryName() + "; aimTerritory: " + aim.getTerritoryName() + "; demand units: " + numUnits + " / available units: " + source.getTerritoryUnits());
    }

    /**
    * @Description: This function runOrder is to execute the move order.
    * @Param: []
    * @return: void
    * @Author: Leo
    * @Date: 2020/4/13
    */
    private void runOrder() {
        aim.increaseUnits(numUnits);
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
        if (checkLegal())
            runOrder();
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
        return player.checkReachable(source, aim);
    }

    /**
     * @Description: This function deserialize is to generate new order based on info received by server from clients.
     * @Param: [moveObject, playerMap, territoryMap]
     * @return: edu.duke651.wlt.models.AttackOrder
     * @Author: Will
     * @Date: 2020/4/13
     */
    public static MoveOrder deserialize(JSONObject moveObject, Map<String, Player> playerMap, Map<String, Territory> territoryMap) {
        return new MoveOrder(playerMap.get(moveObject.getString("player")),
                territoryMap.get(moveObject.getString("source")),
                territoryMap.get(moveObject.getString("aim")),
                moveObject.getInt("num"));
    }
}
