package main.client;

import main.interfaces.ClientInfo;
import main.interfaces.Server;
import main.interfaces.ServerInfo;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientMain {

    public static void main(String[] args) throws RemoteException, NotBoundException, UnknownHostException {
        String serverAddress = "localhost";
        String serviceName = "Game";
        Registry registry = LocateRegistry.getRegistry(serverAddress); // server's ip address args[0]
        Server remoteObject = (Server) registry.lookup(serviceName);
        ServerInfo info = remoteObject.enterGame(InetAddress.getLocalHost().toString()+args[0]);
        System.out.println(info.getStatus());
        System.out.println(info.getTCPPort());
        System.out.println(info.getHostAddress());
        System.out.println(info.getUDPPort());
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
    }

}
