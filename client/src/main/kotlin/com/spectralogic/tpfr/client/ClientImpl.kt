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

package com.spectralogic.tpfr.client

import com.spectralogic.tpfr.api.ServerService
import com.spectralogic.tpfr.client.model.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import java.util.concurrent.ForkJoinPool
import java.util.function.Supplier

class ClientImpl constructor(private val serverService: ServerService, private val executor: Executor = ForkJoinPool.commonPool()) : Client {

    override fun indexFile(filePath: String): CompletableFuture<IndexStatus> {
        return CompletableFuture.supplyAsync<IndexStatus>(Supplier { IndexStatus.toIndexStatus(serverService.indexFile(filePath)) }, executor)
    }

    override fun fileStatus(filePath: String): CompletableFuture<IndexStatus> {
        return CompletableFuture.supplyAsync<IndexStatus>(Supplier { IndexStatus.toIndexStatus(serverService.fileStatus(filePath)) }, executor)
    }

    override fun questionTimecode(params: QuestionTimecodeParams): CompletableFuture<OffsetsStatus> {
        return CompletableFuture.supplyAsync<OffsetsStatus>(Supplier { OffsetsStatus.toOffsetsStatus(serverService.questionTimecode(params.params)) }, executor)
    }

    override fun reWrap(params: ReWrapParams): CompletableFuture<ReWrapResponse> {
        return CompletableFuture.supplyAsync<ReWrapResponse>(Supplier { ReWrapResponse.toReWrapResponse(serverService.reWrap(params.params)) }, executor)
    }

    override fun reWrapStatus(targetFileName: String): CompletableFuture<ReWrapStatus> {
        return CompletableFuture.supplyAsync<ReWrapStatus>(Supplier { ReWrapStatus.toReWrapStatus(serverService.reWrapStatus(targetFileName)) }, executor)
    }
}