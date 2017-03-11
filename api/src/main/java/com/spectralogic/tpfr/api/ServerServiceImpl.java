package com.spectralogic.tpfr.api;

import com.spectralogic.tpfr.api.response.IndexStatusResponse;
import com.spectralogic.tpfr.api.response.OffsetsStatusResponse;
import com.spectralogic.tpfr.api.response.ReWrapResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Response;
import retrofit2.http.QueryMap;

import java.io.IOException;
import java.util.Map;

class ServerServiceImpl implements ServerService {

    private final Logger LOG = LoggerFactory.getLogger(ServerServiceImpl.class);
    private final Api api;

    ServerServiceImpl(final Api api) {
        this.api = api;
    }

    @Override
    public IndexStatusResponse indexFile(final String filePath) throws IOException {
        final Response<IndexStatusResponse> response = api.indexFile(filePath).execute();

        if (!response.isSuccessful()) {
            LOG.error("indexFile api call failed ({}, {})", response.code(), response.message());
            return new IndexStatusResponse("Unknown", response.code(), response.message());
        }

        return response.body();
    }

    @Override
    public IndexStatusResponse fileStatus(final String filePath) throws IOException {
        final Response<IndexStatusResponse> response = api.fileStatus(filePath).execute();

        if (!response.isSuccessful()) {
            LOG.error("fileStatus api call failed ({}, {})", response.code(), response.message());
            return new IndexStatusResponse("Unknown", response.code(), response.message());
        }

        return response.body();
    }

    @Override
    public OffsetsStatusResponse questionTimecode(final Map<String, String> params) throws IOException {
        final Response<OffsetsStatusResponse> response = api.questionTimecode(params).execute();

        if (!response.isSuccessful()) {
            LOG.error("questionTimecode failed ({}, {})", response.code(), response.message());
            return new OffsetsStatusResponse("Unknown");
        }

        return response.body();
    }

    @Override
    public ReWrapResponse reWrap(@QueryMap final Map<String, String> params) throws IOException {
        final Response<ReWrapResponse> response = api.reWrap(params).execute();

        if (!response.isSuccessful()) {
            LOG.error("reWrap failed ({}, {})", response.code(), response.message());
            return new ReWrapResponse("Unknown");
        }

        return response.body();
    }
}