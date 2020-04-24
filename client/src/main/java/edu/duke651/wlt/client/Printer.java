//package edu.duke651.wlt.client;
//
//import edu.duke651.wlt.models.Player;
//import edu.duke651.wlt.models.Territory;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Map;
//import java.util.Objects;
///**
// * @Author: Tong
// * @Description: the only printer class get input from players
// * @Pattern:Singleton
// * @Modified by: Will
// */
//
//public class Printer {
//    private final PromptBase promptBase;
//    private static final Printer printer = new Printer();
//
//    private Printer() {
//        promptBase = new PromptBase();
//    }
//
//    public static Printer getInstance() {
//        return printer;
//    }
//    //print parameter message
//    public void printMessage(String message) {
//        System.out.println(message);
//    }
//    //print welcome information
//    public void printWelcomeInfo() {
//        System.out.println(PromptBase.WELCOME_INFO_PROMPT);
//    }
//    //print invalid prompt of action choice
//    public void printInvalidAction() {
//        System.out.println(PromptBase.INVALID_ACTION_PROMPT);
//    }
//    //print invalid prompt of units
//    public void printInvalidUnits() {
//        System.out.println(PromptBase.INVALID_UNITS_PROMPT);
//    }
//    //print too large prompt of units
//    public void printLargerUnits() {
//        System.out.println(PromptBase.LARGER_UNITS_PROMPT);
//    }
//    //print invalid prompt of source territory input
//    public void printInvalidSourcePlace(){
//        System.out.println(PromptBase.INVALID_SOURCE_PLACE_PROMPT);
//    }
//    //print invalid prompt of move target territory input
//    public void printInvalidEndPlace() {
//        System.out.println(PromptBase.INVALID_END_PLACE_PROMPT);
//    }
//    //print invalid prompt of attack territory input
//    public void printInvalidAttackPlace() {
//        System.out.println(PromptBase.INVALID_ATTACK_PLACE_PROMPT);
//    }
//    //print no existing territory matching input
//    public void printNoMatchPlace() {
//        System.out.println(PromptBase.NO_MATCH_PLACE_PROMPT);
//    }
//    //print input territory unreachable
//    public void printUnreachablePlace() {
//        System.out.println(PromptBase.UN_REACHABLE_PLACE_PROMPT);
//    }
//    //print current all player and territory realtions' information
//    public void printCurrMap(Collection<Player> players) {
//        System.out.println(promptBase.currMap_Prompt(players));
//    }
//
//
//    //print units
//    public void printUnits_prompt() {
//        System.out.println(PromptBase.UNITS_PROMPT);
//    }
//    //print action choices
//    public void printActionChoice() {
//        System.out.println(PromptBase.ACTION_CHOICE_PROMPT);
//    }
//    //print source territory
//    public void printOriginalTerritoriesChoice() {
//        System.out.println(PromptBase.ORIGINAL_TERRITORIES_CHOICE_PROMPT);
//    }
//    //print target ~
//    public void printEndTerritoriesChoice() {
//        System.out.println(PromptBase.END_TERRITORIES_CHOICE_PROMPT);
//    }
//    //print the game round end prompt
//    public void printEndRound_prompt() {
//        System.out.println(PromptBase.END_ROUND_PROMPT);
//    }
//
//    public void printInputNothing() {
//        System.out.println("input nothing, please input again");
//    }
//
//    //not used function
//    public void printCurrMapWithoutPlayer(Map<String, Territory> territoryMap) {
//        System.out.println(promptBase.currMap_Prompt_WithoutPlayer(territoryMap));
//    }
//
//    //not used function
//    public void printTerritoryChoice_prompt() {
//        System.out.println(PromptBase.TERRITORY_CHOICE_PROMPT);
//    }
//
//    //not used function
//    public void printFinishSelection_prompt() {
//        System.out.println(PromptBase.FINISH_SELECTION_PROMPT);
//    }
//
//    //not used function
//    public void printInvalidChoice_prompt() {
//        System.out.println(PromptBase.INVALID_CHOICE_PROMPT);
//    }
//
//    //not used function
//    public void printCurrTerritories_Prompt(Player player) {
//        System.out.println(promptBase.currTerritories_Prompt(player));
//    }
//
//    //not used function
//    public void printPlayerResult_Prompt(Player player, Map<String, Territory> territoryMap) {
//        String tmp = promptBase.playerResult_Prompt(player, territoryMap);
//        System.out.println(Objects.requireNonNullElse(tmp, "the game is still going on"));
//    }
//
//    //not used function
//    public void printSelectTerritories_Prompt(Player player, Map<String, ArrayList<Territory>> territoryGroup){
//        String a = promptBase.selectTerritories_prompt(player, territoryGroup);
//        System.out.println(a);
//    }
//}
