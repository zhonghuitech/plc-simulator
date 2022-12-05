package com.sunlent.iot.plc.melsec;

import com.sunlent.iot.plc.base.BaseWorker;
import com.sunlent.iot.plc.util.ByteUtils;
import com.sunlent.iot.plc.util.LogUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 见文档：
 * <a href="https://blog.csdn.net/wy749929317/article/details/124144389">三菱PLC MC协议</a>
 *
 * @author aborn (jiangguobao)
 * @date 2022/12/05 15:54
 */
public class Qna3EWorker extends BaseWorker {
    ConcurrentHashMap<String, byte[]> MEMORY = new ConcurrentHashMap<>();

    @Override
    public void run() {
        handleClient();
    }

    Qna3EWorker(Socket socket, int id) throws IOException {
        LogUtils.log("Worker_" + id + ", running...");
        this.socket = socket;
        this.socketid = id;
    }

    private void handleClient() {
        try {
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();

            byte[] buffer = new byte[1024];
            byte[] s7Pdu = new byte[1024];
            byte[] isoHeader = new byte[7];

            int n;
            while ((n = in.read(buffer)) > 0) {
                boolean status = false;
                int pduLen = n - 7;
                LogUtils.log("socket_" + this.getSocketid() + "， port:" + this.getSocket().getPort() + " read buffer running...");
                LogUtils.log("buffer:" + LogUtils.getBytesString(buffer, n));
                Qna3EHeader qna3EHeader = new Qna3EHeader(buffer);


                Qna3ERequestItem qna3ERequestItem = new Qna3ERequestItem(buffer);

                // 0xa8 表示D点
                if ((byte) 0xa8 == qna3ERequestItem.getSofCode()) {
                    String address = getAddress(qna3ERequestItem.getAddress());
                    LogUtils.log("Data Length=" + ByteUtils.byteArrayToShortS(qna3EHeader.getDataLen())
                            + ", Write data to address: " + address + ", write data: " + LogUtils.getBytesString(qna3ERequestItem.getData()));

                    write(address, qna3ERequestItem.getData());
                    byte[] resBuffer = new byte[11];
                    System.arraycopy(buffer, 0, resBuffer, 0, 11);
                    resBuffer[0] = (byte) 0xd0;
                    out.write(resBuffer, 0, n);
                }
            }
            LogUtils.log("handleClient finished.");
        } catch (IOException ioException) {
            LogUtils.log("handleClient io exception." + ioException.getMessage());
        } catch (Exception e) {
            LogUtils.log("handleClient exception." + e.getMessage());
        }
    }

    private String getAddress(byte[] address) {
        // TO Big-Ed
        byte[] addr = new byte[2];
        addr[0] = address[1];
        addr[1] = address[0];
        short addrV = ByteUtils.byteArrayToShort(addr);
        return "D" + addrV;
    }

    @Override
    protected void write(String address, byte[] value) {
        MEMORY.put(address, value);
    }

    @Override
    protected byte[] read(String address) {
        // 3、4 为数据长度位
        byte[] dataValue = new byte[]{0x00, 0x00, 0x00, 0x0d};

        if (MEMORY.containsKey(address)) {
            dataValue = MEMORY.get(address);
        } else {
            LogUtils.log("位置：" + address + ", 值不存在!");
            // TODO 不存在时根据类型，可以返回一个随机的值
        }
        return dataValue;
    }

}
