package com.spectralogic.tpfr.api;

import com.spectralogic.tpfr.api.response.IndexStatus;
import com.spectralogic.tpfr.api.response.errors.GeneralErrorResponseException;

public interface ServerService {
    IndexStatus indexFile(final String filePath) throws GeneralErrorResponseException, Exception;
    IndexStatus fileStatus(final String filePath) throws GeneralErrorResponseException, Exception;
}