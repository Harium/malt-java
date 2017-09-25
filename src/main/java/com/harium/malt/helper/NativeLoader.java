package com.harium.malt.helper;

import gnu.io.Config;

import java.io.File;
import java.lang.reflect.Field;

public class NativeLoader {

    public static void loadLibrary() {
        String osFolder = buildOSFolder();
        String archFolder = buildArchFolder();

        String path = System.getProperty("user.dir");
        String nativesPath = path + "/libs/natives/" + osFolder + File.separator + archFolder + File.separator;

        if (Config.DEBUG) {
            System.out.println("Loading libraries from: " + nativesPath);
        }

        System.setProperty("java.library.path", nativesPath);

        //set sys_paths to null
        try {
            Field sysPathsField = ClassLoader.class.getDeclaredField("sys_paths");
            sysPathsField.setAccessible(true);
            sysPathsField.set(null, null);
        } catch (NoSuchFieldException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static String buildOSFolder() {
        OS os = OSDiscover.getOS();

        switch (os) {
            case LINUX:
                return "unix";
            case MAC:
                return "mac";
            case WINDOWS:
                return "windows";
            default:
                return "";
        }
    }

    private static String buildArchFolder() {
        Architecture arch = OSDiscover.getArchitecture();

        String archFolder = "";

        if (Architecture.X32 == arch) {
            archFolder = "i586";
        } else if (Architecture.X64 == arch) {
            archFolder = "x64";
        }
        return archFolder;
    }

}
