package edu.duke651.wlt.client;

import edu.duke651.wlt.models.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import javafx.scene.text.*;
import javafx.stage.Stage;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GUIController {
    private Stage primaryStage;
    private ServerHandler serverHandler;
    private Map<String, Player> playerMap;
    private Map<String, Territory> territoryMap;
    private String playerName;
    private Text steps;
    private VBox vBox = new VBox();

    //orders
    private ArrayList<AttackActionOrder> attackActionOrders;
    private ArrayList<MoveActionOrder> moveActionOrders;
    private ArrayList<UpgradeUnitOrder> upgradeUnitOrders;
    private ArrayList<UpgradeTechOrder> upgradeTechOrders;

    public GUIController(Stage primaryStage) throws IOException {
        this.playerMap = new HashMap<>();
        this.territoryMap = new TerritoryMapInit().getMap();
        this.attackActionOrders = new ArrayList<>();
        this.moveActionOrders = new ArrayList<>();
        this.upgradeUnitOrders = new ArrayList<>();
        this.upgradeTechOrders = new ArrayList<>();
        this.steps = new Text("Territory");
        this.primaryStage = primaryStage;
        this.vBox.setPrefSize(150,400);
        this.vBox.setStyle("-fx-background-color:#AEEEEE");

        createName();
    }

    public void createName() {
        AnchorPane root = new AnchorPane();

        steps.setText("Please input your name:");
        steps.setTextAlignment(TextAlignment.CENTER);
        steps.setFont(Font.font(30));
        TextFlow tf = new TextFlow();
        tf.setTextAlignment(TextAlignment.CENTER);
        tf.setPrefWidth(1000);
        tf.getChildren().add(steps);

        Button confirm = new Button("Start Game!");
        confirm.setLayoutX(450);
        confirm.setLayoutY(350);

        TextField textField = new TextField();
        textField.setLayoutX(420);
        textField.setLayoutY(200);
        textField.setPromptText("Player Name");

        confirm.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                playerName = textField.getText();
                try {
                    serverHandler = new ServerHandler(playerName);
                    nextRound();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        root.getChildren().addAll(tf, confirm, textField);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public void nextRound() throws IOException {
        attackActionOrders.clear();
        moveActionOrders.clear();
        upgradeTechOrders.clear();
        upgradeTechOrders.clear();
        vBox.getChildren().clear();

        try {
            steps.setText("Waiting for other players...");
            //get the game info
            this.serverHandler.getResults(playerMap, territoryMap);

            renderScreen();

            //judge if the player has lose
            if (!this.playerMap.get(playerName).getTerritories().isEmpty()) {
                steps.setText("New round, take your actions!");
            } else {
                //lost prompt
                steps.setText("You have lost, but you can continue watching the game.");
            }
        } catch (IllegalStateException e) {
            System.out.println("Game over! The winner is: " + e.getMessage());
            this.primaryStage.close();
        } catch (JSONException e) {
            System.out.println("JSONException: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Fatal error: " + e.getMessage());
        }
    }

    public void finishThisRound() throws IOException {
        ArrayList<Order> orders = new ArrayList<>();
        orders.addAll(attackActionOrders);
        orders.addAll(moveActionOrders);
        orders.addAll(upgradeUnitOrders);
        orders.addAll(upgradeTechOrders);
        this.serverHandler.sendOrders(orders);
        nextRound();
    }

    public void renderScreen() {
        AnchorPane root = new AnchorPane();

        steps.setText("Territory");
        steps.setTextAlignment(TextAlignment.CENTER);
        steps.setFont(Font.font(30));
        TextFlow tf = new TextFlow();
        tf.setTextAlignment(TextAlignment.CENTER);
        tf.setPrefWidth(1000);
        tf.getChildren().add(steps);

        Button confirm = new Button("Finish this round");
        confirm.setLayoutX(450);
        confirm.setLayoutY(450);

        Button upgrade = new Button("Upgrade Tech");
        upgrade.setLayoutX(20);
        upgrade.setLayoutY(420);

        upgrade.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                vBox.getChildren().add(new Text("Upgrade Technology level"));
                upgradeTechOrders.add(new UpgradeTechOrder(playerMap.get(playerName)));
            }
        });

        confirm.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    finishThisRound();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        territoryMap.forEach((k, v) -> {
            GUIController guiController = this;
            Polyline polyline = ServerSetting.TERRITORY_POLY_MAP.get(k);
            polyline.setFill(Color.valueOf(v.getTerritoryOwner().getColor()));
            Text text = new Text(k);
            text.setFont(Font.font("Arial", FontWeight.BOLD, 20));
            text.setX(ServerSetting.NAME_ON_MAP.get(k).getKey());
            text.setY(ServerSetting.NAME_ON_MAP.get(k).getValue());

            //mouse event
            if (v.getTerritoryOwner() == playerMap.get(playerName)) {
                polyline.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    //click mouse action
                    public void handle(MouseEvent mouseEvent) {
                        ActionMenu g = new ActionMenu(v, guiController);
                        g.show(polyline, Side.RIGHT, -50, 50);
                    }
                });
            }

            root.getChildren().add(polyline);
            root.getChildren().add(text);
        });
        root.getChildren().add(confirm);
        root.getChildren().add(tf);
        root.getChildren().add(vBox);
        root.getChildren().add(upgrade);

        primaryStage.setScene(new Scene(root));
        primaryStage.setHeight(600);
        primaryStage.setWidth(1000);
        primaryStage.setTitle("wlt-RISC");
        primaryStage.show();
    }

    public void showAttackMenu(Territory territory) {
        AnchorPane anchorPane = new AnchorPane();

        Text attackText = new Text("Attack: from " + territory.getTerritoryName() + " to ");
        attackText.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        choiceBox.setItems(FXCollections.observableArrayList(territory.getAttackTerritory()));
        attackText.setX(300);
        attackText.setY(100);
        choiceBox.setLayoutX(550);
        choiceBox.setLayoutY(80);
        anchorPane.getChildren().addAll(attackText, choiceBox);

        ArrayList<TextField> textFields = new ArrayList<>();
        ArrayList<Integer> levels = territory.getTerritoryUnitsInLevel();
        for (int i = 0; i < levels.size(); i++) {
            Text levelText = new Text("You have " + levels.get(i) + " unit(s) in level " + i + ", and how many will dispatch for this attack?");
            levelText.setFont(Font.font("Arial",15));
            levelText.setX(150);
            levelText.setY(200 + i * 30);
            TextField textField = new TextField();
            textField.setLayoutX(650);
            textField.setLayoutY(200 + i * 30 - 15);
            textField.setText("0");
            anchorPane.getChildren().addAll(levelText, textField);
            textFields.add(textField);
        }

        Button confirm = new Button("Confirm");
        Button cancel = new Button("Cancel");
        confirm.setLayoutX(300);
        confirm.setLayoutY(450);
        cancel.setLayoutX(600);
        cancel.setLayoutY(450);
        anchorPane.getChildren().addAll(confirm, cancel);

        Stage menuStage = new Stage();
        menuStage.setScene(new Scene(anchorPane));
        menuStage.setHeight(600);
        menuStage.setWidth(1000);
        menuStage.setTitle("wlt-RISC");
        menuStage.show();

        cancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                menuStage.close();
            }
        });
        confirm.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ArrayList<Integer> unitList = new ArrayList<>();
                textFields.forEach(textField -> unitList.add(Integer.valueOf(textField.getText().trim())));
                attackActionOrders.add(new AttackActionOrder(playerMap.get(playerName), territory, territoryMap.get(choiceBox.getSelectionModel().getSelectedItem()), unitList));
                vBox.getChildren().add(new Text("Attack: from " + territory.getTerritoryName() + " to " + choiceBox.getSelectionModel().getSelectedItem()));
                menuStage.close();
            }
        });
    }

    public void showMoveMenu(Territory territory) {
        AnchorPane anchorPane = new AnchorPane();

        Text attackText = new Text("Move: from " + territory.getTerritoryName() + " to ");
        attackText.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        choiceBox.setItems(FXCollections.observableArrayList(playerMap.get(playerName).getTerritories().keySet()));
        attackText.setX(300);
        attackText.setY(100);
        choiceBox.setLayoutX(550);
        choiceBox.setLayoutY(80);
        anchorPane.getChildren().addAll(attackText, choiceBox);

        ArrayList<TextField> textFields = new ArrayList<>();
        ArrayList<Integer> levels = territory.getTerritoryUnitsInLevel();
        for (int i = 0; i < levels.size(); i++) {
            Text levelText = new Text("You have " + levels.get(i) + " unit(s) in level " + i + ", and how many will dispatch for this move?");
            levelText.setFont(Font.font("Arial",15));
            levelText.setX(150);
            levelText.setY(200 + i * 30);
            TextField textField = new TextField();
            textField.setLayoutX(650);
            textField.setLayoutY(200 + i * 30 - 15);
            textField.setText("0");
            anchorPane.getChildren().addAll(levelText, textField);
            textFields.add(textField);
        }

        Button confirm = new Button("Confirm");
        Button cancel = new Button("Cancel");
        confirm.setLayoutX(300);
        confirm.setLayoutY(450);
        cancel.setLayoutX(600);
        cancel.setLayoutY(450);
        anchorPane.getChildren().addAll(confirm, cancel);

        Stage menuStage = new Stage();
        menuStage.setScene(new Scene(anchorPane));
        menuStage.setHeight(600);
        menuStage.setWidth(1000);
        menuStage.setTitle("wlt-RISC");
        menuStage.show();

        cancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                menuStage.close();
            }
        });
        confirm.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ArrayList<Integer> unitList = new ArrayList<>();
                textFields.forEach(textField -> unitList.add(Integer.valueOf(textField.getText().trim())));
                moveActionOrders.add(new MoveActionOrder(playerMap.get(playerName), territory, territoryMap.get(choiceBox.getSelectionModel().getSelectedItem()), unitList));
                vBox.getChildren().add(new Text("Move: from " + territory.getTerritoryName() + " to " + choiceBox.getSelectionModel().getSelectedItem()));
                menuStage.close();
            }
        });
    }

    public void showUpgradeMenu(Territory territory) {
        AnchorPane anchorPane = new AnchorPane();

        Text attackText = new Text("Upgrade: in " + territory.getTerritoryName());
        attackText.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        attackText.setX(400);
        attackText.setY(100);
        anchorPane.getChildren().add(attackText);

        ArrayList<TextField> textFields = new ArrayList<>();
        ArrayList<Integer> levels = territory.getTerritoryUnitsInLevel();
        for (int i = 0; i < levels.size(); i++) {
            Text levelText = new Text("You have " + levels.get(i) + " unit(s) in level " + i + ", and how many will upgrade to?");
            levelText.setFont(Font.font("Arial",15));
            levelText.setX(150);
            levelText.setY(200 + i * 30);
            TextField textField = new TextField();
            textField.setLayoutX(650);
            textField.setLayoutY(200 + i * 30 - 15);
            textField.setText("0");
            anchorPane.getChildren().addAll(levelText, textField);
            textFields.add(textField);
        }

        Button confirm = new Button("Confirm");
        Button cancel = new Button("Cancel");
        confirm.setLayoutX(300);
        confirm.setLayoutY(450);
        cancel.setLayoutX(600);
        cancel.setLayoutY(450);
        anchorPane.getChildren().addAll(confirm, cancel);

        Stage menuStage = new Stage();
        menuStage.setScene(new Scene(anchorPane));
        menuStage.setHeight(600);
        menuStage.setWidth(1000);
        menuStage.setTitle("wlt-RISC");
        menuStage.show();

        cancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                menuStage.close();
            }
        });
        confirm.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ArrayList<Integer> unitList = new ArrayList<>();
                textFields.forEach(textField -> unitList.add(Integer.valueOf(textField.getText().trim())));
                upgradeUnitOrders.add(new UpgradeUnitOrder(territory, unitList));
                vBox.getChildren().add(new Text("Upgrade: in " + territory.getTerritoryName()));
                menuStage.close();
            }
        });
    }
}
