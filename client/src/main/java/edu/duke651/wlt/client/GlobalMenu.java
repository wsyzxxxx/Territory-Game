package edu.duke651.wlt.client;



import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public class GlobalMenu extends ContextMenu
{
    /**
     * singleton
     */
    private static GlobalMenu INSTANCE = null;

    private GlobalMenu()
    {
        MenuItem attack = new MenuItem("Attack");
        MenuItem move = new MenuItem("Move");
        MenuItem upgrades = new MenuItem("Upgrades");



        getItems().add(attack);
        getItems().add(move);
        getItems().add(upgrades);
    }

    /**
     * get instance
     * @return GlobalMenu
     */
    public static GlobalMenu getInstance()
    {
        if (INSTANCE == null)
        {
            INSTANCE = new GlobalMenu();
        }

        return INSTANCE;
    }
}

