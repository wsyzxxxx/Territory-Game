package edu.duke651.wlt.client;

import edu.duke651.wlt.models.*;
import javafx.application.Platform;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class GuiUpdator {
    //after commit an order
    public void updateOrderListText(VBox vBox, Order order){
        if(order instanceof MoveActionOrder){
            Text text = new Text("Move" + /*order.getUnits()*/  " from "+ ((MoveActionOrder) order).getSource() + " to " /*+ order.getAim()*/);
            vBox.getChildren().add(text);
        } else if(order instanceof AttackActionOrder) {
            Text text = new Text("Attack" + /*order.getUnits()*/  " from "+ ((AttackActionOrder) order).getSource() + " to " /*+ order.getAim()*/);
            vBox.getChildren().add(text);
        } else if(order instanceof UpdateTechOrder)
            {
            Text text = new Text("Upgrades Tech Level" /*order.getUnits()*/   /*+ order.getAim()*/);
            vBox.getChildren().add(text);
        } else if(order instanceof UpdateUnitOrder){
            Text text = new Text("Upgrades Unit Level" /*order.getUnits()*/   /*+ order.getAim()*/);
            vBox.getChildren().add(text);
        }
    }
    //after a round of game
    //this function is the same as GUIinitializer renderColor... May has some change
    public void updateTerritoryColor(Collection<Player> players){
        for(Player player: players){
            String color = player.getColor();
            Map<String, Territory> territories = player.getTerritories();
            for(String name:territories.keySet()){
                GuiComponents.polyTerritory.get(name).setFill(Color.valueOf(color));
            }
        }
    }

    //after commit an order
    public String updateTerritoryInfo(Territory territory){
        String info = "";
        info += info + "Unites:" + territory.getTerritoryUnits() +"\n";
        info += info + "Size:" + territory.getSize() + "\n";
    }
    public String updateInstructionInfo(Text instruction){

    }
    public void personalInfo(Collection<Player> players, Player player){

    }
}
