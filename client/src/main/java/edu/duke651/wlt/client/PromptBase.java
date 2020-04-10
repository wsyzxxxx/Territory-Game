package edu.duke651.wlt.client;

import edu.duke651.wlt.models.Player;
import edu.duke651.wlt.models.Territory;

public class PromptBase {
    GameController gameController ;
    PromptBase(){
        gameController = new GameController(){};
    }

    String actionChoice_prompt = "Now choose your action:\n" +
            "please input M for Move\n"+
            "please input A for Attack\n" +
            "please input N for end of this round\n";
    String originalTerritoriesChoice_prompt = "Now choose your starting territory: ";
    String endTerritoriesChoice_prompt = "Now choose your end territory: ";
    String invalidAction_prompt = "Invalid action, please input again";
    String invalidUnits_prompt = "Invalid unites, please input again";
    String largerUnits_prompt = "This territory do no contains so many units, please input again";
    String invalidPlace_prompt = "Invalid place, please input again";
    String noMatchPlace_prompt = "You do not have the territory";
    String unReachablePlace_prompt = "The target territory is unreachable";

    String getUnit_prompt(Territory territory){
        return territory.getUnits() + " units in " + territory.getName();
    }

    String getNeighbor_prompt(Territory territory, TerritoryRelation territoryRelation) {
        String neighbor_prompt = " (next to: ";
        for(Territory t : territoryRelation.get(territory)){
             neighbor_prompt += " "+ t.getName();
        }
        return neighbor_prompt + ")\n";
    }

    String getPlayerName_prompt(Player player){
        return player.getName() + "\n";
    }

    String getTerritories_prompt(Player player, TerritoryRelation territoryRelation){
        String t = "";
        for(Territory territory: player.getTerritories()) {//that map<name, Territory>
            t += getUnit_prompt(territory) + getNeighbor_prompt(territory, territoryRelation);
        }
        return t;
    }

    String currMap_Prompt(TerritoryRelation territoryRelation){
        String init_territories = "";
        for(player : gameController.getPlayerList()){
            init_territories += getPlayerName_prompt(player) + "-------------";
            init_territories += getTerritories_prompt(player, territoryRelation);
        }
        return init_territories;
    }

    String showOrder(){

        String a = "Your action is ";
    }

}
