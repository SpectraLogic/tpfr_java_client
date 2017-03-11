package com.spectralogic.tpfr.api.response;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name = "IndexerReport")
public class IndexStatus {

    @Attribute(name = "IndexResult")
    public IndexResult indexResult;

    @Attribute(name = "IndexTime", required = false)
    public String indexTime;

    @Attribute(name = "FileStartTC", required = false)
    public String fileStartTc;

    @Attribute(name = "FileDuration", required = false)
    public String fileFrameRate;

    @Attribute(name = "FileFrameRate", required = false)
    public String fileDuration;

    @Attribute(name = "errorCode", required = false)
    public String errorCode;

    @Attribute(name = "errorStr", required = false)
    public String errorMessage;

    public IndexStatus() {}

    public IndexStatus(final IndexResult result) {
        indexResult = result;
    }
}
