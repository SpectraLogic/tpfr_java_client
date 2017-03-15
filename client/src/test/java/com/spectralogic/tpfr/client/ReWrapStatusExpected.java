package com.spectralogic.tpfr.client;

import com.spectralogic.tpfr.client.model.Phase;

public class ReWrapStatusExpected {
    private final Phase phase;
    private final String percentComplete;
    private final String errorCode;
    private final String errorMessage;

    public ReWrapStatusExpected(final Phase phase, final String percentComplete, final String errorCode, final String errorMessage) {
        this.phase = phase;
        this.percentComplete = percentComplete;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public Phase getPhase() {
        return phase;
    }

    public String getPercentComplete() {
        return percentComplete;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
