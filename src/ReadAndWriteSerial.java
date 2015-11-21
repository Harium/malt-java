import com.malt.serial.Serial;

public class ReadAndWriteSerial {
	
	public static void main(String[] args) throws Exception {
		final Serial serial = ReadSerial.initSerial();
		
		ReadSerial.waitAlive();
		System.out.println("Started");
		
		//Send a message
		serial.send("Hello Microcontroller.");
		//serial.send("2 on;");
	}
		
}
