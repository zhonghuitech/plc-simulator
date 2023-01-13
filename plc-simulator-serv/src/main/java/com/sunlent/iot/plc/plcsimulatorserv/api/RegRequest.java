package com.sunlent.iot.plc.plcsimulatorserv.api;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author aborn (jiangguobao)
 * @date 2023/01/13 11:16
 */
@Data
@NoArgsConstructor
public class RegRequest {
    private List<RegData> regs;
}
