package edu.duke651.wlt.client;

import edu.duke651.wlt.models.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;
import javafx.stage.Stage;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GUIController {
    private final Stage primaryStage;
    private ServerHandler serverHandler;
    private final Map<String, Player> playerMap;
    private final Map<String, Territory> territoryMap;
    private String playerName;
    private final Text steps = new Text();
    private final VBox vBox = new VBox();
    private final VBox vBox2 = new VBox();

    //orders
    private final ArrayList<AttackActionOrder> attackActionOrders;
    private final ArrayList<MoveActionOrder> moveActionOrders;
    private final ArrayList<UpgradeUnitOrder> upgradeUnitOrders;
    private final ArrayList<UpgradeTechOrder> upgradeTechOrders;

    public GUIController(Stage primaryStage) {
        this.playerMap = new HashMap<>();
        this.territoryMap = new TerritoryMapInit().getMap();
        this.attackActionOrders = new ArrayList<>();
        this.moveActionOrders = new ArrayList<>();
        this.upgradeUnitOrders = new ArrayList<>();
        this.upgradeTechOrders = new ArrayList<>();
        this.primaryStage = primaryStage;
        this.vBox.setPrefSize(150,400);
        this.vBox.setStyle("-fx-background-color:#FEE790");
        this.vBox2.setPrefSize(180,400);
        this.vBox2.setStyle("-fx-background-color:#FEE790");
        this.vBox2.setLayoutX(820);
    }

    public void startGame() {
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

        confirm.setOnAction(actionEvent -> {
            Task<Void> task = new Task<>() {
                @Override
                public Void call() {
                    playerName = textField.getText();
                    try {
                        serverHandler = new ServerHandler(playerName);
                        Platform.runLater(() -> {
                            steps.setText("Please wait for other users to connect...");
                            confirm.setDisable(true);
                            textField.setDisable(true);
                        });
                        nextRound();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            };
            new Thread(task).start();
        });

        root.getChildren().addAll(tf, confirm, textField);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public void nextRound() {
        attackActionOrders.clear();
        moveActionOrders.clear();
        upgradeTechOrders.clear();
        upgradeUnitOrders.clear();

        try {
            //get the game info
            this.serverHandler.getResults(playerMap, territoryMap);

            Platform.runLater(() -> {
                vBox.getChildren().clear();
                vBox2.getChildren().clear();

                renderScreen();

                //judge if the player has lose
                if (!this.playerMap.get(playerName).getTerritories().isEmpty()) {
                    steps.setText("New round, take your actions!");
                } else {
                    steps.setText("You have lost, but you can continue watching the game.");
                    nextRound();
                }
            });
        } catch (IllegalStateException e) {
            Platform.runLater(() -> {
                vBox.getChildren().clear();
                vBox2.getChildren().clear();
                renderScreen();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Game over!");
                alert.setHeaderText("Game over, the winner is");
                alert.setContentText(e.getMessage());

                alert.setOnCloseRequest(dialogEvent -> {
                    primaryStage.close();
                    Platform.exit();
                });
                alert.show();
            });
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

        serverHandler.sendOrders(orders);
        nextRound();
    }

    public void refreshVBox2() {
        vBox2.getChildren().clear();

        playerMap.forEach((k, v) -> {
            Rectangle rectangle = new Rectangle();
            rectangle.setFill(Color.web(v.getColor()));
            rectangle.setWidth(15);
            rectangle.setHeight(15);
            HBox hBox = new HBox();
            hBox.getChildren().addAll(new Text("  " + k + ": "), rectangle);
            this.vBox2.getChildren().add(hBox);
            this.vBox2.getChildren().add(new Text("  Food: " + v.getFoodResources()));
            this.vBox2.getChildren().add(new Text("  Tech Resources: " + v.getTechResources()));
            this.vBox2.getChildren().add(new Text("  Current Tech Level: " + v.getTechLevel()));
            this.vBox2.getChildren().add(new Text());
        });
        this.vBox2.getChildren().addAll(new Text(), new Text());

        territoryMap.forEach((k, v) -> {
            HBox hBox = new HBox();
            hBox.getChildren().addAll(new Text("  " + k + ": "), new Text("  Size: " + v.getSize()), new Text("  Units: " + v.getTerritoryUnits()));
            vBox2.getChildren().add(hBox);
        });
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

        upgrade.setOnAction(actionEvent -> {
            UpgradeTechOrder upgradeTechOrder = new UpgradeTechOrder(playerMap.get(playerName));
            if (!upgradeTechOrder.checkLegal()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Illegal Order!");
                alert.setHeaderText("Illegal Order, please check your input!");

                alert.showAndWait();
            } else {
                upgradeTechOrder.execute();
                upgradeTechOrders.add(upgradeTechOrder);
                vBox.getChildren().add(new Text("Upgrade Technology level"));
                refreshVBox2();
            }
        });

        confirm.setOnAction(actionEvent -> {
            Task<Void> task = new Task<>() {
                @Override
                public Void call() {
                    try {
                        Platform.runLater(() -> {
                            steps.setText("Please wait for other users to finish this round...");
                            confirm.setDisable(true);
                            upgrade.setDisable(true);
                            ServerSetting.TERRITORY_POLY_MAP.values().forEach(e -> e.setOnMouseClicked(null));
                        });
                        finishThisRound();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            };
            new Thread(task).start();
        });

        playerMap.forEach((k, v) -> {
            Rectangle rectangle = new Rectangle();
            rectangle.setFill(Color.web(v.getColor()));
            rectangle.setWidth(15);
            rectangle.setHeight(15);
            HBox hBox = new HBox();
            hBox.getChildren().addAll(new Text("  " + k + ": "), rectangle);
            this.vBox2.getChildren().add(hBox);
            this.vBox2.getChildren().add(new Text("  Food: " + v.getFoodResources()));
            this.vBox2.getChildren().add(new Text("  Tech Resources: " + v.getTechResources()));
            this.vBox2.getChildren().add(new Text("  Current Tech Level: " + v.getTechLevel()));
            this.vBox2.getChildren().add(new Text());
        });
        this.vBox2.getChildren().addAll(new Text(), new Text());

        territoryMap.forEach((k, v) -> {
            GUIController guiController = this;
            Polyline polyline = ServerSetting.TERRITORY_POLY_MAP.get(k);
            polyline.setOnMouseClicked(null);
            polyline.setFill(Color.valueOf(v.getTerritoryOwner().getColor()));
            Text text = new Text(k);
            text.setFont(Font.font("Arial", FontWeight.BOLD, 20));
            text.setX(ServerSetting.NAME_ON_MAP.get(k).getKey());
            text.setY(ServerSetting.NAME_ON_MAP.get(k).getValue());

            if (!this.playerMap.get(playerName).getTerritories().isEmpty()) {
                //mouse event
                if (v.getTerritoryOwner() == playerMap.get(playerName)) {
                    //click mouse action
                    polyline.setOnMouseClicked(mouseEvent -> {
                        ActionMenu g = new ActionMenu(v, guiController);
                        g.show(polyline, Side.RIGHT, -50, 50);
                    });
                }
            }

            root.getChildren().add(polyline);
            root.getChildren().add(text);

            HBox hBox = new HBox();
            hBox.getChildren().addAll(new Text("  " + k + ": "), new Text("  Size: " + v.getSize()), new Text("  Units: " + v.getTerritoryUnits()));
            vBox2.getChildren().add(hBox);
        });
        if (!this.playerMap.get(playerName).getTerritories().isEmpty()) {
            root.getChildren().add(confirm);
            root.getChildren().add(upgrade);
        }
        root.getChildren().add(tf);
        root.getChildren().add(vBox);
        root.getChildren().add(vBox2);

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

        cancel.setOnAction(actionEvent -> menuStage.close());
        confirm.setOnAction(actionEvent -> {
            try {
                ArrayList<Integer> unitList = new ArrayList<>();
                textFields.forEach(textField -> unitList.add(Integer.valueOf(textField.getText().trim())));
                AttackActionOrder attackActionOrder = new AttackActionOrder(playerMap.get(playerName), territory, territoryMap.get(choiceBox.getSelectionModel().getSelectedItem()), unitList);
                if (!attackActionOrder.checkLegal()) {
                    throw new IllegalArgumentException();
                }
                attackActionOrder.moveOut();
                attackActionOrder.getPlayer().consumeFoodResource(attackActionOrder.calculateFoodCost());
                attackActionOrders.add(attackActionOrder);
                vBox.getChildren().add(new Text("Attack: from " + territory.getTerritoryName() + " to " + choiceBox.getSelectionModel().getSelectedItem()));
                menuStage.close();
                refreshVBox2();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Illegal Order!");
                alert.setHeaderText("Illegal Order, please check your input!");
                //alert.setContentText(e.getMessage());

                alert.showAndWait();
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

        cancel.setOnAction(actionEvent -> menuStage.close());
        confirm.setOnAction(actionEvent -> {
            try {
                ArrayList<Integer> unitList = new ArrayList<>();
                textFields.forEach(textField -> unitList.add(Integer.valueOf(textField.getText().trim())));
                MoveActionOrder moveActionOrder = new MoveActionOrder(playerMap.get(playerName), territory, territoryMap.get(choiceBox.getSelectionModel().getSelectedItem()), unitList);
                if (!moveActionOrder.checkLegal()) {
                    throw new IllegalArgumentException();
                }
                moveActionOrder.moveOut();
                moveActionOrder.getPlayer().consumeFoodResource(moveActionOrder.calculateFoodCost());
                moveActionOrders.add(moveActionOrder);
                vBox.getChildren().add(new Text("Move: from " + territory.getTerritoryName() + " to " + choiceBox.getSelectionModel().getSelectedItem()));
                menuStage.close();
                refreshVBox2();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Illegal Order!");
                alert.setHeaderText("Illegal Order, please check your input!");
                //alert.setContentText(e.getMessage());

                alert.showAndWait();
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

        cancel.setOnAction(actionEvent -> menuStage.close());
        confirm.setOnAction(actionEvent -> {
            try {
                ArrayList<Integer> unitList = new ArrayList<>();
                textFields.forEach(textField -> unitList.add(Integer.valueOf(textField.getText().trim())));
                UpgradeUnitOrder upgradeUnitOrder = new UpgradeUnitOrder(territory, unitList);
                if (!upgradeUnitOrder.checkLegal()) {
                    throw new IllegalArgumentException();
                }
                upgradeUnitOrder.execute();
                upgradeUnitOrders.add(upgradeUnitOrder);
                vBox.getChildren().add(new Text("Upgrade: in " + territory.getTerritoryName()));
                menuStage.close();
                refreshVBox2();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Illegal Order!");
                alert.setHeaderText("Illegal Order, please check your input!");

                alert.showAndWait();
            }
        });
    }
}
