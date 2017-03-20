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

package com.spectralogic.tpfr.client;

import com.spectralogic.tpfr.client.model.FrameRate;
import com.spectralogic.tpfr.client.model.TimeCode;
import org.junit.Test;

import java.time.LocalTime;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TestTimeCode {

    @Test
    public void getTimeCodeForDropFrameRatesTest() {
        final LocalTime time = LocalTime.of(12, 34, 56);
        final FrameRate framerate = FrameRate.Companion.of("00");
        final TimeCode tc2 = TimeCode.Companion.getTimeCodeForDropFrameRates(time, framerate);
        assertThat(tc2.getTimecode(), is("12:34:56;00"));
    }

    @Test
    public void getTimeCodeForNonDropFrameRatesTest() {
        final LocalTime time = LocalTime.of(12, 34, 56);
        final FrameRate framerate = FrameRate.Companion.of("00");
        final TimeCode tc2 = TimeCode.Companion.getTimeCodeForNonDropFrameRates(time, framerate);
        assertThat(tc2.getTimecode(), is("12:34:56:00"));
    }
}
