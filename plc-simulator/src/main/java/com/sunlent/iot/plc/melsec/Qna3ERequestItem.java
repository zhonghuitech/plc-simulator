package com.sunlent.iot.plc.melsec;

import com.sunlent.iot.plc.util.ByteUtils;

/**
 * @author aborn (jiangguobao)
 * @date 2022/12/05 19:01
 */
public class Qna3ERequestItem {
    /**
     * 起始软件元件，保存数据的地址
     */
    private byte[] address = new byte[3];

    /**
     * 软件元件代码
     */
    private byte sofCode;

    /**
     * 软件元件点数，要写入的数据长度
     */
    private byte[] dataLength = new byte[2];

    /**
     * 软件元件点数的数据
     */
    private byte[] data;

    public Qna3ERequestItem(byte[] qna) {
        int offset = 15;
        System.arraycopy(qna, offset, this.address, 0, 3);
        this.sofCode = qna[offset + 3];
        this.dataLength[0] = qna[offset + 4];
        this.dataLength[1] = qna[offset + 5];
        short len = ByteUtils.byteArrayToShortL(this.dataLength);
        this.data = new byte[len];
        System.arraycopy(qna, offset + 6, this.data, 0, len);
    }

    public byte[] getAddress() {
        return address;
    }

    public byte[] getData() {
        return data;
    }

    public byte getSofCode() {
        return sofCode;
    }

    public byte[] getDataLength() {
        return dataLength;
    }
}
