package com.sunlent.iot.plc.omron;

/**
 * @author aborn (jiangguobao)
 * @date 2022/12/09 16:30
 */
public class OmronCommandReq extends OmronBaseReq {

    private byte icf;
    private byte rsv;
    private byte gct;
    private byte dna;
    private byte da1;
    private byte da2;
    private byte sna;
    private byte sa1;
    private byte sa2;
    private byte sid;
    private byte mrc;
    private byte src;
    private byte[] params;

    public OmronCommandReq(byte[] buf, int len) {
        super(buf);
        int offset = 16;
        icf = buf[offset + 0];
        rsv = buf[offset + 1];
        gct = buf[offset + 2];
        dna = buf[offset + 3];
        da1 = buf[offset + 4];
        da2 = buf[offset + 5];
        sna = buf[offset + 6];
        sa1 = buf[offset + 7];
        sa2 = buf[offset + 8];
        sid = buf[offset + 9];
        mrc = buf[offset + 10];
        src = buf[offset + 11];
        this.params = new byte[len - 28];
        System.arraycopy(buf, 28, params, 0, this.params.length);
    }

    public byte[] getParams() {
        return params;
    }
}
