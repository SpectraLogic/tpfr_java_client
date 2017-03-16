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

import com.spectralogic.tpfr.api.response.OffsetsStatusResponse;

public class OffsetsStatus {

    private final OffsetsResult offsetsResult;

    private final String inBytes;

    private final String outBytes;

    public OffsetsStatus(final OffsetsStatusResponse offsetsStatusResponse) {
        offsetsResult = getOffsetsResult(offsetsStatusResponse.getOffsetsResult());
        inBytes = offsetsStatusResponse.getInBytes();
        outBytes = offsetsStatusResponse.getOutBytes();
    }

    public OffsetsResult getOffsetsResult() {
        return offsetsResult;
    }

    public String getInBytes() {
        return inBytes;
    }

    public String getOutBytes() {
        return outBytes;
    }

    private OffsetsResult getOffsetsResult(final String result)
    {
        switch (result)
        {
            case "Succeeded":
                return OffsetsResult.Succeeded;
            case "Error File Not Found":
                return OffsetsResult.ErrorFileNotFound;
            default:
                return OffsetsResult.Unknown;
        }
    }
}
