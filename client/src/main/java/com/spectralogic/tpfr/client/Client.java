package com.spectralogic.tpfr.client;

import com.spectralogic.tpfr.api.response.IndexStatus;

public interface Client {
    IndexStatus indexFile(final String filePath) throws Exception;
}
