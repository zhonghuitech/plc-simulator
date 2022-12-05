package com.sunlent.iot.plc.melsec;

/**
 * 总长度 15byte
 * @author aborn (jiangguobao)
 * @date 2022/12/05 18:50
 */
public class Qna3EHeader {

    /**
     * 协议常量，始终设置 0x50
     */
    private byte protocolId;

    private byte msgType;

    /**
     * 网络编号
     */
    private byte netNo;

    /**
     * 可编程控制器网络编号
     */
    private byte pNetNo;

    /**
     * 请求目标模块 I/O 编号
     */
    private byte[] ioCode = new byte[2];

    /**
     * 请求目标模块 站号
     */
    private byte sideCode;

    /**
     * 请求数据长度， 是指 dataLen后面的byte长度，从 qna[9]开始算起
     */
    private byte[] dataLen = new byte[2];

    /**
     * CPU监视定时器
     */
    private byte[] cpuTimer = new byte[2];

    private byte[] command = new byte[2];

    private byte[] subCommand = new byte[2];

    public Qna3EHeader(byte[] qnaData) {
        this.protocolId = qnaData[0];
        this.msgType = qnaData[1];
        this.netNo = qnaData[2];
        this.pNetNo = qnaData[3];
        this.ioCode[0] = qnaData[4];
        this.ioCode[1] = qnaData[5];
        this.sideCode = qnaData[6];
        this.dataLen[0] = qnaData[7];
        this.dataLen[1] = qnaData[8];
        this.cpuTimer[0] = qnaData[9];
        this.cpuTimer[1] = qnaData[10];
        this.command[0] = qnaData[11];
        this.command[1] = qnaData[12];
        this.subCommand[0] = qnaData[13];
        this.subCommand[1] = qnaData[14];
    }

    public byte[] getDataLen() {
        return dataLen;
    }
}
