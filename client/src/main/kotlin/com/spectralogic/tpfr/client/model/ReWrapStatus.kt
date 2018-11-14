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

import com.spectralogic.tpfr.api.response.ReWrapStatusResponse

class ReWrapStatus(
    val phase: Phase?,
    val percentComplete: Int?,
    val error: String?,
    val errorCode: String?,
    val errorMessage: String?,
    val exception: Exception?
) {

    companion object {
        fun toReWrapStatus(reWrapStatusResponse: ReWrapStatusResponse): ReWrapStatus {
            return ReWrapStatus(
                    if (reWrapStatusResponse.phase != null) Phase.getPhaseResult(reWrapStatusResponse.phase!!) else null,
                    if (reWrapStatusResponse.percentcomplete != null) Integer.valueOf(reWrapStatusResponse.percentcomplete!!) else 0,
                    reWrapStatusResponse.error,
                    reWrapStatusResponse.errorCode,
                    reWrapStatusResponse.errorMessage,
                    reWrapStatusResponse.exception)
        }
    }
}