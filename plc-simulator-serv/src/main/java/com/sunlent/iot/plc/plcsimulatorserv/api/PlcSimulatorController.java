package com.sunlent.iot.plc.plcsimulatorserv.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
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
}
