package waterRowerService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.TooManyListenersException;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import waterRowerService.DataEvent.EventType;

public class SerialDataConnector implements DataConnector {

	final private String device="/dev/ttyUSB0";

	private CommPortIdentifier portId = null;  // will be set if port found

	private SerialPort serialPort;

	private OutputStream outputStream;
	
	private ArrayList<DataNotifier> dataNotifiers=new ArrayList<DataNotifier>();

	private InputStream inputStream;

	public SerialDataConnector() throws DataConnectorException {
		Enumeration<?> portIdentifiers = CommPortIdentifier.getPortIdentifiers();
		 //
		 // Check each port identifier if 
		 //   (a) it indicates a serial (not a parallel) port, and
		 //   (b) matches the desired name.
		 //
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
		     throw new DataConnectorException( "Failed to connect.");
		 } 
		 	System.out.println( portId.getName()+": "+portId.getPortType()+"\n");
		 
		 
		 try {
				serialPort = 
				    (SerialPort) portId.open("SimpleWrite", 2000);
			    } catch (PortInUseException e) {
				System.out.println("Port in use.");
			     throw new DataConnectorException( "Port in use.");
			    } 

			    try {
				outputStream = serialPort.getOutputStream();
			    } catch (IOException e) {}
			    
	   //         CommPort commPort = portIdentifier.open(this.getClass().getName(),2000);
	            
	            if ( serialPort instanceof SerialPort )
	            {
	                
	                try {
						inputStream = serialPort.getInputStream();
		                serialPort.addEventListener(new SerialReader(inputStream, dataNotifiers));
		                serialPort.notifyOnDataAvailable(true);
	                } catch (IOException | TooManyListenersException e) {
	                	throw new DataConnectorException( e.getLocalizedMessage());
	                }
	                                  
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
		dataNotifiers.add(notifier);
	}

	@Override
	public void unregister(DataNotifier notifier) {
		dataNotifiers.remove(notifier);
	}
	

    /** */
    /**
     * Handles the input coming from the serial port. A new line character
     * is treated as the end of a block in this example. 
     */
    public static class SerialReader implements SerialPortEventListener 
    {
        private InputStream in;
        private byte[] buffer = new byte[1024];
		private ArrayList<DataNotifier> dataNotifiers;
        
        public SerialReader ( InputStream in, ArrayList<DataNotifier> dataNotifiers)
        {
            this.in = in;
            this.dataNotifiers=dataNotifiers;
        }
        
        public void serialEvent(SerialPortEvent arg0) {
            int data;
          
            try
            {
                int len = 0;
                while ( ( data = in.read()) > -1 )
                {
                    if ( data == '\n' ) {
                        break;
                    }
                    buffer[len++] = (byte) data;
                }
                for( DataNotifier notifier:dataNotifiers) {
                    //System.out.print(new String(buffer,0,len));
                	notifier.readEvent(new DataEvent(EventType.DATA, new String(buffer, 0, len)));
                }
            }
            catch ( IOException e )
            {
                e.printStackTrace();
                System.exit(-1);
            }             
        }

    }
    
    
	@Override
	public void write(String data) throws DataConnectorException {
		try {
			outputStream.write(data.getBytes());
		} catch (IOException e) {
			throw new DataConnectorException(e.getLocalizedMessage());
		}
	}

}
