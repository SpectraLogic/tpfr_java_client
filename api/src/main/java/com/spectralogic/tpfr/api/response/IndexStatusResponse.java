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
    private String fileFrameRate;

    @Attribute(name = "FileFrameRate", required = false)
    private String fileDuration;

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
