package com.spectralogic.tpfr.api.response;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name = "IndexerReport")
public class OffsetsStatusResponse {

    @Attribute(name = "fileoffsetsResult")
    private String offsetsResult;

    @Attribute(name = "in_bytes", required = false)
    private String inBytes;

    @Attribute(name = "out_bytes", required = false)
    private String outBytes;

    public OffsetsStatusResponse() {}

    public OffsetsStatusResponse(final String offsetsResult) {
        this.offsetsResult = offsetsResult;
    }

    public String getOffsetsResult() {
        return offsetsResult;
    }

    public void setOffsetsResult(final String offsetsResult) {
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
}
