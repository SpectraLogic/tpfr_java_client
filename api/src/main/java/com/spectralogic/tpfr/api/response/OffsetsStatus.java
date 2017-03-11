package com.spectralogic.tpfr.api.response;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name = "IndexerReport")
public class OffsetsStatus {

    @Attribute(name = "fileoffsetsResult")
    public OffsetsResult offsetsResult;

    @Attribute(name = "in_bytes", required = false)
    public String inBytes;

    @Attribute(name = "out_bytes", required = false)
    public String outBytes;

    public OffsetsStatus() {}

    public OffsetsStatus(final OffsetsResult result) {
        offsetsResult = result;
    }
}
