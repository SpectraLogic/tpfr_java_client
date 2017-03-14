package com.spectralogic.tpfr.client.model;

import com.spectralogic.tpfr.api.response.ReWrapStatusResponse;

public class ReWrapStatus {

    private Phase phase;

    private String percentComplete;

    private String error;

    private String errorCode;

    private String errorMessage;

    public ReWrapStatus(final ReWrapStatusResponse reWrapStatusResponse) {
        this.phase = getPhaseResult(reWrapStatusResponse.getPhase());
        this.percentComplete = reWrapStatusResponse.getPercentcomplete();
        this.error = reWrapStatusResponse.getError();
        this.errorCode = reWrapStatusResponse.getErrorCode();
        this.errorMessage = reWrapStatusResponse.getErrorMessage();
    }

    public Phase getPhase() {
        return phase;
    }

    public void setPhase(final Phase phase) {
        this.phase = phase;
    }

    public String getPercentComplete() {
        return percentComplete;
    }

    public void setPercentComplete(final String percentComplete) {
        this.percentComplete = percentComplete;
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

    private static Phase getPhaseResult(final String result)
    {
        if (result == null) {
            return null;
        }

        switch (result)
        {
            case "Pending":
                return Phase.Pending;
            case "Parsing":
                return Phase.Parsing;
            case "Transferring":
                return Phase.Transferring;
            case "Complete":
                return Phase.Complete;
            case "Failed":
                return Phase.Failed;
            default:
                return Phase.Unknown;
        }
    }
}
