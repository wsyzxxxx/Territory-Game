package edu.duke651.wlt.client;

import edu.duke651.wlt.models.Territory;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public class ActionMenu extends ContextMenu {
    public ActionMenu(Territory territory, GUIController guiController) {
        MenuItem attack = new MenuItem("Attack");
        MenuItem move = new MenuItem("Move");
        MenuItem upgrade = new MenuItem("View/Upgrade Units");

        getItems().add(attack);
        getItems().add(move);
        getItems().add(upgrade);

        attack.setOnAction(actionEvent -> guiController.showAttackMenu(territory));
        move.setOnAction(actionEvent -> guiController.showMoveMenu(territory));
        upgrade.setOnAction(actionEvent -> guiController.showUpgradeMenu(territory));
    }
}

