package waterRowerService;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WaterRowerServiceImpl implements WaterRowerService {

	private SerialDataConnector sdc;
	private long waitfor=50;
	private DataNotifier serviceNotifier;

	public WaterRowerServiceImpl() throws DataConnectorException {

		System.out.println(this.getClass().getName());
		sdc=new SerialDataConnector();
		
		this.serviceNotifier=new ServiceDataNotifier(this);
		
		registerNotifier(serviceNotifier);
		
		reset();
		
	}
	
	public void reset() throws DataConnectorException {

		System.out.println(this.getClass().getName()+"::reset()");
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
		System.out.println(this.getClass().getName()+"::registerNotifier()");
		sdc.register(notifier);
	}
	
	public void unregisterNotifier( DataNotifier notifier) {
		System.out.println(this.getClass().getName()+"::unregisterNotifier()");
		sdc.unregister(notifier);
	}
	
	

	public static void main(String[] args) throws DataConnectorException, IOException {

		WaterRowerServiceImpl wrs=new WaterRowerServiceImpl();

//		ServerSocket ss=new ServerSocket(1963);
//		while( executeService) {
//			Socket s=ss.accept();
//			Client cl=new Client( wrs, s);
//			cl.run();
//		}
		
		ServerSocket listener = new ServerSocket(1963);
		try {
            System.out.println("The water rower server is running...");
            ExecutorService pool = Executors.newFixedThreadPool(9);
            while (true) {
                pool.execute(new Client(wrs, listener.accept()));
            }
        } catch( IOException e) {
        	System.err.println( "Error while listening: "+e.getLocalizedMessage());
        }
		
		wrs.close();
		listener.close();
		
	}
	

	
	private void close() {
		unregisterNotifier( serviceNotifier);
		
	}
}
