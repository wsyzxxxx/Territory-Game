package edu.duke651.wlt.client;

import edu.duke651.wlt.models.*;
import org.json.JSONException;

import java.io.IOException;
import java.util.*;

public class ClientController {
    private ServerHandler serverHandler;
    private Map<String, Player> playerMap;
    private Map<String, Territory> territoryMap;
    private Interpreter interpreter;
    private Printer printer;
    private String playerName;

    private ArrayList<Order> moveOrders;
    private ArrayList<Order> attackOrders;


    public ClientController() throws IOException {
        this.interpreter = Interpreter.getInstance();
        this.printer = Printer.getInstance();
        this.printer.printWelcomeInfo();
        this.playerName = this.interpreter.getStringInput();

        this.serverHandler = new ServerHandler(this.playerName);
        this.playerMap = new HashMap<>();
        this.territoryMap = new HashMap<>();
    }


    private void getOrders(){
        this.moveOrders = new ArrayList<>();
        this.attackOrders = new ArrayList<>();
        while (true) {
            Order order = interpreter.getOrder(this.playerMap.get(playerName));
            if (order instanceof AttackOrder) {
                this.attackOrders.add(order);
            } else if (order instanceof MoveOrder) {
                this.moveOrders.add(order);
            } else {
                break;
            }
        }
    }

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
                }

                //wait for next rounds
                printer.printMessage("Waiting for other players......");
            }
        } catch (IllegalStateException e) {
            printer.printMessage("Game over! The winner is: " + e.getMessage());
        } catch (JSONException e) {
            printer.printMessage("JSONException: " + e.getMessage());
        } catch (Exception e) {
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
