package waterRowerService;

import static org.junit.Assert.*;

import org.junit.Test;

public class DataRecordTest {

	@Test
	public void testToString() throws DataRecordException {
		DataRecord dataRec=new DataRecord("A8000310008010311021050047302");
		String toString=dataRec.toString();
		String expectedString=new String("31 80\n");
		assertTrue(expectedString.equals( toString));
		dataRec=new DataRecord("A8001310008010311021050047302");
		assertTrue(new String( "91 80\n").equals(dataRec.toString()));
	}
   
}
