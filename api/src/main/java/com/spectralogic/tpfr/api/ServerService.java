package com.spectralogic.tpfr.api;

import com.spectralogic.tpfr.api.response.IndexStatusResponse;
import com.spectralogic.tpfr.api.response.OffsetsStatusResponse;
import com.spectralogic.tpfr.api.response.ReWrapResponse;
import com.spectralogic.tpfr.api.response.ReWrapStatusResponse;

import java.io.IOException;
import java.util.Map;

public interface ServerService {
    IndexStatusResponse indexFile(final String filePath) throws IOException;
    IndexStatusResponse fileStatus(final String filePath) throws IOException;
    OffsetsStatusResponse questionTimecode(final Map<String, String> params) throws IOException;
    ReWrapResponse reWrap(final Map<String, String> params) throws IOException;
    ReWrapStatusResponse reWrapStatus(final String targetFileName) throws IOException;
}