package com.spectralogic.tpfr.client;

import com.google.common.collect.ImmutableMap;
import com.spectralogic.tpfr.client.model.*;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class TestClient {

    private static Client client;
    private static MockWebServer server;

    @BeforeClass
    public static void startup() throws IOException {
        server = new MockWebServer();
        server.start();

        client = new ClientImpl(server.url("/").toString());
    }

    @Test
    public void successfulIndexFile() throws Exception {
        server.enqueue(new MockResponse()
        .setResponseCode(200)
                .setBody(ReadFileHelper.readFile("xml/index/SuccessfulIndexFileOrFileStatusCall.xml")));

        final IndexStatus indexStatus = client.indexFile("filePath");
        assertThat(indexStatus.getIndexResult(), is(IndexResult.Succeeded));
        assertThat(indexStatus.getIndexTime(), is("2011/10/21 11:40:53"));
        assertThat(indexStatus.getFileStartTc(), is("01:00:00;00"));
        assertThat(indexStatus.getFileDuration(), is("1800"));
        assertThat(indexStatus.getFileFrameRate(), is("29.97"));
    }

    @Test
    public void successfulFileStatus() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(ReadFileHelper.readFile("xml/index/SuccessfulIndexFileOrFileStatusCall.xml")));

        final IndexStatus indexStatus = client.fileStatus("filePath");
        assertThat(indexStatus.getIndexResult(), is(IndexResult.Succeeded));
        assertThat(indexStatus.getIndexTime(), is("2011/10/21 11:40:53"));
        assertThat(indexStatus.getFileStartTc(), is("01:00:00;00"));
        assertThat(indexStatus.getFileDuration(), is("1800"));
        assertThat(indexStatus.getFileFrameRate(), is("29.97"));
    }

    @Test
    public void failedIndexFile() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(ReadFileHelper.readFile("xml/index/FailedToIndex.xml")));

        final IndexStatus indexStatus = client.indexFile("filePath");
        assertThat(indexStatus.getIndexResult(), is(IndexResult.Failed));
        assertThat(indexStatus.getIndexTime(), is("2011/10/21 15:30:15"));
        assertThat(indexStatus.getErrorCode(), is("400"));
        assertThat(indexStatus.getErrorMessage(), is("Failed to index"));
    }

    @Test
    public void failedIndexStatus() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(ReadFileHelper.readFile("xml/index/FailedToIndex.xml")));

        final IndexStatus indexStatus = client.fileStatus("filePath");
        assertThat(indexStatus.getIndexResult(), is(IndexResult.Failed));
        assertThat(indexStatus.getIndexTime(), is("2011/10/21 15:30:15"));
        assertThat(indexStatus.getErrorCode(), is("400"));
        assertThat(indexStatus.getErrorMessage(), is("Failed to index"));
    }

    @Test
    public void fileStatusIndexing() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(ReadFileHelper.readFile("xml/index/FileStatusIndexing.xml")));

        final IndexStatus indexStatus = client.fileStatus("filePath");
        assertThat(indexStatus.getIndexResult(), is(IndexResult.Indexing));
    }

    @Test
    public void fileNotIndexedFileStatus() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(ReadFileHelper.readFile("xml/index/FileStatusWhenFileNotIndexed.xml")));

        final IndexStatus indexStatus = client.fileStatus("filePath");
        assertThat(indexStatus.getIndexResult(), is(IndexResult.NotIndexed));
    }

    @Test
    public void fileNotFoundFileStatus() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(ReadFileHelper.readFile("xml/index/FileStatusWhenFileNotPresent.xml")));

        final IndexStatus indexStatus = client.fileStatus("filePath");
        assertThat(indexStatus.getIndexResult(), is(IndexResult.ErrorFileNotFound));
    }

    @Test
    public void succeededQuestionTimecode() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(ReadFileHelper.readFile("xml/fileOffset/GoodFileOffsetsCall.xml")));

        final OffsetsStatus offsetsStatus = client.questionTimecode(new QuestionTimecodeParams("filePath", new TimeCode("00:00:00:00"), new TimeCode("00:00:00:00"), "0"));
        assertThat(offsetsStatus.getOffsetsResult(), is(OffsetsResult.Succeeded));
        assertThat(offsetsStatus.getInBytes(), is("0x0060000"));
        assertThat(offsetsStatus.getOutBytes(), is("0x0080000"));
    }

    @Test
    public void succeededQuestionTimecodeAskingForLastFrame() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(ReadFileHelper.readFile("xml/fileOffset/GoodFileOffsetsCallAskingForLastFrame.xml")));

        final OffsetsStatus offsetsStatus = client.questionTimecode(new QuestionTimecodeParams("filePath", new TimeCode("00:00:00:00"), new TimeCode("00:00:00:00"), "0"));
        assertThat(offsetsStatus.getOffsetsResult(), is(OffsetsResult.Succeeded));
        assertThat(offsetsStatus.getInBytes(), is("0x7c28014"));
        assertThat(offsetsStatus.getOutBytes(), is("0xffffffffffffffff"));
    }

    @Test
    public void fileNotFoundQuestionTimecode() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(ReadFileHelper.readFile("xml/fileOffset/FileNotFoundOffsetsCall.xml")));

        final OffsetsStatus offsetsStatus = client.questionTimecode(new QuestionTimecodeParams("filePath", new TimeCode("00:00:00:00"), new TimeCode("00:00:00:00"), "0"));
        assertThat(offsetsStatus.getOffsetsResult(), is(OffsetsResult.ErrorFileNotFound));
    }

    @Test
    public void reWrap() {
        final Map<String, ReWrapResult> testSource = new ImmutableMap.Builder<String, ReWrapResult>()
                .put("SuccessfulReWrap.xml", ReWrapResult.Succeeded)
                .put("DuplicateParameter.xml", ReWrapResult.ErrorDuplicateParameter)
                .put("MissingParameter.xml", ReWrapResult.ErrorMissingParameter)
                .put("IncorrectFramerate.xml", ReWrapResult.ErrorBadFramerate)
                .build();

        testSource.forEach((k, v) -> {
            server.enqueue(new MockResponse()
                    .setResponseCode(200)
                    .setBody(ReadFileHelper.readFile("xml/reWrap/" + k)));

            try {
                final ReWrapResponse reWrapResponse = client.reWrap(new ReWrapParams("filePath", new TimeCode("00:00:00:00"), new TimeCode("00:00:00:00"), "0", "partialFilePath", "outputFileName"));
                assertThat(reWrapResponse.getReWrapResult(), is(v));
            } catch (final Exception e) {
                fail(e.getMessage());
            }
        });
    }

    @Test
    public void reWrapStatus() {
        final Map<String, ReWrapStatusExpected> testSource = new ImmutableMap.Builder<String, ReWrapStatusExpected>()
                .put("JobPending.xml", new ReWrapStatusExpected(Phase.Pending, "0", null, null))
                .put("JobParsing.xml", new ReWrapStatusExpected(Phase.Parsing, "25", null, null))
                .put("JobTransferring.xml", new ReWrapStatusExpected(Phase.Transferring, "50", null, null))
                .put("JobComplete.xml", new ReWrapStatusExpected(Phase.Complete, "100", null, null))
                .put("JobFailed.xml", new ReWrapStatusExpected(Phase.Failed, "0", "-2132778983", "Failed to create file"))
                .build();

        testSource.forEach((k, v) -> {
            server.enqueue(new MockResponse()
                    .setResponseCode(200)
                    .setBody(ReadFileHelper.readFile("xml/reWrapStatus/" + k)));

            try {
                final ReWrapStatus reWrapStatus = client.reWrapStatus("outputFileName");
                assertThat(reWrapStatus.getPhase(), is(v.getPhase()));
                assertThat(reWrapStatus.getPercentComplete(), is(v.getPercentComplete()));
                assertThat(reWrapStatus.getErrorCode(), is(v.getErrorCode()));
                assertThat(reWrapStatus.getErrorMessage(), is(v.getErrorMessage()));
            } catch (final Exception e) {
                fail(e.getMessage());
            }
        });
    }

    @Test
    public void reWrapError() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(ReadFileHelper.readFile("xml/reWrapStatus/PartialFileStatusError.xml")));

        final ReWrapStatus reWrapStatus = client.reWrapStatus("outFileName");
        assertThat(reWrapStatus.getError(), is("Job not found"));
    }
}
