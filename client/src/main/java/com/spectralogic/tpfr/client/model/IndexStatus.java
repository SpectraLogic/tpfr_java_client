package com.spectralogic.tpfr.client.model;

import com.spectralogic.tpfr.api.response.IndexStatusResponse;

public class IndexStatus {

    public IndexResult indexResult;

    public String indexTime;

    public String fileStartTc;

    public String fileFrameRate;

    public String fileDuration;

    public String errorCode;

    public String errorMessage;

    public IndexStatus(final IndexStatusResponse indexStatusResponse) {
        indexResult = getIndexResult(indexStatusResponse.indexResult);
        indexTime = indexStatusResponse.indexTime;
        fileStartTc = indexStatusResponse.fileStartTc;
        fileFrameRate = indexStatusResponse.fileFrameRate;
        fileDuration = indexStatusResponse.fileDuration;
        errorCode = indexStatusResponse.errorCode;
        errorMessage = indexStatusResponse.errorMessage;
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
