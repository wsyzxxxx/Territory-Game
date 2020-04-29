package edu.duke651.wlt.models;

import java.util.Random;

/**
 * @program: wlt-risc
 * @description: This is the plague class which operates on territories.
 * @author: Leo
 * @create: 2020-04-28 14:51
 **/
public class Plague {
    private static double naturalProbability = 0.2;
    private static double disappearProbability = 0.2;
    private static double actionOrderProbability = 0.5;
    private static int burstSeverity = 10;
    private static Random r = new Random();

    public static void naturalPropagate(Territory source) { //20%
        //Random r = new Random();
        for (Territory aim : source.getTerritoryNeighbors().values()) {
            if (r.nextDouble() < naturalProbability) {
                propagateToOneTerritory(aim);
            }
        }
    }

    public static void burst(Territory source) {
        int unitsToKill = source.getTerritoryUnits() / burstSeverity;
        source.randomKillUnits(unitsToKill);
    }

    public static void naturalDisappear(Territory source)  {//20%
        if (r.nextDouble() < disappearProbability) {
            forceDisappear(source);
        }
    }

    public static void forceDisappear(Territory source) {
        source.setPlagueMode(false);
    }

    public static void actionOrderPropagate(Territory aim) { //50%
        //Random r = new Random();
        if (r.nextDouble() < actionOrderProbability) {
            propagateToOneTerritory(aim);
        }
    }

    public static void propagateToOneTerritory(Territory aim) {
        aim.setPlagueMode(true);
    }
}
