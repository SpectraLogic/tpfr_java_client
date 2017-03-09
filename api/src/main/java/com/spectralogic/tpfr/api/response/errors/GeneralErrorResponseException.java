package com.spectralogic.tpfr.api.response.errors;

public class GeneralErrorResponseException extends Throwable {

    private final GeneralError generalError;

    public GeneralErrorResponseException(final GeneralError generalError) {
        this.generalError = generalError;
    }

    public GeneralError getGeneralError() {
        return generalError;
    }
}