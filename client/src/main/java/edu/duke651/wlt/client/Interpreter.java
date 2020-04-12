package edu.duke651.wlt.client;

import edu.duke651.wlt.models.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class Interpreter {
    private static Interpreter instance = new Interpreter();
    private Printer printer;
    private Scanner scanner;
    private String source;
    private String aim;

    private Interpreter() {
        this.scanner = new Scanner(System.in);
        this.printer = Printer.getInstance();
    }

    public static Interpreter getInstance(){
        return instance;
    }

//    public void getGroupSelection(Player player, Map<String, ArrayList<Territory>> territoryGroup) {
//        if(territoryGroup.isEmpty()){
//            System.out.println("No more territories group to choose!");
//            return;
//        }
//        //printer.printCurrMapWithoutPlayer(territoryMap); for now there is no need,
//        printer.printSelectTerritories_Prompt(player, territoryGroup);
//
//        while(true) {
//            String key = getStringInput();
//            if (territoryGroup.containsKey(key)) {
//                printer.printTerritoryChoice_prompt();//may need call territory's name
//                for(Territory territory : territoryGroup.get(key)){
//                    territory.setTerritoryOwner(player);
//                }
//                territoryGroup.remove(key);
//            } else {
//                printer.printInvalidChoice_prompt();
//            }
//        }
//    }
//
//    //this function is used for choose territory one by one by player.
//    public void getSelection(Player player, Map<String, Territory> territoryMap){
//        if(territoryMap.isEmpty()){
//            System.out.println("No more territories to choose!");
//            return;
//        }
//        printer.printSelectTerritories_Prompt();
//        printer.printCurrMapWithoutPlayer(territoryMap);
//
//        String territoryName = getStringInput(scanner);
//        while(true) {
//            if (territoryMap.containsKey(territoryName)) {
//                printer.printTerritoryChoice_prompt();//may need call territory's name
//                territoryMap.get(territoryName).setTerritoryOwner(player);
//                territoryMap.remove(territoryName);
//            } else {
//                printer.printInvalidChoice_prompt();
//            }
//        }
//    }

    public Order getOrder(Player player){
        //show the map? . no just show once , so need to be called in ClientController
        printer.printActionChoice();
        while(true) {
            String action = getStringInput().toUpperCase();
            switch (action) {
                case "N":
                    printer.printEndRound_prompt();//this may need another function in promptBase and printer
                    return null;
                case "M":
                    return getMove(player);
                case "A":
                    return getAttack(player);
                default:
                    printer.printInvalidAction();
                    break;
            }
        }
    } // done now

    Territory getSource(Player player) {
        printer.printOriginalTerritoriesChoice();

        while(true){
            source = getStringInput();
            if(isValidOriginalPlace(source, player)){
                return player.getTerritories().get(source);
            } else {
                printer.printInvalidSourcePlace();
            }
        }
    }

    int getUnits(Player player) {
        printer.printUnits_prompt();
        while(true) {
            String units = getStringInput();
            try {
                int output = Integer.parseInt(units);
                if (player.getTerritories().get(source).getTerritoryUnits() < output || output <= 0) {
                    printer.printLargerUnits();
                } else {
                    return output;
                }
            } catch (NumberFormatException e) {
                printer.printInvalidUnits();
            }
        }
    }

    Territory getAim(Player player) {
        printer.printEndTerritoriesChoice();
        while(true){
            this.aim = getStringInput();
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
            String attackPlace = getStringInput();
            if(isValidAttackPlace(attackPlace, player)){
                return player.getTerritories().get(attackPlace);
            } else {
                //here may need printer.
                printer.printInvalidAttackPlace();
            }
        }
    }

    AttackOrder getAttack(Player player) {
        return new AttackOrder(player, getSource(player), getAttackPlace(player), getUnits(player));
    }

    MoveOrder getMove(Player player) {
        return new MoveOrder(player, getSource(player), getAim(player), getUnits(player));
    }

    String getStringInput() {
        String res = "";
        while (res.isEmpty()) {
            res = scanner.nextLine().trim();
            printer.printInputNothing();
        }
        return res;
    }

    boolean isValidAttackPlace(String placeInput, Player player) {
        placeInput = placeInput.trim();
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
        placeInput = placeInput.trim();

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
        placeInput = placeInput.trim();
        if (player.getTerritories().containsKey(placeInput)){
            return true;
        }

        printer.printNoMatchPlace();
        return false;
    }
}