package com.spectralogic.tpfr.api.response;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name = "partialfile")
public class ReWrapResponse {

    @Attribute(name = "partialfileResult")
    private String reWrapResult;

    public ReWrapResponse() {}

    public ReWrapResponse(final String reWrapResult) {
        this.reWrapResult= reWrapResult;
    }

    public String getReWrapResult() {
        return reWrapResult;
    }

    public void setReWrapResult(final String reWrapResult) {
        this.reWrapResult = reWrapResult;
    }
}
