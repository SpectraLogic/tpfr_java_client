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
import java.util.*

class TpfrClientImpl constructor(private val serverService: ServerService) : TpfrClient {

    override suspend fun indexFile(filePath: String, indexId: UUID): IndexStatus {
        return IndexStatus.toIndexStatus(serverService.indexFile(filePath, indexId.toString()))
    }

    override suspend fun fileStatus(indexId: UUID): IndexStatus {
        return IndexStatus.toIndexStatus(serverService.fileStatus(indexId.toString()))
    }

    override suspend fun questionTimecode(params: QuestionTimecodeParams): OffsetsStatus {
        return OffsetsStatus.toOffsetsStatus(serverService.questionTimecode(params.params))
    }

    override suspend fun reWrap(params: ReWrapParams): ReWrapResponse {
        return ReWrapResponse.toReWrapResponse(serverService.reWrap(params.params))
    }

    override suspend fun reWrapStatus(targetFileName: String): ReWrapStatus {
        return ReWrapStatus.toReWrapStatus(serverService.reWrapStatus(targetFileName))
    }
}