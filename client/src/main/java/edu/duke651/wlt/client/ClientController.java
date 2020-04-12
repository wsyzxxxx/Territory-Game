package edu.duke651.wlt.client;

import edu.duke651.wlt.models.*;

import java.io.IOException;
import java.util.*;

public class ClientController {
    private ServerHandler serverHandler;
    private Map<String, Player> playerMap;
    private Map<String, Territory> territoryMap;
    private Interpreter interpreter;
    private Printer printer;

    public ClientController(String clientName) throws IOException {
        this.serverHandler = new ServerHandler(clientName);
        this.playerMap = new HashMap<>();
        this.territoryMap = new HashMap<>();
    }

    Player player;
    Scanner scanner;
    Collection<Player> players;
    //GameController gameController;
    List<Order> myAttackOrders;
    List<Order> myMoveOrders;

    HashMap<String, ArrayList<Territory> > territoryGroup;//I need this map!

    public ClientController(){
        printer = new Printer();
      //  gameController = new GameController();
        interpreter = Interpreter.getInstance(new Scanner(System.in));
    }

    public void gameStart() throws Exception {
        //step1 select group
        selectTerritories(player, territoryGroup);
        //step2 get orders of this player
        getOrders();

    }
    public void getOrders(){
        printer.printCurrMap(players);//players list need store into each client information
        myAttackOrders = new ArrayList<>();
        myMoveOrders = new ArrayList<>();
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
    public void selectTerritories(Player player, HashMap<String, ArrayList<Territory> > territoryGroup) throws Exception {
        if(territoryGroup.isEmpty()) {
            throw new Exception("Game not start!");
        }
        interpreter.getGroupSelection(player, territoryGroup);
        /*
        while(numsTerritories > 0) {
            numsTerritories--;
            interpreter.getSelection(player, territoryMap);
        }
        this part is for player select territory one by one
         */
        printer.printFinishSelection_prompt();
        printer.printCurrTerritories_Prompt(player);
    }




//    Player player;
//    Scanner scanner;
//    TerritoryRelation territoryRelation;
//    Printer printer;
//    ServerHandler serverHandler;
//    Interpreter interpreter;
//    Scanner scanner;
//    public ClientController(){
//        printer = Printer.getInstance();
//        player = new Player();
//        territoryRelation = new TerritoryRelation();
//        interpreter = Interpreter.getInstance(new Scanner(System.in));
//    }
//
//    public void gameStart(Scanner scanner){
//        this.scanner = scanner;
//        printer.printCurrMap(territoryRelation);
//        Order myorder;
//        while(true){
//            myorder = interpreter.getOrder();
//            if(myorder==null){
//                break;
//            }
//        }
//    }

}
