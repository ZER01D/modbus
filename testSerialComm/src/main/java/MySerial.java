import purejavacomm.*;
import java.util.concurrent.TimeUnit;
import java.io.*;
import io.netty.buffer.ByteBuf;
import static io.netty.buffer.Unpooled.*;


public class MySerial {
    SerialPort serialPort = null;
    OutputStream outputStream = null;
    InputStream inputStream  = null;
    static final int  TIME_OUT = 1000;   //Port Open timeout
    static final String PORT_NAME[] = {
        "tty.usbmodem", // Mac OS X
        "usbdev", // Linux
        "tty", // Linux
        "pts", //Linux Dummy
        "serial", // Linux
        "COM3", // Windows
    };


    public static void main(String[] args) {
        MySerial mySerial = new MySerial();
        mySerial.initSerial();
    }

    public void initSerial() {
        try {
            CommPortIdentifier portId = null;

            try {
                portId = CommPortIdentifier.getPortIdentifier("/dev/pts/28");
            } catch (NoSuchPortException ex) {
                System.out.println("NO Such port ex : " +  ex);
            }
            System.out.println("Found Port : " + portId.getName() + "\tPortType : " + portId.getPortType());

            try {
                serialPort = (SerialPort) portId.open("MySerial", TIME_OUT);
                System.out.println("SerialPort is Open now" + serialPort.getName());
            } catch (PortInUseException ex) {
                System.out.println("Port in use ex " + ex);
            }

            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
            serialPort.setSerialPortParams(9600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);

            System.out.println("Set Serial Param Done");


            inputStream = serialPort.getInputStream();
              ByteBuf buffer1 = readResponseOnSerialPort(inputStream);
              printBufferMsg(buffer1);


            TimeUnit.SECONDS.sleep(10);


//            String str = 11 03 06 AE41 5652 4340 49AD;
            ByteBuf buf = buffer(11);
            buf.writeByte(0x11);
            buf.writeByte(0x03);
            buf.writeByte(0x06);
            buf.writeByte(0xAE);
            buf.writeByte(0x41);
            buf.writeByte(0x56);
            buf.writeByte(0x52);
            buf.writeByte(0x43);
            buf.writeByte(0x40);
            buf.writeByte(0x49);
            buf.writeByte(0xAD);

            byte[] msg = new byte[11];
            for(int i =0 ; i < buf.capacity(); i++) {
                msg[i] = buf.getByte(i);
                System.out.println("Byte : " + msg[i]);
            }

            outputStream = serialPort.getOutputStream();
            sendRequestOutStream(outputStream, msg);
        } catch(Exception ex) {
            System.out.println("Port in use : " + ex + "  PortId : " );
        }
    }

    ByteBuf readResponseOnSerialPort(InputStream inputStream) {
        System.out.println("In Reading....");
        try {
            ByteBuf buffer = buffer(8);
            for(int i=0; i < 8; i++) {
                buffer.writeByte(inputStream.read());
            }
            return buffer;
        } catch (IOException ex) {
            System.out.print("Error in Reading Ex : " + ex);
        }
        return  null;
    }

    private void printBufferMsg(ByteBuf buffer) {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i < buffer.capacity(); i++) {
            byte b = buffer.getByte(i);
            sb.append(String.format("%02X ", b));
        }
        System.out.println("HEX STring : " + sb.toString().replaceAll("\\s+",""));
    }

    void sendRequestOutStream(OutputStream outputStream, byte[] msg) {
        try {
            outputStream.write(msg);
        } catch (IOException ex) {
            System.out.println("IOException in sendRequestOutStream ex :" + ex);
        }
    }
}
