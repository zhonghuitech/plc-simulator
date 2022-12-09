package com.sunlent.iot.plc.omron;

/**
 * @author aborn (jiangguobao)
 * @date 2022/12/09 16:00
 */
public class OmronComCode {


    public enum ComCodeEnum {
        /**
         * TCP连接后的握手命令
         */
        HANDSHAKE(new byte[]{0x00, 0x00, 0x00, 0x00}),
        /**
         * 读取
         */
        Read(new byte[]{0x00, 0x00, 0x00, 0x02}),
        Write(new byte[]{0x00, 0x00, 0x00, 0x02}),
        ;

        private byte[] code;

        ComCodeEnum(byte[] code) {
            this.code = code;
        }

        public static OmronComCode.ComCodeEnum from(byte[] code) {
            if (code == null || code.length != 4) {
                return null;
            }

            for (OmronComCode.ComCodeEnum codeEnum : OmronComCode.ComCodeEnum.values()) {
                if (code[0] == codeEnum.code[0] && code[1] == codeEnum.code[1]
                        && code[2] == codeEnum.code[2] && code[3] == codeEnum.code[3]) {
                    return codeEnum;
                }
            }

            return null;
        }
    }
}
