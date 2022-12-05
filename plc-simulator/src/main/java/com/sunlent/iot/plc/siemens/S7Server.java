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
         * 默认为 102 端口， TPKT协议
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
            LogUtils.log(this.name + " 等待连接，端口号：" + this.port);
            Socket client = this.socket.accept();
            this.countPlusOne();
            LogUtils.log("新连接接入 : InetAddress = "
                    + this.socket);
            LogUtils.log("超时时间：" + socket.getSoTimeout() + ", current:" + count);
            if (this.getCount() < MAX_CONNECT_COUNT) {
                new S7Worker(client, count).run();
            }
        } catch (IOException e) {
            LogUtils.log("异常" + e.getMessage());
        }
    }
}
