package edu.duke651.wlt.client;

import edu.duke651.wlt.models.Order;
import edu.duke651.wlt.models.Player;

import java.util.Scanner;

public class ClientController {
    Player player;
    Scanner scanner;
    TerritoryRelation territoryRelation;
    Printer printer;
    ServerHandler serverHandler;
    Interpreter interpreter;
    Scanner scanner;
    public ClientController(){
        printer = Printer.getInstance();
        player = new Player();
        territoryRelation = new TerritoryRelation();
        interpreter = Interpreter.getInstance(new Scanner(System.in));
    }

    public void gameStart(Scanner scanner){
        this.scanner = scanner;
        printer.printCurrMap(territoryRelation);
        Order myorder;
        while(true){
            myorder = interpreter.getOrder();
            if(myorder==null){
                break;
            }
        }
    }

}
