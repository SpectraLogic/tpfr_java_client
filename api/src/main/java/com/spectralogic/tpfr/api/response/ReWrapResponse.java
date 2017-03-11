package com.spectralogic.tpfr.api.response;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name = "partialfile")
public class ReWrapResponse {

    @Attribute(name = "partialfileResult")
    public String reWrapResult;

    public ReWrapResponse() {}

    public ReWrapResponse(final String reWrapResult) {
        this.reWrapResult= reWrapResult;
    }
}
