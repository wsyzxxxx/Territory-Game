package edu.duke651.wlt.server;

/**
 * @program: wlt-risc
 * @description: This is an inheritance of Order Class.
 * @author: Leo
 * @create: 2020-04-09 12:15
 **/
public class MoveOrder extends Order {

    MoveOrder(Player player,Territory source, Territory aim, int num) {
        this.player = player;
        this.source = source;
        this.aim = aim;
        this.numUnits = num;
    }

    public void execute() {
        if (checkLegal())
            runOrder();

    }

    private void runOrder() {
        source.reduceUnits(numUnits);
        aim.increaseUnits(numUnits);
    }

    private boolean checkLegal() {
        return player.checkReachable(source, aim) && source.getTerritoryUnits() >= numUnits;
    }

}
