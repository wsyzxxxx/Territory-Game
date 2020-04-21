package edu.duke651.wlt.models;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @program: wlt-risc
 * @description: This class is inheritanced from ActionOrder class and if for a player to update his units.
 * @author: Leo
 * @create: 2020-04-20 12:08
 **/
public class UpdateUnitOrder extends Order{
    //TODO
    private Territory source;
    private int cost = 0;
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
        this.cost = getCost();
    }

    /**
    * @Description: This function getCost is to get total cost for an update order.
    * @Param: [unitsAfterUpdate]
    * @return: int
    * @Author: Leo
    * @Date: 2020/4/21
    */
    private int getCost() {
        ArrayList<Integer> unitsBeforeUpdate = this.source.getTerritoryUnitsInLevel();
        int afterCost = 0;
        int beforeCost = 0;
        for (int i = 0; i < unitsBeforeUpdate.size(); ++i) {
            beforeCost += unitsBeforeUpdate.get(i) * ServerSetting.UNIT_LEVEL_COST_MAP.get(i).getTotalCost();
            afterCost += unitsAfterUpdate.get(i) * ServerSetting.UNIT_LEVEL_COST_MAP.get(i).getTotalCost();
        }
        return afterCost - beforeCost;
    }

    @Override
    public void execute() {
        this.source.setTerritoryUnitsInLevel(this.unitsAfterUpdate);
    }

    @Override
    public boolean checkLegal() {
        return this.player.getResources() >= cost;
    }
}
