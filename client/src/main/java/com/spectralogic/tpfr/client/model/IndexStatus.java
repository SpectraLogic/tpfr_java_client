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

import com.spectralogic.tpfr.api.response.IndexStatusResponse;

public class IndexStatus {

    private final IndexResult indexResult;

    private final String indexTime;

    private final String fileStartTc;

    private final String fileFrameRate;

    private final String fileDuration;

    private final String errorCode;

    private final String errorMessage;

    public IndexStatus(final IndexResult indexResult, final String indexTime, final String fileStartTc,
                       final String fileFrameRate, final String fileDuration, final String errorCode,
                       final String errorMessage) {
        this.indexResult = indexResult;
        this.indexTime = indexTime;
        this.fileStartTc = fileStartTc;
        this.fileFrameRate = fileFrameRate;
        this.fileDuration = fileDuration;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public static IndexStatus toIndexStatus(final IndexStatusResponse indexStatusResponse) {
        return new IndexStatus(
                IndexResult.getIndexResult(indexStatusResponse.getIndexResult()),
                indexStatusResponse.getIndexTime(),
                indexStatusResponse.getFileStartTc(),
                indexStatusResponse.getFileFrameRate(),
                indexStatusResponse.getFileDuration(),
                indexStatusResponse.getErrorCode(),
                indexStatusResponse.getErrorMessage());
    }

    public IndexResult getIndexResult() {
        return indexResult;
    }

    public String getIndexTime() {
        return indexTime;
    }

    public String getFileStartTc() {
        return fileStartTc;
    }

    public String getFileFrameRate() {
        return fileFrameRate;
    }

    public String getFileDuration() {
        return fileDuration;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
