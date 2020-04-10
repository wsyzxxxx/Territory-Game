package edu.duke651.wlt.server;

import edu.duke651.wlt.models.LinkInfo;
import org.json.JSONObject;

public class MessageSender {
    private static final String FINISH = "finish";
    private static final String SUCCESS = "success";
    private static final String ERROR = "error";

    public void sendFinishMessage(LinkInfo linkInfo, JSONObject dataObject) {
        JSONObject messageJSON = new JSONObject();
        messageJSON.put("status", FINISH);
        messageJSON.put("data", dataObject);
        linkInfo.sendMessage(messageJSON.toString());
    }

    public void sendErrorMessage(LinkInfo linkInfo, String errorMessage) {
        JSONObject messageJSON = new JSONObject();
        messageJSON.put("status", ERROR);
        messageJSON.put("data", errorMessage);
        linkInfo.sendMessage(messageJSON.toString());
    }

    public void sendMessage(LinkInfo linkInfo, JSONObject dataObject) {
        JSONObject messageJSON = new JSONObject();
        messageJSON.put("status", SUCCESS);
        messageJSON.put("data", dataObject);
        linkInfo.sendMessage(messageJSON.toString());
    }
}
