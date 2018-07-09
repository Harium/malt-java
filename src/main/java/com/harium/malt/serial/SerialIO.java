package com.harium.malt.serial;

import java.io.IOException;

public interface SerialIO {
    void initAsync(SerialListener listener);

    void send(String msg) throws IOException;

    boolean isConnected();
}
