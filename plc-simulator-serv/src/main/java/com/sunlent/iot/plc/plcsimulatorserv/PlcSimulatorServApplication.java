package com.sunlent.iot.plc.plcsimulatorserv;

import com.sunlent.iot.plc.melsec.Qna3EServer;
import com.sunlent.iot.plc.modbus.ModbusServer;
import com.sunlent.iot.plc.omron.OmronServer;
import com.sunlent.iot.plc.siemens.S7Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

/**
 * @author aborn
 */
@SpringBootApplication
public class PlcSimulatorServApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlcSimulatorServApplication.class, args);

		System.out.println("start simulator...");
		try {
			new S7Server().start();
			new Qna3EServer().start();
			new ModbusServer().start();
			new OmronServer().start();
		} catch (IOException e) {
			System.out.println("exception start simulator.!");
		}
		System.out.println("finished starting simulator!");
	}

}
