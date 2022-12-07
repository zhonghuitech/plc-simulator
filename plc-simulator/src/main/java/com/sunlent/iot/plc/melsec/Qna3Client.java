package com.sunlent.iot.plc.melsec;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * @author aborn (jiangguobao)
 * @date 2022/12/07 16:53
 */
public class Qna3Client {
    public static void main(String[] args) {

        try {
            Socket clientSocket = new Socket();
            SocketAddress endPoint = new InetSocketAddress("127.0.0.1", 6000);
            clientSocket.connect(endPoint, 10*1000);
            System.out.println(clientSocket);

            if (clientSocket.isConnected()) {
                System.out.println("connected ok");
            }
        } catch (IOException ioException) {
            System.out.println("ioException" + ioException.getMessage());
        }
    }
}
