package com.spectralogic.tpfr.api;

import com.spectralogic.tpfr.api.response.IndexStatusResponse;
import com.spectralogic.tpfr.api.response.OffsetsStatusResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Response;

import java.util.Map;

class ServerServiceImpl implements ServerService {

    private final Logger LOG = LoggerFactory.getLogger(ServerServiceImpl.class);
    private final Api api;

    ServerServiceImpl(final Api api) {
        this.api = api;
    }

    @Override
    public IndexStatusResponse indexFile(final String filePath) throws Exception {
        final Response<IndexStatusResponse> response = api.indexFile(filePath).execute();

        if (!response.isSuccessful()) {
            LOG.error("indexFile api call failed ({}, {})", response.code(), response.message());
            return new IndexStatusResponse("Unknown", response.code(), response.message());
        }

        return response.body();
    }

    @Override
    public IndexStatusResponse fileStatus(final String filePath) throws Exception {
        final Response<IndexStatusResponse> response = api.fileStatus(filePath).execute();

        if (!response.isSuccessful()) {
            LOG.error("fileStatus api call failed ({}, {})", response.code(), response.message());
            return new IndexStatusResponse("Unknown", response.code(), response.message());
        }

        return response.body();
    }

    @Override
    public OffsetsStatusResponse questionTimecode(final Map<String, String> params) throws Exception{
        final Response<OffsetsStatusResponse> response = api.questionTimecode(params).execute();

        if (!response.isSuccessful()) {
            LOG.error("indexFile failed ({}, {})", response.code(), response.message());
            return new OffsetsStatusResponse("Unknown");
        }

        return response.body();
    }
}