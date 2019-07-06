package waterRowerService;

import java.util.Enumeration;

import gnu.io.CommPortIdentifier;

public class SerialDataConnector implements DataConnector {

	final private String device="/dev/USB01";
	
	public SerialDataConnector() {
		Enumeration<?> portIdentifiers = CommPortIdentifier.getPortIdentifiers();
		 //
		 // Check each port identifier if 
		 //   (a) it indicates a serial (not a parallel) port, and
		 //   (b) matches the desired name.
		 //
		 CommPortIdentifier portId = null;  // will be set if port found
		 while (portIdentifiers.hasMoreElements())
		 {
		     CommPortIdentifier pid = (CommPortIdentifier) portIdentifiers.nextElement();
		     if(pid.getPortType() == CommPortIdentifier.PORT_SERIAL &&
		        pid.getName().equals(device)) 
		     {
		         portId = pid;
		         break;
		     }
		 }
		 if(portId == null)
		 {
		     System.err.println("Could not find serial port " + device);
		     System.exit(1);
		 }
	}

	@Override
	public void initialize() throws DataConnectorException {
		// TODO Auto-generated method stub

	}

	@Override
	public void reset() throws DataConnectorException {
		// TODO Auto-generated method stub

	}

	@Override
	public void register(DataNotifier notifier) {
		// TODO Auto-generated method stub

	}

	@Override
	public void write(String data) throws DataConnectorException {
		// TODO Auto-generated method stub

	}

}
