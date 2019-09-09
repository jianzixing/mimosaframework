package org.mimosaframework.monitor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * Created by yangankang on 2017/3/27.
 */
public class MonitorUdpServer {
    public static void main(String[] args) throws IOException {
        DatagramSocket server = new DatagramSocket(8082);
        byte[] buff = new byte[4096];
        DatagramPacket receivePacket = new DatagramPacket(buff, buff.length);
        while (true) {
            server.receive(receivePacket);
            String receiveString = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println(receiveString);

            /*int port = receivePacket.getPort();
            InetAddress addr = receivePacket.getAddress();
            String sendStr = "Hello ! I'm Server";
            byte[] sendBuf;
            sendBuf = sendStr.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendBuf, sendBuf.length, addr, port);
            server.send(sendPacket);*/
        }
        //server.close();
    }
}
