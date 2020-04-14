package edu.duke651.wlt.client;

import edu.duke651.wlt.models.*;
import org.json.JSONException;

import java.io.IOException;
import java.util.*;

/**
 * @Author: Will, Tong
 * @Description: the game controller in client end
 * @Modified by: Will
 */

public class ClientController {
    private final ServerHandler serverHandler;
    private Map<String, Player> playerMap;
    private Map<String, Territory> territoryMap;
    private final Interpreter interpreter;
    private final Printer printer;
    private String playerName;
    //use two lists to store the orders get from player, ready to send it to server
    private ArrayList<Order> moveOrders;
    private ArrayList<Order> attackOrders;

    //initialize the controller fields
    public ClientController() throws IOException {
        this.interpreter = Interpreter.getInstance();
        this.printer = Printer.getInstance();
        this.printer.printWelcomeInfo();
        this.playerName = this.interpreter.getStringInput();

        this.serverHandler = new ServerHandler(this.playerName);
        this.playerMap = new HashMap<>();
        this.territoryMap = new HashMap<>();
    }

    //this is the only function can get the input from client
    //the goal it to get orders and store them in order lists.
    private void getOrders(){
        this.moveOrders = new ArrayList<>();
        this.attackOrders = new ArrayList<>();
        while (true) {
            //get order
            Order order = interpreter.getOrder(this.playerMap.get(playerName), territoryMap);
            if (order instanceof AttackOrder) {
                this.attackOrders.add(order);
            } else if (order instanceof MoveOrder) {
                this.moveOrders.add(order);
            } else {
                break;
            }
        }
    }
    //run game in client, get interaction with server
    public void startGame() throws IOException {
        this.printer.printMessage("Game start!");
        try {
            while (true) {
                //get the game info
                this.serverHandler.getResults(playerMap, territoryMap);

                //print the current game info
                this.printer.printCurrMap(this.playerMap.values());

                //judge if the player has lose
                if (!this.playerMap.get(playerName).getTerritories().isEmpty()) {
                    //get all the new orders
                    getOrders();
                    //send move orders
                    this.serverHandler.sendOrders(moveOrders);
                    //send attack orders
                    this.serverHandler.sendOrders(attackOrders);

                    //wait for next rounds
                    printer.printMessage("Waiting for other players......");
                } else {
                    //lost prompt
                    printer.printMessage("You have lost, but you can continue watching the game.");
                }
            }
        } catch (IllegalStateException e) {
            printer.printMessage("Game over! The winner is: " + e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
            printer.printMessage("JSONException: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            printer.printMessage("Fatal error: " + e.getMessage());
        }

        this.serverHandler.closeLink();
    }

//    public void selectTerritories(Player player, HashMap<String, ArrayList<Territory> > territoryGroup) throws Exception {
//        if(territoryGroup.isEmpty()) {
//            throw new Exception("Game not start!");
//        }
//        interpreter.getGroupSelection(player, territoryGroup);
//        /*
//        while(numTerritories > 0) {
//            numTerritories--;
//            interpreter.getSelection(player, territoryMap);
//        }
//        this part is for player select territory one by one
//         */
//        printer.printFinishSelection_prompt();
//        printer.printCurrTerritories_Prompt(player);
//    }
}
