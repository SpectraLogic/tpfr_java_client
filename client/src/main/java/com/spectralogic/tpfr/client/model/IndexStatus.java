package com.spectralogic.tpfr.client.model;

import com.spectralogic.tpfr.api.response.IndexStatusResponse;

public class IndexStatus {

    private IndexResult indexResult;

    private String indexTime;

    private final String fileStartTc;

    private final String fileFrameRate;

    private final String fileDuration;

    private final String errorCode;

    private final String errorMessage;

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
