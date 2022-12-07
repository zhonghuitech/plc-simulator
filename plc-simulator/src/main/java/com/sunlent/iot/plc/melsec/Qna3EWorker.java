package com.sunlent.iot.plc.melsec;

import com.sunlent.iot.plc.base.BaseWorker;
import com.sunlent.iot.plc.base.PLCConstents;
import com.sunlent.iot.plc.util.ByteUtils;
import com.sunlent.iot.plc.util.LogUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Ref：
 * <a href="https://blog.csdn.net/wy749929317/article/details/124144389">三菱PLC MC协议</a>
 * <a href="https://zhuanlan.zhihu.com/p/546432657">三菱Q系列PLC（可编程控制器）：TCP MC协议</a>
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
            int n;
            int round = 0;
            while ((n = in.read(buffer)) > 0) {
                boolean status = false;
                LogUtils.log("---------S:" + round + "---------");
                LogUtils.log("socket_" + this.getSocketid() + "， port:" + this.getSocket().getPort() + " read buffer running...");
                LogUtils.log("buffer:" + LogUtils.getBytesString(buffer, n));

                Qna3EHeader qna3EHeader = new Qna3EHeader(buffer);
                Qna3ERequestItem qna3ERequestItem = new Qna3ERequestItem(buffer);
                Qna3EComCode.ComCodeEnum codeEnum = Qna3EComCode.ComCodeEnum.from(qna3EHeader.getCommand());
                Qna3ERequestItem.TypeCodeEnum addressType = qna3ERequestItem.getTypeCodeEnum();
                String address = getAddress(qna3ERequestItem.getAddress(), addressType);


                if (codeEnum == Qna3EComCode.ComCodeEnum.WriteVar) {
                    byte[] lenB = qna3ERequestItem.getDataLength();
                    short lenShort = ByteUtils.byteArrayToShortL(lenB);
                    short dataLen = (short) (addressType.isWord() ? lenShort * 2 : 2*lenShort);
                    byte[] writeData = new byte[dataLen];
                    System.arraycopy(buffer, 21, writeData, 0, dataLen);

                    LogUtils.log("__Write data to address: " + address + ", Data Length=" + ByteUtils.byteArrayToShortL(qna3EHeader.getDataLen())
                            + ", write data: " + LogUtils.getBytesString(writeData) + ", dataLen:" + dataLen);

                    write(address, writeData);
                    out.write(PLCConstents.Qna3E_WRITE_SUCCESS, 0, PLCConstents.Qna3E_WRITE_SUCCESS.length);
                    status = true;
                } else if (codeEnum == Qna3EComCode.ComCodeEnum.ReadVar) {
                    byte[] readV = read(address);
                    byte[] readLenB = qna3ERequestItem.getDataLength();
                    short readLenShort = ByteUtils.byteArrayToShortL(readLenB);
                    short dataLen = (short) (addressType.isWord() ? readLenShort * 2 : 2*readLenShort);
                    byte[] resBuffer = new byte[(dataLen) + PLCConstents.Qna3E_WRITE_SUCCESS.length];

                    LogUtils.log("__Read data from address: " + address + ", Data Length=" + ByteUtils.byteArrayToShortL(qna3EHeader.getDataLen())
                            + ", read data value: " + LogUtils.getBytesString(readV) + ", read dataLen:" + dataLen);

                    System.arraycopy(PLCConstents.Qna3E_WRITE_SUCCESS, 0, resBuffer, 0, PLCConstents.Qna3E_WRITE_SUCCESS.length);
                    System.arraycopy(readV, 0, resBuffer, PLCConstents.Qna3E_WRITE_SUCCESS.length, readV.length);
                    resBuffer[0] = (byte) 0xD0;

                    // 计算数据部分长度
                    short resDataLen = (short) (resBuffer.length - 9);
                    byte[] dataLenArr = ByteUtils.shorToByteArrayL(resDataLen);
                    resBuffer[7] = dataLenArr[0];
                    resBuffer[8] = dataLenArr[1];

                    LogUtils.log("Read Res buffer:" + LogUtils.getBytesString(resBuffer));
                    out.write(resBuffer, 0, resBuffer.length);
                    status = true;
                }

                if (!status) {
                    out.write(PLCConstents.Qna3E_WRITE_SUCCESS, 0, PLCConstents.Qna3E_WRITE_SUCCESS.length);
                }

                round++;
                LogUtils.log("---------E:" + round + "---------");
            }
            LogUtils.log("handleClient finished." + this.socketid + ", " + this.socket);
        } catch (IOException ioException) {
            LogUtils.log("handleClient io exception." + ioException.getMessage());
        } catch (Exception e) {
            LogUtils.log("handleClient exception." + e.getMessage());
        }
    }

    private String getAddress(byte[] address, Qna3ERequestItem.TypeCodeEnum addrType) {
        // TO Big-Ed
        byte[] addr = new byte[2];
        addr[0] = address[1];
        addr[1] = address[0];
        short addrV = ByteUtils.byteArrayToShort(addr);
        StringBuilder stringBuilder = new StringBuilder();

        /**
         * 0xA8 represents Dxx
         */
        if (addrType != null) {
            stringBuilder.append(addrType.name());
        }
        stringBuilder.append(addrV);
        return stringBuilder.toString();
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
