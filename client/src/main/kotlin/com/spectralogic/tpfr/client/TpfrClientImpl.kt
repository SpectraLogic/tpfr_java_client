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
import io.reactivex.Single
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.asCoroutineDispatcher
import kotlinx.coroutines.experimental.rx2.rxSingle
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.ForkJoinPool

class TpfrClientImpl constructor(private val serverService: ServerService, executor: Executor = ForkJoinPool.commonPool()) : TpfrClient {

    private val coroutineDispatcher = executor.asCoroutineDispatcher()

    override fun indexFile(filePath: String, indexId: UUID): Single<IndexStatus> {
        return GlobalScope.rxSingle(coroutineDispatcher, {
            IndexStatus.toIndexStatus(serverService.indexFile(filePath, indexId.toString()))
        })
    }

    override fun fileStatus(indexId: UUID): Single<IndexStatus> {
        return GlobalScope.rxSingle(coroutineDispatcher) {
            IndexStatus.toIndexStatus(serverService.fileStatus(indexId.toString()))
        }
    }

    override fun questionTimecode(params: QuestionTimecodeParams): Single<OffsetsStatus> {
        return GlobalScope.rxSingle(coroutineDispatcher) {
            OffsetsStatus.toOffsetsStatus(serverService.questionTimecode(params.params))
        }
    }

    override fun reWrap(params: ReWrapParams): Single<ReWrapResponse> {
        return GlobalScope.rxSingle(coroutineDispatcher) {
            ReWrapResponse.toReWrapResponse(serverService.reWrap(params.params))
        }
    }

    override fun reWrapStatus(targetFileName: String): Single<ReWrapStatus> {
        return GlobalScope.rxSingle(coroutineDispatcher) {
            ReWrapStatus.toReWrapStatus(serverService.reWrapStatus(targetFileName))
        }
    }
}