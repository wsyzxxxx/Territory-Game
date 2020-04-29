package edu.duke651.wlt.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * @program: wlt-risc
 * @description: This is one territory which contains its neighbors, owner, and name.
 * @author: Leo
 * @create: 2020-04-09 11:57
 **/
public class Territory {
    //fields:
    private String territoryName;
    private Player territoryOwner;
    private Map<String, Territory> territoryNeighbors = new HashMap<>();
    private int techResourceGenerate = ServerSetting.INIT_TECH_RESOURCE_GENERATE_LEVEL_BASE;
    private int foodResourceGenerate = ServerSetting.INIT_FOOD_RESOURCE_GENERATE_LEVEL_BASE;
    private int size = ServerSetting.INIT_SIZE_BASE;

    //This is an array of numbers of different level of units. [0] is number of units of level 0.
    private ArrayList<Integer> territoryUnitsInLevel = new ArrayList<>(Collections.nCopies(7, 0));

    //This is for EVO3: Plague
    private boolean plagueMode = false;
    private boolean quarantineMode = false;

    /**
    * @Description: This is the constructor with only territory name.
    * @Param: [territoryName]
    * @return:
    * @Author: Leo
    * @Date: 2020/4/13
    */
    public Territory(String territoryName) {
        this.territoryName = territoryName;
    }

    /**
    * @Description: This is the full constructor.
    * @Param: [name, neighbors]
    * @return:
    * @Author: Leo
    * @Date: 2020/4/13
    */
    public Territory(String name, Map<String, Territory> neighbors) {
        this.territoryName = name;
        this.territoryNeighbors = neighbors;
    }

    //methods:

    public boolean isQuarantineMode() {
        return quarantineMode;
    }

    public void setQuarantineMode(boolean quarantineMode) {
        this.quarantineMode = quarantineMode;
    }

    public void randomKillUnits(int unitsToKill) {
        int unitsKilled = 0;
        Random r = new Random();
        while (unitsKilled < unitsToKill) {
            int index = r.nextInt(7);
            if (getTerritoryUnitsInLevel().get(index) > 0) {
                this.territoryUnitsInLevel.set(index, this.territoryUnitsInLevel.get(index) - 1);
                unitsKilled++;
            }
        }
    }

    public boolean isPlagueMode() {
        return plagueMode;
    }

    public void setPlagueMode(boolean plagueMode) {
        this.plagueMode = plagueMode;
    }

    public void setTechResourceGenerate(int techResourceGenerate) {
        this.techResourceGenerate = techResourceGenerate;
    }

    public ArrayList<Integer> getTerritoryUnitsInLevel() {
        return territoryUnitsInLevel;
    }

    public void setTerritoryUnitsInLevel(ArrayList<Integer> territoryUnitsInLevel) {
        this.territoryUnitsInLevel = territoryUnitsInLevel;
    }

    public int getFoodResourceGenerate() {
        return foodResourceGenerate;
    }

