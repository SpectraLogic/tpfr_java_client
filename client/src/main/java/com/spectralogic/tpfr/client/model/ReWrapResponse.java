package com.spectralogic.tpfr.client.model;

public class ReWrapResponse {

    private ReWrapResult reWrapResult;

    public ReWrapResponse(final com.spectralogic.tpfr.api.response.ReWrapResponse reWrapResponse) {
        this.reWrapResult = getReWrapResult(reWrapResponse.getReWrapResult());
    }

    public ReWrapResult getReWrapResult() {
        return reWrapResult;
    }

    public void setReWrapResult(final ReWrapResult reWrapResult) {
        this.reWrapResult = reWrapResult;
    }

    private ReWrapResult getReWrapResult(final String result)
    {
        switch (result)
        {
            case "Succeeded":
                return ReWrapResult.Succeeded;
            case "Error Duplicate parameter":
                return ReWrapResult.ErrorDuplicateParameter;
            case "Error Missing parameter":
                return ReWrapResult.ErrorMissingParameter;
            case "Error Bad framerate":
                return ReWrapResult.ErrorBadFramerate;
            default:
                return ReWrapResult.Unknown;
        }
    }
}