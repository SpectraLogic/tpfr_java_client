package com.spectralogic.tpfr.api;

import com.spectralogic.tpfr.api.response.IndexStatusResponse;
import com.spectralogic.tpfr.api.response.OffsetsStatusResponse;
import com.spectralogic.tpfr.api.response.ReWrapResponse;
import com.spectralogic.tpfr.api.response.ReWrapStatusResponse;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Map;

class ServerServiceImpl implements ServerService {

    private final Logger LOG = LoggerFactory.getLogger(ServerServiceImpl.class);
    private final Api api;
    private final Retrofit retrofit;

    ServerServiceImpl(final Retrofit retrofit, final Api api) {
        this.retrofit = retrofit;
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
    public ReWrapResponse reWrap(final Map<String, String> params) throws IOException {
        final Response<ReWrapResponse> response = api.reWrap(params).execute();

        if (!response.isSuccessful()) {
            // Due to bad design in Marquis
            if (response.code() == 400 && response.message().equals("OK")) {
                return getErrorBody(response.errorBody(), ReWrapResponse.class);
            }
            LOG.error("reWrap failed ({}, {})", response.code(), response.message());
            return new ReWrapResponse("Unknown");
        }

        return response.body();
    }

    @Override
    public ReWrapStatusResponse reWrapStatus(final String targetFileName) throws IOException {
        final Response<ReWrapStatusResponse> response = api.reWrapStatus(targetFileName).execute();

        if (!response.isSuccessful()) {
            LOG.error("reWrapStatus failed ({}, {})", response.code(), response.message());
            return new ReWrapStatusResponse("Unknown");
        }

        return response.body();
    }

    private <T> T getErrorBody(final ResponseBody responseBody, final Class<T> clazz) throws IOException {
        final Converter<ResponseBody, T> errorConverter =
                retrofit.responseBodyConverter(clazz, new Annotation[0]);

        return errorConverter.convert(responseBody);
    }
}