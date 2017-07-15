package org.hashmap.modbus.RtuMaster;

import java.io.*;
import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import purejavacomm.*;
import static io.netty.buffer.Unpooled.*;


class SerialManager {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    SerialManager() {}

    CommPortIdentifier getCommPortIdentifier(String portName) {
        try {
            return CommPortIdentifier.getPortIdentifier(portName);
        } catch (NoSuchPortException ex) {
            System.out.println("Port with :" + portName + "is not available. Exception : " + ex);
        }
        return null;
    }

    SerialPort openSerialPort(CommPortIdentifier portId, int timeout) {
        try {
            return (SerialPort) portId.open("Modbus", timeout);
        } catch (PortInUseException ex) {
            System.out.println("Port in use exception ex : " + ex);
        }
        return null;
    }

    void setFlowControlMode(SerialPort serialPort, int flowcontrol) {
        try {
            serialPort.setFlowControlMode(flowcontrol);
        } catch(UnsupportedCommOperationException ex) {
            System.out.println("UnsupportedCommOperationExcetion : ex - " + ex);
        }
    }

    void setSerialPortParams(SerialPort serialPort, ModbusRtuMasterConfig config) {
        try {
            serialPort.setSerialPortParams(config.getBaudRate(),
                    config.getDataBits(),
                    config.getStopBits(),
                    config.getParity());
        } catch (UnsupportedCommOperationException e) {
            System.out.println("UnsupportedCommOperationException e : " + e);
        }
    }

    OutputStream getSerialPortOutputStream(SerialPort serialPort) {
        try {
            return serialPort.getOutputStream();
        } catch (IOException ex) {
            System.out.println("IOException in getOutputStream ex : " + ex);
        }
        return null;
    }

    void sendRequestOutStream(OutputStream outputStream, byte[] msg) {
        try {
            outputStream.write(msg);
        } catch (IOException ex) {
            System.out.println("IOException in sendRequestOutStream ex :" + ex);
        }
    }

    InputStream getSerialPortInputStream(SerialPort serialPort) {
        try {
            return serialPort.getInputStream();
        } catch (IOException ex) {
            System.out.println("IOExcepetion in getInputStream ex : " + ex);
        }
        return null;
    }

    void addOwnerShipOfPort(CommPortIdentifier portId) {
        portId.addPortOwnershipListener(new CommPortOwnershipListener() {
            public void ownershipChange(int i) {
                i = PORT_OWNERSHIP_REQUESTED;
            }
        });
    }

    ByteBuf readResponseOnSerialPort(InputStream inputStream) {
        try {
            int slaveId = inputStream.read();
            int fcCode = inputStream.read();
            int noDataByte = inputStream.read();
            int[] receivedValues = new int[noDataByte];
            for (int i = 0; i < noDataByte; i++) {
                receivedValues[i] = inputStream.read();
            }
            int[] crcCode = new int[2];
            crcCode[0] = inputStream.read();
            crcCode[1] = inputStream.read();
            int size = 1 + 1 + 1 + noDataByte + 2;
            ByteBuf buffer = createByteBuffer(slaveId, fcCode, noDataByte,
                                              receivedValues, crcCode,
                                              size);
            return buffer;
        } catch (IOException ex) {
            System.out.print("Error in Reading Ex : " + ex);
        }
        return  null;
    }

    private ByteBuf createByteBuffer(int slaveId, int fcCode,int noDataByte, int[] receivedValues, int[] crcCode, int size) {
        ByteBuf buffer = buffer(size);
        buffer.writeByte(slaveId);
        buffer.writeByte(fcCode);
        buffer.writeByte(noDataByte);
        for(int i = 0; i < receivedValues.length; i++) {
            buffer.writeByte(receivedValues[i]);
        }
        buffer.writeByte(crcCode[0]);
        buffer.writeByte(crcCode[1]);
        return buffer;
    }
}
