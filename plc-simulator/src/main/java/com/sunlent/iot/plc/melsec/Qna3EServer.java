package com.sunlent.iot.plc.melsec;

import com.sunlent.iot.plc.base.BaseServer;
import com.sunlent.iot.plc.base.BaseWorker;
import com.sunlent.iot.plc.siemens.S7Worker;
import com.sunlent.iot.plc.util.LogUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author aborn (jiangguobao)
 * @date 2022/12/05 15:49
 */
public class Qna3EServer extends BaseServer {

    public Qna3EServer() throws IOException {
        super(6000);
        this.name = "Melsec_Qna3E";
    }

    @Override
    protected BaseWorker getWorker(Socket client) {
        return new Qna3EWorker(client, getCount());
    }
}
