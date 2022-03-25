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

        ServerServer game = new ServerServer(25560,9);
        System.setProperty("java.rmi.server.hostname", "192.168.100.55");
        LocateRegistry.createRegistry(1099);
        String serviceName = "Game";
        Server stub = (Server) UnicastRemoteObject.exportObject(game, 0);
        Registry registry = LocateRegistry.getRegistry();
        registry.rebind(serviceName, stub);
        System.out.println("server online... ");
        scan.nextLine();
        game.start();
        String input = "y";
        while (true){
            input = scan.next();
            if(input.equalsIgnoreCase("n") ){
                System.out.println("goodbye");
                break;
            }
            game.start();
        }
    }

}
