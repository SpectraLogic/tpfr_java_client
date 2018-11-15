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

//class TimeCode private constructor(timeStr: String, val frameRate: String, val delimiter: String) {
data class TimeCode constructor(val time: LocalTime, val frame: Int, val delimiter: String) {

    val timecode: String

    init  {
        val timeStr = getFormattedTime(time)
        timecode = String.format("%s%s%02d", timeStr, delimiter, frame)
    }

    companion object {

        private fun getFormattedTime(time: LocalTime): String {
            val dtf = DateTimeFormatter.ofPattern("HH:mm:ss")
            return time.format(dtf)
        }

        /***
         * Timecode format should be in form hh:mm:ss:ff for non-drop framerates.
         */
        fun getTimeCodeForNonDropFrameRates(timecode: LocalTime, frame: Int): TimeCode {
            return TimeCode(timecode, frame, ":")
        }

        /***
         * Timecode format should be in form hh:mm:ss;ff for drop framerates.
         */
        fun getTimeCodeForDropFrameRates(timecode: LocalTime, frame: Int): TimeCode {
            return TimeCode(timecode, frame, ";")
        }

        fun of(str: String): TimeCode
        {
            val dtf = DateTimeFormatter.ofPattern("HH:mm:ss")
            val delim = if (str.contains(";")) ";" else ":"
            val time = LocalTime.parse(str.substringBeforeLast(delim), dtf)
            val frame = str.substringAfterLast(delim).toInt()
            return TimeCode(time, frame, delim)
        }
    }
}