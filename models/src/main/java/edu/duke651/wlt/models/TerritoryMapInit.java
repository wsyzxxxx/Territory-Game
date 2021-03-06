package edu.duke651.wlt.models;

import org.json.JSONArray;

import java.util.*;

/**
 * @program: wlt-risc
 * @description: This is the class used to initialize map, in case of possible more maps later.
 * @author: Leo, Will
 * @create: 2020-04-11 11:28
 **/
public class TerritoryMapInit {

    private HashMap<String, Territory> territoryMapInit = new HashMap<>();
    private ArrayList<ArrayList<Territory>> territoryGroups = new ArrayList<>();

    /**
    * @Description: This function TerritoryMapInit is to generate the specific map of this game.
    * @Param: []
    * @return: 
    * @Author: Leo
    * @Date: 2020/4/11
    */
    public TerritoryMapInit() {
        Territory Narnia = new Territory("Narnia");
        Territory Midkemia = new Territory("Midkemia");
        Territory Oz = new Territory("Oz");
        Territory Gondor = new Territory("Gondor");
        Territory Elantris = new Territory("Elantris");
        Territory Scadrial = new Territory("Scadrial");
        Territory Roshar = new Territory("Roshar");
        Territory Mordor = new Territory("Mordor");
        Territory Hogwarts = new Territory("Hogwarts");
        //NarniaInit
        Narnia.addTerritoryNeighbors(Elantris);
        Narnia.addTerritoryNeighbors(Midkemia);
        //Narnia.setTerritoryUnits(10);
        //MidkemiaInit
        Midkemia.addTerritoryNeighbors(Narnia);
        Midkemia.addTerritoryNeighbors(Elantris);
        Midkemia.addTerritoryNeighbors(Scadrial);
        Midkemia.addTerritoryNeighbors(Oz);
        //Midkemia.setTerritoryUnits(10);
        //Oz
        Oz.addTerritoryNeighbors(Midkemia);
        Oz.addTerritoryNeighbors(Scadrial);
        Oz.addTerritoryNeighbors(Gondor);
        Oz.addTerritoryNeighbors(Mordor);
        //Oz.setTerritoryUnits(10);
        //Gondor
        Gondor.addTerritoryNeighbors(Oz);
        Gondor.addTerritoryNeighbors(Mordor);
        //Gondor.setTerritoryUnits(10);
        //Elantris
        Elantris.addTerritoryNeighbors(Narnia);
        Elantris.addTerritoryNeighbors(Midkemia);
        Elantris.addTerritoryNeighbors(Scadrial);
        Elantris.addTerritoryNeighbors(Roshar);
        //Elantris.setTerritoryUnits(10);
        //Scandrial
        Scadrial.addTerritoryNeighbors(Elantris);
        Scadrial.addTerritoryNeighbors(Midkemia);
        Scadrial.addTerritoryNeighbors(Oz);
        Scadrial.addTerritoryNeighbors(Mordor);
        Scadrial.addTerritoryNeighbors(Hogwarts);
        Scadrial.addTerritoryNeighbors(Roshar);
        //Scadrial.setTerritoryUnits(10);
        //Mordor
        Mordor.addTerritoryNeighbors(Oz);
        Mordor.addTerritoryNeighbors(Scadrial);
        Mordor.addTerritoryNeighbors(Gondor);
        Mordor.addTerritoryNeighbors(Hogwarts);
        //Mordor.setTerritoryUnits(10);
        //Hogwarts
        Hogwarts.addTerritoryNeighbors(Scadrial);
        Hogwarts.addTerritoryNeighbors(Mordor);
        Hogwarts.addTerritoryNeighbors(Roshar);
        //Hogwarts.setTerritoryUnits(10);
        //Roshar
        Roshar.addTerritoryNeighbors(Elantris);
        Roshar.addTerritoryNeighbors(Scadrial);
        Roshar.addTerritoryNeighbors(Hogwarts);
        //Roshar.setTerritoryUnits(10);
        //put in
        territoryMapInit.put(Narnia.getTerritoryName(), Narnia);
        territoryMapInit.put(Elantris.getTerritoryName(), Elantris);
        territoryMapInit.put(Oz.getTerritoryName(), Oz);
        territoryMapInit.put(Gondor.getTerritoryName(), Gondor);
        territoryMapInit.put(Scadrial.getTerritoryName(), Scadrial);
        territoryMapInit.put(Midkemia.getTerritoryName(), Midkemia);
        territoryMapInit.put(Mordor.getTerritoryName(), Mordor);
        territoryMapInit.put(Hogwarts.getTerritoryName(), Hogwarts);
        territoryMapInit.put(Roshar.getTerritoryName(), Roshar);

        //divide groups
        JSONArray groupArray = new JSONArray(ServerSetting.PLAYER_TERRITORY_GROUP_MAP.get(ServerSetting.PLAYER_NUM));

        for (int i = 0; i < groupArray.length(); i++) {
            JSONArray groupMembers = groupArray.getJSONArray(i);
            ArrayList<Territory> group = new ArrayList<>();
            for (int j = 0; j < groupMembers.length(); j++) {
                group.add(territoryMapInit.get(groupMembers.getString(j)));
            }
            territoryGroups.add(group);
        }
    }

    /**
    * @Description: This function getMap is to return the specific map.
    * @Param: []
    * @return: java.util.HashMap<java.lang.String,edu.duke651.wlt.models.Territory>
    * @Author: Leo
    * @Date: 2020/4/11
    */
    public HashMap<String, Territory> getMap() {
        return territoryMapInit;
    }
    public ArrayList<ArrayList<Territory>> getGroups() {return territoryGroups;}
}
