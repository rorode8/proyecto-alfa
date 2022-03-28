package main.client.game;

import main.interfaces.Server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class SubmitListener implements ActionListener {

    private Game game;


    public SubmitListener(Game game) {
        super();
        this.game = game;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        Registry registry = null; // server's ip address args[0]
        try {
            registry = LocateRegistry.getRegistry(game.textField.getText());
            game.gameServer = (Server) registry.lookup("Game");
            game.info = game.gameServer.enterGame(InetAddress.getLocalHost().toString());
            game.sendtool = new TCPClient(game.info.getTCPPort(),game.textField.getText());
            game.listentool = new ListenUDPThread(game.info.getHostAddress(),game.info.getUDPPort(), game);
            game.listentool.start();
        } catch (RemoteException ex) {
            ex.printStackTrace();
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        } catch (NotBoundException ex) {
            ex.printStackTrace();
        }

        game.loadMenu();

    }

}