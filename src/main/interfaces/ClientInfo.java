package main.interfaces;

import main.server.ServerServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientInfo extends Thread{
    public int score;
    public String address;
    public int tcpPort;
    private ServerServer game;
    private boolean gameOn;
    private ServerSocket listenSocket = null;
    private Socket clientSocket = null;
    private DataOutputStream out = null;
    private DataInputStream in = null;

    public ClientInfo(int score, String address, ServerServer game) {
        this.score = score;
        this.address = address;
        this.game=game;
        this.gameOn = true;
        try {
            this.listenSocket = new ServerSocket(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.tcpPort = this.listenSocket.getLocalPort();



    }



    public void run(){

        try {

            clientSocket = this.listenSocket.accept();
            out = new DataOutputStream(clientSocket.getOutputStream());
            in = new DataInputStream(clientSocket.getInputStream());
            int answer;
            while(gameOn){
                answer = in.readInt();
                game.hit(answer, this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void turnOff(){

        this.gameOn = false;

        try {
            out.close();
            in.close();
            clientSocket.close();
            listenSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
