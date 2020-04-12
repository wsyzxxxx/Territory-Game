package edu.duke651.wlt.client;

import edu.duke651.wlt.models.Player;
import edu.duke651.wlt.models.Territory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
/**
 * @Author: Tong
 * @Description: printer information, all stored into string.
 * @Pattern:Singleton
 * @Modified by: Will
 */

public class PromptBase {
    public PromptBase() {}

    public static final String ACTION_CHOICE_PROMPT =
            "Now choose your action:\n" +
            "please input M for Move\n"+
            "please input A for Attack\n" +
            "please input N for end of this round\n";
    public static final String ORIGINAL_TERRITORIES_CHOICE_PROMPT = "Now choose your starting territory:";
    public static final String END_TERRITORIES_CHOICE_PROMPT =      "Now choose your target territory:";
    public static final String UNITS_PROMPT =                       "Now input number of units:";
    public static final String INVALID_ACTION_PROMPT =      "Invalid action, please input again";
    public static final String INVALID_UNITS_PROMPT =       "Invalid unites, please input again";
    public static final String INVALID_END_PLACE_PROMPT =    "Invalid aim place, please input again";
    public static final String INVALID_ATTACK_PLACE_PROMPT = "Invalid attack place, please input again";
    public static final String LARGER_UNITS_PROMPT = "This territory do no contains so many units, please input again";
    public static final String INVALID_SOURCE_PLACE_PROMPT = "Invalid source place, please input again";
    public static final String NO_MATCH_PLACE_PROMPT = "You do not have the territory";
    public static final String UN_REACHABLE_PLACE_PROMPT = "The target territory is unreachable";
    public static final String END_ROUND_PROMPT = "You choose to end the input order round";
    public static final String TERRITORY_CHOICE_PROMPT = "Congratulations! You become the owner of this territory group!";
    public static final String FINISH_SELECTION_PROMPT = "Now you have three territories, selection complete!";
    public static final String INVALID_CHOICE_PROMPT =   "Invalid input, do not contain this territory Group or this territory Group has been taken! Please input again!";
    public static final String WELCOME_INFO_PROMPT = "Welcome to the Territory Game! Please enter your username:";

    //print territory group name
    private String territoryGroupName(String key,Map<String, ArrayList<Territory>> territoryGroup){
        ArrayList<Territory> t = territoryGroup.get(key);
        StringBuilder init = new StringBuilder(" ");
        for(Territory territory:t){
            init.append(territory.getTerritoryName()).append(" \n");
        }
        return init.toString();
    }
    //not used
    public String selectTerritories_prompt(Player player, Map<String, ArrayList<Territory>> territoryGroup){
        return "Now select your three territories: \n" +
                "please input 1 for Group1:\n"+ territoryGroupName("1", territoryGroup) +
                "please input 2 for Group2\n" + territoryGroupName("2", territoryGroup) +
                "please input 3 for Group3\n" + territoryGroupName("3", territoryGroup);
    }

    //server for currMap_Prompt
    String getUnit_prompt(Territory territory){
        return territory.getTerritoryUnits() + " units in " + territory.getTerritoryName();
    }
    //server for currMap_Prompt
    String getNeighbor_prompt(Territory territory) {
        StringBuilder neighbor_prompt = new StringBuilder(" (next to: ");
        for(Territory t : territory.getTerritoryNeighbors().values()){
             neighbor_prompt.append(" ").append(t.getTerritoryName());
        }
        return neighbor_prompt + ")\n";
    }
    //server for currMap_Prompt
    String getPlayerName_prompt(Player player){
        return player.getPlayerName() + "\n";
    }
    //server for currMap_Prompt
    String getTerritories_prompt(Player player){
        StringBuilder stringBuilder = new StringBuilder();
        for(Territory territory: player.getTerritories().values()) {//that map<name, Territory>
            stringBuilder.append(getUnit_prompt(territory)).append(getNeighbor_prompt(territory));
        }
        return stringBuilder.toString();
    }
    //print current map, for example
    //leo
    //-------------
    //4 units in Roshar (next to:  Elantris Hogwarts Scadrial)
    //zt
    //-------------
    //5 units in Elantris (next to:  Narnia Scadrial Midkemia Roshar
    String currMap_Prompt(Collection<Player> players){
        StringBuilder init_territories = new StringBuilder();
        for(Player player : players){
            init_territories.append(getPlayerName_prompt(player)).append("-------------").append("\n");
            init_territories.append(getTerritories_prompt(player)); // omniscient Will mark!
        }
        return init_territories.toString();
    }
    //not used function
    String currMap_Prompt_WithoutPlayer(Map<String, Territory> territoryMap){
        StringBuilder init_territories = new StringBuilder("The available territories are listed as following\n");
        for(Territory territory: territoryMap.values()){
            init_territories.append(territory.getTerritoryName()).append(getNeighbor_prompt(territory)); // omniscient Will mark!
        }
        return init_territories.toString();
    }

    String currTerritories_Prompt(Player player){
        StringBuilder init_territories = new StringBuilder("Your territories are:\n");
        for(Territory territory:player.getTerritories().values()){
            init_territories.append(territory.getTerritoryName());
        }
        return init_territories + "\n";
    }

    String playerResult_Prompt(Player player, Map<String, Territory> territoryMap){
        if(player.getTerritories().isEmpty()) {
            return player.getPlayerName() + " , you lose.\n";
        } else if (territoryMap.size() == player.getTerritories().size()){
            return player.getPlayerName() + " , you win.\n";
        }
        return null;//
    }

}
