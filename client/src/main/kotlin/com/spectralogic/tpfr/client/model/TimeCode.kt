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

data class TimeCode constructor(val hours: Int, val minutes: Int, val seconds: Int, val frames: Int, val dropFrame: Boolean) {

    val timecode: String

    init  {
        val delimiter = if (dropFrame) ";" else ":"
        timecode = String.format("%02d:%02d:%02d%s%02d", hours, minutes, seconds, delimiter, frames)
    }

    companion object {

        /***
         * Timecode format should be in form hh:mm:ss:ff for non-drop framerates.
         */
        fun getTimeCodeForNonDropFrameRates(time: LocalTime, frame: Int): TimeCode {
            return TimeCode(time.hour, time.minute, time.second, frame, false)
        }

        /***
         * Timecode format should be in form hh:mm:ss;ff for drop framerates.
         */
        fun getTimeCodeForDropFrameRates(time: LocalTime, frame: Int): TimeCode {
            return TimeCode(time.hour, time.minute, time.second, frame, true)
        }

        fun of(str: String): TimeCode
        {
            val dtf = DateTimeFormatter.ofPattern("HH:mm:ss")
            val dropFrame = str.contains(";")
            val delim = if (dropFrame) ";" else ":"
            val time = LocalTime.parse(str.substringBeforeLast(delim), dtf)
            val frame = str.substringAfterLast(delim).toInt()
            return TimeCode(time.hour, time.minute, time.second, frame, dropFrame)
        }
    }
}