package main;

import main.client.ClientMain;

import java.io.IOException;
import java.rmi.NotBoundException;

public class Main {

    public static void main(String[] args) throws IOException, NotBoundException {
	// write your code here
        ClientMain.eun("1");
        ClientMain.eun("2");
        ClientMain.eun("3");
    }
}
