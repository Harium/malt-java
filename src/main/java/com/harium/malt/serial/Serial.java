package com.harium.malt.serial;

import gnu.io.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.TooManyListenersException;

public class Serial implements SerialPortEventListener {

    public static boolean DEBUG = false;

    private static final String PORT_NAMES[] = {
            "/dev/tty\\.usbserial-.+", // Mac OS X
            "/dev/ttyACM\\d+", // Raspberry Pi
            "/dev/ttyUSB\\d+", // Linux
            "COM\\d+", // Windows
    };

    private SerialListener listener;

    /**
     * The port we're normally going to use.
     */
    private SerialPort serialPort;

    /**
     * A BufferedReader which will be fed by a InputStreamReader
     * converting the bytes into characters
     * making the displayed results codepage independent
     */

    protected BufferedReader input;
    /**
     * The output stream to the port
     */
    protected OutputStream output;

    /**
     * Milliseconds to block while waiting for port open
     */
    private static final int TIME_OUT = 5000;

    /**
     * Default bits per second for COM port.
     */
    private static final int DATA_RATE = 9600;

    protected String port = "";
    protected int baudRate = DATA_RATE;

    private boolean connected = false;

    public Serial() {
        super();
    }

    public Serial(String port, int baudRate) {
        super();
        this.port = port;
        this.baudRate = baudRate;
    }

    public Serial(int baudRate) {
        super();
        this.baudRate = baudRate;
    }

    public void initAsync(final SerialListener listener) {
        new Thread(new Runnable() {

            public void run() {
                try {
                    init(listener);
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

    public void init(SerialListener listener) throws PortInUseException, UnsupportedCommOperationException, IOException, TooManyListenersException {
        this.listener = listener;

        configureSerial();

        CommPortIdentifier portId = findPort();

        if (portId == null) {
            System.out.println("Could not find COM port.");
            return;
        }

        openPort(portId);

        // Add event listeners
        serialPort.addEventListener(this);
        serialPort.notifyOnDataAvailable(true);
    }

    private void openPort(CommPortIdentifier portId) throws PortInUseException, UnsupportedCommOperationException, IOException {
        // Open serial port, and use class name for the appName.
        serialPort = (SerialPort) portId.open(this.getClass().getName(),
                TIME_OUT);

        // Set port parameters
        serialPort.setSerialPortParams(baudRate,
                SerialPort.DATABITS_8,
                SerialPort.STOPBITS_1,
                SerialPort.PARITY_NONE);

        // Open the streams
        input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
        output = serialPort.getOutputStream();

        // Success
        connected = true;
    }

    /* package */ CommPortIdentifier findPort() {
        CommPortIdentifier portId = null;
        Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

        if (!port.isEmpty()) {
            while (portEnum.hasMoreElements()) {
                CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
                if (port.equals(currPortId.getName())) {
                    portId = currPortId;
                }
            }
        } else {
            findPort(portEnum, PORT_NAMES);
        }
        return portId;
    }

    /* package */
    static CommPortIdentifier findPort(Enumeration<CommPortIdentifier> portEnum, String[] regexList) {
        CommPortIdentifier portId = null;
        // First, find an instance of serial port as set in PORT_NAMES.
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier currPortId = portEnum.nextElement();
            for (String regex : regexList) {
                if (currPortId.getName().matches(regex)) {
                    portId = currPortId;
                    break;
                }
            }
        }

        return portId;
    }

    private void configureSerial() {
        // The next line is for Raspberry Pi and
        // Gets us into the while loop and was suggested here:
        // http://www.raspberrypi.org/phpBB3/viewtopic.php?f=81&t=32186
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

    /**
     * Helper method to read line
     *
     * @throws IOException
     */
    public void receive() throws IOException {
        String str;
        while ((str = input.readLine()) != null) {
            listener.receive(str);
            if (DEBUG) {
                System.out.println(str);
            }
        }
    }

    public void send(String msg) throws IOException {
        // Ignore send call if not connected
        if (!connected) {
            return;
        }
        output.write(msg.getBytes());
    }

    /**
     * Handle an event on the serial port. Read the data and print it.
     */
    public synchronized void serialEvent(SerialPortEvent oEvent) {
        switch (oEvent.getEventType()) {
            case SerialPortEvent.DATA_AVAILABLE:
                try {
                    receive();
                } catch (Exception e) {
                    listener.error(e);
                    System.err.println(e.toString());
                }
                break;
            // Ignore all the other eventTypes, but you should consider the other ones.
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
            case SerialPortEvent.CTS:
            case SerialPortEvent.DSR:
            case SerialPortEvent.RI:
            case SerialPortEvent.CD:
            case SerialPortEvent.OE:
            case SerialPortEvent.PE:
            case SerialPortEvent.FE:
            case SerialPortEvent.BI:
            default:
                break;
        }
    }

}
