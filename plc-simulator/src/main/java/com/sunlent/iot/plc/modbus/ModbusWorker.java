package com.sunlent.iot.plc.modbus;

import com.sunlent.iot.plc.base.BaseWorker;
import com.sunlent.iot.plc.util.ByteUtils;
import com.sunlent.iot.plc.util.LogUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * <a href="https://mp.weixin.qq.com/s/jY_JTYViQoHPXzquinhTzg">CRC校验原来这么简单</a>
 *
 * @author aborn (jiangguobao)
 * @date 2022/12/08 19:16
 */
public class ModbusWorker extends BaseWorker {
    public static final String AREA = "MODBUS";

    public ModbusWorker(Socket socket) {
        super(socket);
    }

    public ModbusWorker(Socket socket, int id) {
        super(socket, id);
    }

    @Override
    protected String getArea() {
        return AREA;
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
                LogUtils.log("---------S:" + round + "---------" + getArea() + "---------");

                // 设备编号
                byte[] deviceId = new byte[]{buffer[0]};
                byte funCode = buffer[1];

                if (deviceId[0] == (byte) 0x00) {
                    // boradcast
                } else {
                    if (funCode == (byte) 0x06) {
                        // single write
                        byte[] address = new byte[2];
                        address[0] = buffer[2];
                        address[1] = buffer[3];
                        LogUtils.debug("__write single-value, start address:" + LogUtils.getBytesString(address));
                        byte[] data = new byte[2];
                        data[0] = buffer[4];
                        data[1] = buffer[5];

                        write(ByteUtils.bytesToString(deviceId, address), data);
                        out.write(buffer, 0, n);
                    } else if (funCode == (byte) 0x10) {
                        // write multiple-valued
                        byte[] address = new byte[2];
                        address[0] = buffer[2];
                        address[1] = buffer[3];
                        byte[] regCount = new byte[2];
                        regCount[0] = buffer[4];
                        regCount[1] = buffer[5];
                        short regCountS = ByteUtils.byteArrayToShort(regCount);
                        LogUtils.debug("__write multiple-value, start address:" + LogUtils.getBytesString(address) + ", regCount:" + regCountS);
                        short dataLen = ByteUtils.byteArrayToShort(new byte[]{0x00, buffer[6]});
                        byte[] data = new byte[dataLen];
                        System.arraycopy(buffer, 7, data, 0, dataLen);
                        for (short i = 0; i < regCountS; i++) {
                            byte[] currentAddress = ByteUtils.byteAddressPlus(address, i);
                            byte[] dataCurrent = new byte[]{data[i * 2], data[i * 2 + 1]};
                            LogUtils.log("write address:" + LogUtils.getBytesString(currentAddress) + ", val:" + LogUtils.getBytesString(dataCurrent));
                            write(ByteUtils.bytesToString(deviceId, currentAddress), dataCurrent);
                        }
                        byte[] resBuf = new byte[8];
                        System.arraycopy(buffer, 0, resBuf, 0, 6);
                        byte[] crcV = utils.CRC16Modbus.calCRC(resBuf, 0, resBuf.length - 2);
                        System.arraycopy(crcV, 0, resBuf, resBuf.length - 2, crcV.length);
                        LogUtils.debug(LogUtils.getBytesString(resBuf));
                        out.write(resBuf, 0, resBuf.length);
                    } else if (funCode == (byte) 0x03) {
                        // 读多个寄存器
                        byte[] address = new byte[2];
                        address[0] = buffer[2];
                        address[1] = buffer[3];
                        byte[] regCount = new byte[2];
                        regCount[0] = buffer[4];
                        regCount[1] = buffer[5];
                        short regCountS = ByteUtils.byteArrayToShort(regCount);
                        LogUtils.log("__read multiple-value, start address:" + LogUtils.getBytesString(address) + ", regCount:" + regCountS);

                        byte[] data = new byte[1024];
                        short len = 0;
                        for (short i = 0; i < regCountS; i++) {
                            byte[] currentAddress = ByteUtils.byteAddressPlus(address, i);
                            byte[] dataCurrent = read(ByteUtils.bytesToString(deviceId, currentAddress));
                            if (dataCurrent == null) {
                                dataCurrent = new byte[2];
                            }
                            System.arraycopy(dataCurrent, 0, data, len, dataCurrent.length);
                            len += dataCurrent.length;
                        }
                        byte[] dataLen = ByteUtils.shortToByteArray(len);
                        byte[] resBuf = new byte[2 + 1 + len + 2];
                        System.arraycopy(buffer, 0, resBuf, 0, 2);
                        // the length of return data (unit: byte)
                        resBuf[2] = dataLen[1];
                        // write data to resBuf
                        System.arraycopy(data, 0, resBuf, 3, len);
                        byte[] crcV = utils.CRC16Modbus.calCRC(resBuf, 0, resBuf.length - 2);
                        System.arraycopy(crcV, 0, resBuf, resBuf.length - 2, crcV.length);
                        LogUtils.debug(LogUtils.getBytesString(resBuf));
                        out.write(resBuf, 0, resBuf.length);
                    }
                }

                LogUtils.log("---------E:" + round + "---------" + getArea() + "---------");
                round++;
            }
        } catch (IOException ioException) {
            LogUtils.log("handleClient io exception." + ioException.getMessage());
        } catch (Exception e) {
            LogUtils.log("handleClient exception." + e.getMessage());
        }
    }
}
