package com.digitalpetri.modbus.codec;

import com.digitalpetri.modbus.ModbusPdu;

public class ModbusRtuPayload {

    private final byte slaveId;
    private final ModbusPdu modbusPdu;
    private int crcCode;

    public ModbusRtuPayload(byte slaveId, ModbusPdu modbusPdu) {
        this.slaveId = slaveId;
        this.modbusPdu = modbusPdu;
    }

    public byte getSlaveId() {
        return slaveId;
    }

    public ModbusPdu getModbusPdu() {
        return modbusPdu;
    }

    public int getCrcCode() {return crcCode;}

    public ModbusRtuPayload setCrcCode(int crcCode) {
        this.crcCode = crcCode;
        return this;
    }
}
