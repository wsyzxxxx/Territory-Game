package edu.duke651.wlt.server;

import javax.swing.*;
import java.awt.event.TextEvent;
import java.util.Random;

/**
 * @program: wlt-risc
 * @description: This is an inheritance of Order Class
 * @author: Leo
 * @create: 2020-04-09 12:16
 **/
public class AttackOrder extends Order {

    AttackOrder(Player player,Territory source, Territory aim, int num) {
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
        System.out.println("Attack order creation failed: not enough units.\nPlayer: " + player.getPlayerName() + "; sourceTerritory: " + source.getTerritoryName() + "; aimTerritory: " + aim.getTerritoryName() + "; demand units: " + numUnits + " / available units: " + source.getTerritoryUnits());
    }

    public void execute() {
        if (checkLegal())
            runOrder();

    }

    private void runOrder() {
        Random dice = new Random();
        int attackUnits = numUnits;
        int defendUnits = aim.getTerritoryUnits();
        while(true) {
            if (attackUnits == 0) {
                aim.setTerritoryUnits(defendUnits);
                break;
            }
            if (defendUnits == 0) {
                aim.setTerritoryUnits(attackUnits);
                aim.setTerritoryOwner(player);
                break;
            }
            int attackDice = dice.nextInt(20);
            int defendDice = dice.nextInt(20);
            if (attackDice > defendDice) --defendUnits;
            else if (attackDice < defendDice) --attackUnits;
        }
    }

    @Override
    public boolean checkLegal() {
        return source.checkNeighbor(aim) && !source.getTerritoryOwner().equals(aim.getTerritoryOwner()) && numUnits <= source.getTerritoryUnits();
    }


}
