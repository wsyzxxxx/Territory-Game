package edu.duke651.wlt.client;

import edu.duke651.wlt.models.Order;
import edu.duke651.wlt.models.Player;
import edu.duke651.wlt.models.Territory;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import javax.swing.plaf.IconUIResource;
import javax.swing.text.TabExpander;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GuiComponents {
    private Text instruction;
    private Collection<Order> moveOrders;
    private Collection<Order> attackOrders;
    private Collection<Order> upgradeOrders;
    public static final Polyline[] TR = new Polyline[9];{
        TR[0] = new Polyline(  300,200,  400,190, 400,270, 370,350, 320,350, 260,250, 300,200);
        TR[1] = new Polyline(  400,270,  370,350, 570,350, 580,290, 480,290, 400,270);
        TR[2] = new Polyline(  400,270,  400,230, 500,210, 540,250, 590,250, 580,290, 480,290, 400,270);

        TR[3] = new Polyline(  570,350,  580,290, 590,250, 660,240, 700,300, 650,370, 570,350);
        TR[4] = new Polyline(  500,210,  540,250, 590,250, 660,240, 670,170, 500,210);
        TR[5] = new Polyline(  585,190,  670,170, 680,175, 690,90, 590,100, 585,190);

        TR[6] = new Polyline(  320,198,  400,190, 430,130, 380,100, 310,130, 320,198);
        TR[7] = new Polyline(  400,190,  400,230, 490,212, 590,100, 460,70, 400,190);
        TR[8] = new Polyline(  490,212,  500,210, 585,190, 590,100, 490,212);
    }
    public static final HashMap<String,Polyline> polyTerritory = new HashMap<>();{
        polyTerritory.put("Elantris", TR[0]);
        polyTerritory.put("Roshar", TR[1]);
        polyTerritory.put("Scadrial", TR[2]);
        polyTerritory.put("Hogwarts", TR[3]);
        polyTerritory.put("Mordor", TR[4]);
        polyTerritory.put("Gondor", TR[5]);
        polyTerritory.put("Narnia", TR[6]);
        polyTerritory.put("Midkemia", TR[7]);
        polyTerritory.put("Oz", TR[8]);
    }
    public Polyline[] getTerritories(){
        return TR;
    }

    public void initialize(Stage primaryStage){
        Collection<Player> players = getPlayers();

        moveOrders = new ArrayList<>();
        attackOrders = new ArrayList<>();
        upgradeOrders = new ArrayList<>();
        Button commit = new Button();
        AnchorPane root = new AnchorPane();
        Text instruction = new Text("Please select your territory and order");
        Text test = new Text("My Orders:\n");
        instruction.setTextAlignment(TextAlignment.CENTER);
        instruction.setFont(Font.font(30));
        TextFlow tf = new TextFlow();
        //tf.setStyle("-fx-background-color:#EECFA1");
        tf.setTextAlignment(TextAlignment.CENTER);
        tf.setPrefWidth(1000);
        tf.getChildren().add(instruction);
        VBox vBox = new VBox();
        vBox.setPrefSize(100,100);
        vBox.getChildren().add(test);
        vBox.setStyle("-fx-background-color:#AEEEEE");
        //paint color.
        renderColor(players);

        root.getChildren().addAll(TR[1],TR[2],TR[3],TR[4],TR[5],TR[7],TR[8],TR[0],TR[6],tf,vBox);

        Scene sc = new Scene(root);
        primaryStage.setScene(sc);
        primaryStage.setHeight(600);
        primaryStage.setWidth(1000);
        primaryStage.setTitle("RISC");
        primaryStage.show();
    }
    //waiting to implement. get players from server at first
    private Collection<Player> getPlayers() {
        return null;
    }

    private void renderColor(Collection<Player> players){
        for(Player player: players){
            String color = player.getColor();
            Map<String, Territory> territories = player.getTerritories();
            for(String name:territories.keySet()){
                polyTerritory.get(name).setFill(Color.valueOf(color));
            }
        }
    }
}
