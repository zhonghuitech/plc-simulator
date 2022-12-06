package com.sunlent.iot.plc.melsec;

/**
 * Qna3EHeader里的command字段
 * @author aborn (jiangguobao)
 * @date 2022/12/06 09:50
 */
public class Qna3EComCode {

    private byte[] command = new byte[2];

    public Qna3EComCode(byte[] command) {
        this.command = command;
    }

    public enum ComCodeEnum {
        /**
         * 读取
         */
        ReadVar(new byte[]{0x01, 0x04}),
        WriteVar(new byte[]{0x01, 0x14}),
        ;

        private byte[] code;

        ComCodeEnum(byte[] code) {
            this.code = code;
        }

        public static ComCodeEnum from(byte[] code) {
            if (code == null || code.length != 2) {
                return null;
            }

            for (ComCodeEnum codeEnum: ComCodeEnum.values()) {
                if (code[0] == codeEnum.code[0] && code[1] == codeEnum.code[1]) {
                    return codeEnum;
                }
            }

            return null;
        }

    }
}
