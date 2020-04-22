package edu.duke651.wlt.models;

/**
 * @program: wlt-risc
 * @description:
 * @author: Leo
 * @create: 2020-04-21 22:00
 **/
public class UnitInfo {
    private String levelName;
    private int level;
    private int bonus;
    private int totalCost;

    public UnitInfo(int level, String levelName, int bonus, int totalCost) {
        this.levelName = levelName;
        this.level = level;
        this.bonus = bonus;
        this.totalCost = totalCost;
    }

    public String getLevelName() {
        return levelName;
    }

    public int getLevel() {
        return level;
    }

    public int getBonus() {
        return bonus;
    }

    public int getTotalCost() {
        return totalCost;
    }
}
