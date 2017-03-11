package com.spectralogic.tpfr.client.model;

import com.spectralogic.tpfr.api.response.IndexStatusResponse;

public class IndexStatus {

    private IndexResult indexResult;

    private String indexTime;

    private String fileStartTc;

    private String fileFrameRate;

    private String fileDuration;

    private String errorCode;

    private String errorMessage;

    public IndexStatus(final IndexStatusResponse indexStatusResponse) {
        indexResult = getIndexResult(indexStatusResponse.getIndexResult());
        indexTime = indexStatusResponse.getIndexTime();
        fileStartTc = indexStatusResponse.getFileStartTc();
        fileFrameRate = indexStatusResponse.getFileFrameRate();
        fileDuration = indexStatusResponse.getFileDuration();
        errorCode = indexStatusResponse.getErrorCode();
        errorMessage = indexStatusResponse.getErrorMessage();
    }

    public IndexResult getIndexResult() {
        return indexResult;
    }

    public void setIndexResult(final IndexResult indexResult) {
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

    private IndexResult getIndexResult(final String result)
    {
        switch (result)
        {
            case "Succeeded":
                return IndexResult.Succeeded;
            case "Failed":
                return IndexResult.Failed;
            case "Error File Not Found":
                return IndexResult.ErrorFileNotFound;
            case "Not Indexed":
                return IndexResult.NotIndexed;
            case "Indexing":
                return IndexResult.Indexing;
            default:
                return IndexResult.Unknown;
        }
    }
}
