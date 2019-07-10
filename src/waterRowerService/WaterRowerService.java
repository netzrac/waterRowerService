package waterRowerService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import waterRowerService.DataEvent.EventType;

public class WaterRowerService {

	private static boolean executeService=true;
	private SerialDataConnector sdc;
	private long waitfor=50;

	public WaterRowerService() throws DataConnectorException {
		
//		class SimpleDataNotifier implements DataNotifier {
//
//			private WaterRowerService waterRowerService;
//
//			public SimpleDataNotifier(WaterRowerService waterRowerService) {
//				this.waterRowerService=waterRowerService;
//			}
//
//			@Override
//			public void readEvent(DataEvent e) throws DataConnectorException {
//				System.out.println( e.getEventType()+": "+e.getRawData());
//				if( e.getEventType()==EventType.F_REPL) {
//					waterRowerService.reset();
//				}
//			}
//			
//		}

		sdc=new SerialDataConnector();
		
		reset();
		
	}
	
	public void reset() throws DataConnectorException {

			try {
				synchronized (sdc) {
					sdc.write( "C\n");
					Thread.sleep(waitfor);
					sdc.write( "T\n");
					Thread.sleep(waitfor);
					sdc.write( "V\n");
					Thread.sleep(waitfor);
					sdc.write( "L\n");
					Thread.sleep(waitfor);
					sdc.write( "H\n");
					Thread.sleep(waitfor);
					sdc.write( "R\n");					
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}


	}
	
	public void registerNotifier(DataNotifier notifier) {
		sdc.register(notifier);
	}
	
	public void unregisterNotifier( DataNotifier notifier) {
		sdc.unregister(notifier);
	}

	public static void main(String[] args) throws DataConnectorException, IOException {
		WaterRowerService wrs=new WaterRowerService();
		ServerSocket ss=new ServerSocket(1963);
		while( executeService) {
			Socket s=ss.accept();
			Client cl=new Client( wrs, s);
			cl.run();
		}
		ss.close();
	}


}
