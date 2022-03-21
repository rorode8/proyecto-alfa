package main;

import main.client.ClientMain;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Main {

    public static void main(String[] args) throws UnknownHostException, NotBoundException, RemoteException {
	// write your code here
        ClientMain.eun("1");
        ClientMain.eun("2");
        ClientMain.eun("3");

    }
}
