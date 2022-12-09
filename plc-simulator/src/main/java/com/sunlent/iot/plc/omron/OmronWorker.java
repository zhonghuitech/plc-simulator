package com.sunlent.iot.plc.omron;

import com.sunlent.iot.plc.base.BaseWorker;
import com.sunlent.iot.plc.util.LogUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author aborn (jiangguobao)
 * @date 2022/12/09 15:55
 */
public class OmronWorker extends BaseWorker {

    public OmronWorker(Socket socket, int id) {
        super(socket, id);
    }

    @Override
    protected String getArea() {
        return "OMRON";
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
                OmronReqItem omronReqItem = new OmronReqItem(buffer);
                OmronComCode.ComCodeEnum codeEnum = OmronComCode.ComCodeEnum.from(omronReqItem.getCommand());

                if (codeEnum == OmronComCode.ComCodeEnum.HANDSHAKE) {
                    int len = 6*4;
                    byte[] resBuffer = new byte[len];
                    System.arraycopy(buffer, 0, resBuffer, 0, n);
                    // 返回长度
                    resBuffer[7] = 0x10;
                    resBuffer[len - 1] = 0x01;
                } else if (codeEnum == OmronComCode.ComCodeEnum.Read) {

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
