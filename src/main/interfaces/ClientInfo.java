package main.interfaces;

import main.server.ServerServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class ClientInfo extends Thread{
    public int score;
    public String name;
    public int tcpPort;
    private ServerServer game;
    private boolean gameOn;
    private ServerSocket listenSocket = null;
    private Socket clientSocket = null;
    private DataOutputStream out = null;
    private DataInputStream in = null;

    public ClientInfo(int score, String address, ServerServer game) {
        this.score = score;
        this.name = address;
        this.game=game;
        this.gameOn = true;
        try {
            //automatically assign a free port
            this.listenSocket = new ServerSocket(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //save the port
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
                System.out.println(answer);
                game.hit(answer, this);
            }
        }catch (EOFException e){
            //remote TCP client closed the connection
            System.out.println("remote peer closed connection");
            this.close();
        }catch(SocketException e){
            //the connection was interrupted abruptly
            System.out.println("remote client disconnected");
            this.close();
        }catch (Exception e) {
            System.out.println(e.getMessage());
            this.close();
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

    private void close(){

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
