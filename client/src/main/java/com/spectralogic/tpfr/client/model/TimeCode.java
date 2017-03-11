package com.spectralogic.tpfr.client.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeCode {
    private final String time;

    public TimeCode(final String timeCode) {
        if (!isValidFormat(timeCode)) {
            throw new IllegalArgumentException("The format of the time code is not valid. Time code format should be in form hh:mm:ss:ff for non-drop framerates and hh:mm:ss;ff for drop framerates.");
        }
        this.time = timeCode;
    }

    private boolean isValidFormat(final String timeCode) {
        final Pattern pattern = Pattern.compile("[0-9][0-9]:[0-9][0-9]:[0-9][0-9][:;][0-9][0-9]");
        final Matcher matcher = pattern.matcher(timeCode);
        return matcher.find();
    }

    public String getTime() {
        return time;
    }
}
