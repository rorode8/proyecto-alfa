package main.interfaces;

import java.io.Serializable;

public class ServerInfo implements Serializable {
    private int UDPPort, TCPPort, status;
    private String hostAddress;

    public ServerInfo(int UDPPort, int TCPPort, int status, String host) {
        this.UDPPort = UDPPort;
        this.TCPPort = TCPPort;
        this.status = status;
        this.hostAddress = host;
    }

    public int getUDPPort() {
        return UDPPort;
    }

    public int getTCPPort() {
        return TCPPort;
    }

    public int getStatus() {
        return status;
    }

    public String getHostAddress(){
        return hostAddress;
    }
}
