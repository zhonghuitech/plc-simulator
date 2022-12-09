package com.sunlent.iot.plc.omron;

import com.sunlent.iot.plc.base.BaseWorker;
import com.sunlent.iot.plc.util.ByteUtils;
import com.sunlent.iot.plc.util.LogUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * <a href="https://blog.csdn.net/xiketangAndy/article/details/120267036">FinsTCP协议报文详细分析</a>
 * <a href="https://blog.csdn.net/weixin_37700863/article/details/120536223">欧姆龙PLC的FinsTCP协议</a>
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
            InetAddress inetAddress = socket.getLocalAddress();
            byte[] ipAddress = inetAddress.getAddress();

            int n;
            int round = 0;
            while ((n = in.read(buffer)) > 0) {
                LogUtils.log("---------S:" + round + "---------");
                OmronBaseReq omronBaseReq = new OmronBaseReq(buffer);
                OmronComCode.ComCodeEnum codeEnum = OmronComCode.ComCodeEnum.from(omronBaseReq.getCommand());
                LogUtils.debug(" income:" + LogUtils.getBytesString(buffer, n) + ", command:" + codeEnum.name());

                if (codeEnum == OmronComCode.ComCodeEnum.HANDSHAKE) {
                    int len = 6 * 4;
                    byte[] resBuffer = new byte[len];
                    System.arraycopy(buffer, 0, resBuffer, 0, n);
                    // 返回长度
                    resBuffer[7] = 0x10;
                    resBuffer[len - 1] = ipAddress[ipAddress.length - 1];
                    out.write(resBuffer, 0, len);
                } else {
                    // 读写的命令码为同一个
                    // if (codeEnum == OmronComCode.ComCodeEnum.Read) {
                    OmronCommandReq commandReq = new OmronCommandReq(buffer, n);
                    byte[] params = commandReq.getParams();

                    byte[] area = new byte[1];
                    area[0] = params[0];
                    byte[] address = new byte[3];
                    System.arraycopy(params, 1, address, 0, 3);
                    byte[] regCount = new byte[2];
                    regCount[0] = params[4];
                    regCount[1] = params[5];
                    short regCountS = ByteUtils.byteArrayToShort(regCount);

                    if (params.length == 6) {
                        // Read
                        short len = 0;
                        byte[] data = new byte[1024];
                        for (short i = 0; i < regCountS; i++) {
                            byte[] currentAddress = ByteUtils.byteAddressPlus(address, i);
                            byte[] dataCurrent = read(ByteUtils.bytesToString(area, currentAddress));
                            if (dataCurrent == null) {
                                dataCurrent = new byte[2];
                            }
                            LogUtils.log("__read address:" + LogUtils.getBytesString(currentAddress) + ", val:" + LogUtils.getBytesString(dataCurrent));
                            System.arraycopy(dataCurrent, 0, data, len, dataCurrent.length);
                            len += dataCurrent.length;
                        }
                        byte[] resBuf = new byte[28 + 2 + len];
                        System.arraycopy(buffer, 0, resBuf, 0, 28);

                        int dataLen = resBuf.length - 8;
                        byte[] dataLenArr = ByteUtils.intToByteArray(dataLen);
                        // Command 后的数据长度
                        System.arraycopy(dataLenArr, 0, resBuf, 4, dataLenArr.length);
                        // ICF 返回的固定值为 0xC0
                        resBuf[16] = (byte) 0xC0;
                        // 目标节点（PC的IP最后一位）
                        resBuf[20] = buffer[23];
                        // 源节点号（PLC的IP最后一位）
                        resBuf[23] = buffer[20];

                        System.arraycopy(data, 0, resBuf, 30, len);
                        LogUtils.debug("__read resBuf" + LogUtils.getBytesString(resBuf));
                        out.write(resBuf, 0, resBuf.length);
                    } else {
                        // Write
                        byte[] data = new byte[regCountS * 2];
                        System.arraycopy(buffer, 28 + 6, data, 0, data.length);

                        for (short i = 0; i < regCountS; i++) {
                            byte[] currentAddress = ByteUtils.byteAddressPlus(address, i);
                            byte[] dataCurrent = new byte[]{data[i * 2], data[i * 2 + 1]};
                            LogUtils.log("__write address:" + LogUtils.getBytesString(currentAddress) + ", val:" + LogUtils.getBytesString(dataCurrent));
                            write(ByteUtils.bytesToString(area, currentAddress), dataCurrent);
                        }
                        byte[] resBuf = new byte[28 + 2];
                        System.arraycopy(buffer, 0, resBuf, 0, 28);

                        int dataLen = resBuf.length - 8;
                        byte[] dataLenArr = ByteUtils.intToByteArray(dataLen);
                        // Command 后的数据长度
                        System.arraycopy(dataLenArr, 0, resBuf, 4, dataLenArr.length);
                        // ICF 返回的固定值为 0xC0
                        resBuf[16] = (byte) 0xC0;
                        // 目标节点（PC的IP最后一位）
                        resBuf[20] = buffer[23];
                        // 源节点号（PLC的IP最后一位）
                        resBuf[23] = buffer[20];

                        LogUtils.debug("__write resBuf" + LogUtils.getBytesString(resBuf));
                        out.write(resBuf, 0, resBuf.length);
                    }
                }
                LogUtils.log("---------E:" + round + "---------");
                round++;
            }
        } catch (IOException ioException) {
            LogUtils.log("handleClient io exception." + ioException.getMessage());
        } catch (Exception e) {
            LogUtils.log("handleClient exception." + e.getMessage());
        }
    }

    public static void main(String[] args) {

    }
}
