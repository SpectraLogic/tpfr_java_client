package com.spectralogic.tpfr.api;

import com.spectralogic.tpfr.api.response.IndexStatusResponse;
import com.spectralogic.tpfr.api.response.OffsetsStatusResponse;
import com.spectralogic.tpfr.api.response.ReWrapResponse;
import retrofit2.http.QueryMap;

import java.io.IOException;
import java.util.Map;

public interface ServerService {
    IndexStatusResponse indexFile(final String filePath) throws IOException;
    IndexStatusResponse fileStatus(final String filePath) throws IOException;
    OffsetsStatusResponse questionTimecode(Map<String, String> params) throws IOException;
    ReWrapResponse reWrap(@QueryMap Map<String, String> params) throws IOException;

}