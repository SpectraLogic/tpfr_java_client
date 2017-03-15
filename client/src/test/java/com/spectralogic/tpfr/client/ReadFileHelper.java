package com.spectralogic.tpfr.client;

import org.apache.commons.io.IOUtils;

import java.io.IOException;

public class ReadFileHelper {
    public static String readFile(final String fileName){

        String result = "";

        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try {
            result = IOUtils.toString(classLoader.getResourceAsStream(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;

    }
}
