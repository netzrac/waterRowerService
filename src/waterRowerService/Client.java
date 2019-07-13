package waterRowerService;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client implements DataNotifier, Runnable {
	
	private WaterRowerService wrs;
	private Socket s;
	private Scanner in;
	private PrintWriter out;
	private long waitTime=100; // wait ms before checking for new command

	public Client( WaterRowerService wrs, Socket s) throws IOException {
		this.wrs=wrs;
		wrs.registerNotifier(this);
		this.s=s;
        in = new Scanner(s.getInputStream());
        out = new PrintWriter(s.getOutputStream(), true);
        out.println("HELO");
	}
	
	@Override
	public void run() {

		boolean listen=true;
	    while (in.hasNextLine()) {
	    	String cmd=in.nextLine();
	        if( "X".equals(cmd)) {
		        System.out.println("Stop receiving input from client.");
	        	//listen=false;
		        break;
	        } else if( "H".equals(cmd)) {
		        System.out.println("HELO received.");
	        } else if( "R".equals(cmd)) {
		        System.out.println("Resetting water rower on demand.");
	        	try {
					wrs.reset();
				} catch (DataConnectorException e) {
					System.err.println("Exception caught resetting water rower: "+e.getLocalizedMessage());
					//listen=false;
					break;
				}
	        }
	    }
		
		System.out.println("Unregister client notifier."); 
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
			out.println(e.getRawData());
			out.flush();
		}
	}

}
