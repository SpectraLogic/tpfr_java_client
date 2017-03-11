package com.spectralogic.tpfr.api;

import com.spectralogic.tpfr.api.response.IndexResult;
import com.spectralogic.tpfr.api.response.IndexStatus;
import com.spectralogic.tpfr.api.response.OffsetsResult;
import com.spectralogic.tpfr.api.response.OffsetsStatus;
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
    public IndexStatus indexFile(final String filePath) throws Exception {
        final Response<IndexStatus> response = api.indexFile(filePath).execute();

        if (!response.isSuccessful()) {
            LOG.error("indexFile api call failed ({}, {})", response.code(), response.message());
            return new IndexStatus(IndexResult.Unknown);
        }

        return response.body();
    }

    @Override
    public IndexStatus fileStatus(final String filePath) throws Exception {
        final Response<IndexStatus> response = api.fileStatus(filePath).execute();

        if (!response.isSuccessful()) {
            LOG.error("fileStatus api call failed ({}, {})", response.code(), response.message());
            return new IndexStatus(IndexResult.Unknown);
        }

        return response.body();
    }

    @Override
    public OffsetsStatus questionTimecode(final Map<String, String> params) throws Exception{
        final Response<OffsetsStatus> response = api.questionTimecode(params).execute();

        if (!response.isSuccessful()) {
            LOG.error("indexFile failed ({}, {})", response.code(), response.message());
            return new OffsetsStatus(OffsetsResult.Unknown);
        }

        return response.body();
    }
}