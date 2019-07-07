package waterRowerService;

import java.net.Socket;

public class Client implements DataNotifier, Runnable {
	
	private WaterRowerService wrs;
	private Socket s;

	public Client( WaterRowerService wrs, Socket s) {
		this.wrs=wrs;
		this.s=s;
	}

	@Override
	public void run() {
		wrs.registerNotifier(this);
		
		WaterRowerService.sleep();
		
		wrs.unregisterNotifier(this);
	}

	@Override
	public void readEvent(DataEvent e) throws DataConnectorException {
		
	}

}
