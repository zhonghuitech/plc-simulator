package com.sunlent.iot.plc.base;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author aborn (jiangguobao)
 * @date 2022/12/07 17:42
 */
public class SimuData {

    public static final ConcurrentHashMap<String, byte[]> MEMORY = new ConcurrentHashMap<>();

    public static void put(String area,  String address, byte[] value) {
        MEMORY.put(area + address, value);
    }
    public static boolean containsKey(String area, String address) {
        return MEMORY.containsKey(area + address);
    }

    public static byte[] get(String area, String address) {
        return MEMORY.getOrDefault(area + address, null);
    }

}
