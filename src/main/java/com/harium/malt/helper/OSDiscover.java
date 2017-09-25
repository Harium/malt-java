package com.harium.malt.helper;

public class OSDiscover {

	public static Architecture getArchitecture() {
		String arch = System.getProperty("os.arch");
		
		if (arch.endsWith("86")) {
			return Architecture.X32;
		} else if (arch.endsWith("64")) {
			return Architecture.X64;
		} else {
			return Architecture.UNKNOWN;
		}
	}
	
	public static OS getOS() {
		String os = System.getProperty("os.name");
		
		if (isWindows(os)) {
			return OS.WINDOWS;
		} else if (isMac(os)) {
			return OS.MAC;
		} else if (isUnix(os)) {
			return OS.LINUX;
		} else if (isSolaris(os)) {
			return OS.SOLARIS;
		} else {
			return OS.UNKNOWN;
		}
	}
	
	private static boolean isWindows(String os) {
		return (os.indexOf("win") >= 0);
	}

	private static boolean isMac(String os) {
		return (os.indexOf("mac") >= 0);
	}

	private static boolean isUnix(String os) {
		return (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0 || os.indexOf("aix") > 0 );		
	}

	private static boolean isSolaris(String os) {
		return (os.indexOf("sunos") >= 0);
	}
	
}
