/*
 * ***************************************************************************
 *   Copyright 2016-2017 Spectra Logic Corporation. All Rights Reserved.
 *   Licensed under the Apache License, Version 2.0 (the "License"). You may not use
 *   this file except in compliance with the License. A copy of the License is located at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   or in the "license" file accompanying this file.
 *   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 *   CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations under the License.
 * ***************************************************************************
 */

package com.spectralogic.tpfr.api;

import com.spectralogic.tpfr.api.response.*;

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
    public IndexStatusResponse indexFile(final String filePath, final String indexId) {
        final Response<IndexStatusResponse> response;
        try {
            response = api.indexFile(filePath, indexId).execute();
        } catch (final IOException e) {
            LOG.error("indexFile api call failed with exception", e);
            return new IndexStatusResponse("Exception", e);
        }

        if (!response.isSuccessful()) {
            LOG.error("indexFile api call failed ({}, {})", response.code(), response.message());
            return new IndexStatusResponse("Unknown", String.valueOf(response.code()), response.message());
        }

        return response.body();
    }

    @Override
    public IndexStatusResponse fileStatus(final String indexId) {
        final Response<IndexStatusResponse> response;
        try {
            response = api.fileStatus(indexId).execute();
        } catch (final IOException e) {
            LOG.error("fileStatus api call failed with exception", e);
            return new IndexStatusResponse("Exception", e);
        }

        if (!response.isSuccessful()) {
            LOG.error("fileStatus api call failed ({}, {})", response.code(), response.message());
            return new IndexStatusResponse("Unknown", String.valueOf(response.code()), response.message());
        }

        return response.body();
    }

    @Override
    public OffsetsStatusResponse questionTimecode(final Map<String, String> params) {
        final Response<OffsetsStatusResponse> response;
        try {
            response = api.questionTimecode(params).execute();
        } catch (final IOException e) {
            LOG.error("questionTimecode api call failed with exception", e);
            return new OffsetsStatusResponse("Exception", e);
        }

        if (!response.isSuccessful()) {
            LOG.error("questionTimecode failed ({}, {})", response.code(), response.message());
            return new OffsetsStatusResponse("Unknown", String.valueOf(response.code()), response.message());
        }

        return response.body();
    }

    @Override
    public ReWrapResponse reWrap(final Map<String, String> params) {
        final Response<ReWrapResponse> response;
        try {
            response = api.reWrap(params).execute();
        } catch (final IOException e) {
            LOG.error("reWrap api call failed with exception", e);
            return new ReWrapResponse("Exception", e);
        }

        if (!response.isSuccessful()) {
            // Due to bad design in Marquis
            if (response.code() == 400 && response.message().equals("OK")) {
                try {
                    return getErrorBody(response.errorBody(), ReWrapResponse.class);
                } catch (final IOException e) {
                    LOG.error("Failed to get error body for reWrap api call", e);
                    return new ReWrapResponse("Exception", e);
                }
            }
            LOG.error("reWrap failed ({}, {})", response.code(), response.message());
            return new ReWrapResponse("Unknown", String.valueOf(response.code()), response.message());
        }

        return response.body();
    }

    @Override
    public ReWrapStatusResponse reWrapStatus(final String targetFileName) {
        final Response<ReWrapStatusResponse> response;
        try {
            response = api.reWrapStatus(targetFileName).execute();
        } catch (final IOException e) {
            LOG.error("reWrapStatus api call failed with exception", e);
            return new ReWrapStatusResponse("Exception", e);
        }

        if (!response.isSuccessful()) {
            LOG.error("reWrapStatus failed ({}, {})", response.code(), response.message());
            return new ReWrapStatusResponse("Unknown", String.valueOf(response.code()), response.message());
        }

        return response.body();
    }


    private <T> T getErrorBody(final ResponseBody responseBody, final Class<T> clazz) throws IOException {
        final Converter<ResponseBody, T> errorConverter =
                retrofit.responseBodyConverter(clazz, new Annotation[0]);

        return errorConverter.convert(responseBody);
    }
}