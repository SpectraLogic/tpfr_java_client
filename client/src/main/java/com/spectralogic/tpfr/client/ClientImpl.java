package com.spectralogic.tpfr.client;

import com.spectralogic.tpfr.api.ServerService;
import com.spectralogic.tpfr.api.ServerServiceFactory;
import com.spectralogic.tpfr.api.ServerServiceFactoryImpl;
import com.spectralogic.tpfr.api.response.IndexStatus;
import com.spectralogic.tpfr.api.response.errors.GeneralError;
import com.spectralogic.tpfr.api.response.errors.GeneralErrorResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class ClientImpl implements Client {
    private static final Logger LOG = LoggerFactory.getLogger(ClientImpl.class);

    private final ServerServiceFactory serverServiceFactory;

    public ClientImpl(final String endpoint){
        this.serverServiceFactory = new ServerServiceFactoryImpl();
        serverServiceFactory.createServerService(endpoint);
    }

    public IndexStatus indexFile(final String filePath) throws Exception {
        final Optional<ServerService> serverServiceOptional = serverServiceFactory.getServerService();
        if (serverServiceOptional.isPresent()) {
            try {
                return serverServiceOptional.get().indexFile(filePath);
            } catch (final GeneralErrorResponseException e) {
                final GeneralError generalError = e.getGeneralError();
                LOG.error("Failed to index file ({}, {})", generalError.getCode(), generalError.getMessage());
                throw new Exception(String.format("Failed to index file ({}, {})", generalError.getCode(), generalError.getMessage()));
            } catch (final Exception e) {
                LOG.error("Received an exception", e);
                throw e;
            }
        } else {
            LOG.error("Failed to create server service");
            throw new Exception("Failed to create server service");
        }
    }
}
