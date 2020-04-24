package edu.duke651.wlt.models;

import java.util.ArrayList;

/**
 * @program: wlt-risc
 * @description: This class is inheritanced from ActionOrder class and if for a player to update his units.
 * @author: Leo
 * @create: 2020-04-20 12:08
 **/
public class UpdateUnitOrder extends Order{
    private Territory source;
    private int techCost;
    private ArrayList<Integer> unitsAfterUpdate;

    /**
    * @Description: This function UpdateUnitOrder is the constructor.
    * @Param: [source, unitsAfterUpdate]
    * @return:
    * @Author: Leo
    * @Date: 2020/4/21
    */
    public UpdateUnitOrder(Territory source, ArrayList<Integer> unitsAfterUpdate) {
        this.unitsAfterUpdate = unitsAfterUpdate;
        this.source = source;
        this.techCost = calculateTechCost();
    }

    /**
    * @Description: This function getCost is to get total cost for an update order.
    * @Param: [unitsAfterUpdate]
    * @return: int
    * @Author: Leo
    * @Date: 2020/4/21
    */
    private int calculateTechCost() {
        ArrayList<Integer> unitsBeforeUpdate = this.source.getTerritoryUnitsInLevel();
        int afterCost = 0;
        int beforeCost = 0;
        for (int i = 0; i < unitsBeforeUpdate.size(); ++i) {
            beforeCost += unitsBeforeUpdate.get(i) * ServerSetting.UNIT_LEVEL_COST_MAP.get(i).getTotalCost();
            afterCost += unitsAfterUpdate.get(i) * ServerSetting.UNIT_LEVEL_COST_MAP.get(i).getTotalCost();
        }
        return afterCost - beforeCost;
    }

    /**
    * @Description: This function getRequireLevel is to check the required tech level for one update order.
    * @Param: []
    * @return: int
    * @Author: Leo
    * @Date: 2020/4/22
    */
    public int getRequireLevel() {
        for (int i = this.unitsAfterUpdate.size() - 1; i > 0; --i) {
            if (this.unitsAfterUpdate.get(i) != 0) return i;
        }
        return 0;
    }

    public int getTechCost() {
        return techCost;
    }

    public Territory getSource() {
        return source;
    }

    public ArrayList<Integer> getUnitsAfterUpdate() {
        return unitsAfterUpdate;
    }

    @Override
    public void execute() {
        this.player.consumeTechResource(techCost);
        this.source.setTerritoryUnitsInLevel(this.unitsAfterUpdate);
    }

    @Override
    public boolean checkLegal() {
        return this.player.getTechResources() >= techCost && getRequireLevel() <= this.player.getTechLevel();
    }
}
