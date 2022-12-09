package com.sunlent.iot.plc.modbus;

import com.sunlent.iot.plc.base.BaseWorker;
import com.sunlent.iot.plc.util.ByteUtils;
import com.sunlent.iot.plc.util.LogUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author aborn (jiangguobao)
 * @date 2022/12/08 19:16
 */
public class ModbusWorker extends BaseWorker {

    public ModbusWorker(Socket socket) {
        super(socket);
    }

    public ModbusWorker(Socket socket, int id) {
        super(socket, id);
    }

    @Override
    protected String getArea() {
        return "MODBUS";
    }

    @Override
    protected void handleClient() {
        try {
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();

            byte[] buffer = new byte[1024];

            int n;
            int round = 0;
            while ((n = in.read(buffer)) > 0) {
                LogUtils.log("---------S:" + round + "---------");

                // 设备编号
                byte deviceId = buffer[0];
                byte funCode = buffer[1];

                if (deviceId == (byte) 0x00) {
                    // boradcast
                } else {
                    if (funCode == (byte) 0x06) {
                        // single write
                        byte[] address = new byte[2];
                        address[0] = buffer[2];
                        address[1] = buffer[3];
                        byte[] data = new byte[2];
                        data[0] = buffer[4];
                        data[1] = buffer[5];
                        write(ByteUtils.bytesToString(address), data);
                        out.write(buffer, 0, n);
                    } else if (funCode == (byte) 0x10) {
                        // 写多个保持寄存器
                    } else if (funCode == (byte) 0x03) {
                        // 读多个寄存器
                    }
                }

                round++;
                LogUtils.log("---------E:" + round + "---------");
            }
        } catch (IOException ioException) {
            LogUtils.log("handleClient io exception." + ioException.getMessage());
        } catch (Exception e) {
            LogUtils.log("handleClient exception." + e.getMessage());
        }
    }
}
