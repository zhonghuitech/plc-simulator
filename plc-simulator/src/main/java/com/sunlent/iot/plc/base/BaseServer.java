package com.sunlent.iot.plc.base;

import java.net.ServerSocket;

/**
 * @author aborn (jiangguobao)
 * @date 2022/12/01 09:51
 */
public class BaseServer extends Thread {
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
}
