package com.spectralogic.tpfr.client.model;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class QuestionTimecodeParams {

    private final Map<String, String> params;

    /**
     * Represent the QuestionTimecode API call query parameters
     * @param filePath The full path (via a mapped drive) to the media file whose partial offsets are being requested.
     * @param tcin Timecode of the first frame requested
     * @param tcout Timecode of the last frame requested
     * @param fileFrameRate Frame rate, as returned in the file status report
     */
    public QuestionTimecodeParams(final String filePath, final TimeCode tcin, final TimeCode tcout, final String fileFrameRate) {
        params = ImmutableMap.of(
                "filepath", filePath,
                "tcin", tcin.getTime(),
                "tcout", tcout.getTime(),
                "fileframerate", fileFrameRate);
    }

    public Map<String, String> getParams() {
        return params;
    }
}
