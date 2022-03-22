package main.client.game;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class SendingTCPThread extends Thread{

    private int port;
    private String host;
    private Socket socket=null;
    private DataOutputStream out;
    private DataInputStream in;
    public boolean online;
    public int num;


    public SendingTCPThread(int port, String host) {
        this.port = port;
        this.host = host;
        this.online = true;
        this.num = -1;
    }

    public void sendNum(int target){
        this.num = target;
    }

    public void run(){
        try{
            socket = new Socket(this.host,this.port);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());
            System.out.println("ready to launch TCP");
            while(online){
                Thread.sleep(100);
                if(this.num>=0){
                    System.out.println("TCP sent: "+this.num);
                    out.writeInt(this.num);
                    this.num = -1;
                }
            }
            out.close();
            in.close();
            socket.close();
        }catch(Exception e){
            e.printStackTrace();
        }



    }

}
