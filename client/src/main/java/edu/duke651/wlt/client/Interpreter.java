package edu.duke651.wlt.client;

import edu.duke651.wlt.models.*;

import java.lang.management.PlatformLoggingMXBean;
import java.util.Scanner;

public class Interpreter {

    Scanner scanner;

    private static Interpreter instance = new Interpreter();
    private Interpreter(){}
    public static Interpreter getInstance(){
        return instance;
    }
    Order getOrder(){
        String action = getStringInput(scanner);
        if(action.equals("N")) {
            System.out.println("you choose do nothing in this round");//this may need another function in promptbase and printer
            return null;
        }else if(action.equals("M")) {
            return getMove();
        }else if(action.equals("A"){
            return getAttack();
        }
    }
    Territory getSource(Player player) throws Exception {
        String source = getStringInput(scanner);
        while(true){
            if(isValidOriginalPlace(source, player)){
                return player.getTerritories.get(source);
            } else {
                //here may need printer.
                System.out.println("");
            }
        }
    }
    int getUnites(Player player) throws Exception {
        String aim = getStringInput(scanner);
        while(true){
            if(isValidNums(aim, player)){
                return Integer.parseInt(aim);
            } else {
                //here may need printer.
                System.out.println("");
            }
        }
    }
    Territory getAim(Player player) throws Exception {
        String aim = getStringInput(scanner);
        while(true){
            if(isValidEndPlace(aim, player)){
                return player.getTerritories.get(aim);
            } else {
                //here may need printer.
                System.out.println("");
            }
        }
    }
    AttackOrder getAttack(Player player) throws Exception {

        new AttackOrder(player, getSource(player), getAim(player), getUnites(player));
    }
    MoveOrder getMove(){
        new MoveOrder(player, getSource(player), getAim(player), getUnites(player));
    }
    String getStringInput(Scanner scanner){
        String curInput = scanner.nextLine();
        return curInput;
    }
    boolean isValidNums(String numsInput, Player player) throws Exception {
        if(numsInput == "" || numsInput == null) {
            throw new Exception("input invalid, please input again");
            return false;
        }
        int i = nullifySpace(numsInput);
        numsInput = numsInput.substring(i);
        if(isNumeric(numsInput)){
            return true;
        }
        return false;
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

    boolean isValidAttackPlace(String placeInput, Player player) throws Exception {
        if(placeInput == "" || placeInput == null) {
            throw new Exception("input invalid, please input again");
            return false;
        }
        int i = nullifySpace(placeInput);
        placeInput = placeInput.substring(i);
        if(checkReachable()) {
            if(player.territories.containsKey(placeInput)) {
                System.out.println("you attack ur own territory");
                return false;
            }
            return true;
        } else {
            throw new Exception("input invalid, please input again");
        }
    }
    boolean isValidEndPlace(String placeInput, Player player) throws Exception{
        if(placeInput == "" || placeInput == null) {
            throw new Exception("input invalid, please input again");
            return false;
        }
        int i = nullifySpace(placeInput);
        placeInput = placeInput.substring(i);
        //next, check the place, original place
        if(player.territories.containsKey(placeInput)) {
            return checkReachable();
        } else{
            throw new Exception("invalid input of place");
        }
    }
    boolean isValidOriginalPlace(String placeInput, Player player) throws Exception {
        if(placeInput == "" || placeInput == null) {
            throw new Exception("input invalid, please input again");
            return false;
        }
        int i = nullifySpace(placeInput);
        placeInput = placeInput.substring(i);
        //next, check the place, original place
        if(player.territories.containsKey(placeInput)){
            return true;
        }
        throw new Exception("invalid input of place");
    }

    boolean isValidAction(String actioninput, Player player) throws Exception {
        if(actioninput == null){
            throw new Exception("input invalid, please input again");
            return false;
        }
        int i = nullifySpace(actioninput);
        if(actioninput.charAt(i) != 'M' || actioninput.charAt(i) != 'A'|| actioninput.charAt(i) != 'N'){
            throw new Exception("input invalid, please input again");
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