    public void setFoodResourceGenerate(int foodResourceGenerate) {
        this.foodResourceGenerate = foodResourceGenerate;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public ArrayList<String> getAttackTerritory() {
        ArrayList<String> result = new ArrayList<>();
        this.territoryNeighbors.forEach((k, v) -> {
            if (v.getTerritoryOwner() != this.territoryOwner) {
                result.add(k);
            }
        });
        return result;
    }

    public void increaseUnits(ArrayList<Integer> unitsArray) {
        for (int i = 0; i < this.territoryUnitsInLevel.size(); ++i) {
            this.territoryUnitsInLevel.set(i, this.territoryUnitsInLevel.get(i) + unitsArray.get(i));
        }
    }

    public void reduceUnits(ArrayList<Integer> unitsArray) {
        for (int i = 0; i < this.territoryUnitsInLevel.size(); ++i) {
            this.territoryUnitsInLevel.set(i, this.territoryUnitsInLevel.get(i) - unitsArray.get(i));
        }
    }

    public void incrementUnits() {
        this.territoryUnitsInLevel.set(0, this.territoryUnitsInLevel.get(0) + 1);
    }

    public String getTerritoryName() {
        return territoryName;
    }

    public void setTerritoryName(String territoryName) {
        this.territoryName = territoryName;
    }

    public void setTerritoryNeighbors(Map<String, Territory> territoryNeighbors) {
        this.territoryNeighbors = territoryNeighbors;
    }

    public void addTerritoryNeighbors(Territory neighbour) {
        this.territoryNeighbors.put(neighbour.getTerritoryName(), neighbour);
    }

    public Map<String, Territory> getTerritoryNeighbors() {
        return territoryNeighbors;
    }

    public Player getTerritoryOwner() {
        return territoryOwner;
    }

    public int getTerritoryUnits() {
        int sum = 0;
        for (Integer integer : this.territoryUnitsInLevel) {
            sum += integer;
        }
        return sum;
    }

    public void setTerritoryOwner(Player territoryOwner) {
        this.territoryOwner = territoryOwner;
    }

    public int getTechResourceGenerate() {
        return techResourceGenerate;
    }

    /**
    * @Description: This function checkNeighbor is to check whether one territory is neighbor of aim.
    * @Param: [aim]
    * @return: boolean
    * @Author: Leo
    * @Date: 2020/4/13
    */
    public boolean checkNeighbor(Territory aim) {
        return territoryNeighbors.containsValue(aim);
    }

    /**
    * @Description: This function increaseResourceGenerateLevel is for map's tech resource generate initialization.
    * @Param: [resourceGenerateLevel]
    * @return: void
    * @Author: Leo
    * @Date: 2020/4/23
    */
    public void increaseTechResourceGenerateLevel(int techResourceGenerateLevel) {
        this.techResourceGenerate += techResourceGenerateLevel;
    }

    /**
    * @Description: This function increaseFoodResourceGenerateLevel is for map's food resource generate initialization.
    * @Param: [foodResourceGenerateLevel]
    * @return: void
    * @Author: Leo
    * @Date: 2020/4/24
    */
    public void increaseFoodResourceGenerateLevel(int foodResourceGenerateLevel) {
        this.foodResourceGenerate += foodResourceGenerateLevel;
    }

    /**
    * @Description: This function increaseSize is for map's size initialization.
    * @Param: [size]
    * @return: void
    * @Author: Leo
    * @Date: 2020/4/23
    */
    public void increaseSize(int size) {
        this.size += size;
    }

    public boolean hasEnoughUnits(ArrayList<Integer> unitList) {
        if (unitList.size() != this.territoryUnitsInLevel.size()) {
            return false;
        }

        for (int i = 0; i < this.territoryUnitsInLevel.size(); i++) {
            if (unitList.get(i) > this.territoryUnitsInLevel.get(i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @Description: This function serialize is to serialize the player in order to send over network.
     * @Param: []
     * @return: org.json.JSONObject
     * @Author: Will
     * @Date: 2020/4/13
     */
    public JSONObject serialize() {
        JSONObject territoryItem = new JSONObject();
        territoryItem.put("name", this.territoryName);
        JSONArray neighbourList = new JSONArray();
        this.territoryNeighbors.keySet().forEach(neighbourList::put);
        territoryItem.put("neighbours", neighbourList);
        JSONArray unitList = new JSONArray();
        this.territoryUnitsInLevel.forEach(unitList::put);
        territoryItem.put("unitList", unitList);

        return territoryItem;
    }

    /**
     * @Description: This function deserialize is to deserialize the player after receiving the message.
     * @Param: [playerObject, territoryMap]
     * @return: edu.duke651.wlt.models.Player
     * @Author: Will
     * @Date: 2020/4/13
     */
    public static Territory deserialize(JSONObject territoryObject) throws JSONException {
        Territory territory = new Territory(territoryObject.getString("name"));
        JSONArray unitArray = territoryObject.getJSONArray("unitList");
        for (int i = 0; i < 7; i++) {
            territory.territoryUnitsInLevel.set(i, unitArray.getInt(i));
        }

        return territory;
    }
}
