package com.sunlent.iot.plc.melsec;

import com.sunlent.iot.plc.base.BaseServer;
import com.sunlent.iot.plc.util.LogUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author aborn (jiangguobao)
 * @date 2022/12/05 15:49
 */
public class Qna3EServer extends BaseServer {

    private static final int MAX_CONNECT_COUNT = 1024;

    public Qna3EServer() throws IOException {
        // 默认端口号
        this(6000);
    }

    public Qna3EServer(int port) throws IOException {
        this.socket = new ServerSocket(port);
        this.port = port;
        this.name = "Melsec_Qna3E";
    }

    @Override
    public void run() {
        try {
            while (true) {
                LogUtils.log(this.name + " waiting for connect，listen port：" + this.port);
                Socket client = this.socket.accept();
                this.countPlusOne();
                LogUtils.log(this.name + "==> new access : InetAddress = "
                        + this.socket + "， client:" + client);
                LogUtils.log("timeout：" + socket.getSoTimeout() + ", current:" + count);
                if (this.getCount() < MAX_CONNECT_COUNT) {
                    new Qna3EWorker(client, count).start();
                }
                LogUtils.log("--- finished access ---");
            }
        } catch (IOException e) {
            LogUtils.log("exception" + e.getMessage());
        }
    }

}
