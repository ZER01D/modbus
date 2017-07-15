package org.hashmap.modbus.RtuExample.master;

import com.digitalpetri.modbus.requests.*;
import org.hashmap.modbus.RtuMaster.ModbusRtuMaster;
import org.hashmap.modbus.RtuMaster.ModbusRtuMasterConfig;
import com.digitalpetri.modbus.codec.Modbus;
import com.digitalpetri.modbus.responses.ReadHoldingRegistersResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RtuMasterExample {

    public static void main(String[] args) throws InterruptedException {
        new RtuMasterExample().start();
    }

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private volatile boolean started = false;

    public RtuMasterExample() {

    }

    public void start() {
        started = true;

        /*
            Serial Port to be required.
         */
        ModbusRtuMasterConfig config = new ModbusRtuMasterConfig.Builder("/dev/pts/45")
                .build();

        ModbusRtuMaster master = new ModbusRtuMaster(config);
        sendAndReceive(master);
    }

    private void sendAndReceive(ModbusRtuMaster master) {
        if (!started) return;

        byte slaveId = 0x11;
        master.sendRequest(new ReadHoldingRegistersRequest(107, 3), slaveId);
//        master.sendRequest(new ReadCoilsRequest(1956, 37), slaveId);
//        master.sendRequest(new ReadDiscreteInputsRequest(196, 22), slaveId);
//        master.sendRequest(new ReadInputRegistersRequest(8, 1), slaveId);
    }

    public void stop() {
        started = false;
    }

}
