package waterRowerService;

import static org.junit.Assert.*;

import org.junit.Test;

public class SerialDataConnectorTest {

	@Test
	public void testSerialDataConnector() {
		SerialDataConnector sdc = null;
		try {
			sdc=new SerialDataConnector();
		} catch (Exception e) {
			assertFalse("Exception caught.", true);
		}
		assertNotNull( "Object is null.", sdc);
	}

}
