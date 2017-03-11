package com.spectralogic.tpfr.api;

import com.spectralogic.tpfr.api.response.IndexStatus;
import com.spectralogic.tpfr.api.response.OffsetsStatus;

import java.util.Map;

public interface ServerService {
    IndexStatus indexFile(final String filePath) throws Exception;
    IndexStatus fileStatus(final String filePath) throws Exception;
    OffsetsStatus questionTimecode(Map<String, String> params) throws Exception;
}