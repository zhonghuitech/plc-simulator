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
            LogUtils.log(this.name + " 等待连接，端口号：" + this.port);
            Socket client = this.socket.accept();
            this.countPlusOne();
            LogUtils.log("新连接接入 : InetAddress = "
                    + this.socket);
            LogUtils.log("超时时间：" + socket.getSoTimeout() + ", current:" + count);
            if (this.getCount() < MAX_CONNECT_COUNT) {
                new Qna3EWorker(client, count).run();
            }
        } catch (IOException e) {
            LogUtils.log("异常" + e.getMessage());
        }
    }

}
