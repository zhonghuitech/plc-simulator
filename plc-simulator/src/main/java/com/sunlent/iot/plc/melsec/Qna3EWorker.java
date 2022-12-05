package com.sunlent.iot.plc.melsec;

import com.sunlent.iot.plc.base.BaseWorker;
import com.sunlent.iot.plc.util.LogUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 见文档：
 * <a href="https://blog.csdn.net/wy749929317/article/details/124144389">三菱PLC MC协议</a>
 * @author aborn (jiangguobao)
 * @date 2022/12/05 15:54
 */
public class Qna3EWorker extends BaseWorker {

    @Override
    public void run() {
        handleClient();
    }

    Qna3EWorker(Socket socket, int id) throws IOException {
        LogUtils.log("Worker_" + id + ", running...");
        this.socket = socket;
        this.socketid = id;
    }

    private void handleClient() {
        try {
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();

            byte[] buffer = new byte[1024];
            byte[] s7Pdu = new byte[1024];
            byte[] isoHeader = new byte[7];

            int n;
            while ((n = in.read(buffer)) > 0) {
                boolean status = false;
                int pduLen = n - 7;
                LogUtils.log("socket_" + this.getSocketid() + "， port:" + this.getSocket().getPort() + " read buffer running...");
                LogUtils.log("buffer:" + LogUtils.getBytesString(buffer, n));
                out.write(buffer, 0, n);
            }
            LogUtils.log("handleClient finished.");
        } catch (IOException ioException) {
            LogUtils.log("handleClient io exception." + ioException.getMessage());
        } catch (Exception e) {
            LogUtils.log("handleClient exception." + e.getMessage());
        }
    }
}
