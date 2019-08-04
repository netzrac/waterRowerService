/**
 *
 * Copyright 2019 Carsten Pratsch
 *
 * This file is part of waterRowerService.
 *
 * waterRowerService is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * waterRowerService is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with waterRowerService.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package waterRowerService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.TooManyListenersException;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

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
                DataEvent dataEvent = new DataEvent(new String(buffer, 0, len));
                for( DataNotifier notifier:dataNotifiers) {
                    //System.out.print(new String(buffer,0,len));
                	notifier.readEvent(dataEvent);
                }
            }
            catch ( IOException | DataConnectorException e )
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
