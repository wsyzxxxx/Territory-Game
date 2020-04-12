package edu.duke651.wlt.client;

import edu.duke651.wlt.models.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

/**
 * @Author: Tong
 * @Description: the only class get input from players
 * @Pattern:Singleton
 * @Modified by: Will
 */

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

    //get single order from input,
    // @Parameter: player who make the input,
    // @Parameter: territoryMap who stores all information about territories
    public Order getOrder(Player player, Map<String, Territory> territoryMap) {
        //show the map? . no just show once , so need to be called in ClientController
        printer.printActionChoice();
        while(true) {
            String action = getStringInput().toUpperCase();
            try {
                switch (action) {
                    case "N":
                        printer.printEndRound_prompt();//this may need another function in promptBase and printer
                        return null;
                    case "M":
                        return getMove(player);
                    case "A":
                        return getAttack(player, territoryMap);
                    default:
                        printer.printInvalidAction();
                        break;
                }
            } catch (IllegalArgumentException e) {
                printer.printMessage("Please re-choose your action");
                printer.printActionChoice();
            }
        }
    } // done now

    //get source territory from player's input
    // @Parameter: player who make the input
    Territory getSource(Player player) {
        printer.printOriginalTerritoriesChoice();
        source = getStringInput();
        if(isValidOriginalPlace(source, player)){
            return player.getTerritories().get(source);
        } else {
            printer.printInvalidSourcePlace();
            throw new IllegalArgumentException();
        }
    }
    //get units number from input,
    // @Parameter: player who make the input,
    int getUnits(Player player) {
        printer.printUnits_prompt();
        String units = getStringInput();
        try {
            int output = Integer.parseInt(units);
            if (player.getTerritories().get(source).getTerritoryUnits() < output || output <= 0) {
                printer.printLargerUnits();
                throw new IllegalArgumentException();
            } else {
                return output;
            }
        } catch (NumberFormatException e) {
            printer.printInvalidUnits();
            throw new IllegalArgumentException();
        }
    }

    //get move action target territory from input,
    // @Parameter: player who make the input,
    Territory getAim(Player player) {
        printer.printEndTerritoriesChoice();
        this.aim = getStringInput();
        if(isValidEndPlace(aim, player)){
            return player.getTerritories().get(aim);
        } else {
            printer.printInvalidEndPlace();
            throw new IllegalArgumentException();
        }
    }

    //get attack action target territory from input,
    // @Parameter: player who make the input,
    // @Parameter: territoryMap who stores all information about territories
    Territory getAttackPlace(Player player, Map<String, Territory> territoryMap) {
        printer.printEndTerritoriesChoice();
        String attackPlace = getStringInput();
        if (isValidAttackPlace(attackPlace, player, territoryMap)) {
            return territoryMap.get(attackPlace);
        } else {
            printer.printInvalidAttackPlace();
            throw new IllegalArgumentException();
        }
    }

    //get attack order from input,
    // @Parameter: player who make the input,
    // @Parameter: territoryMap who stores all information about territories
    AttackOrder getAttack(Player player, Map<String, Territory> territoryMap) {
        return new AttackOrder(player, getSource(player), getAttackPlace(player, territoryMap), getUnits(player));
    }
    //get move order from input,
    // @Parameter: player who make the input,
    // @Parameter: territoryMap who stores all information about territories
    MoveOrder getMove(Player player) {
        return new MoveOrder(player, getSource(player), getAim(player), getUnits(player));
    }
    //get input from player
    String getStringInput() {
        String res = scanner.nextLine().trim();
        while (res.isEmpty()) {
            printer.printInputNothing();
            res = scanner.nextLine().trim();
        }
        return res;
    }
    //check attack target valid in both semantics and in territory relation.
    // @Parameter: input from player
    // @Parameter: player who make the input,
    // @Parameter: territoryMap who stores all information about territories
    boolean isValidAttackPlace(String placeInput, Player player, Map<String, Territory> territoryMap) {
        placeInput = placeInput.trim();

        if (player.getTerritories().containsKey(placeInput)) {
            System.out.println("you cannot attack ur own territory! Please input again");
            return false;
        } else if (player.getTerritories().get(source).checkNeighbor(territoryMap.get(placeInput))) {
            return true;
        } else {
            printer.printUnreachablePlace();
            return false;
        }
    }

    //done now,
    //check move target valid in both semantics and in territory relation.
    // @Parameter: input from player
    // @Parameter: player who make the input
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
    //check move target valid in both semantics and in territory relation.
    // @Parameter: input from player
    // @Parameter: player who make the input
    boolean isValidOriginalPlace(String placeInput, Player player) {
        placeInput = placeInput.trim();
        if (player.getTerritories().containsKey(placeInput)){
            return true;
        }

        printer.printNoMatchPlace();
        return false;
    }
}