package examples;

import java.util.Enumeration;

import gnu.io.CommPortIdentifier;

public class SerialPortFinder {

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
