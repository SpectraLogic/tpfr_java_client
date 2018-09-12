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

import com.spectralogic.tpfr.client.model.FrameRate
import org.assertj.core.api.KotlinAssertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.util.*

@RunWith(Parameterized::class)
class TestFrameRate(private val goodFrameRateInput: String, private val badFrameRateInput: String) {

    companion object {
        @Parameterized.Parameters @JvmStatic
        fun data(): Collection<Array<Any>> {
            return Arrays.asList(arrayOf<Any>("00", "0"), arrayOf<Any>("25", "000"), arrayOf<Any>("30", "0000"))
        }
    }

    @Test
    fun testGoodFrameRateFormat() {
        val frameRate = FrameRate.of(goodFrameRateInput)
        assertThat(frameRate.frameRate).isEqualTo(goodFrameRateInput)
    }

    @Test(expected = IllegalArgumentException::class)
    fun TestBadTimeCodeFormat() {
        FrameRate.of(badFrameRateInput)
    }
}