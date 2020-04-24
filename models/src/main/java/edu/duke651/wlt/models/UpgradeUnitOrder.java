package edu.duke651.wlt.models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

/**
 * @program: wlt-risc
 * @description: This class is inheritanced from ActionOrder class and if for a player to update his units.
 * @author: Leo
 * @create: 2020-04-20 12:08
 **/
public class UpgradeUnitOrder extends Order{
    private Territory source;
    private ArrayList<Integer> unitsAfterUpdate;

    /**
    * @Description: This function UpdateUnitOrder is the constructor.
    * @Param: [source, unitsAfterUpdate]
    * @return:
    * @Author: Leo
    * @Date: 2020/4/21
    */
    public UpgradeUnitOrder(Territory source, ArrayList<Integer> unitsAfterUpdate) {
        this.player = source.getTerritoryOwner();
        this.type = "upgradeUnits";
        this.unitsAfterUpdate = unitsAfterUpdate;
        this.source = source;
    }

    /**
    * @Description: This function getCost is to get total cost for an update order.
    * @Param: [unitsAfterUpdate]
    * @return: int
    * @Author: Leo
    * @Date: 2020/4/21
    */
    public int calculateTechCost() {
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
        return calculateTechCost();
    }

    public Territory getSource() {
        return source;
    }

    public ArrayList<Integer> getUnitsAfterUpdate() {
        return unitsAfterUpdate;
    }

    private int sumUnits() {
        int sum = 0;
        for (Integer integer : this.unitsAfterUpdate) {
            sum += integer;
        }
        return sum;
    }

    @Override
    public void execute() {
        this.player.consumeTechResource(calculateTechCost());
        this.source.setTerritoryUnitsInLevel(this.unitsAfterUpdate);
    }

    @Override
    public boolean checkLegal() {
        return this.player.getTechResources() >= calculateTechCost() &&
               getRequireLevel() <= this.player.getTechLevel() &&
               calculateTechCost() >= 0 &&
               this.sumUnits() == source.getTerritoryUnits();
    }

    @Override
    public JSONObject serialize() {
        JSONObject orderObject = new JSONObject();
        orderObject.put("type", this.type);
        orderObject.put("player", this.player.getPlayerName());
        orderObject.put("source", this.source.getTerritoryName());
        JSONArray unitsLevelArray = new JSONArray();
        unitsAfterUpdate.forEach(unitsLevelArray::put);
        orderObject.put("units", unitsAfterUpdate);

        return orderObject;
    }

    public static UpgradeUnitOrder deserialize(JSONObject upgradeTechObject, Map<String, Player> playerMap, Map<String, Territory> territoryMap) {
        ArrayList<Integer> unitsAfterUpdate = new ArrayList<>();
        upgradeTechObject.getJSONArray("units").forEach(e -> unitsAfterUpdate.add((Integer)e));
        if (unitsAfterUpdate.size() != 7 || territoryMap.get(upgradeTechObject.getString("source")).getTerritoryOwner() != playerMap.get(upgradeTechObject.getString("player"))) {
            throw new IllegalArgumentException("It's not your territory!");
        }
        return new UpgradeUnitOrder(territoryMap.get(upgradeTechObject.getString("source")), unitsAfterUpdate);
    }
}
