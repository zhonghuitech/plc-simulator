package com.sunlent.iot.plc.siemens;

import com.sunlent.iot.plc.base.BaseServer;
import com.sunlent.iot.plc.util.LogUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author aborn (jiangguobao)
 * @date 2022/12/01 09:49
 */
public class S7Server extends BaseServer {

    private static final int MAX_CONNECT_COUNT = 1024;

    public S7Server() throws IOException {
        /**
         * Default port: 102， TPKT protocal.
         */
        this(102);
    }

    public S7Server(int port) throws IOException {
        this.socket = new ServerSocket(port);
        this.port = port;
        this.name = "Siemens_S7";
    }

    @Override
    public void run() {
        try {
            while (true) {
                LogUtils.log(this.name + " waiting for connect，listen port：" + this.port);
                Socket client = this.socket.accept();
                this.countPlusOne();
                LogUtils.log("new access : InetAddress = "
                        + this.socket);
                LogUtils.log("timeout：" + socket.getSoTimeout() + ", current:" + count);
                if (this.getCount() < MAX_CONNECT_COUNT) {
                    new S7Worker(client, count).start();
                }
                LogUtils.log("--- finished access ---" + this.name);
            }
        } catch (IOException e) {
            LogUtils.log("exception" + e.getMessage());
        }
    }
}
