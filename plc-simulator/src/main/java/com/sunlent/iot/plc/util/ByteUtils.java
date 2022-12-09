package com.sunlent.iot.plc.util;

import java.util.HexFormat;

/**
 * byte相关的函数
 *
 * @author aborn (jiangguobao)
 * @date 2022/12/05 10:14
 */
public class ByteUtils {

    /**
     * byte 数组与 int 的相互转换
     *
     * @param b
     * @return
     */
    public static int byteArrayToInt(byte[] b) {
        return b[3] & 0xFF |
                (b[2] & 0xFF) << 8 |
                (b[1] & 0xFF) << 16 |
                (b[0] & 0xFF) << 24;
    }

    public static byte[] intToByteArray(int a) {
        return new byte[]{
                (byte) ((a >> 24) & 0xFF),
                (byte) ((a >> 16) & 0xFF),
                (byte) ((a >> 8) & 0xFF),
                (byte) (a & 0xFF)
        };
    }

    /**
     * Big endian
     *
     * @param a
     * @return
     */
    public static byte[] shortToByteArray(short a) {
        return new byte[]{
                (byte) ((a >> 8) & 0xFF),
                (byte) (a & 0xFF)
        };
    }

    /**
     * short 需要2 个
     *
     * @param b
     * @return
     */
    public static short byteArrayToShort(byte[] b) {
        return (short)
                ((b[1] & 0xFF) |
                        /**  相当于乘 2^8 = 乘 256  */
                        (b[0] & 0xFF) << 8);
    }

    public static byte[] byteAddressPlus(byte[] startAddress, short step) {
        short start = byteArrayToShort(startAddress);
        return shortToByteArray((short) (start + step));
    }

    /**
     * Little endian
     *
     * @param address
     * @return
     */
    public static short byteArrayToShortL(byte[] address) {
        byte[] addr = new byte[2];
        addr[0] = address[1];
        addr[1] = address[0];
        return ByteUtils.byteArrayToShort(addr);
    }

    /**
     * Little endian
     *
     * @param sV
     * @return
     */
    public static byte[] shorToByteArrayL(short sV) {
        byte[] bigEBytes = shortToByteArray(sV);
        byte[] res = new byte[2];
        res[0] = bigEBytes[1];
        res[1] = bigEBytes[0];
        return res;
    }

    public static String bytesToString(byte[]... bytes) {
        StringBuilder sb = new StringBuilder();
        HexFormat hexFormat = HexFormat.of();
        for (byte[] item : bytes) {
            sb.append(hexFormat.formatHex(item));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        byte[] bytes = new byte[]{(byte) 0xfe, (byte) 0x02, (byte) 0x00, (byte) 0x00};
        System.out.println(byteArrayToInt(bytes));
        byte[] test = new byte[2];
        test[0] = 0x11;
        test[1] = (byte) 0xfe;
        System.out.println(bytesToString(test));

        byte[] startAddress = new byte[]{0x01, 0x05};
        for (short i = 0; i < 3; i++) {
            LogUtils.log(LogUtils.getBytesString(byteAddressPlus(startAddress, i)));
        }
    }
}
