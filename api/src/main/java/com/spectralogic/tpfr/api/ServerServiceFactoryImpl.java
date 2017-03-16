package com.spectralogic.tpfr.api;

import com.google.common.base.Strings;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;

import static okhttp3.OkHttpClient.Builder;

public class ServerServiceFactoryImpl implements ServerServiceFactory {

    private final Logger LOG = LoggerFactory.getLogger(ServerServiceFactoryImpl.class);
    private final String endpoint;
    private final String proxyHost;
    private final int proxyPort;

    public ServerServiceFactoryImpl(final String endpointUrl) {
        this(endpointUrl, "", 0);
    }

    public ServerServiceFactoryImpl(final String endpoint, final String proxyHost, final int proxyPort) {
        this.endpoint = endpoint;
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
    }

    @Override
    public ServerService createServerService() {

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

        final OkHttpClient client;
        if (Strings.isNullOrEmpty(proxyHost)) {
            client = httpClient.build();
        } else {
            final Proxy proxy = new Proxy(Proxy.Type.HTTP,  new InetSocketAddress(proxyHost, proxyPort));
            client = new OkHttpClient.Builder().proxy(proxy).build();
        }

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(endpoint)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .client(client)
                .build();


        final Api api = retrofit.create(Api.class);

        return new ServerServiceImpl(retrofit, api);
    }
}
