package com.sunlent.iot.plc.melsec;

import com.sunlent.iot.plc.util.LogUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

            Socket socket = new Socket();
            SocketAddress endPoint = new InetSocketAddress("127.0.0.1", 6000);
            socket.connect(endPoint, 10*1000);
            if (socket.isConnected()) {
                System.out.println("connected ok");
            }
            System.out.println(socket);

            // Read D100 address data!
            byte[] READCOMMOND = new byte[] {
                    0x50, 0x00, 0x00, (byte) 0xff, (byte) 0xff, 0x03, 0x00, 0x0c, 0x00, 0x0a, 0x00, 0x01, 0x04, 0x00, 0x00,
                    0x64, 0x00, 0x00,
                    (byte) 0xa8, 0x02, 0x00
            };

            byte[] buffer = new byte[1024];
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();

            // 发送命令
            out.write(READCOMMOND);
            System.out.println("Send finished.");

            int n;
            while ((n = in.read(buffer)) > 0) {
                System.out.println("Read data");
                LogUtils.log(LogUtils.getBytesString(buffer, n));
            }
        } catch (IOException ioException) {
            System.out.println("ioException" + ioException.getMessage());
        }
    }
}
