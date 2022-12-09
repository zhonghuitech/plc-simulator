package com.sunlent.iot.plc.omron;

import com.sunlent.iot.plc.base.BaseServer;
import com.sunlent.iot.plc.base.BaseWorker;
import com.sunlent.iot.plc.modbus.ModbusWorker;

import java.io.IOException;
import java.net.Socket;

/**
 * @author aborn (jiangguobao)
 * @date 2022/12/09 12:03
 */
public class OmronServer extends BaseServer {

    public OmronServer() throws IOException {
        super(9600);
        this.name = "Omron-TCP";
    }

    @Override
    protected BaseWorker getWorker(Socket client) {
        // 子类实现
        return new OmronWorker(client, getCount());
    }
}
