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
