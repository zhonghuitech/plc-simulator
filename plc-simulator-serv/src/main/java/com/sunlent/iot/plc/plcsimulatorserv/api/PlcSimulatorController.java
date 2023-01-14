package com.sunlent.iot.plc.plcsimulatorserv.api;

import com.sunlent.iot.plc.base.SimuData;
import com.sunlent.iot.plc.melsec.Qna3EWorker;
import com.sunlent.iot.plc.modbus.ModbusWorker;
import com.sunlent.iot.plc.plcsimulatorserv.http.Result;
import com.sunlent.iot.plc.util.ByteUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author aborn (jiangguobao)
 * @date 2023/01/11 17:44
 */
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PlcSimulatorController {

    @GetMapping("/hello")
    public Result hello() {
        Map<String, Object> res = new HashMap<>();
        res.put("status", "success");
        res.put("code", 200);
        return Result.of(res);
    }

    @PostMapping("/read")
    public Result<List<RegData>> read(@RequestBody RegRequest regs) {
        List<RegData> list = new ArrayList<>();
        if (regs != null && !CollectionUtils.isEmpty(regs.getRegs())) {
            for (RegData regData : regs.getRegs()) {
                String address = regData.getAddress();
                String v;
                if (ModbusWorker.AREA.equals(regData.getArea())) {
                    byte[] deviceId = new byte[]{0x01};
                    byte[] startAddress = ByteUtils.shortToByteArray((short) Integer.parseInt(regData.getAddress()));
                    address = ByteUtils.bytesToString(deviceId, startAddress);
                    byte[] currentAddress = ByteUtils.byteAddressPlus(startAddress, (short) 1);
                    String ca = ByteUtils.bytesToString(deviceId, currentAddress);

                    byte[] value = SimuData.get(regData.getArea(), address);
                    // 第二位
                    byte[] valueCa = SimuData.get(regData.getArea(), ca);

                    v = (value == null || valueCa == null) ? null : ByteUtils.byteArrayNumberToValueString(ByteUtils.join(value, valueCa), false);
                } else {
                    byte[] value = SimuData.get(regData.getArea(), address);
                    boolean isL = false;
                    if (Qna3EWorker.AREA.equals(regData.getArea())) {
                        isL = true;
                    }
                    v = ByteUtils.byteArrayNumberToValueString(value, isL);
                }
                regData.setValue(v == null ? "-" : v);
                list.add(regData);
            }
        }
        return Result.of(list);
    }
}
