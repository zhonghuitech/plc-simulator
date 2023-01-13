package com.sunlent.iot.plc.plcsimulatorserv.api;

import com.sunlent.iot.plc.base.SimuData;
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
                byte[] value = SimuData.get(regData.getArea(), regData.getAddress());
                if (value != null) {
                    if (value.length == 2) {
                        regData.setValue(ByteUtils.byteArrayToShortL(value));
                    } else if (value.length == 4) {
                        regData.setValue(ByteUtils.byteArrayToIntL(value));
                    } // todo toLong
                }

                list.add(regData);
            }
        }
        return Result.of(list);
    }
}
