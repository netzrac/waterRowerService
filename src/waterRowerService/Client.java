package waterRowerService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import sun.security.action.GetLongAction;

public class Client implements DataNotifier, Runnable {
	
	private WaterRowerService wrs;
	private Socket s;
	private InputStream is;
	private OutputStream os;

	public Client( WaterRowerService wrs, Socket s) throws IOException {
		this.wrs=wrs;
		this.s=s;
		this.is=s.getInputStream();
		this.os=s.getOutputStream();
	}

	private static Object semaphore=new Object();
	
	@Override
	public void run() {
		
		wrs.registerNotifier(this);
				
		try {
			semaphore.wait();
		} catch (InterruptedException e) {
		}
		
		wrs.unregisterNotifier(this);
		try {
			is.close();
			os.close();
		} catch (IOException e) {
			System.err.println( "Exception caught closing streams: "+e.getLocalizedMessage());
		}
	}

	@Override
	public void readEvent(DataEvent e) throws DataConnectorException {
		
	}

}
