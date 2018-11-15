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

package com.spectralogic.tpfr.client.model

import com.spectralogic.tpfr.api.response.IndexStatusResponse

class IndexStatus(
    val indexResult: IndexResult,
    val indexTime: String?,
    val fileStartTc: TimeCode?,
    val fileFrameRate: Float?,
    val originalFile: String?,
    val indexID: String?,
    val fileDuration: Long?,
    val errorCode: String?,
    val errorMessage: String?,
    val exception: Exception?
) {

    companion object {
        fun toIndexStatus(indexStatusResponse: IndexStatusResponse): IndexStatus {
            return IndexStatus(
                    IndexResult.getIndexResult(indexStatusResponse.indexResult),
                    indexStatusResponse.indexTime,
                    indexStatusResponse.fileStartTc?.let { TimeCode.of(it) },
                    indexStatusResponse.fileFrameRate?.toFloat(),
                    indexStatusResponse.originalFile,
                    indexStatusResponse.indexID,
                    indexStatusResponse.fileDuration?.toLong(),
                    indexStatusResponse.errorCode,
                    indexStatusResponse.errorMessage,
                    indexStatusResponse.exception)
        }
    }
}