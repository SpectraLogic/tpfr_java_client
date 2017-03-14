package com.spectralogic.tpfr.api;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static okhttp3.OkHttpClient.Builder;

public class ServerServiceFactoryImpl implements ServerServiceFactory {

    private final Logger LOG = LoggerFactory.getLogger(ServerServiceFactoryImpl.class);
    private ServerService serverService;

    @Override
    public Optional<ServerService> getServerService() {
        return Optional.ofNullable(serverService);
    }

    @Override
    public ServerService createServerService(final String endpoint) {

        LOG.info("Creating a server service to: {}", endpoint);

        final Builder httpClient = new Builder();
        httpClient.addInterceptor(chain -> {
            final Request original = chain.request();

            final Request request = original.newBuilder()
                    .header("Content-Type", "application/xml")
                    .method(original.method(), original.body())
                    .build();
            try {
                return chain.proceed(request);
            } catch (final Exception e) {
                LOG.error("Failed to connect to the server...", e);
                return null;
            }
        });

        final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(LOG::debug);
        // set your desired log level
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        httpClient.addInterceptor(interceptor);

        final long defaultTimeOutValue = 1;
        httpClient.connectTimeout(defaultTimeOutValue, TimeUnit.MINUTES);
        httpClient.writeTimeout(defaultTimeOutValue, TimeUnit.MINUTES);
        httpClient.readTimeout(defaultTimeOutValue, TimeUnit.MINUTES);

        final OkHttpClient client = httpClient.build();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(endpoint)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .client(client)
                .build();


        final Api api = retrofit.create(Api.class);

        serverService = new ServerServiceImpl(retrofit, api);

        return serverService;
    }
}
