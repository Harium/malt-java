package com.harium.malt.serial;

import gnu.io.CommPortIdentifier;
import gnu.io.Config;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SerialTest {

    @Test
    public void testInit() {
        Serial serial = new Serial(9600);
        Assert.assertEquals(9600, serial.baudRate);
    }

    @Test
    public void testFindWindowsPortWithRegex() {
        // Windows port
        CommPortIdentifier windowsPort = mock(CommPortIdentifier.class);
        when(windowsPort.getName()).thenReturn("COM3");

        List<CommPortIdentifier> portList = new ArrayList<>();
        portList.add(windowsPort);
        Enumeration ports = Collections.enumeration(portList);

        CommPortIdentifier found = Serial.findPort(ports, new String[]{"COM\\d+"});
        Assert.assertEquals(windowsPort, found);
    }

    @Test
    public void testMacPortWithRegex() {
        // MacOS port
        CommPortIdentifier macPort = mock(CommPortIdentifier.class);
        when(macPort.getName()).thenReturn("/dev/tty.usbserial-mydevice");

        List<CommPortIdentifier> portList = new ArrayList<>();
        portList.add(macPort);
        Enumeration ports = Collections.enumeration(portList);

        CommPortIdentifier found = Serial.findPort(ports, new String[]{"/dev/tty.usbserial*"});
        Assert.assertEquals(macPort, found);
    }

    @Test
    public void testRaspberryPiPortWithRegex() {
        // Raspberry Pi port
        CommPortIdentifier macPort = mock(CommPortIdentifier.class);
        when(macPort.getName()).thenReturn("/dev/ttyACM0");

        List<CommPortIdentifier> portList = new ArrayList<>();
        portList.add(macPort);
        Enumeration ports = Collections.enumeration(portList);

        CommPortIdentifier found = Serial.findPort(ports, new String[]{"/dev/ttyACM\\d+"});
        Assert.assertEquals(macPort, found);
    }

    @Test
    public void testLinusPiPortWithRegex() {
        // Linux port
        CommPortIdentifier macPort = mock(CommPortIdentifier.class);
        when(macPort.getName()).thenReturn("/dev/ttyUSB0");

        List<CommPortIdentifier> portList = new ArrayList<>();
        portList.add(macPort);
        Enumeration ports = Collections.enumeration(portList);

        CommPortIdentifier found = Serial.findPort(ports, new String[]{"/dev/ttyUSB\\d+"});
        Assert.assertEquals(macPort, found);
    }

}
