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
