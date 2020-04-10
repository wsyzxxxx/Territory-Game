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

    public void execute() {
        if (checkLegal())
            runOrder();

    }

    private void runOrder() {
        source.reduceUnits(numUnits);
        aim.increaseUnits(numUnits);
    }

    @Override
    public boolean checkLegal() {
        return player.checkReachable(source, aim) && source.getTerritoryUnits() >= numUnits;
    }

}
