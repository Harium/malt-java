package com.malt.serial;

public interface SerialListener {
	public void receive(String text);
	public void error(Exception e);
}
