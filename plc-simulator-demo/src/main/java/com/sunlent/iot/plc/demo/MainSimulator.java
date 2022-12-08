package com.sunlent.iot.plc.demo;

import com.sunlent.iot.plc.melsec.Qna3EServer;
import com.sunlent.iot.plc.modbus.ModbusServer;
import com.sunlent.iot.plc.siemens.S7Server;
import com.sunlent.iot.plc.util.LogUtils;

import java.io.IOException;

/**
 * @author aborn (jiangguobao)
 * @date 2022/12/01 09:57
 */
public class MainSimulator {

    public static void main(String[] args) {
        System.out.println("start..");
        try {
            new S7Server().start();
            new Qna3EServer().start();
            new ModbusServer().start();
        } catch (IOException e) {
            LogUtils.log("main start exception.!");
        }
    }
}
