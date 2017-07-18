package org.hashmap.modbus.RtuMaster;
import purejavacomm.*;

public class ModbusRtuMasterConfig {

    private final String portName;
    private final int baudRate;
    private final int timeout;
    private final int dataBits;
    private final int stopBits;
    private final int parity;
    private final int flowControl;
    private final boolean lsbWordFirst;

    public ModbusRtuMasterConfig(String portName,
                                 int baudRate,
                                 int timeout,
                                 int dataBits,
                                 int stopBits,
                                 int parity,
                                 int flowControl,
                                 boolean lsbWordFirst) {
        this.portName = portName;
        this.baudRate = baudRate;
        this.timeout = timeout;
        this.dataBits = dataBits;
        this.stopBits = stopBits;
        this.parity = parity;
        this.flowControl = flowControl;
        this.lsbWordFirst = lsbWordFirst;
    }

    public String getPortName() {
        return portName;
    }

    public int getBaudRate() {
        return baudRate;
    }

    public int getTimeout() {
        return timeout;
    }

    public int getDataBits() {
        return dataBits;
    }

    public int getStopBits() {
        return stopBits;
    }

    public int getFlowControl() { return flowControl; }

    public int getParity() { return  parity; }

    public boolean isLsbWordFirst() { return lsbWordFirst; }

    public static class Builder {

        private final String portName;
        private int baudRate = 9600;
        private int timeout = 1000;
        private int dataBits = SerialPort.DATABITS_8;
        private int stopBits = SerialPort.STOPBITS_1;
        private int parity = SerialPort.PARITY_NONE;
        private int flowControl = SerialPort.FLOWCONTROL_NONE;
        private boolean lsbWordFirst = false;

        public Builder(String portName) {
            this.portName = portName;
        }

        public Builder setBaudRate(int baudRate) {
            this.baudRate = baudRate;
            return this;
        }

        public Builder setTimeout(int timeout) {
            this.timeout = timeout;
            return this;
        }

        public Builder setDataBits(int dataBits) {
            this.dataBits = dataBits;
            return this;
        }

        public Builder setStopBits(int stopBits) {
            this.stopBits = stopBits;
            return this;
        }

        public Builder setParity(int parity) {
            this.parity = parity;
            return this;
        }

        public Builder setFlowControl(int flowControl) {
            this.flowControl = flowControl;
            return this;
        }

        public Builder setLsbWordFirst(boolean lsbWordFirst) {
            this.lsbWordFirst = lsbWordFirst;
            return this;
        }


        public ModbusRtuMasterConfig build() {
            return new ModbusRtuMasterConfig(
                    portName,
                    baudRate,
                    timeout,
                    dataBits,
                    stopBits,
                    parity,
                    flowControl,
                    lsbWordFirst);
        }
    }
}
