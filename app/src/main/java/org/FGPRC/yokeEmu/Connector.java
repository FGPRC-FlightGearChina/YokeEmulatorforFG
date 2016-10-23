package org.FGPRC.yokeEmu;
import java.io.*;
import java.net.*;

public class Connector{
	private String mIP;
	private int mport;
	private DatagramSocket mds;
	private InetAddress maddr;

		
	public Connector(String IP,int port) throws SocketException, UnknownHostException{
		mIP=IP;
		mport=port;
		mds=new DatagramSocket(23333);
		maddr=InetAddress.getByName(IP);
	}
	public void sendMessage(String data) throws IOException{
		sendMessage(data.getBytes());
	}
	public void sendMessage(byte[]data) throws IOException{
		
			DatagramPacket dp = new DatagramPacket(data, data.length,maddr,mport);  
			mds.send(dp);
			mds.close();
	}
}
