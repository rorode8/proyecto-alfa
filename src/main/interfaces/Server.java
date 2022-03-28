package main.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Server extends Remote {

    public ServerInfo enterGame(String key) throws RemoteException; //should return status code 0 for waiting, 1 for started game


}
