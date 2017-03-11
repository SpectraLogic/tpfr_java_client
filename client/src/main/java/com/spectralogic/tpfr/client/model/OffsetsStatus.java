package com.spectralogic.tpfr.client.model;

import com.spectralogic.tpfr.api.response.OffsetsStatusResponse;

public class OffsetsStatus {

    public OffsetsResult offsetsResult;

    public String inBytes;

    public String outBytes;

    public OffsetsStatus(final OffsetsStatusResponse offsetsStatusResponse) {
        offsetsResult = getOffsetsResult(offsetsStatusResponse.offsetsResult);
        inBytes = offsetsStatusResponse.inBytes;
        outBytes = offsetsStatusResponse.outBytes;
    }


    private OffsetsResult getOffsetsResult(final String result)
    {
        switch (result)
        {
            case "Succeeded":
                return OffsetsResult.Succeeded;
            case "Error File Not Found":
                return OffsetsResult.ErrorFileNotFound;
            default:
                return OffsetsResult.Unknown;
        }
    }
}
