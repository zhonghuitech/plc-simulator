package com.sunlent.iot.plc.siemens;

/**
 * @author aborn (jiangguobao)
 * @date 2022/12/05 11:32
 */
public class S7DataItem {

    /**
     * 操作的返回值，
     * 服务返回：0xff -- 信号成功。
     * 客户端请求的Write Request消息中，此字段始终设置为0。
     */
    byte errorCode;

    /**
     * varType与count保持与 RequestItem的相同
     */
    byte varType;

    byte[] count = new byte[2];

    /**
     * 实际的数据，如果是写，则表示写的 value
     */
    byte[] data;

    S7DataItem(byte[] s7Pdu) {
        /**
         * 12： s7pdu header 长度 10 + s7spu param 2
         * 12： s7 request 长度为12
         */
        int offset = 12 + 12;
        this.errorCode = s7Pdu[offset];
        this.varType = s7Pdu[offset + 1];
        this.count[0] = s7Pdu[offset + 2];
        this.count[1] = s7Pdu[offset + 3];
        int dataLen = s7Pdu.length - (offset + 4);
        this.data = new byte[dataLen];
        System.arraycopy(s7Pdu, offset + 4, this.data, 0, dataLen);
    }

    public byte getErrorCode() {
        return errorCode;
    }

    public byte getVarType() {
        return varType;
    }

    public byte[] getCount() {
        return count;
    }

    public byte[] getData() {
        return data;
    }
}
