package com.harium.malt.serial;

import org.junit.Assert;
import org.junit.Test;

public class SerialTest {

    @Test
    public void testInit() {
        Serial serial = new Serial(9600);
        Assert.assertEquals(9600, serial.baudRate);
    }

}
