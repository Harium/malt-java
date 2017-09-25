package com.harium.malt.serial;

public interface SerialListener {
    void receive(String text);

    void error(Exception e);
}
