package edu.duke651.wlt.models;

import java.io.*;
import java.net.Socket;

public class LinkInfo {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String playerName;

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

    public String readMessage() throws IOException {
        String res = this.bufferedReader.readLine();
        System.out.println("receive message! " + res);
        return res;
    }

    public void sendMessage(String message) throws IOException {
        System.out.println("before sending " + message);
        this.bufferedWriter.write(message);
        this.bufferedWriter.newLine();
        this.bufferedWriter.flush();
        System.out.println("Send message! " + message);
    }

    public boolean isAlive() {
        return !this.socket.isClosed();
    }

    public void closeLink() throws IOException {
        this.bufferedWriter.close();
        this.bufferedReader.close();
        this.socket.close();
    }
}
