package com.sunlent.iot.plc.base;

import com.sunlent.iot.plc.siemens.S7Worker;
import com.sunlent.iot.plc.util.LogUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author aborn (jiangguobao)
 * @date 2022/12/01 09:51
 */
public class BaseServer extends Thread {

    protected BaseServer() throws IOException {}

    protected BaseServer(int port) throws IOException {
        this.socket = new ServerSocket(port);
        this.port = port;
    }

    protected ServerSocket socket;
    protected int port;

    protected String name;

    /**
     * 连接数
     */
    protected int count = 0;

    protected void countPlusOne() {
        this.count ++;
    }

    public int getCount() {
        return count;
    }

    public void run() {
        try {
            while (true) {
                LogUtils.log(this.name + " waiting for connect，listen port：" + this.getPort());
                Socket client = this.socket.accept();
                this.countPlusOne();
                LogUtils.log("new access " + getCount() + ": InetAddress = "
                        + this.socket);
                LogUtils.log("timeout：" + socket.getSoTimeout() + ", current:" + count);
                if (this.getCount() < PLCConstents.MAX_CONNECT_COUNT) {
                    getWorker(client).start();
                }
                LogUtils.log("--- finished access ---" + this.name);
            }
        } catch (IOException e) {
            LogUtils.log("exception" + e.getMessage());
        }
    }

    protected BaseWorker getWorker(Socket client) {
        // 子类实现
        return null;
    }

    public int getPort() {
        return port;
    }
}
