package com.sunlent.iot.plc.util;

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

    /**
     * Small-Edien
     * @param address
     * @return
     */
    public static short byteArrayToShortS(byte[] address) {
        byte[] addr = new byte[2];
        addr[0] = address[1];
        addr[1] = address[0];
        return ByteUtils.byteArrayToShort(addr);
    }

    public static void main(String[] args) {
        byte[] bytes = new byte[]{(byte) 0xfe, (byte) 0x02, (byte) 0x00, (byte) 0x00};
        System.out.println(byteArrayToInt(bytes));
    }
}
