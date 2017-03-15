package com.spectralogic.tpfr.api;

public interface ServerServiceFactory {
    ServerService createServerService(final String endpoint);
}