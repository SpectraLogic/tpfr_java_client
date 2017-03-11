package com.spectralogic.tpfr.client.model;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class QuestionTimecodeParams {

    private final Map<String, String> params;

    public QuestionTimecodeParams(final String filePath, final TimeCode tcin, final TimeCode tcout, final String fileFrameRate) {
        params = new ImmutableMap.Builder<String, String>()
                .put("filepath", filePath)
                .put("tcin", tcin.getTime())
                .put("tcout", tcout.getTime())
                .put("fileframerate", fileFrameRate)
                .build();
    }

    public Map<String, String> getParams() {
        return params;
    }
}
