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
                portId = CommPortIdentifier.getPortIdentifier("/dev/pts/2");
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
            ByteBuf buffer1 = readRequestOnSerialPort(inputStream);
            printBufferMsg(buffer1);


            TimeUnit.SECONDS.sleep(10);

            /********************************************************
                Function Code - 01
                Request :  11 01 0013 0025 0E84
                Response : 11 01 05 CD6BB20E1B 45E6
             ********************************************************/
            ByteBuf buf = buffer(10);
            buf.writeByte(0x11);
            buf.writeByte(0x01);
            buf.writeByte(0x05);
            buf.writeByte(0xCD);
            buf.writeByte(0x6B);
            buf.writeByte(0xB2);
            buf.writeByte(0x0E);
            buf.writeByte(0x1B);
            buf.writeByte(0x45);
            buf.writeByte(0xE6);

            byte[] msg = new byte[10];
            for(int i =0 ; i < buf.capacity(); i++) {
                msg[i] = buf.getByte(i);
                System.out.println("Byte : " + msg[i]);
            }


            /*******************************************************
                Function Code - 02
                Request : 11 02 00C4 0016 BAA9
                Response : 11 02 03 ACDB35 2018
             *******************************************************/
            /*ByteBuf buf = buffer(8);
            buf.writeByte(0x11);
            buf.writeByte(0x02);
            buf.writeByte(0x03);
            buf.writeByte(0xAC);
            buf.writeByte(0xDB);
            buf.writeByte(0x35);
            buf.writeByte(0x20);
            buf.writeByte(0x18);

            byte[] msg = new byte[8];
            for(int i =0 ; i < buf.capacity(); i++) {
                msg[i] = buf.getByte(i);
                System.out.println("Byte : " + msg[i]);
            }*/


            /******************************************************
                Function Code - 03
                Request : 11 03 006B 0003 7687
                Response : 11 03 06 AE41 5652 4340 49AD
             ******************************************************/
            /*ByteBuf buf = buffer(11);
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
            }*/


            /******************************************************
                Function Code - 04
                Request : 11 04 0008 0001 B298
                Response : 11 04 02 000A F8F4
             ******************************************************/
            /*ByteBuf buf = buffer(7);
            buf.writeByte(0x11);
            buf.writeByte(0x04);
            buf.writeByte(0x02);
            buf.writeByte(0x00);
            buf.writeByte(0x0A);
            buf.writeByte(0xF8);
            buf.writeByte(0xF4);

            byte[] msg = new byte[7];
            for(int i =0 ; i < buf.capacity(); i++) {
                msg[i] = buf.getByte(i);
                System.out.println("Byte : " + msg[i]);
            }*/


            /*******************************************************
                Function Code - 05
                Request : 11 05 00AC FF00 4E8B
                Response : 11 05 00AC FF00 4E8B
             *******************************************************/
            /*ByteBuf buf = buffer(8);
            buf.writeByte(0x11);
            buf.writeByte(0x05);
            buf.writeByte(0x00);
            buf.writeByte(0xAC);
            buf.writeByte(0xFF);
            buf.writeByte(0x00);
            buf.writeByte(0x4E);
            buf.writeByte(0x8B);

            byte[] msg = new byte[8];
            for(int i =0 ; i < buf.capacity(); i++) {
                msg[i] = buf.getByte(i);
                System.out.println("Byte : " + msg[i]);
            }*/


            /*******************************************************
                Function Code - 06
                Request : 11 06 0001 0003 9A9B
                Response : 11 06 0001 0003 9A9B
             *******************************************************/
            /*ByteBuf buf = buffer(8);
            buf.writeByte(0x11);
            buf.writeByte(0x06);
            buf.writeByte(0x00);
            buf.writeByte(0x01);
            buf.writeByte(0x00);
            buf.writeByte(0x03);
            buf.writeByte(0x9A);
            buf.writeByte(0x9B);

            byte[] msg = new byte[8];
            for(int i =0 ; i < buf.capacity(); i++) {
                msg[i] = buf.getByte(i);
                System.out.println("Byte : " + msg[i]);
            }*/


            /*******************************************************
                Function Code - 15
                Request : 11 0F 0013 000A 02 0D01 BF0B
                Response : 11 0F 0013 000A 2699
             *******************************************************/
            /*ByteBuf buf = buffer(8);
            buf.writeByte(0x11);
            buf.writeByte(0x0F);
            buf.writeByte(0x00);
            buf.writeByte(0x13);
            buf.writeByte(0x00);
            buf.writeByte(0x0A);
            buf.writeByte(0x26);
            buf.writeByte(0x99);

            byte[] msg = new byte[8];
            for(int i =0 ; i < buf.capacity(); i++) {
                msg[i] = buf.getByte(i);
                System.out.println("Byte : " + msg[i]);
            }*/


            /*****************************************************
                Function Code - 16
                Request : 11 10 0001 0002 04 000A 0102 C6F0
                Response : 11 10 0001 0002 1298
             ******************************************************/
            /*ByteBuf buf = buffer(8);
            buf.writeByte(0x11);
            buf.writeByte(0x10);
            buf.writeByte(0x00);
            buf.writeByte(0x01);
            buf.writeByte(0x00);
            buf.writeByte(0x02);
            buf.writeByte(0x12);
            buf.writeByte(0x98);

            byte[] msg = new byte[8];
            for(int i =0 ; i < buf.capacity(); i++) {
                msg[i] = buf.getByte(i);
                System.out.println("Byte : " + msg[i]);
            }*/

            outputStream = serialPort.getOutputStream();
            sendRequestOutStream(outputStream, msg);
            serialPort.close();
        } catch(Exception ex) {
            System.out.println("Port in use : " + ex + "  PortId : " );
        }
    }

    ByteBuf readRequestOnSerialPort(InputStream inputStream) {
        System.out.println("In Reading....");
        try {
            ByteBuf buffer = buffer(8);
            for(int i=0; i < buffer.capacity(); i++) {
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
