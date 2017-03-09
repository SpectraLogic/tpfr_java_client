package com.spectralogic.tpfr.api;

import java.util.*;

public interface ServerServiceFactory {
    Optional<ServerService> getServerService();
    ServerService createServerService(final String endpoint);
}