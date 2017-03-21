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

import java.time.LocalTime
import java.time.format.DateTimeFormatter

class TimeCode private constructor(timecode: String, frameRate: String, delimiter: String) {

    val timecode: String = String.format("%s%s%s", timecode, delimiter, frameRate)

    companion object {
        private fun getFormattedTime(time: LocalTime): String {
            val dtf = DateTimeFormatter.ofPattern("HH:mm:ss")
            return time.format(dtf)
        }

        private fun getTimeCode(timecode: LocalTime, frameRate: FrameRate, delimiter: String): TimeCode {
            return TimeCode(getFormattedTime(timecode), frameRate.frameRate, delimiter)
        }

        /***
         * Timecode format should be in form hh:mm:ss:ff for non-drop framerates.
         */
        fun getTimeCodeForNonDropFrameRates(timecode: LocalTime, frameRate: FrameRate): TimeCode {
            return getTimeCode(timecode, frameRate, ":")
        }

        /***
         * Timecode format should be in form hh:mm:ss;ff for drop framerates.
         */
        fun getTimeCodeForDropFrameRates(timecode: LocalTime, frameRate: FrameRate): TimeCode {
            return getTimeCode(timecode, frameRate, ";")
        }
    }
}