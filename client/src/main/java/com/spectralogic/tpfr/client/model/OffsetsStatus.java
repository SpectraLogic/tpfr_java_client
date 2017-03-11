package com.spectralogic.tpfr.client.model;

import com.spectralogic.tpfr.api.response.OffsetsStatusResponse;

public class OffsetsStatus {

    private OffsetsResult offsetsResult;

    private String inBytes;

    private String outBytes;

    public OffsetsStatus(final OffsetsStatusResponse offsetsStatusResponse) {
        offsetsResult = getOffsetsResult(offsetsStatusResponse.offsetsResult);
        inBytes = offsetsStatusResponse.inBytes;
        outBytes = offsetsStatusResponse.outBytes;
    }

    public OffsetsResult getOffsetsResult() {
        return offsetsResult;
    }

    public void setOffsetsResult(final OffsetsResult offsetsResult) {
        this.offsetsResult = offsetsResult;
    }

    public String getInBytes() {
        return inBytes;
    }

    public void setInBytes(final String inBytes) {
        this.inBytes = inBytes;
    }

    public String getOutBytes() {
        return outBytes;
    }

    public void setOutBytes(final String outBytes) {
        this.outBytes = outBytes;
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
