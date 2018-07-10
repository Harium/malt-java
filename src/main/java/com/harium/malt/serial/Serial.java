package com.harium.malt.serial;

import com.fazecast.jSerialComm.SerialPort;

import java.io.IOException;
import java.io.InputStream;

public class Serial implements SerialIO {

    public static final String END = "\n";

    protected String port = "";
    protected int baudRate = 9600;
    protected int timeout = 100;

    protected SerialPort comPort;
    protected SerialListener listener;

    // Input
    private InputStream in;
    private String receive = "";


    public Serial() {
        super();
    }

    public Serial(String port) {
        super();
        this.port = port;
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

    @Override
    public void initAsync(final SerialListener listener) {
        new Thread(new Runnable() {
            public void run() {
                init(listener);
            }
        }).start();
    }

    public void init(SerialListener listener) {
        this.listener = listener;

        if (!port.isEmpty()) {
            comPort = SerialPort.getCommPort(port);
        } else {
            comPort = SerialPort.getCommPorts()[0];
        }
        comPort.setBaudRate(baudRate);
        comPort.openPort();
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, timeout, 0);
        in = comPort.getInputStream();
        try {
            while (true) {
                receive();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        comPort.closePort();
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receive() throws InterruptedException, IOException {
        if (!isConnected()) {
            return;
        }
        int available = 0;
        while (available == 0) {
            available = in.available();
            Thread.sleep(20);
        }

        if (available < 0) {
            return;
        }

        byte[] readBuffer = new byte[available];
        in.read(readBuffer);

        receive += new String(readBuffer);
        if (receive.endsWith(END)) {
            receive = receive.substring(0, receive.length() - END.length());
            listener.receive(receive);
            // Clear Buffer
            receive = "";
        }
    }

    @Override
    public void send(String msg) throws IOException {
        if (!isConnected()) {
            return;
        }
        byte[] bytes = msg.getBytes();
        comPort.getOutputStream().write(bytes);
        comPort.getOutputStream().flush();
    }

    @Override
    public boolean isConnected() {
        return comPort.isOpen();
    }
}
