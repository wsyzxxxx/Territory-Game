package edu.duke651.wlt.client;

import edu.duke651.wlt.models.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;

/**
 * Handle the interaction with the server
 */
public class ServerHandler {
    //finish flag
    private static final String FINISH = "finish";
    //success flag
    private static final String SUCCESS = "success";
    //error flag
    private static final String ERROR = "error";

    //link info with the server
    private final LinkInfo linkInfo;

    /**
     * Constructor to build the class
     * @param clientName the name of the player
     * @throws IOException when network error happens
     */
    public ServerHandler(String clientName) throws IOException {
        this.linkInfo = new LinkInfo(new Socket(ServerSetting.HOST, ServerSetting.PORT));
        this.linkInfo.setPlayerName("server");
        this.linkInfo.sendMessage(clientName);
    }

    /** The function is left for further usage
     * send the selection of the territory
     * @param player the player object of the current client
     * @throws IOException when network error happens
     */
    public void sendSelection(Player player) throws IOException {
        JSONObject messageJSON = new JSONObject();
        messageJSON.put("status", SUCCESS);
        messageJSON.put("data", player.serialize());
        linkInfo.sendMessage(messageJSON.toString());
    }

    /**
     * send the orders of the current turn
     * @param orders the list of the orders
     * @throws IOException when network error happens
     */
    public void sendOrders(ArrayList<Order> orders) throws IOException{
        JSONObject messageJSON = new JSONObject();
        messageJSON.put("status", SUCCESS);

        JSONArray orderList = new JSONArray();
        orders.forEach(order -> orderList.put(order.serialize()));
        messageJSON.put("data", orderList);
        linkInfo.sendMessage(messageJSON.toString());
    }

    /**
     * get the full results from the server
     * @param playerMap the map to accept the player's information
     * @param territoryMap the map to accept the territory information
     * @throws IOException when network error happens
     * @throws JSONException when JSON parsing error happens
     * @throws IllegalArgumentException when the argument is illegal
     * @throws IllegalStateException when the game end
     */
    public void getResults(Map<String, Player> playerMap, Map<String, Territory> territoryMap) throws IOException, JSONException, IllegalArgumentException, IllegalStateException {
        JSONObject resultObject = new JSONObject(linkInfo.readMessage());
        if (resultObject.getString("status").equals("finish")) {
            throw new IllegalStateException(resultObject.getString("data"));
        } else if (!resultObject.getString("status").equals("success")) {
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
        resultObject.getJSONArray("territoryList").forEach(element ->
            ((JSONObject)element).getJSONArray("neighbours").forEach(neighbour ->
                    territoryMap.get(((JSONObject)element).getString("name")).addTerritoryNeighbors(territoryMap.get(neighbour)))
        );
        resultObject.getJSONArray("playerList").forEach(element -> {
            Player player = Player.deserialize((JSONObject)element, territoryMap);
            playerMap.put(player.getPlayerName(), player);
        });
    }

    /**
     * Get the initial territory map of the current client
     * @param initTerritoryMap the map to accept the initial territory info
     * @throws IOException when network error happens
     */
    public void getInitTerritoryMap(Map<String, Territory> initTerritoryMap) throws IOException {
        JSONObject initObject = new JSONObject(linkInfo.readMessage());
        //response state checks
        if (!initObject.getString("status").equals("success")) {
            throw new IllegalArgumentException("Network error with server");
        }

        //initialize the list
        initObject.getJSONArray("territoryList").forEach(element -> {
            Territory territory = Territory.deserialize((JSONObject)element);
            initTerritoryMap.put(territory.getTerritoryName(), territory);
        });
        //add the relationship
        initObject.getJSONArray("territoryList").forEach(element ->
            ((JSONObject)element).getJSONArray("neighbours").forEach(neighbour ->
                    initTerritoryMap.get(((JSONObject)element).getString("name")).addTerritoryNeighbors(initTerritoryMap.get(neighbour)))
        );
    }

    //close the link to the server
    public void closeLink() throws IOException {
        this.linkInfo.closeLink();
    }
}
