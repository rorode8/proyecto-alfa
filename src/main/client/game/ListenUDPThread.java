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

            System.out.println("Waiting for messages");
            while(online){
                byte[] buffer = new byte[1000];
                DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
                System.out.println("waiting for UDP");
                socket.receive(messageIn);

                String message = (new String(messageIn.getData())).trim();
                String[] data = message.split(":");

                System.out.println("message: "+message);

                if (data[0].equalsIgnoreCase("monster")){
                    try {
                        this.game.setGoomba(Integer.parseInt(data[1]));
                    } catch (Exception e) {
                        System.out.println("error while placing goomba, will try again");
                    }
                }else if (data[0].equalsIgnoreCase("winner")){
                    this.game.loadMenu(data[1]);
                }

            }
            socket.leaveGroup(group);
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error while listening UDP: " + e.getMessage());
        }

    }
}
