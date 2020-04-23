package edu.duke651.wlt.models;

/**
 * @program: wlt-risc
 * @description: This is the level updating order which updates one player's technology level once a round.
 * @author: Leo
 * @create: 2020-04-21 21:30
 **/
public class UpdateTechOrder extends Order{
    private int levelAfterUpdate;
    private int techCost = 0;

    public UpdateTechOrder() {
        this.levelAfterUpdate = this.player.getTechLevel();
        if (checkLegal()) {
            this.levelAfterUpdate++;
            techCost = ServerSetting.TECH_LEVEL_COST_MAP.get(levelAfterUpdate);
        }
    }

    public int getLevelAfterUpdate() {
        return levelAfterUpdate;
    }

    public int getTechCost() {
        return techCost;
    }

    @Override
    public void execute() {
        this.player.consumeTechResource(techCost);
        this.player.setTechLevel(levelAfterUpdate);
    }

    @Override
    public boolean checkLegal() {
        return this.player.getTechResources() >= techCost;
    }
}
