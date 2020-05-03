package edu.duke651.wlt.client;

import javafx.application.Application;
import javafx.stage.Stage;

public class Client extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            GUIController guiController = new GUIController(primaryStage);
            guiController.startGame();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERROR: Please restart the program!");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
