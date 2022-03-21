package main.server;

import main.interfaces.Server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class ServerMain {

    public static void main(String[] args) throws RemoteException {
        Scanner scan = new Scanner(System.in);
        System.out.println("start the game by typing any character");
        //scan.nextLine();
        ServerServer game = new ServerServer(49159,9);
        game.start();
        System.setProperty("java.rmi.server.hostname", "localhost");
        LocateRegistry.createRegistry(1099);
        String serviceName = "Game";
        Server stub = (Server) UnicastRemoteObject.exportObject(game, 0);
        Registry registry = LocateRegistry.getRegistry();
        registry.rebind(serviceName, stub);
        System.out.println("server online... ");
        String input = "";
        while (input!="quit"){
            scan.nextLine();
        }
    }

}
