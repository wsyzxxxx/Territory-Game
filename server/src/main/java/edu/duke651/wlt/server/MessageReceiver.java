package edu.duke651.wlt.server;

import edu.duke651.wlt.models.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * @program: wlt-risc
 * @description: This is the message receiver class responsible for receiving info from connected clients using their Linkinfo.
 * @author: Will
 * @create: 2020-04-11 11:20
 **/
public class MessageReceiver {
    /**
    * @Description: This function receiveNewTurn is to receive new messages, especially new orders, every turn of game.
    * @Param: [linkInfo, playerMap, territoryMap, requiredType]
    * @return: java.util.ArrayList<edu.duke651.wlt.models.ActionOrder>
    * @Author: Will
    * @Date: 2020/4/13
    */
    public void receiveNewTurn(LinkInfo linkInfo, Map<String, Player> playerMap, Map<String, Territory> territoryMap,
                                                 ArrayList<ActionOrder> attackOrders, ArrayList<ActionOrder> moveOrders, ArrayList<UpgradeUnitOrder> upgradeUnitOrders, ArrayList<UpgradeTechOrder> upgradeTechOrders) throws IOException, JSONException, IllegalArgumentException {
        JSONObject turnObject = new JSONObject(linkInfo.readMessage());
        if (!turnObject.getString("status").equals("success")) {
            throw new IllegalArgumentException("Network error with player " + linkInfo.getPlayerName());
        }

        turnObject.getJSONArray("data").forEach(element -> {
            if (!((JSONObject)element).getString("player").equals(linkInfo.getPlayerName())) {
                throw new IllegalArgumentException("Wrong order type with player " + linkInfo.getPlayerName());
            }

            if (((JSONObject)element).getString("type").equals("move")) {
                moveOrders.add(MoveActionOrder.deserialize((JSONObject)element, playerMap, territoryMap));
            } else if (((JSONObject)element).getString("type").equals("attack")) {
                attackOrders.add(AttackActionOrder.deserialize((JSONObject)element, playerMap, territoryMap));
            } else if (((JSONObject)element).getString("type").equals("upgradeTech")) {
                upgradeTechOrders.add(UpgradeTechOrder.deserialize((JSONObject)element, playerMap));
            } else if (((JSONObject)element).getString("type").equals("upgradeUnits")) {
                upgradeUnitOrders.add(UpgradeUnitOrder.deserialize((JSONObject)element, playerMap, territoryMap));
            } else {
                throw new IllegalArgumentException("Wrong order type with player " + linkInfo.getPlayerName());
            }
        });
    }

    /**
    * @Description: This function receiveSelection is to facilitate every client's selecting their own group of territories. For version 1, this is not required and thus not fulfilled entirely.
    * @Param: [linkInfo, territoryMap]
    * @return: edu.duke651.wlt.models.Player
    * @Author: Will
    * @Date: 2020/4/13
    */
    public Player receiveSelection(LinkInfo linkInfo, Map<String, Territory> territoryMap) throws IOException {
        JSONObject selectionObject = new JSONObject(linkInfo.readMessage());
        if (!selectionObject.getString("status").equals("success") ||
             selectionObject.getJSONObject("data").getJSONArray("territories").length() != territoryMap.keySet().size()) {
            throw new IllegalArgumentException("Network error with player " + linkInfo.getPlayerName());
        }

        Player player = new Player(selectionObject.getJSONObject("data").getString("playerName"));
        linkInfo.setPlayerName(player.getPlayerName());
        selectionObject.getJSONObject("data").getJSONArray("territories").forEach(element -> {
            if (!territoryMap.containsKey(((JSONObject)element).getString("name"))) {
                throw new IllegalArgumentException("Wrong selection with player " + player.getPlayerName());
            }
            //territoryMap.get(((JSONObject)element).getString("name")).setTerritoryUnits(((JSONObject)element).getInt("units"));
            player.addTerritory(territoryMap.get(((JSONObject)element).getString("name")));
            territoryMap.remove(((JSONObject)element).getString("name"));
        });

        return player;
    }
}
