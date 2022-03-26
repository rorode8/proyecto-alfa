package main.client.tools;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class TCPoutput {

    private int port;
    private String host;
    private Socket socket=null;
    private DataOutputStream out;
    private DataInputStream in;

    public TCPoutput(int port, String host) {
        this.port = port;
        this.host = host;
        try {
            socket = new Socket(this.host,this.port);
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
        } catch (Exception e) {
            System.out.println("error while initializing TCP");
            e.printStackTrace();
        }
        System.out.println("ready to launch TCP");
    }

    public void sendNum(int target){
        try {
            out.writeInt(target);
        } catch (Exception e) {
            System.out.println("error while sending TCP message");
            e.printStackTrace();
        }
    }

    public void end(){
        try {
            out.close();
            in.close();
            socket.close();
        } catch (Exception e) {
            System.out.println("error while closing TCP");
            e.printStackTrace();
        }
    }


}
