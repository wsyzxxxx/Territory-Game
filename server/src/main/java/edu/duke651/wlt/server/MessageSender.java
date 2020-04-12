package edu.duke651.wlt.server;

import edu.duke651.wlt.models.LinkInfo;
import edu.duke651.wlt.models.Player;
import edu.duke651.wlt.models.Territory;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * @program: wlt-risc
 * @description: This is the message sender class responsible for sending game info to client using their Linkinfo. 
 * @author: Will
 * @create: 2020-04-11 11:20
 **/
public class MessageSender {
    private static final String FINISH = "finish";
    private static final String SUCCESS = "success";
    private static final String ERROR = "error";

    /**
    * @Description: This function sendFinishMessage is to send finishing message to every client when the game is over.
    * @Param: [linkInfo, winnerName]
    * @return: void
    * @Author: Will
    * @Date: 2020/4/13
    */
    public void sendFinishMessage(LinkInfo linkInfo, String winnerName) throws IOException {
        JSONObject messageJSON = new JSONObject();
        messageJSON.put("status", FINISH);
        messageJSON.put("data", winnerName);
        linkInfo.sendMessage(messageJSON.toString());
    }

    /**
    * @Description: This function sendErrorMessage is to send error message in case there is an error happens which requires client to handle it.
    * @Param: [linkInfo, errorMessage]
    * @return: void
    * @Author: Will
    * @Date: 2020/4/13
    */
    public void sendErrorMessage(LinkInfo linkInfo, String errorMessage) throws IOException {
        JSONObject messageJSON = new JSONObject();
        messageJSON.put("status", ERROR);
        messageJSON.put("data", errorMessage);
        linkInfo.sendMessage(messageJSON.toString());
    }

    /**
    * @Description: This function sendMessage is to send general message using JSONObject.
    * @Param: [linkInfo, dataObject]
    * @return: void
    * @Author: Will
    * @Date: 2020/4/13
    */
    public void sendMessage(LinkInfo linkInfo, JSONObject dataObject) throws IOException {
        JSONObject messageJSON = new JSONObject();
        messageJSON.put("status", SUCCESS);
        messageJSON.put("data", dataObject);
        linkInfo.sendMessage(messageJSON.toString());
    }

    /**
    * @Description: This function sendResults is the main method which sends every turn's game info to all client.
    * @Param: [playerLinkInfoMap, territoryMap]
    * @return: void
    * @Author: Will
    * @Date: 2020/4/13
    */
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

        ArrayList<Player> brokenList = new ArrayList<>();
        //send the results
        playerLinkInfoMap.forEach((player, linkInfo) -> {
            try {
                linkInfo.sendMessage(messageJSON.toString());
            } catch (Exception e) {
                brokenList.add(player);
            }
        });
        brokenList.forEach(player -> playerLinkInfoMap.remove(player));
    }

    /**
    * @Description: This function sendTerritoryList is to send to one player a list of territories.
    * @Param: [linkInfo, territories]
    * @return: void
    * @Author: Will
    * @Date: 2020/4/13
    */
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
