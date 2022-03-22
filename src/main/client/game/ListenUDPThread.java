package main.client.game;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class ListenUDPThread extends Thread{

    private int port;
    private Game game;
    private String host;
    public boolean online;
    private MulticastSocket socket = null;

    public ListenUDPThread(String host, int port, Game game) {
        this.port = port;
        this.game = game;
        this.host = host;
        this.online = true;
    }

    public void run(){

        try {
            InetAddress group = InetAddress.getByName(host); // destination multicast group
            socket = new MulticastSocket(port);
            socket.joinGroup(group);
            byte[] buffer = new byte[1];
            System.out.println("Waiting for messages");
            while(online){
                DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
                System.out.println("waiting for UDP");
                socket.receive(messageIn);
                System.out.println("message: "+messageIn.getData().toString());
                this.game.setGoomba((int)messageIn.getData()[0]);
            }
            socket.leaveGroup(group);
            socket.close();
        } catch (Exception e) {
            System.out.println("Socket: " + e.getMessage());
        }

    }
}
