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
     * 软件元件类型
     * --------------------------------
     * 类型 -- ASCII  -- Hx二进制
     * --------------------------------
     * 位  --  X      -- 0x9C
     * 位  --  Y      -- 0x9D
     * 位  --  M      -- 0x90
     * 位  --  L      -- 0x92
     * 位  --  F      -- 0x93
     * 位  --  V      -- 0x94
     * 位  --  B      -- 0xA0
     * --------------------------------
     * 字  --  D      -- 0xA8
     * 字  --  W      -- 0x84
     * --------------------------------
     */
    private byte sofTypeCode;

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
        this.sofTypeCode = qna[offset + 3];
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

    public byte getSofTypeCode() {
        return sofTypeCode;
    }

    public byte[] getDataLength() {
        return dataLength;
    }

    public TypeCodeEnum getTypeCodeEnum() {
        return TypeCodeEnum.from(this.sofTypeCode);
    }

    public enum TypeCodeEnum {
        /**
         * 类型
         */
        X((byte) 0x9C),
        Y((byte) 0x9D),
        M((byte) 0x90),
        L((byte) 0x92),
        F((byte) 0x93),
        V((byte) 0x94),
        B((byte) 0xA0),
        D((byte) 0xA8, true),
        W((byte) 0x84, true),
        ;

        private byte type;
        private boolean word = false;

        TypeCodeEnum(byte type) {
            this.type = type;
            this.word = false;
        }

        TypeCodeEnum(byte type, boolean isWord) {
            this.type = type;
            this.word = isWord;
        }

        public static TypeCodeEnum from(byte type) {
            for (TypeCodeEnum typeCodeEnum : TypeCodeEnum.values()) {
                if (typeCodeEnum.type == type) {
                    return typeCodeEnum;
                }
            }
            return null;
        }

        public boolean isWord() {
            return word;
        }

        public static void main(String[] args) {
            System.out.println(TypeCodeEnum.X.name());
        }
    }
}
