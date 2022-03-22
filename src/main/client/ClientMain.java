package main.client;

import main.client.game.Game;
import main.interfaces.Server;
import main.interfaces.ServerInfo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientMain {

    public static void main(String[] args) throws RemoteException, NotBoundException, UnknownHostException {
        Game g = new Game("game");
    }

    public static void eun(String args) throws RemoteException, NotBoundException, UnknownHostException {
        String serverAddress = "localhost";
        String serviceName = "Game";
        Registry registry = LocateRegistry.getRegistry(serverAddress); // server's ip address args[0]
        Server remoteObject = (Server) registry.lookup(serviceName);
        ServerInfo info = remoteObject.enterGame(InetAddress.getLocalHost().toString()+args);
        System.out.println(info.getStatus());
        System.out.println(info.getTCPPort());
        System.out.println(info.getHostAddress());
        System.out.println(info.getUDPPort());
        int message = 3;
        try {
            Socket socket = new Socket("localhost",info.getTCPPort());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());
            System.out.println("envia: "+message);
            out.writeInt(message);
            //Thread.sleep(3000);
            out.close();
            in.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
