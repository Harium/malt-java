package examples;

import gnu.io.CommPortIdentifier;

import java.util.Enumeration;

import com.malt.helper.NativeLoader;

public class SerialPortFinder {

	static {
		NativeLoader.loadLibrary();
	}
	
	public static void main(String[] args) {

		System.out.println("Searching ports...");

		CommPortIdentifier serialPortId;

		Enumeration enumComm = CommPortIdentifier.getPortIdentifiers();

		while(enumComm.hasMoreElements()) {
			serialPortId = (CommPortIdentifier) enumComm.nextElement();
			if(serialPortId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				System.out.println(serialPortId.getName());
			}
		}

		System.out.println("Complete...");
	}

}
