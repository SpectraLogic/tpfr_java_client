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

package com.spectralogic.tpfr.client;

import com.spectralogic.tpfr.api.ServerService;
import com.spectralogic.tpfr.client.model.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ClientImpl implements Client {

    private final ServerService serverService;
    private final Executor executor;

    public ClientImpl(final ServerService serverService) {
        this(serverService, Executors.newSingleThreadExecutor());
    }

    public ClientImpl(final ServerService serverService, final Executor executor) {
        this.serverService = serverService;
        this.executor = executor;
    }

    @Override
    public CompletableFuture<IndexStatus> indexFile(final String filePath) {
        return CompletableFuture.supplyAsync(() -> IndexStatus.Companion.toIndexStatus(serverService.indexFile(filePath)), executor);
    }

    @Override
    public CompletableFuture<IndexStatus> fileStatus(final String filePath) {
        return CompletableFuture.supplyAsync(() -> IndexStatus.Companion.toIndexStatus(serverService.fileStatus(filePath)), executor);
    }

    @Override
    public CompletableFuture<OffsetsStatus> questionTimecode(final QuestionTimecodeParams params) {
        return CompletableFuture.supplyAsync(() -> OffsetsStatus.Companion.toOffsetsStatus(serverService.questionTimecode(params.getParams())), executor);
    }

    @Override
    public CompletableFuture<ReWrapResponse> reWrap(final ReWrapParams params) {
        return CompletableFuture.supplyAsync(() -> ReWrapResponse.Companion.toReWrapResponse(serverService.reWrap(params.getParams())), executor);
    }

    @Override
    public CompletableFuture<ReWrapStatus> reWrapStatus(final String targetFileName) {
        return CompletableFuture.supplyAsync(() -> ReWrapStatus.Companion.toReWrapStatus(serverService.reWrapStatus(targetFileName)), executor);
    }
}