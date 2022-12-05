package com.sunlent.iot.plc.siemens;

/**
 * @author aborn (jiangguobao)
 * @date 2022/12/01 15:53
 */
public class S7FunCode {
    private byte code;

    S7FunCode(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

    public FunCodeEnum getCodeEnum() {
        return FunCodeEnum.from(this.code);
    }

    public enum FunCodeEnum {
        /**
         * Cpu
         */
        CpuServices((byte) 0x00),
        /**
         * 初始化连接
         */
        SetupCommunication((byte) 0xf0),
        /**
         * 读变量
         */
        ReadVar((byte) 0x04),
        WriteVar((byte) 0x05),
        RequestDownload((byte) 0x1a),
        DownloadBlock((byte)0x1b),
        DownloadEnd((byte)0x1c),
        StartUpload((byte)0x1d),
        Upload((byte)0x1e),
        EndUpload((byte)0x1f),
        PLCControl((byte)0x28),
        PLCStop((byte)0x29)
        ;
        private byte code;


        FunCodeEnum(byte code) {
            this.code = code;
        }

        public static FunCodeEnum from(byte code) {
            for (FunCodeEnum codeEnum : FunCodeEnum.values()) {
                if (codeEnum.getCode() == code) {
                    return codeEnum;
                }
            }
            return null;
        }

        public byte getCode() {
            return this.code;
        }

    }
}
