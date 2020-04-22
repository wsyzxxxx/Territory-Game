package edu.duke651.wlt.models;

/**
 * @program: wlt-risc
 * @description: This is the level updating order which updates one player's technology level once a round.
 * @author: Leo
 * @create: 2020-04-21 21:30
 **/
public class UpdateTechOrder extends Order{
    private int levelAfterUpdate;
    private int cost = 0;

    public UpdateTechOrder() {
        this.levelAfterUpdate = this.player.getTechLevel();
        if (checkLegal()) {
            this.levelAfterUpdate++;
            cost = ServerSetting.TECH_LEVEL_COST_MAP.get(levelAfterUpdate);
        }
    }

    @Override
    public void execute() {
        this.player.setTechLevel(levelAfterUpdate);
    }

    @Override
    public boolean checkLegal() {
        return this.player.getResources() >= cost;
    }
}
