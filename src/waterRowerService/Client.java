package waterRowerService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import sun.security.action.GetLongAction;

public class Client implements DataNotifier, Runnable {
	
	private WaterRowerService wrs;
	private Socket s;
	private Scanner in;
	private PrintWriter out;
	private long waitTime=100; // wait ms before checking for new command

	public Client( WaterRowerService wrs, Socket s) throws IOException {
		this.wrs=wrs;
		this.s=s;
        in = new Scanner(s.getInputStream());
        out = new PrintWriter(s.getOutputStream(), true);
	}

	private static Object semaphore=new Object();
	
	@Override
	public void run() {
		
		wrs.registerNotifier(this);

		boolean listen=true;
		while( listen && s.isConnected()) {
		    while (in.hasNextLine()) {
		    	String cmd=in.nextLine();
		        System.out.println("RCVD: "+cmd);
		        if( "X".equals(cmd)) {
			        System.out.println("Stop receiving input from client.");
		        	listen=false;
		        }
		    }
		    try {
				Thread.sleep(waitTime);
			} catch (InterruptedException e) {
			}
		}
		
		wrs.unregisterNotifier(this);
		
		// Flush streams
		in.close();
		out.close();
		
    	try {
			s.close();
		} catch (IOException e) {
	        System.out.println("Exception caught closing socket: "+e.getLocalizedMessage());
		}
	}

	@Override
	public void readEvent(DataEvent e) throws DataConnectorException {
		// Send data to client
		if( e.getEventType().equals(DataEvent.EventType.DATA)) {
			out.print(e.getRawData());
		}
	}

}
