package edu.duke651.wlt.server;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * @program: wlt-risc
 * @description: This is Player class which contains all the information for one player
 * @author: Leo
 * @create: 2020-04-09 11:55
 **/
public class Player {
    //fields:
    private String playerName;
    private HashSet<Territory> territories;

    public Player(String playerName) {
        this.playerName = playerName;
        this.territories = new HashSet<>();
    }
//    private ArrayList<Order> orders; //this is to be sent to server.

    //methods:
    public void addTerritory(Territory t){
        territories.add(t);
    }

    public String getPlayerName() {
        return playerName;
    }

    public void removeTerritory(Territory t) {
        territories.remove(t);
    }

    void transferTerritory(Player aim, Territory t) {
        removeTerritory(t);
        aim.addTerritory(t);
    }

    /**
    * @Description: This function createOrder is to create an instance of Order.
    * @Param: [source, aim, num]
    * @return: void
    * @Author: Leo
    * @Date: 2020/4/9
    */
    void createOrder(Territory source, Territory aim, int num) {

    }

    /**
    * @Description: This function updateTerritories is to replace existing territories with updatedList.
    * @Param: [updatedList]
    * @return: void
    * @Author: Leo
    * @Date: 2020/4/9
    */
    void updateTerritories(HashSet<Territory> updatedSet){
        this.territories = updatedSet;
    }

    /**
    * @Description: This function checkReachable is to use recursion on self's neighbors to check whether two territories are connected to enable move orders.
    * @Param: [source, aim]
    * @return: boolean
    * @Author: Leo
    * @Date: 2020/4/9
    */
    boolean checkReachable(Territory source, Territory aim){

        if (!source.getTerritoryOwner().equals(aim.getTerritoryOwner()))
            return false;
        Territory curr = source;
        ArrayList<Territory> checked = new ArrayList<>();
        ArrayList<Territory> toCheck = new ArrayList<>();
        toCheck.add(source);
        while (!source.checkNeighbor(aim)) {
            checked.add(source);
            for (Territory t : source.getTerritoryNeighbors().values()) {
                if (!checked.contains(t)) toCheck.add(t);
            }
            toCheck.remove(source);
            if (toCheck.isEmpty()) return false;
            source = toCheck.get(0);
        }
        return true;
    }
}
