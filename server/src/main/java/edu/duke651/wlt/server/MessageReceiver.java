package edu.duke651.wlt.server;

import edu.duke651.wlt.models.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class MessageReceiver {
    public ArrayList<Order> receiveNewTurn(LinkInfo linkInfo, Map<String, Player> playerMap, Map<String, Territory> territoryMap) throws IOException, JSONException, IllegalArgumentException {
        JSONObject turnObject = new JSONObject(linkInfo.readMessage());
        if (!turnObject.getString("status").equals("success")) {
            throw new IllegalArgumentException("Network error with player " + linkInfo.getPlayerName());
        }

        ArrayList<Order> orderList = new ArrayList<>();
        turnObject.getJSONArray("data").forEach(element -> {
            if (((JSONObject)element).getString("type").equals("move")) {
                orderList.add(MoveOrder.deserialize((JSONObject)element, playerMap, territoryMap));
            } else if (((JSONObject)element).getString("type").equals("attack")) {
                orderList.add(AttackOrder.deserialize((JSONObject)element, playerMap, territoryMap));
            } else {
                throw new IllegalArgumentException("Wrong order type with player " + linkInfo.getPlayerName());
            }
        });

        return orderList;
    }


}
