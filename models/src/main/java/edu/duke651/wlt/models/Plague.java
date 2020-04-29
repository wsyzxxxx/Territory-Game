package edu.duke651.wlt.models;

import java.util.Random;

/**
 * @program: wlt-risc
 * @description: This is the plague class which operates on territories.
 * @author: Leo
 * @create: 2020-04-28 14:51
 **/
public class Plague {
    private static double naturalAppearProbability = 0.1;
    private static double naturalPropagateProbability = 0.2;
    private static double disappearProbability = 0.2;
    private static double actionOrderPropagateProbability = 0.5;
    private static int burstSeverity = 10;
    private static Random r = new Random();

    public static void naturalAppear(Territory source) { //10%
        if (r.nextDouble() < naturalAppearProbability) {
            propagateToOneTerritory(source);
            System.out.println("Plague naturally appears in " + source.getTerritoryName());
        }
    }

    public static void naturalPropagate(Territory source) { //20%
        //Random r = new Random();
        if (!source.isPlagueMode() || source.isQuarantineMode()) return;
        for (Territory aim : source.getTerritoryNeighbors().values()) {
            if (r.nextDouble() < naturalPropagateProbability) {
                propagateToOneTerritory(aim);
                System.out.println("Plague naturally propagates to " + aim.getTerritoryName() + " from " + source.getTerritoryName());
            }
        }
    }

    public static void burst(Territory source) {
        if (source.isPlagueMode()) {
            int unitsToKill = source.getTerritoryUnits() / burstSeverity + 1;
            source.randomKillUnits(unitsToKill);
            System.out.println("Plague bursts in " + source.getTerritoryName() + " and kills " + unitsToKill + " units");
        }
    }

    public static void naturalDisappear(Territory source)  {//20%
        if (r.nextDouble() < disappearProbability) {
            forceDisappear(source);
            System.out.println("Plague naturally disappears in " + source.getTerritoryName());
        }
    }

    public static void forceDisappear(Territory source) {
        source.setPlagueMode(false);
        System.out.println("Plague eliminated in " + source.getTerritoryName());
    }

    public static void actionOrderPropagate(Territory source, Territory aim) { //50%
        //Random r = new Random();
        if (source.isPlagueMode() && r.nextDouble() < actionOrderPropagateProbability) {
            propagateToOneTerritory(aim);
            System.out.println("Plague propagates to " + aim.getTerritoryName() + " from " + source.getTerritoryName() + "by action orders");
        }
    }

    public static void propagateToOneTerritory(Territory aim) {
        aim.setPlagueMode(true);
    }
}
