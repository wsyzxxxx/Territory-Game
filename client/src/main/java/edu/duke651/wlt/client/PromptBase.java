package edu.duke651.wlt.client;

import edu.duke651.wlt.models.Player;
import edu.duke651.wlt.models.Territory;

import java.util.Collection;
import java.util.Map;

public class PromptBase {
    public PromptBase(){ }
    String selectTerritories_prompt = "Now select your three territories: \n";

    String actionChoice_prompt = "Now choose your action:\n" +
            "please input M for Move\n"+
            "please input A for Attack\n" +
            "please input N for end of this round\n";
    String originalTerritoriesChoice_prompt = "Now choose your starting territory: \n";
    String endTerritoriesChoice_prompt =      "Now choose your target territory: \n";
    String units_prompt=                      "Now input number of units: \n";
    String invalidAction_prompt =      "Invalid action, please input again\n";
    String invalidUnits_prompt =       "Invalid unites, please input again\n";
    String invalidEndPlace_prompt =    "Invalid aim place, please input agian";
    String invalidAttackPlace_prompt = "Invalid attack place, please input agian";
    String largerUnits_prompt = "This territory do no contains so many units, please input again";
    String invalidSourcePlace_prompt = "Invalid source place, please input again";
    String noMatchPlace_prompt = "You do not have the territory";
    String unReachablePlace_prompt = "The target territory is unreachable";
    String endRound_prompt = "You choose to end the input order round\n";

    String territoryChoice_prompt = "Congratulations! You become the owner of this territory!\n";
    String finishSelection_prompt = "Now you have three territories, selection complete!\n";
    String invalidChoice_prompt =   "Invalid input, do not contain this territory or this territory has been taken! Please input again!\n";
    //server for currMap_Prompt
    String getUnit_prompt(Territory territory){
        return territory.getTerritoryUnits() + " units in " + territory.getTerritoryName();
    }
    //server for currMap_Prompt
    String getNeighbor_prompt(Territory territory) {
        String neighbor_prompt = " (next to: ";
        for(Territory t : territory.getTerritoryNeighbors().values()){
             neighbor_prompt += " "+ t.getTerritoryName();
        }
        return neighbor_prompt + ")\n";
    }
    //server for currMap_Prompt
    String getPlayerName_prompt(Player player){
        return player.getPlayerName() + "\n";
    }
    //server for currMap_Prompt
    String getTerritories_prompt(Player player){
        String t = "";
        for(Territory territory: player.getTerritories().values()) {//that map<name, Territory>
            t += getUnit_prompt(territory) + getNeighbor_prompt(territory);
        }
        return t;
    }

    String currMap_Prompt(Collection<Player> players){
        String init_territories = "";
        for(Player player : players){
            init_territories += getPlayerName_prompt(player) + "-------------";
            init_territories += getTerritories_prompt(player); // omniscient Will mark!
        }
        return init_territories;
    }

    String currMap_Prompt_WithoutPlayer(Map<String, Territory> territoryMap){
        String init_territories = "The available territories are listed as following\n";
        for(Territory territory: territoryMap.values()){
            init_territories += territory.getTerritoryName() + getNeighbor_prompt(territory); // omniscient Will mark!
        }
        return init_territories;
    }
    String currTerritories_Prompt(Player player){
        String init_territories = "Your territories are:\n";
        for(Territory territory:player.getTerritories().values()){
            init_territories += territory.getTerritoryName();
        }
        return init_territories + "\n";
    }

}
