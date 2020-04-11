package edu.duke651.wlt.client;

import edu.duke651.wlt.models.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;

public class ServerHandler {
    private static final String FINISH = "finish";
    private static final String SUCCESS = "success";
    private static final String ERROR = "error";

    private LinkInfo linkInfo;

    public ServerHandler() throws IOException {
        this.linkInfo = new LinkInfo(new Socket(ServerSetting.HOST, ServerSetting.PORT));
        this.linkInfo.setPlayerName(this.linkInfo.readMessage());
    }

    public void sendOrders(ArrayList<Order> orders) {
        JSONObject messageJSON = new JSONObject();
        messageJSON.put("status", SUCCESS);

        JSONArray orderList = new JSONArray();
        orders.forEach(order -> orderList.put(order.serialize()));
        messageJSON.put("data", orderList);
        linkInfo.sendMessage(messageJSON.toString());
    }

    public void getResults(Map<String, Player> playerMap, Map<String, Territory> territoryMap) throws IOException, JSONException {
        JSONObject resultObject = new JSONObject(linkInfo.readMessage());
        if (!resultObject.getString("status").equals("success") && !resultObject.getString("status").equals("finish")) {
            throw new IllegalArgumentException("Network error with server");
        }

        //clear data
        playerMap.clear();
        territoryMap.clear();

        //reload the data
        resultObject.getJSONArray("territoryList").forEach(element -> {
            Territory territory = Territory.deserialize((JSONObject)element);
            territoryMap.put(territory.getTerritoryName(), territory);
        });
        resultObject.getJSONArray("territoryList").forEach(element -> {
            ((JSONObject)element).getJSONArray("neighbours").forEach(neighbour ->
                    territoryMap.get(((JSONObject)element).getString("name")).addTerritoryNeighbors(territoryMap.get(neighbour)));
        });
        resultObject.getJSONArray("playerList").forEach(element -> {
            Player player = Player.deserialize((JSONObject)element, territoryMap);
            playerMap.put(player.getPlayerName(), player);
        });
    }
}