package com.sunlent.iot.plc.siemens;
import com.sunlent.iot.plc.util.ByteUtils;

/**
 * @author aborn (jiangguobao)
 * @date 2022/12/05 14:40
 */
public class S7Utils {

    /**
     * 注意：当传入如下int值（十进制： -33423360 ）时，可能会被误判成 string 的写入 （这里不清楚S7的协议！）
     * byte[] bytes = new byte[]{(byte) 0xfe, (byte) 0x02, (byte) 0x00, (byte) 0x00};
     * @param value
     * @return
     */
    public static boolean checkIsWriteString(byte[] value, byte currentByte) {
        if (value[0] == currentByte || (currentByte == (byte) 0x00 && value[0] == (byte) 0xfe)) {
            byte[] shortB = new byte[]{(byte) 0x00, (byte) value[1]};
            short len = ByteUtils.byteArrayToShort(shortB);
            if (len == (value.length - 2)) {
                return true;
            }
        }
        return false;
    }
}
