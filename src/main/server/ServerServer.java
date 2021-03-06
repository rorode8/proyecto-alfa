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
    private static String host = "224.0.0.1";
    private InetAddress group;
    private MulticastSocket socket;
    private ServerClock clock;

    /**
     * n is the number of points to win the round
     * TCP ports get generated per client on demand
     * @param UDPport
     * @param n
     */
    public ServerServer(int UDPport, int n){
        this.clock = null;
        this.sessions = new HashMap<String, ClientInfo>();
        this.UDPport = UDPport;
        this.n = n;
        this.group = null;
        this.socket = null;
        this.openMulticast();

    }


    @Override
    public ServerInfo enterGame(String key) throws RemoteException {
        System.out.println(key);
        if(this.sessions.containsKey(key)){ //active game session
            try {
                System.out.println(java.rmi.server.RemoteServer.getClientHost());
                ClientInfo client = new ClientInfo(this.sessions.get(key).score,key, this);
                client.start();
                this.sessions.replace(key, client);
                return new ServerInfo(this.UDPport,this.sessions.get(key).tcpPort, 1, host);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{ //new game session
            try {
                System.out.println(java.rmi.server.RemoteServer.getClientHost());
                ClientInfo client = new ClientInfo(0,key, this);
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
                this.clock.finish = true;
                this.currentCell = -1;
                this.sendWinner(client.name);
                this.restart();
                System.out.println("to quit press n, to play press any key");
            }else{
                this.clock.t = System.currentTimeMillis();
                this.currentCell = random.nextInt(9);
                this.sendMonster();
            }
            return true;
        }else{
            return false;
        }
    }

    public void start(){
        System.out.println("starting in 4 seconds");
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        clock = new ServerClock(this);
        clock.start();
        this.currentCell = random.nextInt(9);
        this.sendMonster();
    }

    private void openMulticast(){
        group = null; // destination multicast group
        try {
            group = InetAddress.getByName(host);
            this.socket = new MulticastSocket(this.UDPport);
            this.socket.setTimeToLive(10);
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
        String message = "monster:"+this.currentCell;
        byte[] m = message.getBytes();
        try{
            DatagramPacket messageOut =
                    new DatagramPacket(m, m.length, group, this.UDPport);
            socket.send(messageOut);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("sent monster at "+this.currentCell);
    }
    private void sendWinner(String winner){
        String message = "winner:"+winner.split("/")[0];
        byte[] m = message.getBytes();
        try{
            DatagramPacket messageOut =
                    new DatagramPacket(m, m.length, group, this.UDPport);
            socket.send(messageOut);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
