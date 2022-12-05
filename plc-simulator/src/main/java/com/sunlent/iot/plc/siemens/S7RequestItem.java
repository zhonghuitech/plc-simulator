package com.sunlent.iot.plc.siemens;

/**
 * 从Client请求过来的S7 PDU 数据字段的结构，按顺序如下
 * 总共 12个 byte
 * @author aborn (jiangguobao)
 * @date 2022/12/03 17:24
 */
public class S7RequestItem {
    /**
     * Specification Type 固定为 0x12
     */
    byte specType;

    /**
     * 此项其余部分的长度。
     */
    byte length;

    /**
     * 此字段确定项结构其余部分的寻址模式和格式。any-type 模式 常量是0x10 。
     * Syntax ID
     */
    byte syntaxId;

    /**
     * Variable Type
     * https://www.jianshu.com/p/19798f2768e1
     * 用于确定变量的类型和长度（使用常用的S7类型，如REAL，BIT，BYTE，WORD，DWORD，COUNTER等）
     * 0x01 - BIT
     * 0x02 - BYTE
     * 0x03 - CHAR
     * 0x04 - WORD
     * 0x05 - INT
     * 0x06 - DWORD
     * 0x07 - DINT
     * 0x08 - REAL
     * 0x09 - DATE
     * 0x0A - TOD
     * 0x0B - TIME
     */
    byte varType;

    /**
     * 可以用单个item 结构选择整个相似变量数组。这些变量必须具有相同的类型，并且必须在内存中连续，并且count字段确定此数组的大小。对于单变量读或写，它设置为1。
     */
    byte[] count = new byte[2];

    /**
     * DB Number
     * DB快的地址，如果该区域未设置为DB，则忽略该数据库
     */
    byte[] dbNum = new byte[2];

    /**
     * Area
     *  选择地址变量的memory area
     *  0x83 - Flags (M) (Merker)
     */
    byte area;

    /**
     * 含选定memory area中地址变量的offset。基本上，地址被转换成位offset，并按big endian字节顺序编码在3个Byte上。
     * 实际上，由于地址空间小于5 Bit，因此从不使用最高有效位。例如，DBX40.3将是0x000143，即40*8+3。
     */
    byte[] address = new byte[3];

    S7RequestItem(byte[] s7Pdu) {
        int offset = 12;
        specType = s7Pdu[offset + 0];
        length = s7Pdu[offset + 1];
        syntaxId = s7Pdu[offset + 2];
        varType = s7Pdu[offset + 3];
        count[0] = s7Pdu[offset + 4];
        count[1] = s7Pdu[offset + 5];
        dbNum[0] = s7Pdu[offset + 6];
        dbNum[1] = s7Pdu[offset + 7];
        area = s7Pdu[offset + 8];
        address[0] = s7Pdu[offset + 9];
        address[1] = s7Pdu[offset + 10];
        address[2] = s7Pdu[offset + 11];
    }

    public byte getSpecType() {
        return specType;
    }

    public byte getLength() {
        return length;
    }

    public byte getSyntaxId() {
        return syntaxId;
    }

    public byte getVarType() {
        return varType;
    }

    public byte[] getCount() {
        return count;
    }

    public byte[] getDbNum() {
        return dbNum;
    }

    public byte getArea() {
        return area;
    }

    public byte[] getAddress() {
        return address;
    }
}
