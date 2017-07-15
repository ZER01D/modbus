package org.hashmap.modbus.RtuMaster;

import com.digitalpetri.modbus.codec.ModbusRequestEncoder;
import com.digitalpetri.modbus.codec.ModbusResponseDecoder;
import com.digitalpetri.modbus.codec.ModbusRtuCodec;
import com.digitalpetri.modbus.codec.ModbusRtuPayload;
import com.digitalpetri.modbus.requests.ModbusRequest;
import static io.netty.buffer.Unpooled.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import purejavacomm.*;
import java.io.InputStream;
import java.io.OutputStream;
import io.netty.buffer.ByteBuf;

public class ModbusRtuMaster implements SerialPortEventListener{

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ModbusRtuMasterConfig config;
    private ModbusRtuCodec modbusRtuCodec;
    private ModbusRtuPayload modbusRtuPayload;
    private OutputStream outputStream;
    private InputStream inputStream;
    private final SerialManager serialManager;

    public ModbusRtuMaster(ModbusRtuMasterConfig config) {
        serialManager = new SerialManager();
        modbusRtuCodec = new ModbusRtuCodec(new ModbusRequestEncoder(), new ModbusResponseDecoder());
        this.config = config;
    }

    public ModbusRtuMasterConfig getConfig() {
        return config;
    }

    public void sendRequest(ModbusRequest request, byte slaveId) {
        SerialPort serialPort = initSerialComm();
        if (serialPort == null) {
            return;
        }

        byte[]byteMsg = createMsgPayload(slaveId, request);

        outputStream = serialManager.getSerialPortOutputStream(serialPort);
        if (outputStream == null) {
            return;
        }

        serialManager.sendRequestOutStream(outputStream, byteMsg);
        inputStream = serialManager.getSerialPortInputStream(serialPort);
        if (inputStream == null) {
            return;
        }

        ByteBuf buf = serialManager.readResponseOnSerialPort(inputStream);
        if (buf ==null) {
            return;
        }

        decodeReceivedBuffer(slaveId, buf);
        printBufferMsg(buf);
    }

    @Override
    public void serialEvent(SerialPortEvent event) {
        try {
            switch (event.getEventType() ) {
                case SerialPortEvent.DATA_AVAILABLE:
                    System.out.println("Read Bytes : " + inputStream.read());
                    break;
                default:
                    break;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private SerialPort initSerialComm() {
        CommPortIdentifier portId = serialManager.getCommPortIdentifier(config.getPortName());
        if (portId == null) {
            return null;
        }

        serialManager.addOwnerShipOfPort(portId);
        SerialPort serialPort = serialManager.openSerialPort(portId, config.getTimeout());
        if (serialPort == null) {
            return null;
        }

        serialManager.setFlowControlMode(serialPort, config.getFlowControl());
        serialManager.setSerialPortParams(serialPort, config);
        return serialPort;
    }

    private static int calculateCRC(ByteBuf buffer) {
        /*
            CRC is being calculate according to the xls provided by Modbus.
            Link : http://www.simplymodbus.ca/crc.xls
         */
        int crc = 0xffff;     // initial value
        int polynomial =  0xA001;  // 1010000000000001

        for(int i=0; i < buffer.capacity() - 2; i++) {
            crc = crc ^ buffer.getByte(i);
            System.out.println("1. CRC : " + Integer.toHexString(crc) + "  Byte:  " + Integer.toHexString(buffer.getByte(i)));
            for(int j=0; j < 8; j++) {
                boolean bit16 = ((crc & 1) == 1);
                crc = crc >> 1;
                if (bit16) {
                    crc = crc ^ polynomial;
                }
                System.out.println("2. CRC111 : " + Integer.toHexString(crc));
            }
            System.out.println("3. CRC : " + Integer.toHexString(crc));
        }
        crc &= 0xffff;
        int b1 = crc & 0xFF;
        crc = crc >> 8;
        crc = crc | (b1 << 8);
        System.out.println("Final CRC : " + Integer.toHexString(crc));

        return crc;
    }

    private void printBufferMsg(ByteBuf buffer) {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i < buffer.capacity(); i++) {
            byte b = buffer.getByte(i);
            sb.append(String.format("%02X ", b));
        }
        System.out.println("HEX String : " + sb.toString().replaceAll("\\s+",""));
    }

    private byte[] createMsgPayload(byte slaveId, ModbusRequest request) {
        int crcCode;
        modbusRtuPayload = new ModbusRtuPayload(slaveId, request);

        ByteBuf buffer = buffer(8);
        buffer = modbusRtuCodec.encode(modbusRtuPayload, buffer);

        crcCode = calculateCRC(buffer);
        modbusRtuPayload.setCrcCode(crcCode);
        buffer.writeShort(crcCode);

        printBufferMsg(buffer);
        byte[] byteMsg = new byte[buffer.capacity()];
        for(int i=0; i < buffer.capacity(); i++) {
            byteMsg[i] = buffer.getByte(i);
//            System.out.println("byte : " + byteMsg[i]);
        }
        return byteMsg;
    }

    private void decodeReceivedBuffer(byte slaveId, ByteBuf buf) {
        try {
            modbusRtuCodec.decode(slaveId, buf);
        } catch (Exception ex) {
            System.out.println("Error in decoding the Buffer Received");
        }
    }
}
