package com.spectralogic.tpfr.api.response;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name = "IndexerReport")
public class OffsetsStatusResponse {

    @Attribute(name = "fileoffsetsResult")
    public String offsetsResult;

    @Attribute(name = "in_bytes", required = false)
    public String inBytes;

    @Attribute(name = "out_bytes", required = false)
    public String outBytes;

    public OffsetsStatusResponse() {}

    public OffsetsStatusResponse(final String offsetsResult) {
        this.offsetsResult = offsetsResult;
    }
}
