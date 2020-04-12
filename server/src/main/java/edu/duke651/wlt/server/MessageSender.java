package edu.duke651.wlt.server;

import edu.duke651.wlt.models.LinkInfo;
import edu.duke651.wlt.models.Player;
import edu.duke651.wlt.models.Territory;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

public class MessageSender {
    private static final String FINISH = "finish";
    private static final String SUCCESS = "success";
    private static final String ERROR = "error";

    public void sendFinishMessage(LinkInfo linkInfo, String winnerName) throws IOException {
        JSONObject messageJSON = new JSONObject();
        messageJSON.put("status", FINISH);
        messageJSON.put("data", winnerName);
        linkInfo.sendMessage(messageJSON.toString());
    }

    public void sendErrorMessage(LinkInfo linkInfo, String errorMessage) throws IOException {
        JSONObject messageJSON = new JSONObject();
        messageJSON.put("status", ERROR);
        messageJSON.put("data", errorMessage);
        linkInfo.sendMessage(messageJSON.toString());
    }

    public void sendMessage(LinkInfo linkInfo, JSONObject dataObject) throws IOException {
        JSONObject messageJSON = new JSONObject();
        messageJSON.put("status", SUCCESS);
        messageJSON.put("data", dataObject);
        linkInfo.sendMessage(messageJSON.toString());
    }

    public void sendResults(Map<Player, LinkInfo> playerLinkInfoMap, Map<String, Territory> territoryMap) throws IOException {
        JSONObject messageJSON = new JSONObject();
        messageJSON.put("status", SUCCESS);

        //territoryList
        JSONArray territoryList = new JSONArray();
        territoryMap.forEach((name, territory) -> territoryList.put(territory.serialize()));
        messageJSON.put("territoryList", territoryList);

        //playerList
        JSONArray playerWithTerritoryList = new JSONArray();
        playerLinkInfoMap.forEach((player, linkInfo) -> playerWithTerritoryList.put(player.serialize()));
        messageJSON.put("playerList", playerWithTerritoryList);

        //send the results
        playerLinkInfoMap.forEach((player, linkInfo) -> {
            if (linkInfo.isAlive()) {
                try {
                    linkInfo.sendMessage(messageJSON.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                playerLinkInfoMap.remove(player, linkInfo);
            }
        });
    }

    public void sendTerritoryList(LinkInfo linkInfo, Collection<Territory> territories) throws IOException {
        JSONObject messageJSON = new JSONObject();
        messageJSON.put("status", SUCCESS);

        //territoryList
        JSONArray territoryList = new JSONArray();
        territories.forEach(territory -> territoryList.put(territory.serialize()));
        messageJSON.put("territoryList", territoryList);

        //send the results
        linkInfo.sendMessage(messageJSON.toString());
    }
}
