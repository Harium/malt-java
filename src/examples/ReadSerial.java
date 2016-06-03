package examples;
import com.malt.serial.Serial;
import com.malt.serial.SerialListener;

public class ReadSerial {
	
	public static void main(String[] args) throws Exception {
		initSerial();
		
		waitAlive();
		System.out.println("Started");
	}

	public static Serial initSerial() {
		final Serial serial = new Serial();
		serial.init(new SerialListener() {
			
			@Override
			public void receive(String text) {
				System.out.println(text);
			}
			
			@Override
			public void error(Exception e) {
				System.out.println(e.toString());
			}
		});
		
		return serial;
	}

	public static void waitAlive() {
		Thread t = new Thread() {
			public void run() {
				//the following line will keep this app alive for 1000 seconds,
				//waiting for events to occur and responding to them (printing incoming messages to console).
				try {Thread.sleep(1000000);} catch (InterruptedException ie) {}
			}
		};
		t.start();
	}
		
}
