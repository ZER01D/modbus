package org.hashmap.modbus.RtuMaster;

import java.io.*;
import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import purejavacomm.*;
import static io.netty.buffer.Unpooled.*;


class SerialManager {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final int SIZE_INITBUFFER = 8;
    private final int SIZE_SLAVEID = 1;
    private final int SIZE_FUNCODE = 1;
    private final int SIZE_FIRSTADDRESS = 2;
    private final int SIZE_DATACOUNT = 2;
    private final int SIZE_NODATABYTE = 1;
    private final int SIZE_CRCCODE = 2;
    private final int READ_OUTPUT_COILS = 1;
    private final int READ_INPUT_CONTACTS = 2;
    private final int READ_HOLDING_REGISTER = 3;
    private final int READ_INPUT_REGISTER = 4;


    SerialManager() {}

    public int getSIZE_INITBUFFER() { return SIZE_INITBUFFER; }
    public int getSIZE_SLAVEID() { return SIZE_SLAVEID; }
    public int getSIZE_FUNCODE() { return SIZE_FUNCODE; }
    public int getSIZE_FIRSTADDRESS() { return SIZE_FIRSTADDRESS; }
    public int getSIZE_DATACOUNT() { return SIZE_DATACOUNT; }
    public int getSIZE_NODATABYTE() { return SIZE_NODATABYTE; }
    public int getSIZE_CRCCODE() {return SIZE_CRCCODE; }

    CommPortIdentifier getCommPortIdentifier(String portName) {
        try {
            return CommPortIdentifier.getPortIdentifier(portName);
        } catch (NoSuchPortException ex) {
            logger.error("Port with :" + portName + "is not available. Exception : " + ex);
        }
        return null;
    }

    SerialPort openSerialPort(CommPortIdentifier portId, int timeout) {
        try {
            return (SerialPort) portId.open("Modbus", timeout);
        } catch (PortInUseException ex) {
            logger.error("Port in use exception ex : " + ex);
        }
        return null;
    }

    void setFlowControlMode(SerialPort serialPort, int flowcontrol) {
        try {
            serialPort.setFlowControlMode(flowcontrol);
        } catch(UnsupportedCommOperationException ex) {
            logger.error("UnsupportedCommOperationExcetion : ex - " + ex);
        }
    }

    void setSerialPortParams(SerialPort serialPort, ModbusRtuMasterConfig config) {
        try {
            serialPort.setSerialPortParams(config.getBaudRate(),
                    config.getDataBits(),
                    config.getStopBits(),
                    config.getParity());
        } catch (UnsupportedCommOperationException e) {
            logger.error("UnsupportedCommOperationException e : " + e);
        }
    }

    OutputStream getSerialPortOutputStream(SerialPort serialPort) {
        try {
            return serialPort.getOutputStream();
        } catch (IOException ex) {
            logger.error("IOException in getOutputStream ex : " + ex);
        }
        return null;
    }

    void sendRequestOutStream(OutputStream outputStream, byte[] msg) {
        try {
            outputStream.write(msg);
        } catch (IOException ex) {
            logger.error("IOException in sendRequestOutStream ex :" + ex);
        }
    }

    InputStream getSerialPortInputStream(SerialPort serialPort) {
        try {
            return serialPort.getInputStream();
        } catch (IOException ex) {
            logger.error("IOExcepetion in getInputStream ex : " + ex);
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

    ByteBuf readResponseOnSerialPort(InputStream inputStream, ModbusRtuMasterConfig config) {
        int size = 0;
        ByteBuf buffer = null;
        try {
            int slaveId = inputStream.read();
            int fcCode = inputStream.read();
            logger.debug("SlaveId of the Response : " + slaveId + " with Function Code : " + fcCode);

            if (fcCode == READ_OUTPUT_COILS || fcCode == READ_INPUT_CONTACTS ||
                fcCode == READ_HOLDING_REGISTER || fcCode == READ_INPUT_REGISTER) {

                int noDataByte = inputStream.read();
                logger.debug("No. of Bytes to follow : " + noDataByte);
                int[] receivedValues = new int[noDataByte + 1];
                receivedValues[0] = noDataByte;
                for (int i = 1; i < receivedValues.length; i++) {
                    receivedValues[i] = inputStream.read();
                }

                if (config.isLsbWordFirst() &&
                    (fcCode == READ_HOLDING_REGISTER || fcCode == READ_INPUT_REGISTER)) {
                    parseWordOrderedArray(receivedValues, noDataByte);
                }

                int[] crcCode = new int[SIZE_CRCCODE];
                for (int i = 0; i < SIZE_CRCCODE; i++) {
                    crcCode[i] = inputStream.read();
                }
                size = SIZE_SLAVEID + SIZE_FUNCODE + SIZE_NODATABYTE + noDataByte + SIZE_CRCCODE;
                buffer = createByteBuffer(slaveId, fcCode,
                                          receivedValues, crcCode,
                                          size);
            }
            else {
                int[] receivedValues = new int[SIZE_FIRSTADDRESS + SIZE_DATACOUNT];
                for(int i = 0; i < receivedValues.length; i++) {
                    receivedValues[i] = inputStream.read();
                }
                int[] crcCode = new int[SIZE_CRCCODE];
                for (int i = 0; i < SIZE_CRCCODE; i++) {
                    crcCode[i] = inputStream.read();
                }
                size = SIZE_SLAVEID + SIZE_FUNCODE + SIZE_FIRSTADDRESS + SIZE_DATACOUNT + SIZE_CRCCODE;
                buffer = createByteBuffer(slaveId, fcCode,
                                          receivedValues, crcCode,
                                          size);
            }
            return buffer;
        } catch (IOException ex) {
            logger.error("Error in Reading Ex : " + ex);
        }
        return  null;
    }

    private ByteBuf createByteBuffer(int slaveId, int fcCode, int[] receivedValues, int[] crcCode, int size) {
        ByteBuf buffer = buffer(size);
        buffer.writeByte(slaveId);
        buffer.writeByte(fcCode);
        for(int i = 0; i < receivedValues.length; i++) {
            buffer.writeByte(receivedValues[i]);
        }
        buffer.writeByte(crcCode[0]);
        buffer.writeByte(crcCode[1]);
        return buffer;
    }

    private void parseWordOrderedArray(int[] receivedValues, int noDataByte) {
        int noOf32BitsData = noDataByte / 4;
        for ( int i = 3; i <= noOf32BitsData * 4; i++) {
            int temp = receivedValues[i];
            receivedValues[i] = receivedValues[i-2];
            receivedValues[i-2] = temp;
            if ( (i % 4) == 0) {
                i += 2;
            }
        }
    }

    private void parseByteOrderedArray(int[] receivedValues, int noDataByte) {
        int noOf16BitsData = noDataByte / 2;
        for ( int i = 2; i <= noOf16BitsData * 2; i += 2) {
            int temp = receivedValues[i];
            receivedValues[i] = receivedValues[i-1];
            receivedValues[i-1] = temp;
        }
    }
}
