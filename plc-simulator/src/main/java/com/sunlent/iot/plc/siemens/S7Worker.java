package com.sunlent.iot.plc.siemens;

import com.sunlent.iot.plc.base.BaseWorker;
import com.sunlent.iot.plc.base.PLCConstents;
import com.sunlent.iot.plc.util.ByteUtils;
import com.sunlent.iot.plc.util.LogUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;

/**
 * S7协议
 * Refs：
 * <a href="https://blog.csdn.net/Mr_Bobcp/article/details/106648781">西门子S7协议笔记</a>
 * <a href="https://blog.csdn.net/oliver223/article/details/118107094">西门子S7协议介绍</a>
 * <a href="https://www.jianshu.com/p/19798f2768e1">S7协议常量</a>
 *
 * @author aborn (jiangguobao)
 * @date 2022/12/01 15:17
 */
public class S7Worker extends BaseWorker {

    public static final String AREA = "S7WORKER";

    public S7Worker(Socket socket) {
        super(socket);
    }

    public S7Worker(Socket socket, int id) {
        super(socket, id);
    }

    @Override
    protected String getArea() {
        return AREA;
    }

    /**
     * 处理客户端流
     *
     * @throws IOException
     */
    @Override
    protected void handleClient() {
        try {
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();

            byte[] buffer = new byte[1024];
            byte[] s7Pdu = new byte[1024];
            byte[] isoHeader = new byte[7];

            int n;
            int round = 0;
            while ((n = in.read(buffer)) > 0) {
                LogUtils.log("---------S:" + round + "---------");
                boolean status = false;
                int pduLen = n - 7;
                LogUtils.log("socket_" + this.getSocketid() + "， port:" + this.getSocket().getPort() + " read buffer running..." + pduLen);
                // 去掉前面 7 个 byte，前7个byte为 TPKT&COTP 的协议头
                System.arraycopy(buffer, 7, s7Pdu, 0, Math.min(buffer.length - 7, pduLen));
                System.arraycopy(buffer, 0, isoHeader, 0, isoHeader.length);
                LogUtils.log(socket + ":" + "获得 S7PDU 值:" + LogUtils.getBytesString(s7Pdu, pduLen));
                byte[] s7PduV = new byte[pduLen];
                System.arraycopy(s7Pdu, 0, s7PduV, 0, pduLen);
                if (!check(s7Pdu)) {
                    LogUtils.log("协议检测失败！");
                }

                S7FunCode funCode = new S7FunCode(s7Pdu[10]);

                // 读写位置的个数，（仅用于读写）
                byte addressCount = s7Pdu[11];
                S7Header s7Header = new S7Header(s7Pdu);
                S7RequestItem s7RequestItem = new S7RequestItem(s7Pdu);
                if (funCode.getCodeEnum() == S7FunCode.FunCodeEnum.SetupCommunication) {
                    out.write(PLCConstents.S7_1200_INIT, 0, PLCConstents.S7_1200_INIT.length);
                    LogUtils.log("-----------连接初始化操作---------");
                    status = true;
                } else if (funCode.getCodeEnum() == S7FunCode.FunCodeEnum.WriteVar) {
                    byte memoryArea = s7Pdu[20];
                    S7DataItem s7DataItem = new S7DataItem(s7PduV);
                    if ((byte) s7RequestItem.getArea() == memoryArea) {
                        String address = getMarkAddress(s7RequestItem.getAddress());
                        // 剩余长度byte全为值， pdu header 10, pdu parameters 2, requestItem 12, data header 4
                        int left = pduLen - 28;
                        LogUtils.log("__WW__写数据到地址：" + address + ", 类型：" + s7RequestItem.getVarType() + ", 数据byte长度：" + left + ", dataVType:" + s7DataItem.getVarType());
                        byte[] value = new byte[left];
                        System.arraycopy(s7Pdu, 28, value, 0, left);
                        LogUtils.log("value:" + LogUtils.getBytesString(value));
                        byte[] currentValue = read(address);

                        if (S7Utils.checkIsWriteString(value, currentValue[0])) {
                            // 写字符串，client在写string之前会发起读操作 （在写其他类型的时候不会发起读）
                            byte[] valueS = new byte[s7DataItem.getData().length - 2];
                            System.arraycopy(s7DataItem.getData(), 2, valueS, 0, valueS.length);
                            LogUtils.log("写字符串类型：" + new String(valueS, StandardCharsets.US_ASCII));
                            write(address, valueS);
                        } else {
                            // 写的其他类型
                            LogUtils.log("写其他类型");
                            write(address, value);
                        }
                        out.write(PLCConstents.S7_1200_SUCCESS, 0, PLCConstents.S7_1200_SUCCESS.length);
                        status = true;
                    } else {
                        LogUtils.log("写其他位置");
                    }
                } else if (funCode.getCodeEnum() == S7FunCode.FunCodeEnum.ReadVar) {
                    String address = getMarkAddress(s7RequestItem.getAddress());
                    short len = ByteUtils.byteArrayToShort(s7RequestItem.count);
                    LogUtils.log("__RR__从如下位置读数据：" + address + ", 类型：" + s7RequestItem.getVarType() + ", 长度：" + len);
                    byte[] dataValueTemp = read(address);
                    byte[] dataValue = dataValueTemp;
                    if (len > dataValueTemp.length) {
                        dataValue = new byte[len];
                        System.arraycopy(dataValueTemp, 0, dataValue, 0, dataValueTemp.length);
                    }

                    int dataLen = dataValue.length;
                    byte[] dataHeader = new byte[]{(byte) 0xff, 0x04, 0x00, 0x00};
                    byte[] totalLenBytes = ByteUtils.shortToByteArray((short) (dataLen + dataHeader.length));

                    dataHeader[2] = totalLenBytes[0];
                    dataHeader[3] = totalLenBytes[1];

                    /**
                     * 返回的总长度计算：
                     *  7 ： iSO header长度 (包括TPKT 4byte, ISO-COTP 3byte), TPKT 4个byte分别表示：version--1byte, reserved--1byte, length--2byte (总数据包长度)
                     *  12： S7 PDU header长度
                     *  2 ： S7 Parametar 长度
                     *  数据Header(4) + 数据内容(4)
                     */
                    int isoHeaderLength = 7;
                    int resultTotalLen = isoHeaderLength + 12 + 2 + dataHeader.length + dataLen;
                    byte[] resultTotalLenByte = ByteUtils.shortToByteArray((short) resultTotalLen);
                    byte[] result = new byte[resultTotalLen];
                    byte[] resIsoHeader = new byte[7];
                    System.arraycopy(isoHeader, 0, resIsoHeader, 0, isoHeader.length);
                    System.arraycopy(resultTotalLenByte, 0, resIsoHeader, 2, resultTotalLenByte.length);

                    // 1. Copy ISO Header
                    System.arraycopy(resIsoHeader, 0, result, 0, isoHeaderLength);
                    // 2. Copy S7 PDU Header
                    System.arraycopy(s7Pdu, 0, result, isoHeaderLength, 10);
                    // 3. Copy S7 param
                    System.arraycopy(s7Pdu, 10, result, isoHeaderLength + 10 + 2, 2);
                    // 4. Copy S7 data item
                    //  4.1 Copy s7 data item: data header
                    System.arraycopy(dataHeader, 0, result, isoHeaderLength + 12 + 2, dataHeader.length);
                    //  4.2 Copy s7 data item: data dataValue
                    System.arraycopy(dataValue, 0, result, isoHeaderLength + 12 + 2 + dataHeader.length, dataValue.length);

                    int offset = 0;
                    out.write(result, offset, result.length - offset);
                    LogUtils.log("读数据成功，数值为：" + LogUtils.getBytesString(dataValue));
                    LogUtils.log("读返回值" + LogUtils.getBytesString(result));
                    status = true;
                } else {

                }

                LogUtils.log("返回成功信息");
                if (!status) {
                    LogUtils.log("返回兜底成功信息");
                    out.write(PLCConstents.S7_1200_SUCCESS, 0, PLCConstents.S7_1200_SUCCESS.length);
                }

                LogUtils.log("---------E:" + round + "---------");
                round++;
            }
            LogUtils.log(this.getArea() + " handleClient finished." + this.socketid + ", " + this.socket);
        } catch (IOException ioException) {
            LogUtils.log("handleClient io exception." + ioException.getMessage());
        } catch (Exception e) {
            LogUtils.log("handleClient exception." + e.getMessage());
        }
    }

    @Override
    protected byte[] read(String address) {
        // 3、4 为数据长度位
        byte[] defaultV = new byte[]{0x00, 0x00, 0x00, 0x0d};

        byte[] dataV = super.read(address);
        if (dataV == null) {
            LogUtils.log("位置：" + address + ", 值不存在!");
        }
        return dataV == null ? defaultV : dataV;
    }

    String getMarkAddress(byte[] addressInput) {
        byte[] address = new byte[4];
        System.arraycopy(addressInput, 0, address, 1, 3);
        String addressV = "M" + byteArrayToIntM(address);
        return addressV;
    }

    int byteArrayToIntM(byte[] b) {
        return (b[3] & 0xFF |
                (b[2] & 0xFF) << 8 |
                (b[1] & 0xFF) << 16 |
                (b[0] & 0xFF) << 24) >> 3;
    }

    private boolean check(byte[] s7pdu) {
        if ((byte) 0x32 != s7pdu[0]) {
            return false;
        }
        return true;
    }

}
