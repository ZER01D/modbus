package org.hashmap.modbus.RtuExample.master;

import com.digitalpetri.modbus.requests.*;
import io.netty.buffer.ByteBuf;
import org.hashmap.modbus.RtuMaster.ModbusRtuMaster;
import org.hashmap.modbus.RtuMaster.ModbusRtuMasterConfig;
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
        ModbusRtuMasterConfig config = new ModbusRtuMasterConfig.Builder("/dev/pts/27")
                .build();

        ModbusRtuMaster master = new ModbusRtuMaster(config);
        sendAndReceive(master);
    }

    private void sendAndReceive(ModbusRtuMaster master) {
        if (!started) return;

        byte slaveId = 0x11;

        /***********************************************************
                READ REQUESTS
        ************************************************************/
        // Function Code - 01
        ByteBuf buf = master.sendRequest(new ReadCoilsRequest(19, 37), slaveId);

        // Function Code - 02
//        ByteBuf buf = master.sendRequest(new ReadDiscreteInputsRequest(196, 22), slaveId);

        // Function Code - 03
//        ByteBuf buf = master.sendRequest(new ReadHoldingRegistersRequest(107, 3), slaveId);

        // Function Code - 04
//        ByteBuf buf = master.sendRequest(new ReadInputRegistersRequest(8, 1), slaveId);


        /************************************************************
                WRITE REQUESTS
         ************************************************************/
        // Function Code : 05
//        master.sendRequest(new WriteSingleCoilRequest(172, true), slaveId);

        //Function Code : 06
//        master.sendRequest(new WriteSingleRegisterRequest(1, 3), slaveId);

        //Function Code : 15
//        byte[] valuesCoil = new byte[2];
//        valuesCoil[0] = 0x0D;
//        valuesCoil[1] = 0x01;
//        master.sendRequest(new WriteMultipleCoilsRequest(19, 10, valuesCoil), slaveId);

        //Function Code : 16
//        byte[] valueRegister = new byte[4];
//        valueRegister[0] = 0x00;
//        valueRegister[1] = 0x0A;
//        valueRegister[2] = 0x01;
//        valueRegister[3] = 0x02;
//        master.sendRequest(new WriteMultipleRegistersRequest(1,2,valueRegister), slaveId);
    }

    public void stop() {
        started = false;
    }

}
