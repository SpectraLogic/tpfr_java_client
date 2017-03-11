package com.spectralogic.tpfr.client;

import com.spectralogic.tpfr.api.ServerService;
import com.spectralogic.tpfr.api.ServerServiceFactory;
import com.spectralogic.tpfr.api.ServerServiceFactoryImpl;
import com.spectralogic.tpfr.client.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

public class ClientImpl implements Client {
    private static final Logger LOG = LoggerFactory.getLogger(ClientImpl.class);

    private final ServerServiceFactory serverServiceFactory;

    public ClientImpl(final String endpoint){
        this.serverServiceFactory = new ServerServiceFactoryImpl();
        serverServiceFactory.createServerService(endpoint);
    }

    public ClientImpl(final ServerServiceFactory serverServiceFactoryImpl) {
        serverServiceFactory = serverServiceFactoryImpl;
    }

    public IndexStatus indexFile(final String filePath) throws Exception {
        final Optional<ServerService> serverServiceOptional = serverServiceFactory.getServerService();
        if (serverServiceOptional.isPresent()) {
            try {
                return new IndexStatus(serverServiceOptional.get().indexFile(filePath));
            } catch (final IOException e) {
                LOG.error("Received an exception", e);
                throw e;
            }
        } else {
            LOG.error("Failed to create server service");
            throw new Exception("Failed to create server service");
        }
    }

    @Override
    public IndexStatus fileStatus(final String filePath) throws Exception {
        final Optional<ServerService> serverServiceOptional = serverServiceFactory.getServerService();
        if (serverServiceOptional.isPresent()) {
            try {
                return new IndexStatus(serverServiceOptional.get().fileStatus(filePath));
            } catch (final IOException e) {
                LOG.error("Received an exception", e);
                throw e;
            }
        } else {
            LOG.error("Failed to create server service");
            throw new Exception("Failed to create server service");
        }
    }

    @Override
    public OffsetsStatus questionTimecode(final QuestionTimecodeParams params) throws Exception {
        final Optional<ServerService> serverServiceOptional = serverServiceFactory.getServerService();
        if (serverServiceOptional.isPresent()) {
            try {
                return new OffsetsStatus(serverServiceOptional.get().questionTimecode(params.getParams()));
            } catch (final IOException e) {
                LOG.error("Received an exception", e);
                throw e;
            }
        } else {
            LOG.error("Failed to create server service");
            throw new Exception("Failed to create server service");
        }
    }

    @Override
    public ReWrapResponse reWrap(final ReWrapParams params) throws Exception {
        final Optional<ServerService> serverServiceOptional = serverServiceFactory.getServerService();
        if (serverServiceOptional.isPresent()) {
            try {
                return new ReWrapResponse(serverServiceOptional.get().reWrap(params.getParams()));
            } catch (final IOException e) {
                LOG.error("Received an exception", e);
                throw e;
            }
        } else {
            LOG.error("Failed to create server service");
            throw new Exception("Failed to create server service");
        }
    }

    @Override
    public ReWrapStatus reWrapStatus(final String targetFileName) throws Exception {
        final Optional<ServerService> serverServiceOptional = serverServiceFactory.getServerService();
        if (serverServiceOptional.isPresent()) {
            try {
                return new ReWrapStatus(serverServiceOptional.get().reWrapStatus(targetFileName));
            } catch (final IOException e) {
                LOG.error("Received an exception", e);
                throw e;
            }
        } else {
            LOG.error("Failed to create server service");
            throw new Exception("Failed to create server service");
        }
    }
}
