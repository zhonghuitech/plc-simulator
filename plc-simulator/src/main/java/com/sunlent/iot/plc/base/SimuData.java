package com.sunlent.iot.plc.base;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author aborn (jiangguobao)
 * @date 2022/12/07 17:42
 */
public class SimuData {

    private static final String SPLIT = ":";
    public static final ConcurrentHashMap<String, byte[]> MEMORY = new ConcurrentHashMap<>();

    public static void put(String area, String address, byte[] value) {
        MEMORY.put(getKey(area, address), value);
    }

    public static boolean containsKey(String area, String address) {
        return MEMORY.containsKey(getKey(area, address));
    }

    public static byte[] get(String area, String address) {
        return MEMORY.getOrDefault(getKey(area, address), null);
    }

    private static String getKey(String area, String address) {
        return area + SPLIT + address;
    }

    public static void clearAreaData(String area) {
        for (Map.Entry<String, byte[]> entry : MEMORY.entrySet()) {
            String key = entry.getKey();
            if (key.startsWith(area + SPLIT)) {
                MEMORY.remove(key);
            }
        }
    }

}
