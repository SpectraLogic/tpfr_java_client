package com.spectralogic.tpfr.client;

import com.spectralogic.tpfr.api.ServerService;
import com.spectralogic.tpfr.api.ServerServiceFactoryImpl;
import com.spectralogic.tpfr.client.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ClientImpl implements Client {
    private static final Logger LOG = LoggerFactory.getLogger(ClientImpl.class);

    private final ServerService serverService;

    public ClientImpl(final String endpoint){
        final ServerServiceFactoryImpl serverServiceFactory = new ServerServiceFactoryImpl();
        this.serverService = serverServiceFactory.createServerService(endpoint);
    }

    public IndexStatus indexFile(final String filePath) throws Exception {
        try {
            return new IndexStatus(serverService.indexFile(filePath));
        } catch (final IOException e) {
            LOG.error("Received an exception", e);
            throw e;
        }
    }

    @Override
    public IndexStatus fileStatus(final String filePath) throws Exception {
        try {
            return new IndexStatus(serverService.fileStatus(filePath));
        } catch (final IOException e) {
            LOG.error("Received an exception", e);
            throw e;
        }
    }

    @Override
    public OffsetsStatus questionTimecode(final QuestionTimecodeParams params) throws Exception {
        try {
            return new OffsetsStatus(serverService.questionTimecode(params.getParams()));
        } catch (final IOException e) {
            LOG.error("Received an exception", e);
            throw e;
        }
    }

    @Override
    public ReWrapResponse reWrap(final ReWrapParams params) throws Exception {
        try {
            return new ReWrapResponse(serverService.reWrap(params.getParams()));
        } catch (final IOException e) {
            LOG.error("Received an exception", e);
            throw e;
        }
    }

    @Override
    public ReWrapStatus reWrapStatus(final String targetFileName) throws Exception {
        try {
            return new ReWrapStatus(serverService.reWrapStatus(targetFileName));
        } catch (final IOException e) {
            LOG.error("Received an exception", e);
            throw e;
        }
    }
}
