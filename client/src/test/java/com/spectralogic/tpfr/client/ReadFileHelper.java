package com.spectralogic.tpfr.client;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

final class ReadFileHelper {
    private static final Logger LOG = LoggerFactory.getLogger(ClientImpl.class);

    static String readFile(final String fileName){

        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try (final InputStream resourceAsStream = classLoader.getResourceAsStream(fileName)) {
            return IOUtils.toString(resourceAsStream);
        } catch (final IOException e) {
            LOG.error("Failed to read xml from resource", e);
        }

        return "";
    }
}
