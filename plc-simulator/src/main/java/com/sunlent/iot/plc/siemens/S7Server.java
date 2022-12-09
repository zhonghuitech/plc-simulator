package com.sunlent.iot.plc.siemens;

import com.sunlent.iot.plc.base.BaseServer;
import com.sunlent.iot.plc.base.BaseWorker;
import com.sunlent.iot.plc.omron.OmronWorker;
import com.sunlent.iot.plc.util.LogUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author aborn (jiangguobao)
 * @date 2022/12/01 09:49
 */
public class S7Server extends BaseServer {

    public S7Server() throws IOException {
        super(102);
        this.name = "Siemens_S7";
    }

    @Override
    protected BaseWorker getWorker(Socket client) {
        return new S7Worker(client, getCount());
    }
}
