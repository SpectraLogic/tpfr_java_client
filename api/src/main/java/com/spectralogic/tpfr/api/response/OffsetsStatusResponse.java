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

@Root(name = "IndexerReport")
public class OffsetsStatusResponse {

    @Attribute(name = "fileoffsetsResult")
    private String offsetsResult;

    @Attribute(name = "in_bytes", required = false)
    private String inBytes;

    @Attribute(name = "out_bytes", required = false)
    private String outBytes;

    public OffsetsStatusResponse() {}

    public OffsetsStatusResponse(final String offsetsResult) {
        this.offsetsResult = offsetsResult;
    }

    public String getOffsetsResult() {
        return offsetsResult;
    }

    public void setOffsetsResult(final String offsetsResult) {
        this.offsetsResult = offsetsResult;
    }

    public String getInBytes() {
        return inBytes;
    }

    public void setInBytes(final String inBytes) {
        this.inBytes = inBytes;
    }

    public String getOutBytes() {
        return outBytes;
    }

    public void setOutBytes(final String outBytes) {
        this.outBytes = outBytes;
    }
}
