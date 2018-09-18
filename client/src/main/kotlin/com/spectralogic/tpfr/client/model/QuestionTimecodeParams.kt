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

import com.google.common.collect.ImmutableMap
import java.util.*

/**
 * Represent the QuestionTimecode API call query parameters
 * @param indexId The unique identifier for the source file.
 * @param tcin Timecode of the first frame requested
 * @param tcout Timecode of the last frame requested
 * @param fileFrameRate Frame rate, as returned in the file status report
 */
class QuestionTimecodeParams(var indexId: UUID, var tcin: TimeCode, var tcout: TimeCode, var fileFrameRate: String) {

    val params: ImmutableMap<String, String> = ImmutableMap.of(
            "indexid", indexId.toString(),
            "tcin", tcin.timecode,
            "tcout", tcout.timecode,
            "fileframerate", fileFrameRate)

}