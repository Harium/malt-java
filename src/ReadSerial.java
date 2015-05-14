import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import com.malt.serial.SerialReader;

public class ReadSerial {
	
	public static void main(String[] args) throws Exception {
		final SerialReader reader = new SerialReader();
		reader.init(new SerialPortEventListener() {
			
			/**
			 * Handle an event on the serial port. Read the data and print it.
			 */			
			public synchronized void serialEvent(SerialPortEvent oEvent) {
				if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
					try {
						String inputLine = reader.getInput().readLine();
						System.out.println(inputLine);					
					} catch (Exception e) {
						System.err.println(e.toString());
					}
				}
				// Ignore all the other eventTypes, but you should consider the other ones.
			}
		});
		
		Thread t = new Thread() {
			public void run() {
				//the following line will keep this app alive for 1000 seconds,
				//waiting for events to occur and responding to them (printing incoming messages to console).
				try {Thread.sleep(1000000);} catch (InterruptedException ie) {}
			}
		};
		t.start();
		System.out.println("Started");
	}
		
}
