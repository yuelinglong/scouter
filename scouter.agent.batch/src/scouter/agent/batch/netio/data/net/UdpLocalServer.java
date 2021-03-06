package scouter.agent.batch.netio.data.net;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import scouter.agent.batch.Configure;

public class UdpLocalServer extends Thread{
	private static UdpLocalServer instance;

	public static synchronized UdpLocalServer getInstance() {
		if (instance == null) {
			instance = new UdpLocalServer();
			instance.setName("SCOUTER-UDP");
			instance.setDaemon(true);
			instance.start();
		}
		return instance;
	}
	
	public void run(){
		Configure conf = Configure.getInstance();
		byte[] receiveData = new byte[1024];
		DatagramSocket serverSocket = null;
		
		try {
			serverSocket = new DatagramSocket(conf.net_local_udp_port);
	        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
	        
	        String filename = null;
	        while(true){
	        	serverSocket.receive(receivePacket);
	        	filename = new String(receivePacket.getData(), "UTF-8");
	        	TcpAgentReqMgr.getInstance().addJob(filename);
System.out.println("File:" + filename);
	        }
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			if(serverSocket !=null){
				try {serverSocket.close();}catch(Exception ex){}
			}
		}
	}
}
