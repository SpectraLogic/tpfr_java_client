/*
 * ***************************************************************************
 *   Copyright 2016-2017 Spectra Logic Corporation. All Rights Reserved.
 *   Licensed under the Apache License, Version 2.0 (the "License"). You may not use
 *   this file except in compliance with the License. A copy of the License is located at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   or in the "license" file accompanying this file.
 *   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 *   CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations under the License.
 * ***************************************************************************
 */

package com.spectralogic.tpfr.client.model;

import com.spectralogic.tpfr.api.response.ReWrapStatusResponse;

public class ReWrapStatus {

    private final Phase phase;

    private final int percentComplete;

    private final String error;

    private final String errorCode;

    private final String errorMessage;

    public ReWrapStatus(final Phase phase, final int percentComplete, final String error,
                        final String errorCode, final String errorMessage) {
        this.phase = phase;
        this.percentComplete = percentComplete;
        this.error = error;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public static ReWrapStatus toReWrapStatus(final ReWrapStatusResponse reWrapStatusResponse) {
        return new ReWrapStatus(
        Phase.Companion.getPhaseResult(reWrapStatusResponse.getPhase()),
        reWrapStatusResponse.getPercentcomplete() != null ? Integer.valueOf(reWrapStatusResponse.getPercentcomplete()) : 0,
        reWrapStatusResponse.getError(),
        reWrapStatusResponse.getErrorCode(),
        reWrapStatusResponse.getErrorMessage());
    }

    public Phase getPhase() {
        return phase;
    }

    public int getPercentComplete() {
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
}
