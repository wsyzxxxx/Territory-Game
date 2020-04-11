package edu.duke651.wlt.client;

import edu.duke651.wlt.models.*;

import java.util.Scanner;

public class Interpreter {
    static Printer printer;
    static Scanner scanner;
    String source;
    String aim;
    String attackpPlace;
    String unites;
    private static Interpreter instance = new Interpreter();
    private Interpreter(){ }
    public static Interpreter getInstance(Scanner s){
        scanner = s;
        printer = new Printer();
        return instance;
    }

    Order getOrder(Player player){
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
            if(isValidNums(unites, player)){
                return Integer.parseInt(unites);
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
            attackpPlace = getStringInput(scanner);
            if(isValidAttackPlace(attackpPlace, player)){
                return player.getTerritories().get(attackpPlace);
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
    boolean isValidNums(String numsInput, Player player) {
        if(numsInput.equals("")) {
            printer.printInvalidUnits();
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
            return false;
        }
        int i = nullifySpace(placeInput);
        placeInput = placeInput.substring(i);
        //
        if(player.getTerritories().containsKey(placeInput)) {
            System.out.println("you attack ur own territory");
            return false;
        } else if(player.checkReachable(player.getTerritories().get(source), player.getTerritories().get(placeInput))) {
            return true;
        } else {
            return false;
        }
    }
    //done now,
    boolean isValidEndPlace(String placeInput, Player player){
        if(placeInput.equals("")) {
            System.out.println("input invalid, please input again");
            return false;
        }
        int i = nullifySpace(placeInput);
        placeInput = placeInput.substring(i);
        //next, check the place, original place
        if(player.getTerritories().containsKey(placeInput)) {
            return player.checkReachable(player.getTerritories().get(source), player.getTerritories().get(placeInput));
        } else{
            System.out.println("invalid input of place");
            return false;
        }
    }

    //done by now
    boolean isValidOriginalPlace(String placeInput, Player player) {
        if(placeInput.equals("")) {
            System.out.println("input invalid, please input again");
            return false;
        }
        int i = nullifySpace(placeInput);
        placeInput = placeInput.substring(i);

        if(player.getTerritories().containsKey(placeInput)){
            return true;
        }
        System.out.println("invalid input of place");
        return false;
    }

    //done for now
    boolean isValidAction(String actioninput){
        if(actioninput == null){
            System.out.println("input invalid, please input again");
            return false;
        }
        int i = nullifySpace(actioninput);
        if(actioninput.charAt(i) != 'M' || actioninput.charAt(i) != 'A'|| actioninput.charAt(i) != 'N'){
            System.out.println("input invalid, please input again");
            return false;
        }
        return true;
    }
    int nullifySpace(String input){
        int i = 0;
        while(input.charAt(i) == ' ') {
            i++;
        }
        return i;
    }

}