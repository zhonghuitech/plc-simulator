package com.sunlent.iot.plc.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HexFormat;

/**
 * @author aborn (jiangguobao)
 * @date 2022/11/29 18:03
 */
public class LogUtils {

    public static void log(String msg) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS");
        System.out.println(simpleDateFormat.format(new Date()) + " " + msg);
    }

    public static String getBytesString(byte[] bytes, int len) {
        HexFormat hex = HexFormat.of();
        String str = hex.formatHex(bytes, 0, len);
        StringBuffer sb = new StringBuffer();

        int maker = 0;
        for (int i = 0; i < str.length(); i++) {
            if (i % 2 == 0) {
                if (i != 0) {
                    sb.append(", ");
                }
                sb.append("[").append(maker).append("]0x").append(str.charAt(i));
                maker++;
            } else {
                sb.append(str.charAt(i));
            }
        }

        return sb.toString();
    }

    public static String getBytesString(byte[] bytes) {
        return getBytesString(bytes, bytes.length);
    }

    public static void main(String[] args) {

    }
}
