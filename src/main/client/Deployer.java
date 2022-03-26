package main.client;

import main.interfaces.Server;
import main.server.ServerServer;

import java.net.InetAddress;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Deployer {
    public static void main(String[] args) throws RemoteException {
        int numClientes = 10;
        int numRounds = 10;

        String address = "localhost";
        System.out.println("start the game by typing any character");
        ServerServer game = new ServerServer(25560,numRounds);
        System.setProperty("java.rmi.server.hostname", address);
        LocateRegistry.createRegistry(1099);
        String serviceName = "Game";
        Server stub = (Server) UnicastRemoteObject.exportObject(game, 0);
        Registry registry = LocateRegistry.getRegistry();
        registry.rebind(serviceName, stub);
        System.out.println("server online... ");

        //clients here
        ClientTest[] clientes = new ClientTest[numClientes];
        for(int i=0; i<numClientes;i++){
            clientes[i] = new ClientTest(address, "player"+i);
        }
        game.start();

        try {
            for(int i=0; i<numClientes;i++){
                clientes[i].udpThread.join();
            }
            game.end();
            Thread.sleep(2000);


        } catch (Exception e) {
            e.printStackTrace();
        }
        for(int i=0; i<numClientes;i++){
            double[] stats = clientes[i].getStats();
            System.out.println(stats[0]+","+stats[1]+","+stats[2]+","+stats[3]);
        }
        try {
            registry.unbind(serviceName);

            // Unexport; this will also remove us from the RMI runtime
            UnicastRemoteObject.unexportObject(game, true);

            System.out.println("Game exiting.");
        } catch (NotBoundException e) {
            e.printStackTrace();
        }

    }
}
