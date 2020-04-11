package edu.duke651.wlt.client;

import edu.duke651.wlt.models.Player;
import edu.duke651.wlt.models.Territory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
