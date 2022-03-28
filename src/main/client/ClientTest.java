package main.client;

import main.client.game.ListenUDPThread;
import main.client.game.TCPClient;
import main.client.tools.TCPoutput;
import main.client.tools.UDPInput;
import main.interfaces.Server;
import main.interfaces.ServerInfo;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

public class ClientTest extends Thread{

    public String host;
    public Server gameServer;
    public String multicast;
    public int UDPport;
    public int TCPport;
    public ServerInfo info;
    public UDPInput udpThread;
    public TCPoutput output;
    public ArrayList<Long> tiempos;
    public long tiempoRegistro = 0;

    public ClientTest(String hostaddress, String playername){
        tiempos = new ArrayList<>();
        this.host = hostaddress;

        // TODO Auto-generated method stub
        long t0 = System.currentTimeMillis();
        Registry registry = null; // server's ip address args[0]
        try {
            registry = LocateRegistry.getRegistry(hostaddress);
            this.gameServer = (Server) registry.lookup("Game");
            this.info = this.gameServer.enterGame(playername);
            this.tiempoRegistro = System.currentTimeMillis() - t0;
            this.output = new TCPoutput(this.info.getTCPPort(),hostaddress);
            this.udpThread = new UDPInput(this.info.getHostAddress(), this.info.getUDPPort(), this);
            this.udpThread.start();
        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }

    public void sendInput(int number){
        long t0 = System.currentTimeMillis();
        this.output.sendNum(number);
        tiempos.add(System.currentTimeMillis() - t0);
    }

    private double getMean(){
        double sum = 0;
        for(long tiempo: this.tiempos){
            sum+=tiempo;
        }
        return sum/tiempos.size();
    }
    private double getStd(double mean){
        double sum = 0;
        for(long tiempo: this.tiempos){
            sum+=Math.pow((tiempo-mean),2);
        }
        sum = sum/tiempos.size();
        return Math.pow(sum,0.5);
    }

    public double[] getStats(){
        double mean = this.getMean();
        double std  = this.getStd(mean);
        double registerTime = this.tiempoRegistro;
        double iters = tiempos.size();
        double[] ans = {mean, std, iters, registerTime};
        return ans;
    }

}
