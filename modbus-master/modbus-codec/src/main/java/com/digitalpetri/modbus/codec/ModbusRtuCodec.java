package com.digitalpetri.modbus.codec;

import com.digitalpetri.modbus.ModbusPdu;
import com.digitalpetri.modbus.UnsupportedPdu;
import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModbusRtuCodec {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ModbusPduEncoder encoder;
    private final ModbusPduDecoder decoder;

    public ModbusRtuCodec(ModbusPduEncoder encoder, ModbusPduDecoder decoder) {
        this.encoder = encoder;
        this.decoder = decoder;
    }

    public ByteBuf encode(ModbusRtuPayload payload, ByteBuf buffer) {
        buffer.writeByte(payload.getSlaveId());
        encoder.encode(payload.getModbusPdu(), buffer);
        return buffer;
    }

    public void decode(byte slaveId, ByteBuf buffer) throws Exception{
        if (slaveId != buffer.readByte()) {
            throw new Exception("SlaveId are different in Buffer");
        }

        ModbusPdu modbusPdu = decoder.decode(buffer);
        if (modbusPdu instanceof UnsupportedPdu) {
            throw new Exception("Error in decoding Header. UnsupportedPdu");
        }
    }
}
