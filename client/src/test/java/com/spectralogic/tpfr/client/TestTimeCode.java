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

import com.spectralogic.tpfr.client.model.TimeCode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;


@RunWith(Parameterized.class)
public class TestTimeCode {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {"00:00:00:00", "00:00:00.00"},
                {"00:00:00;00", "00.00.00.00"},
                {"12:34:56:78", "00"},
                {"12:34:56;78", "x0:00:00:00"}
        });
    }

    private final String goodTimeCodeInput;
    private final String badTimeCodeInput;
    public TestTimeCode(final String input1, final String input2) {
        goodTimeCodeInput= input1;
        badTimeCodeInput = input2;
    }

    @Test
    public void testGoodTimeCodeFormat() {
        new TimeCode(goodTimeCodeInput);
    }

    @Test(expected=IllegalArgumentException.class)
    public void TestBadTimeCodeFormat() {
        new TimeCode(badTimeCodeInput);
    }

}
