package com.harium.malt.serial;

import org.junit.Assert;
import org.junit.Test;

public class SerialTest {

    private Serial serial;

    @Test
    public void testConstructorWithPort() {
        String port = "MY_PORT";
        serial = new Serial(port);
        Assert.assertEquals(port, serial.port);
    }

    @Test
    public void testConstructorWithBaudRate() {
        int baudRate = 9600;
        serial = new Serial(baudRate);
        Assert.assertEquals(baudRate, serial.baudRate);
    }

    @Test
    public void testConstructorWithPortAndBaudRate() {
        int baudRate = 9600;
        String port = "MY_PORT";
        serial = new Serial(port, baudRate);
        Assert.assertEquals(baudRate, serial.baudRate);
    }

}
