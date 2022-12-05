package com.sunlent.iot.plc.siemens;

/**
 * S7 协议头，前10（client发过来的）， 前12（server发出去的）
 * 按顺序如下
 * @author aborn (jiangguobao)
 * @date 2022/12/01 15:39
 */
public class S7Header {
    /**
     * 协议ID，协议常量，始终设置为0x32
     */
    private byte protocolId;

    /**
     * 消息的一般类型
     */
    private byte msgType;

    /**
     * 保留字段 2 byte
     */
    private byte[] reserved = new byte[2];

    private byte[] pduRef = new byte[2];

    /**
     * 参数字段的长度，Big-Endian
     */
    private byte[] paramLen = new byte[2];

    /**
     * 数据字段的长度，Big-Endian
     */
    private byte[] dataLen = new byte[2];

    /**
     * 最后两位服务端处理后返回
     */
    private byte errCls;

    private byte errCode;

    S7Header(byte[] s7Pdu) {
        this.protocolId = s7Pdu[0];
        this.msgType = s7Pdu[1];
        System.arraycopy(s7Pdu, 2, this.reserved, 0, 2);
        System.arraycopy(s7Pdu, 4, this.pduRef, 0, 2);
        System.arraycopy(s7Pdu, 6, this.paramLen, 0, 2);
        System.arraycopy(s7Pdu, 8, this.dataLen, 0, 2);
        if (s7Pdu.length >=12) {
            this.errCls = s7Pdu[11];
            this.errCode = s7Pdu[12];
        }
    }

    public byte getProtocolId() {
        return protocolId;
    }

    public byte getMsgType() {
        return msgType;
    }

    public byte[] getReserved() {
        return reserved;
    }

    public byte[] getPduRef() {
        return pduRef;
    }

    public byte[] getParamLen() {
        return paramLen;
    }

    public byte[] getDataLen() {
        return dataLen;
    }

    public byte getErrCls() {
        return errCls;
    }

    public byte getErrCode() {
        return errCode;
    }
}
