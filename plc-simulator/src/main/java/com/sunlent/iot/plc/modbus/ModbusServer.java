package com.sunlent.iot.plc.modbus;

import com.sunlent.iot.plc.base.BaseServer;
import com.sunlent.iot.plc.base.BaseWorker;

import java.io.IOException;
import java.net.Socket;

/**
 * https://juejin.cn/post/6988803436309381127
 *
 * @author aborn (jiangguobao)
 * @date 2022/12/06 11:50
 */
public class ModbusServer extends BaseServer {
    public ModbusServer() throws IOException {
        // default 502
        super(502);
        this.name = "Modbus-RTU";
    }

    @Override
    public void run() {
        super.run();
    }

    @Override
    protected BaseWorker getWorker(Socket client) {
        // 子类实现
        return new ModbusWorker(client, getCount());
    }
}
