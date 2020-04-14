package edu.duke651.wlt.models;

import java.io.*;
import java.net.Socket;

/**
 * @program: wlt-risc
 * @description: This is LinkInfo class which contains all the information for one player's connection.
 * @author: Will
 * @create: 2020-04-09 11:55
 **/
public class LinkInfo {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String playerName;

    /**
    * @Description: This function LinkInfo is to construct linkinfo with socket and i/o interfaces.
    * @Param: [socket]
    * @return:
    * @Author: Will
    * @Date: 2020/4/13
    */
    public LinkInfo(Socket socket) throws IOException {
        this.socket = socket;
        this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    /**
     * @Description: This function is to read the message through network
     * @Param: [socket]
     * @return:
     * @Author: Will
     * @Date: 2020/4/13
     */
    public String readMessage() throws IOException {
        String res = this.bufferedReader.readLine();
        //System.out.println("receive message! " + res);
        return res;
    }

    /**
     * @Description: This function is to send the message through network
     * @Param: [socket]
     * @return:
     * @Author: Will
     * @Date: 2020/4/13
     */
    public void sendMessage(String message) throws IOException {
        this.bufferedWriter.write(message);
        this.bufferedWriter.newLine();
        this.bufferedWriter.flush();
        //System.out.println("Send message! " + message);
    }

    public boolean isAlive() {
        return !this.socket.isClosed();
    }

    /**
    * @Description: This function closeLink is to close link.
    * @Param: []
    * @return: void
    * @Author: Will
    * @Date: 2020/4/13
    */
    public void closeLink() throws IOException {
        this.bufferedWriter.close();
        this.bufferedReader.close();
        this.socket.close();
    }
}
