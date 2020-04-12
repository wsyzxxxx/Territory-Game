package edu.duke651.wlt.client;

import edu.duke651.wlt.models.Player;
import edu.duke651.wlt.models.Territory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

public class Printer {
    private PromptBase promptBase;
    private static Printer printer = new Printer();

    private Printer() {
        promptBase = new PromptBase();
    }

    public static Printer getInstance() {
        return printer;
    }

    public void printMessage(String message) {
        System.out.println(message);
    }

    public void printWelcomeInfo() {
        System.out.println(PromptBase.WELCOME_INFO_PROMPT);
    }

    public void printInvalidAction() {
        System.out.println(PromptBase.INVALID_ACTION_PROMPT);
    }

    public void printInvalidUnits() {
        System.out.println(PromptBase.INVALID_UNITS_PROMPT);
    }

    public void printLargerUnits() {
        System.out.println(PromptBase.LARGER_UNITS_PROMPT);
    }

    public void printInvalidSourcePlace(){
        System.out.println(PromptBase.INVALID_SOURCE_PLACE_PROMPT);
    }

    public void printInvalidEndPlace() {
        System.out.println(PromptBase.INVALID_END_PLACE_PROMPT);
    }

    public void printInvalidAttackPlace() {
        System.out.println(PromptBase.INVALID_ATTACK_PLACE_PROMPT);
    }

    public void printNoMatchPlace() {
        System.out.println(PromptBase.NO_MATCH_PLACE_PROMPT);
    }

    public void printUnreachablePlace() {
        System.out.println(PromptBase.UN_REACHABLE_PLACE_PROMPT);
    }

    public void printCurrMap(Collection<Player> players) {
        System.out.println(promptBase.currMap_Prompt(players));
    }

    public void printUnits_prompt() {
        System.out.println(PromptBase.UNITS_PROMPT);
    }

    public void printActionChoice() {
        System.out.println(PromptBase.ACTION_CHOICE_PROMPT);
    }

    public void printOriginalTerritoriesChoice() {
        System.out.println(PromptBase.ORIGINAL_TERRITORIES_CHOICE_PROMPT);
    }

    public void printEndTerritoriesChoice() {
        System.out.println(PromptBase.END_TERRITORIES_CHOICE_PROMPT);
    }

    public void printEndRound_prompt() {
        System.out.println(PromptBase.END_ROUND_PROMPT);
    }

    public void printInputNothing() {
        System.out.println("input nothing, please input again");
    }

    public void printCurrMapWithoutPlayer(Map<String, Territory> territoryMap) {
        System.out.println(promptBase.currMap_Prompt_WithoutPlayer(territoryMap));
    }

    public void printTerritoryChoice_prompt() {
        System.out.println(PromptBase.TERRITORY_CHOICE_PROMPT);
    }

    public void printFinishSelection_prompt() {
        System.out.println(PromptBase.FINISH_SELECTION_PROMPT);
    }

    public void printInvalidChoice_prompt() {
        System.out.println(PromptBase.INVALID_CHOICE_PROMPT);
    }

    public void printCurrTerritories_Prompt(Player player) {
        System.out.println(promptBase.currTerritories_Prompt(player));
    }

    public void printPlayerResult_Prompt(Player player, Map<String, Territory> territoryMap) {
        String tmp = promptBase.playerResult_Prompt(player, territoryMap);
        System.out.println(Objects.requireNonNullElse(tmp, "the game is still going on"));
    }

    public void printSelectTerritories_Prompt(Player player, Map<String, ArrayList<Territory>> territoryGroup){
        String a = promptBase.selectTerritories_prompt(player, territoryGroup);
        System.out.println(a);
    }
}
