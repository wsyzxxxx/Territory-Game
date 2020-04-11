package edu.duke651.wlt.client;

import edu.duke651.wlt.models.Player;
import edu.duke651.wlt.models.Territory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class Printer {
    private PromptBase promptBase;
    public Printer(){
        promptBase = new PromptBase();
    }

    public void printInvalidAction(){
        System.out.println(promptBase.invalidAction_prompt);
    }
    public void printInvalidUnits(){
        System.out.println(promptBase.invalidUnits_prompt);
    }
    public void printLargerUnits(){
        System.out.println(promptBase.largerUnits_prompt);
    }
    public void printInvalidSourcePlace(){
        System.out.println(promptBase.invalidSourcePlace_prompt);
    }
    public void printInvalidEndPlace(){ System.out.println(promptBase.invalidEndPlace_prompt);}
    public void printInvalidAttackPlace(){ System.out.println(promptBase.invalidAttackPlace_prompt);}
    public void printNoMatchPlace(){
        System.out.println(promptBase.noMatchPlace_prompt);
    }
    public void printUnreachablePlace(){
        System.out.println(promptBase.unReachablePlace_prompt);
    }
    public void printCurrMap(Collection<Player> players){
        System.out.println(promptBase.currMap_Prompt(players));
    }
    public void printUnits_prompt(){System.out.println(promptBase.units_prompt);}
    public void printActionChoice(){
        System.out.println(promptBase.actionChoice_prompt);
    }
    public void printOriginalTerritoriesChoice() {
        System.out.println(promptBase.originalTerritoriesChoice_prompt);
    }
    public void printEndTerritoriesChoice(){
        System.out.println(promptBase.endTerritoriesChoice_prompt);
    }
    public void printEndRound_prompt(){
        System.out.println(promptBase.endRound_prompt);
    }
    public void printInputNothing(){
        System.out.println("input nothing, please input again");
    }

    public void printCurrMapWithoutPlayer(Map<String, Territory> territoryMap){
        System.out.println(promptBase.currMap_Prompt_WithoutPlayer(territoryMap));
    }
    public void printTerritoryChoice_prompt(){
        System.out.println(promptBase.territoryChoice_prompt);
    }
    public void printFinishSelection_prompt(){
        System.out.println(promptBase.finishSelection_prompt);
    }
    public void printInvalidChoice_prompt(){
        System.out.println(promptBase.invalidChoice_prompt);
    }
    public void printCurrTerritories_Prompt(Player player){
        System.out.println(promptBase.currTerritories_Prompt(player));
    }

    public void printPlayerResult_Prompt(Player player, Map<String, Territory> territoryMap){
        String tmp = promptBase.playerResult_Prompt(player, territoryMap);
        if(tmp == null) {
            System.out.println("the game is still going on");
        } else {
            System.out.println(tmp);
        }
    }
    public void printSelectTerritories_Prompt(Player player, Map<String, ArrayList<Territory>> territoryGroup){
        String a = promptBase.selectTerritories_prompt(player, territoryGroup);
        System.out.println(a);
    }
}
