package edu.duke651.wlt.models;

import java.io.*;
import java.net.Socket;

public class LinkInfo {
    private Socket socket;
    private BufferedReader bufferedReader;
    private PrintStream printStream;
    private String playerName;

    public LinkInfo(Socket socket) throws IOException {
        this.socket = socket;
        this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.printStream = new PrintStream(new BufferedOutputStream(socket.getOutputStream()));
        this.playerName = this.bufferedReader.readLine();
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public String readMessage() throws IOException {
        return this.bufferedReader.readLine();
    }

    public void sendMessage(String message) {
        this.printStream.println(message);
    }

    public void closeLink() throws IOException {
        this.printStream.close();
        this.bufferedReader.close();
        this.socket.close();
    }
}
