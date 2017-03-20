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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class TestFrameRate {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {"00", "0"},
                {"25", "000"},
                {"30", "0000"},
        });
    }

    private final String goodFrameRateInput;
    private final String badFrameRateInput;

    public TestFrameRate(final String input1, final String input2) {
        goodFrameRateInput= input1;
        badFrameRateInput = input2;
    }

    @Test
    public void testGoodFrameRateFormat() {
        final FrameRate frameRate = FrameRate.Companion.of(goodFrameRateInput);
        assertThat(frameRate.getFrameRate(), is(goodFrameRateInput));
    }

    @Test(expected=IllegalArgumentException.class)
    public void TestBadTimeCodeFormat() {
        FrameRate.Companion.of(badFrameRateInput);
    }
}
