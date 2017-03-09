package com.spectralogic.tpfr.api;

import com.spectralogic.tpfr.api.response.IndexStatus;
import com.spectralogic.tpfr.api.response.errors.GeneralError;
import com.spectralogic.tpfr.api.response.errors.GeneralErrorResponseException;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.lang.annotation.Annotation;

class ServerServiceImpl implements ServerService {

    private final Logger LOG = LoggerFactory.getLogger(ServerServiceImpl.class);
    private final Api api;
    private final Retrofit retrofit;

    ServerServiceImpl(final Retrofit retrofit, final Api api) {
        this.retrofit = retrofit;
        this.api = api;
       }

    @Override
    public IndexStatus indexFile(final String filePath) throws Exception, GeneralErrorResponseException {
        final Response<IndexStatus> response = api.indexFile(filePath).execute();

        if (!response.isSuccessful()) {
            final GeneralError generalError = getErrorBody(response.errorBody(), GeneralError.class);
            throw new GeneralErrorResponseException(generalError);
        }

        return response.body();
    }



    private <T> T getErrorBody(final ResponseBody responseBody, final Class<T> clazz) throws Exception {
        final Converter<ResponseBody, T> errorConverter =
                retrofit.responseBodyConverter(clazz, new Annotation[0]);

        return errorConverter.convert(responseBody);
    }
}