package edu.duke651.wlt.client;

import edu.duke651.wlt.models.Territory;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public class ActionMenu extends ContextMenu {
    private Territory territory;
    private GUIController guiController;

    public ActionMenu(Territory territory, GUIController guiController) {
        MenuItem attack = new MenuItem("Attack");
        MenuItem move = new MenuItem("Move");
        MenuItem upgrade = new MenuItem("View/Upgrade Units");

        getItems().add(attack);
        getItems().add(move);
        getItems().add(upgrade);

        attack.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                guiController.showAttackMenu(territory);
            }
        });
        move.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                guiController.showMoveMenu(territory);
            }
        });
        upgrade.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                guiController.showUpgradeMenu(territory);
            }
        });
    }
}

