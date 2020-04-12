package edu.duke651.wlt.client;

import edu.duke651.wlt.models.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class Interpreter {
    static Printer printer;
    static Scanner scanner;
    String source;
    String aim;
    String attackPlace;
    String unites;
    private static Interpreter instance = new Interpreter();
    private Interpreter(){ }
    public static Interpreter getInstance(Scanner s){
        scanner = s;
        printer = new Printer();
        return instance;
    }

    public void getGroupSelection(Player player, Map<String, ArrayList<Territory>> territoryGroup) {
        if(territoryGroup.isEmpty()){
            System.out.println("No more territories group to choose!");
            return;
        }
        //printer.printCurrMapWithoutPlayer(territoryMap); for now there is no need,
        printer.printSelectTerritories_Prompt(player, territoryGroup);

        while(true) {
            String key = getStringInput(scanner);
            if (territoryGroup.containsKey(key)) {
                printer.printTerritoryChoice_prompt();//may need call territory's name
                for(Territory territory : territoryGroup.get(key)){
                    territory.setTerritoryOwner(player);
                }
                territoryGroup.remove(key);
            } else {
                printer.printInvalidChoice_prompt();
            }
        }
    }
    /*
    this function is used for choose territory one by one by player.
    public void getSelection(Player player, Map<String, Territory> territoryMap){
        if(territoryMap.isEmpty()){
            System.out.println("No more territories to choose!");
            return;
        }
        printer.printSelectTerritories_Prompt();
        printer.printCurrMapWithoutPlayer(territoryMap);

        String territoryName = getStringInput(scanner);
        while(true) {
            if (territoryMap.containsKey(territoryName)) {
                printer.printTerritoryChoice_prompt();//may need call territory's name
                territoryMap.get(territoryName).setTerritoryOwner(player);
                territoryMap.remove(territoryName);
            } else {
                printer.printInvalidChoice_prompt();
            }
        }
    }

     */
    public Order getOrder(Player player){
        //show the map? . no just show once , so need to be called in ClientController
        printer.printActionChoice();
        while(true){
            String action = getStringInput(scanner);
            if(isValidAction(action)){
                if(action.equals("N")) {
                    printer.printEndRound_prompt();//this may need another function in promptbase and printer
                    return null;
                }else if(action.equals("M")) {
                    return getMove(player);
                }else if(action.equals("A")) {
                    return getAttack(player);
                }
            }else {
                printer.printInvalidAction();
            }
        }
    } // done now

    Territory getSource(Player player) {
        printer.printOriginalTerritoriesChoice();
        while(true){
            source = getStringInput(scanner);
            if(isValidOriginalPlace(source, player)){
                return player.getTerritories().get(source);
            } else {
                printer.printInvalidSourcePlace();
            }
        }
    }
    int getUnites(Player player) {
        printer.printUnits_prompt();
        while(true){
            unites = getStringInput(scanner);
            if(isValidNums(unites)){
                int output = Integer.parseInt(unites);
                if(player.getTerritories().get(source).getTerritoryUnits() < output) {
                    printer.printLargerUnits();
                } else {
                    return output;
                }
            } else {
                printer.printInvalidUnits();//this may need some change because judge of larger units situation
            }
        }
    }
    Territory getAim(Player player) {
        printer.printEndTerritoriesChoice();
        while(true){
            aim = getStringInput(scanner);
            if(isValidEndPlace(aim, player)){
                return player.getTerritories().get(aim);
            } else {
                //here may need printer.
                printer.printInvalidEndPlace();
            }
        }
    }

    Territory getAttackPlace(Player player) {
        printer.printEndTerritoriesChoice();
        while(true){
            attackPlace = getStringInput(scanner);
            if(isValidAttackPlace(attackPlace, player)){
                return player.getTerritories().get(attackPlace);
            } else {
                //here may need printer.
                printer.printInvalidAttackPlace();
            }
        }
    }
    AttackOrder getAttack(Player player) {
        return new AttackOrder(player, getSource(player), getAttackPlace(player), getUnites(player));
    }
    MoveOrder getMove(Player player) {
        return new MoveOrder(player, getSource(player), getAim(player), getUnites(player));
    }

    String getStringInput(Scanner scanner){
        return scanner.nextLine();
    }

    //not check larger or not
    boolean isValidNums(String numsInput) {
        if(numsInput.equals("")) {
            printer.printInputNothing();
            return false;
        }

        int i = nullifySpace(numsInput);
        numsInput = numsInput.substring(i);
        return isNumeric(numsInput);
    }
     boolean isNumeric(String str) {
        for (int i = 0; i < str.length(); i++) {
            System.out.println(str.charAt(i));
            if (!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
    }

    boolean isValidAttackPlace(String placeInput, Player player) {
        if(placeInput.equals("")) {
            printer.printInputNothing();
            return false;
        }
        int i = nullifySpace(placeInput);
        placeInput = placeInput.substring(i);
        //
        if(player.getTerritories().containsKey(placeInput)) {
            System.out.println("you cannot attack ur own territory! Please input again");
            return false;
        } else if(player.getTerritories().get(source).checkNeighbor(player.getTerritories().get(placeInput))) {
            return true;
        } else {
            printer.printUnreachablePlace();
            return false;
        }
    }
    //done now,
    boolean isValidEndPlace(String placeInput, Player player){
        if(placeInput.equals("")) {
            printer.printInputNothing();
            return false;
        }
        int i = nullifySpace(placeInput);
        placeInput = placeInput.substring(i);

        if(player.getTerritories().containsKey(placeInput)) {
            if (player.checkReachable(player.getTerritories().get(source), player.getTerritories().get(placeInput))){
                return true;
            } else {
                printer.printUnreachablePlace();
                return false;
            }
        } else{
            printer.printNoMatchPlace();
            return false;
        }
    }

    //done by now
    boolean isValidOriginalPlace(String placeInput, Player player) {
        if(placeInput.equals("")) {
            printer.printInputNothing();
            return false;
        }
        int i = nullifySpace(placeInput);
        placeInput = placeInput.substring(i);

        if(player.getTerritories().containsKey(placeInput)){
            return true;
        }
        printer.printNoMatchPlace();
        return false;
    }

    //done for now
    boolean isValidAction(String actioninput){
        if(actioninput == null){
            printer.printInputNothing();
            return false;
        }
        int i = nullifySpace(actioninput);
        return actioninput.charAt(i) == 'M' && actioninput.charAt(i) == 'A' && actioninput.charAt(i) == 'N';
    }
    //this function may not so useful
    int nullifySpace(String input){
        int i = 0;
        while(input.charAt(i) == ' ') {
            i++;
        }
        return i;
    }

}