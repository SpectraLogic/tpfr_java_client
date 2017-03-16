package com.spectralogic.tpfr.client.model;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class ReWrapParams {
    private final Map<String, String> params;

    /**
     * Represent the ReWrap API call query parameters
     * @param filePath The full path to the media file whose partial offsets are being requested.
     * @param tcin Timecode of the first frame requested
     * @param tcout Timecode of the last frame requested
     * @param fileFrameRate Frame rate, as returned in the file status report
     * @param partFile Full UNC path to partial restored file fragment
     * @param outFileName output file name for partial media file (care should be taken that this does not clash with other part restores, e.g. from other sections of the same source file). This should not have an extension, as this will added automatically.
     */
    public ReWrapParams(final String filePath, final TimeCode tcin, final TimeCode tcout, final String fileFrameRate,
                        final String partFile, final String outFileName) {
        params = new ImmutableMap.Builder<String, String>()
                .put("filepath", filePath)
                .put("tcin", tcin.getTime())
                .put("tcout", tcout.getTime())
                .put("fileframerate", fileFrameRate)
                .put("part_file", partFile)
                .put("out_filename", outFileName)
                .build();
    }

    public Map<String, String> getParams() {
        return params;
    }
}
