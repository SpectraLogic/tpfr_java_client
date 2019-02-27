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

public class ServerServiceFactoryImpl implements ServerServiceFactory {

    private final static Logger LOG = LoggerFactory.getLogger(ServerServiceFactoryImpl.class);
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

        final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(chain -> {
            final Request original = chain.request();

            final Request request = original.newBuilder()
                    .header("Content-Type", "application/xml")
                    .method(original.method(), original.body())
                    .build();
            try {
                return chain.proceed(request);
            } catch (final Exception e) {
                LOG.error("Encountered an error with the network", e);
                throw e;
            }
        });

        final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(LOG::debug);
        // set your desired log level
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        httpClient.addInterceptor(interceptor);

        final long defaultTimeOutValue = 1;
        httpClient.connectTimeout(defaultTimeOutValue, TimeUnit.MINUTES);
        httpClient.writeTimeout(defaultTimeOutValue, TimeUnit.MINUTES);
        httpClient.readTimeout(defaultTimeOutValue, TimeUnit.HOURS);

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
