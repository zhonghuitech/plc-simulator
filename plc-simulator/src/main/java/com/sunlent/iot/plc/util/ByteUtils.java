package com.sunlent.iot.plc.util;

import java.nio.ByteBuffer;
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

    public static byte[] longToByteArray(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(x);
        return buffer.array();
    }

    public static long byteArrayToLong(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.put(bytes);
        buffer.flip();//need flip
        return buffer.getLong();
    }

    public static long byteArrayToLongL(byte[] bytes) {
        return byteArrayToLong(reverse(bytes));
    }

    protected static byte[] reverse(byte[] b) {
        byte[] a = new byte[b.length];
        for (int i = 0; i < b.length; i++) {
            a[i] = b[b.length - i - 1];
        }
        return a;
    }

    public static int byteArrayToIntL(byte[] b) {
        return byteArrayToInt(reverse(b));
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
        return ByteUtils.byteArrayToShort(reverse(address));
    }

    public static String byteArrayNumberToValueString(byte[] value, boolean isLitteEndian) {
        if (value == null) {
            return null;
        }

        if (value.length == 2) {
            return String.valueOf(isLitteEndian ? ByteUtils.byteArrayToShortL(value) : ByteUtils.byteArrayToShort(value));
        } else if (value.length == 4) {
            return String.valueOf(isLitteEndian ? ByteUtils.byteArrayToIntL(value) : ByteUtils.byteArrayToInt(value));
        } else if (value.length == 8) {
            return String.valueOf(isLitteEndian ? ByteUtils.byteArrayToLongL(value) : ByteUtils.byteArrayToLong(value));
        } else {
            return null;
        }
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

    public static byte[] join(byte[]... bytes) {
        int leng = 0;
        for (byte[] item : bytes) {
            leng += item.length;
        }
        byte[] result = new byte[leng];

        int idx = 0;
        for (byte[] item : bytes) {
            System.arraycopy(item, 0, result, idx, item.length);
            idx += item.length;
        }
        return result;
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

        byte[] in = intToByteArray(30);
        LogUtils.log(LogUtils.getBytesString(in));

        byte[] demo = new byte[]{0x01, 0x00, 0x00, 0x00};
        System.out.println(byteArrayToInt(demo));
        System.out.println(byteArrayToIntL(demo));

        // byte to long
        byte[] longBytes = longToByteArray(15L);
        LogUtils.log(LogUtils.getBytesString(longBytes));
        long longValue = byteArrayToLong(longBytes);
        System.out.println(longValue);
    }
}
