package com.spectralogic.tpfr.api.response;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name = "partialfilestatus")
public class ReWrapStatusResponse {

    @Attribute(name = "phase", required = false)
    private String phase;

    @Attribute(name = "percentcomplete", required = false)
    private String percentcomplete;

    @Attribute(name = "error", required = false)
    private String error;

    @Attribute(name = "errorCode", required = false)
    private String errorCode;

    @Attribute(name = "errorStr", required = false)
    private String errorMessage;

    public ReWrapStatusResponse() {}

    public ReWrapStatusResponse(final String phase) {
        this.phase = phase;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(final String phase) {
        this.phase = phase;
    }

    public String getPercentcomplete() {
        return percentcomplete;
    }

    public void setPercentcomplete(final String percentcomplete) {
        this.percentcomplete = percentcomplete;
    }

    public String getError() {
        return error;
    }

    public void setError(final String error) {
        this.error = error;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(final String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
