package edu.duke651.wlt.client;

import java.util.Scanner;

public class ClientController {
    Player player;
    TerritoryRelation territoryRelation;
    Printer printer;
    ServerHandler serverHandler;
    Interpreter interpreter;
    Scanner scanner;
    public ClientController(){
        printer = Printer.getInstance();
        player = new Player();
        territoryRelation = new TerritoryRelation();
        interpreter = Interpreter.getInstance();
    }

    public void gameStart(Scanner scanner){
        this.scanner = scanner;
        printer.printCurrMap(territoryRelation);
        Order myorder;
        while(true){
            printer.printActionChoice();
            myorder = interpreter.getOrder();
            if(myorder==null){
                break;
            }
        }
    }

}
