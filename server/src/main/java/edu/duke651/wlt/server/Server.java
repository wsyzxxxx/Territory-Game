/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package edu.duke651.wlt.server;

import java.util.Scanner;

/**
 * @program: wlt-risc
 * @description: This is the main class of server responsible for newing game class and ending game.
 * @author: Will
 * @create: 2020-04-11 11:20
 **/
public class Server {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        do {
            try {
                System.out.println("Game Start!");
                GameController gameController = new GameController();
                gameController.startGame();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Some error happens! Please restart the game!");
            }
            System.out.println("Start next round game? (y or any key to quit):");
        } while (scanner.nextLine().equals("y"));

        System.out.println("Server closed!");
    }
}
