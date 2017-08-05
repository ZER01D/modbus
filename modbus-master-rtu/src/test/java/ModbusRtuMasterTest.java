import com.digitalpetri.modbus.requests.*;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;
import static org.testng.internal.junit.ArrayAsserts.assertArrayEquals;
import org.hashmap.modbus.RtuMaster.ModbusRtuMaster;

public class ModbusRtuMasterTest {

    @Test
    public void testRtuReadCoilRequestPayload() {
        ModbusRtuMaster master = new ModbusRtuMaster(null);
        ReadCoilsRequest readCoilsRequest = new ReadCoilsRequest(19,37);
        byte[] msgPayload = {0x11, 0x01, 0x00, 0x13, 0x00, 0x25, 0x0E, (byte) 0x84};
        byte[] byteMsg = master.createMsgPayload((byte) 0x11, readCoilsRequest);

        assertEquals(msgPayload.length, byteMsg.length);
        assertArrayEquals(msgPayload, byteMsg);
    }

    @Test
    public void testRtuReadDiscreteInputRequestPayload() {
        ModbusRtuMaster master = new ModbusRtuMaster(null);
        ReadDiscreteInputsRequest readDiscreteInputsRequest = new ReadDiscreteInputsRequest(196, 22);
        byte[] msgPayload = {0x11, 0x02, 0x00, (byte) 0xC4, 0x00, 0x16, (byte) 0xBA, (byte) 0xA9};
        byte[] byteMsg = master.createMsgPayload((byte) 0x11, readDiscreteInputsRequest);

        assertEquals(msgPayload.length, byteMsg.length);
        assertArrayEquals(msgPayload, byteMsg);
    }

    @Test
    public void testRtuReadHoldingRegisterRequestPayload() {
        ModbusRtuMaster master = new ModbusRtuMaster(null);
        ReadHoldingRegistersRequest readHoldingRegistersRequest = new ReadHoldingRegistersRequest(107, 3);
        byte[] msgPayload = {0x11, 0x03, 0x00, 0x6B, 0x00, 0x03, 0x76,(byte) 0x87};
        byte[] byteMsg = master.createMsgPayload((byte) 0x11, readHoldingRegistersRequest);

        assertEquals(msgPayload.length, byteMsg.length);
        assertArrayEquals(msgPayload, byteMsg);
    }

    @Test
    public void testRtuReadInputRegisterRequestPayload() {
        ModbusRtuMaster master = new ModbusRtuMaster(null);
        ReadInputRegistersRequest readInputRegistersRequest = new ReadInputRegistersRequest(8, 1);
        byte[] msgPayload = {0x11, 0x04, 0x00, 0x08, 0x00, 0x01, (byte) 0xB2, (byte) 0x98};
        byte[] byteMsg = master.createMsgPayload((byte) 0x11, readInputRegistersRequest);

        assertEquals(msgPayload.length, byteMsg.length);
        assertArrayEquals(msgPayload,byteMsg);
    }

    @Test
    public void testRtuWriteSingleCoilRequestPayload() {
        ModbusRtuMaster master = new ModbusRtuMaster(null);
        WriteSingleCoilRequest writeSingleCoilRequest = new WriteSingleCoilRequest(172, true);
        byte[] msgPayload = {0x11, 0x05, 0x00, (byte)0xAC, (byte)0xFF, 0x00, 0x4E, (byte)0x8B};
        byte[] byteMsg = master.createMsgPayload((byte) 0x11, writeSingleCoilRequest);

        assertEquals(msgPayload.length, byteMsg.length);
        assertArrayEquals(msgPayload,byteMsg);
    }

    @Test
    public void testRtuWriteSingleRegisterRequestPayload() {
        ModbusRtuMaster master = new ModbusRtuMaster(null);
        WriteSingleRegisterRequest writeSingleRegisterRequest = new WriteSingleRegisterRequest(1, 3);
        byte[] msgPayload = {0x11, 0x06, 0x00, 0x01, 0x00, 0x03, (byte) 0x9A, (byte) 0x9B};
        byte[] byteMsg = master.createMsgPayload((byte) 0x11, writeSingleRegisterRequest);

        assertEquals(msgPayload.length, byteMsg.length);
        assertArrayEquals(msgPayload,byteMsg);
    }

    @Test
    public void testRtuWriteMultipleCoilsRequestPayload() {
        ModbusRtuMaster master = new ModbusRtuMaster(null);
        byte[] valueCoils = {(byte)0xCD, 0x01};
        WriteMultipleCoilsRequest writeMultipleCoilsRequest = new WriteMultipleCoilsRequest(19, 10, valueCoils);
        byte[] msgPayload = {0x11, 0x0F, 0x00, 0x13, 0x00, 0x0A, 0x02, (byte) 0xCD, 0x01, (byte) 0xBF, 0x0B};
        byte[] byteMsg = master.createMsgPayload((byte) 0x11, writeMultipleCoilsRequest);

        assertEquals(msgPayload.length, byteMsg.length);
        assertArrayEquals(msgPayload,byteMsg);
    }

    @Test
    public void testRtuWriteMultipleRegistersRequestPayload() {
        ModbusRtuMaster master = new ModbusRtuMaster(null);
        byte[] valueRegister = {0x00, 0x0A, 0x01, 0x02};
        WriteMultipleRegistersRequest  writeMultipleRegistersRequest= new WriteMultipleRegistersRequest(1,2, valueRegister);
        byte[] msgPayload = {0x11, 0x10, 0x00, 0x01, 0x00, 0x02, 0x04, 0x00, 0x0A, 0x01, 0x02, (byte) 0xC6, (byte) 0xF0};
        byte[] byteMsg = master.createMsgPayload((byte) 0x11, writeMultipleRegistersRequest);

        assertEquals(msgPayload.length, byteMsg.length);
        assertArrayEquals(msgPayload,byteMsg);
    }
}
