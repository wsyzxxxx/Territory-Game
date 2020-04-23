package edu.duke651.wlt.client;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Text instruction = new Text("test");
        instruction.setTextAlignment(TextAlignment.CENTER);
        instruction.setFont(Font.font(30));
        TextFlow tf = new TextFlow();
        //tf.setStyle("-fx-background-color:#EECFA1");
        tf.setTextAlignment(TextAlignment.CENTER);
        tf.setPrefWidth(1000);
        tf.getChildren().add(instruction);
        Polyline[] tr = new Polyline[9];
        tr[0] = new Polyline(  300,200,  400,190, 400,270, 370,350, 320,350, 260,250, 300,200);
        tr[1] = new Polyline(  400,270,  370,350, 570,350, 580,290, 480,290, 400,270);
        tr[2] = new Polyline(  400,270,  400,230, 500,210, 540,250, 590,250, 580,290, 480,290, 400,270);

        tr[3] = new Polyline(  570,350,  580,290, 590,250, 660,240, 700,300, 650,370, 570,350);
        tr[4] = new Polyline(  500,210,  540,250, 590,250, 660,240, 670,170, 500,210);
        tr[5] = new Polyline(  585,190,  670,170, 680,175, 690,90, 590,100, 585,190);

        tr[6] = new Polyline(  320,198,  400,190, 430,130, 380,100, 310,130, 320,198);
        tr[7] = new Polyline(  400,190,  400,230, 490,212, 590,100, 460,70, 400,190);
        tr[8] = new Polyline(  490,212,  500,210, 585,190, 590,100, 490,212);
        tr[0].setFill(Color.valueOf("#00a6ac"));
        tr[1].setFill(Color.valueOf("#00a6ac"));
        tr[2].setFill(Color.valueOf("#00a6ac"));

        tr[3].setFill(Color.valueOf("#e0861a"));
        tr[4].setFill(Color.valueOf("#e0861a"));
        tr[5].setFill(Color.valueOf("#e0861a"));

        tr[6].setFill(Color.valueOf("#7fb80e"));
        tr[7].setFill(Color.valueOf("#7fb80e"));
        tr[8].setFill(Color.valueOf("#7fb80e"));
        AnchorPane root = new AnchorPane();
        for(int i = 0; i < 9; i++) {
            int finalI = i;
            tr[i].setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                //click mouse action
                public void handle(MouseEvent mouseEvent) {
                    GlobalMenu.getInstance().show(tr[finalI], Side.RIGHT, -50, 50);

                }
            });
        }

        root.getChildren().addAll(tr[1],tr[2],tr[3],tr[4],tr[5],tr[7],tr[8],tr[0],tr[6],tf);
        Scene sc = new Scene(root);
        primaryStage.setScene(sc);
        primaryStage.setHeight(600);
        primaryStage.setWidth(1000);
        primaryStage.setTitle("RISC");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
