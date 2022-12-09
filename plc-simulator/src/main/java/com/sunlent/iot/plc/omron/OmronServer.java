package com.sunlent.iot.plc.omron;

import com.sunlent.iot.plc.base.BaseServer;

import java.io.IOException;

/**
 * @author aborn (jiangguobao)
 * @date 2022/12/09 12:03
 */
public class OmronServer extends BaseServer {

    public OmronServer() throws IOException {
        super(9600);
        this.name = "Omron-TCP";
    }
}
