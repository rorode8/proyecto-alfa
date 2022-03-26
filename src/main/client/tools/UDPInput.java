package main.client.tools;

import main.client.ClientTest;
import main.client.game.Game;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class UDPInput extends Thread{

    private int port;
    private ClientTest client;
    private String host;
    public boolean online;
    private MulticastSocket socket = null;

    public UDPInput(String host, int port, ClientTest client) {
        this.port = port;
        this.client = client;
        this.host = host;
        this.online = true;
    }

    public void run(){

        try {
            InetAddress group = InetAddress.getByName(host);
            socket = new MulticastSocket(port);
            socket.joinGroup(group);
            while(online){
                byte[] buffer = new byte[1000];
                DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
                socket.receive(messageIn);

                String message = (new String(messageIn.getData())).trim();
                String[] data = message.split(":");

                if (data[0].equalsIgnoreCase("monster")){
                    this.client.sendInput(Integer.parseInt(data[1]));
                }else if (data[0].equalsIgnoreCase("winner")){
                    this.online = false;
                }

            }
            socket.leaveGroup(group);
            socket.close();
        } catch (Exception e) {
            System.out.println("Error while listening UDP: " + e.getMessage());
        }

    }
}
