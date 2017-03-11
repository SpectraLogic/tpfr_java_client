package com.spectralogic.tpfr.api;

import com.spectralogic.tpfr.api.response.IndexStatusResponse;
import com.spectralogic.tpfr.api.response.OffsetsStatusResponse;
import com.spectralogic.tpfr.api.response.ReWrapResponse;
import retrofit2.http.QueryMap;

import java.util.Map;

public interface ServerService {
    IndexStatusResponse indexFile(final String filePath) throws Exception;
    IndexStatusResponse fileStatus(final String filePath) throws Exception;
    OffsetsStatusResponse questionTimecode(Map<String, String> params) throws Exception;
    ReWrapResponse reWrap(@QueryMap Map<String, String> params) throws Exception;

}