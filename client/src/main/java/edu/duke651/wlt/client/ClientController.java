package edu.duke651.wlt.client;

import edu.duke651.wlt.models.*;

import java.util.*;

public class ClientController {
    Player player;
    Scanner scanner;
    Printer printer;
    ServerHandler serverHandler;
    Interpreter interpreter;
    Collection<Player> players;
    GameController gameController;
    public ClientController(){
        printer = new Printer();
        gameController = new GameController();
        interpreter = Interpreter.getInstance(new Scanner(System.in));
    }

    public void gameStart(){
        selectTerritories(player, 3, gameController.territoriesMap);
        printer.printCurrMap(players);//players list need store into each client information
        List<Order> myAttackOrders = new ArrayList<>();
        List<Order> myMoveOrders = new ArrayList<>();
        while(true){
            Order myOrder = interpreter.getOrder(player);
            if(myOrder==null){
                break;
            }
            if(myOrder instanceof AttackOrder){
                myAttackOrders.add(myOrder);
            }
            if(myOrder instanceof MoveOrder){
                myMoveOrders.add(myOrder);
            }

        }//now we have the two order lists
    }
    public void selectTerritories(Player player, int numsTerritories, HashMap<String, Territory> territoryMap) throws Exception {
        if(territoryMap == null) {
            throw new Exception("Game not start!");
        }
        while(numsTerritories > 0){
            numsTerritories--;
            interpreter.getSelection(player, territoryMap);
        }
        printer.printFinishSelection_prompt();
        printer.printCurrTerritories_Prompt(player);
    }

}
