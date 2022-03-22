package main.server;

import main.interfaces.ClientInfo;
import main.interfaces.Server;
import main.interfaces.ServerInfo;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.util.HashMap;
import java.util.Random;

public class ServerServer implements Server {

    private HashMap<String, ClientInfo> sessions;
    private int UDPport;
    private int currentCell = -1;
    private Random random = new Random();
    private int n;
    private static String host = "228.5.6.7";
    private InetAddress group;
    private MulticastSocket socket;

    public ServerServer(int UDPport, int n){
        this.sessions = new HashMap<String, ClientInfo>();
        this.UDPport = UDPport;
        this.n = n;
        this.group = null;
        this.socket = null;

    }


    @Override
    public ServerInfo enterGame(String key) throws RemoteException {

        if(this.sessions.containsKey(key)){ //active game session
            this.sessions.get(key).start(); //restart the thread
            return new ServerInfo(this.UDPport,this.sessions.get(key).tcpPort, 1, host);
        }else{ //new game session
            try {
                ClientInfo client = new ClientInfo(0,java.rmi.server.RemoteServer.getClientHost(), this);
                client.start();
                this.sessions.put(key, client);
                return new ServerInfo(this.UDPport,client.tcpPort, 0, host);
            } catch (ServerNotActiveException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    public void end(){
        sessions.values().forEach(client -> {
            client.stop();
            client.turnOff();
        });
        this.sessions = new HashMap<String, ClientInfo>();
        this.closeMulticast();
    }

    public void restart(){
        sessions.values().forEach(client -> {
            client.score = 0;
        });
    }

    public synchronized boolean hit(int cell, ClientInfo client){
        if(cell == currentCell){
            client.score+=1;
            if(client.score >= this.n){
                this.currentCell = -1;
            }else{
                this.currentCell = random.nextInt(9);
                this.sendMonster();
            }
            return true;
        }else{
            return false;
        }
    }

    public void start(){
        this.openMulticast();
        System.out.println("starting in 4 seconds");
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.currentCell = random.nextInt(9);
        this.sendMonster();
    }

    private void openMulticast(){
        group = null; // destination multicast group
        try {
            group = InetAddress.getByName(host);
            this.socket = new MulticastSocket(this.UDPport);
            this.socket.joinGroup(group);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void closeMulticast(){

        socket.close();
    }

    public void sendMonster(){
        Integer monster = this.currentCell;
        byte[] m = {monster.byteValue()};
        try{
            DatagramPacket messageOut =
                    new DatagramPacket(m, m.length, group, this.UDPport);
            socket.send(messageOut);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("sent monster at "+this.currentCell);
    }
}
