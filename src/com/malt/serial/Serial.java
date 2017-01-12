package com.malt.serial;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.TooManyListenersException;

public class Serial implements SerialPortEventListener {
	private static final String PORT_NAMES[] = { 
		"/dev/tty.usbserial-A9007UX1", // Mac OS X
		"/dev/ttyACM0", // Raspberry Pi
		"/dev/ttyACM1",
		"/dev/ttyUSB0", // Linux
		"COM3", // Windows
	};

	private SerialListener listener;

	private SerialPort serialPort;
	/** The port we're normally going to use. */

	/**
	 * A BufferedReader which will be fed by a InputStreamReader 
	 * converting the bytes into characters 
	 * making the displayed results codepage independent
	 */
	private BufferedReader input;
	/** The output stream to the port */
	private OutputStream output;
	/** Milliseconds to block while waiting for port open */
	private static final int TIME_OUT = 2000;
	/** Default bits per second for COM port. */
	private static final int DATA_RATE = 9600;

	public void init(final SerialListener listener) {

		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					initDebug(listener);
				} catch (PortInUseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnsupportedCommOperationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (TooManyListenersException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}
		}).start();
	}

	public void initDebug(SerialListener listener) throws PortInUseException, UnsupportedCommOperationException, IOException, TooManyListenersException {
		this.listener = listener;

		configureSerial();

		CommPortIdentifier portId = findPort();

		if (portId == null) {
			System.out.println("Could not find COM port.");
			return;
		}

		openPort(portId);

		// add event listeners
		serialPort.addEventListener(this);
		serialPort.notifyOnDataAvailable(true);
	}

	private void openPort(CommPortIdentifier portId) throws PortInUseException, UnsupportedCommOperationException, IOException {
		// open serial port, and use class name for the appName.
		serialPort = (SerialPort) portId.open(this.getClass().getName(),
				TIME_OUT);

		// set port parameters
		serialPort.setSerialPortParams(DATA_RATE,
				SerialPort.DATABITS_8,
				SerialPort.STOPBITS_1,
				SerialPort.PARITY_NONE);

		// open the streams
		input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
		output = serialPort.getOutputStream();
	}

	private CommPortIdentifier findPort() {
		CommPortIdentifier portId = null;
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

		//First, Find an instance of serial port as set in PORT_NAMES.
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
			for (String portName : PORT_NAMES) {
				if (currPortId.getName().equals(portName)) {
					portId = currPortId;
					break;
				}
			}
		}
		return portId;
	}

	private void configureSerial() {
		// the next line is for Raspberry Pi and 
		// gets us into the while loop and was suggested here was suggested http://www.raspberrypi.org/phpBB3/viewtopic.php?f=81&t=32186
		System.setProperty("gnu.io.rxtx.SerialPorts", "/dev/ttyACM0");
	}

	public void addListener(SerialPortEventListener listener) throws TooManyListenersException {
		serialPort.addEventListener(listener);
	}

	/**
	 * This should be called when you stop using the port.
	 * This will prevent port locking on platforms like Linux.
	 */
	public synchronized void close() {
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}

	public BufferedReader getInput() {
		return input;
	}

	public OutputStream getOutput() {
		return output;
	}

	/**
	 * Helper method to read line
	 * @throws IOException 
	 */
	public String receive() throws IOException {
		return input.readLine();
	}

	public void send(String msg) throws IOException {
		output.write(msg.getBytes());
	}

	/**
	 * Handle an event on the serial port. Read the data and print it.
	 */	
	public synchronized void serialEvent(SerialPortEvent oEvent) {
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				String inputLine = receive();
				listener.receive(inputLine);
				System.out.println(inputLine);
			} catch (Exception e) {
				listener.error(e);
				System.err.println(e.toString());
			}
		}
		// Ignore all the other eventTypes, but you should consider the other ones.
	}

}
