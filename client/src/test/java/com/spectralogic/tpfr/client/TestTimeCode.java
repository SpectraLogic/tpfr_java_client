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
