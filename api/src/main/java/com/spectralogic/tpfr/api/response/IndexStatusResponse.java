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
public class IndexStatusResponse {

    @Attribute(name = "IndexResult")
    private String indexResult;

    @Attribute(name = "IndexTime", required = false)
    private String indexTime;

    @Attribute(name = "FileStartTC", required = false)
    private String fileStartTc;

    @Attribute(name = "FileDuration", required = false)
    private String fileDuration;

    @Attribute(name = "FileFrameRate", required = false)
    private String fileFrameRate;

    @Attribute(name = "errorCode", required = false)
    private String errorCode;

    @Attribute(name = "errorStr", required = false)
    private String errorMessage;

    public IndexStatusResponse() {}

    public IndexStatusResponse(final String indexResult, final int errorCode, final String errorMessage) {
        this.indexResult = indexResult;
        this.errorCode = String.valueOf(errorCode);
        this.errorMessage = errorMessage;
    }

    public String getIndexResult() {
        return indexResult;
    }

    public void setIndexResult(final String indexResult) {
        this.indexResult = indexResult;
    }

    public String getIndexTime() {
        return indexTime;
    }

    public void setIndexTime(final String indexTime) {
        this.indexTime = indexTime;
    }

    public String getFileStartTc() {
        return fileStartTc;
    }

    public void setFileStartTc(final String fileStartTc) {
        this.fileStartTc = fileStartTc;
    }

    public String getFileFrameRate() {
        return fileFrameRate;
    }

    public void setFileFrameRate(final String fileFrameRate) {
        this.fileFrameRate = fileFrameRate;
    }

    public String getFileDuration() {
        return fileDuration;
    }

    public void setFileDuration(final String fileDuration) {
        this.fileDuration = fileDuration;
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
