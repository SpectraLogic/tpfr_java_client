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

import com.spectralogic.tpfr.client.model.TimeCode
import org.assertj.core.api.KotlinAssertions.assertThat
import org.junit.Test
import java.time.LocalTime

class TestTimeCode {

    @Test
    fun getTimeCodeForDropFrameRatesTest() {
        val time = LocalTime.of(12, 34, 56)
        val tc2 = TimeCode.getTimeCodeForDropFrameRates(time, 0)
        assertThat(tc2.timecode).isEqualTo("12:34:56;00")
    }

    @Test
    fun getTimeCodeForNonDropFrameRatesTest() {
        val time = LocalTime.of(12, 34, 56)
        val tc2 = TimeCode.getTimeCodeForNonDropFrameRates(time, 0)
        assertThat(tc2.timecode).isEqualTo("12:34:56:00")
    }
}