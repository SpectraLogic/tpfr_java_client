package com.spectralogic.tpfr.client.model;

import com.spectralogic.tpfr.api.response.ReWrapStatusResponse;

public class ReWrapStatus {

    private final Phase phase;

    private final String percentComplete;

    private final String error;

    private final String errorCode;

    private final String errorMessage;

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

    public String getPercentComplete() {
        return percentComplete;
    }

    public String getError() {
        return error;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
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
