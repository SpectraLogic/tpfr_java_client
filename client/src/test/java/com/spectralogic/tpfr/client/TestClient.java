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

package com.spectralogic.tpfr.client;

import com.google.common.collect.ImmutableMap;
import com.spectralogic.tpfr.api.ServerService;
import com.spectralogic.tpfr.api.ServerServiceFactory;
import com.spectralogic.tpfr.api.ServerServiceFactoryImpl;
import com.spectralogic.tpfr.client.model.*;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalTime;
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

        final String endpoint = server.url("/").toString();
        final String proxyHost = "";
        final int proxyPort = 0;

        final ServerServiceFactory serverServiceFactory = new ServerServiceFactoryImpl(endpoint, proxyHost, proxyPort);
        final ServerService serverService = serverServiceFactory.createServerService();

        client = new ClientImpl(serverService);
    }

    @Test
    public void successfulIndexFile() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(ReadFileHelper.readFile("xml/index/SuccessfulIndexFileOrFileStatusCall.xml")));

        final IndexStatus indexStatus = client.indexFile("filePath").get();
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

        final IndexStatus indexStatus = client.fileStatus("filePath").get();
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

        final IndexStatus indexStatus = client.indexFile("filePath").get();
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

        final IndexStatus indexStatus = client.fileStatus("filePath").get();
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

        final IndexStatus indexStatus = client.fileStatus("filePath").get();
        assertThat(indexStatus.getIndexResult(), is(IndexResult.Indexing));
    }

    @Test
    public void fileNotIndexedFileStatus() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(ReadFileHelper.readFile("xml/index/FileStatusWhenFileNotIndexed.xml")));

        final IndexStatus indexStatus = client.fileStatus("filePath").get();
        assertThat(indexStatus.getIndexResult(), is(IndexResult.NotIndexed));
    }

    @Test
    public void fileNotFoundFileStatus() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(ReadFileHelper.readFile("xml/index/FileStatusWhenFileNotPresent.xml")));

        final IndexStatus indexStatus = client.fileStatus("filePath").get();
        assertThat(indexStatus.getIndexResult(), is(IndexResult.ErrorFileNotFound));
    }

    @Test
    public void succeededQuestionTimecode() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(ReadFileHelper.readFile("xml/fileOffset/GoodFileOffsetsCall.xml")));

        final TimeCode firstFrame = TimeCode.Companion.getTimeCodeForDropFrameRates(LocalTime.of(0, 0, 0), FrameRate.Companion.of("00"));
        final TimeCode lastFrame = TimeCode.Companion.getTimeCodeForDropFrameRates(LocalTime.of(0, 0, 10), FrameRate.Companion.of("00"));
        final OffsetsStatus offsetsStatus = client.questionTimecode(new QuestionTimecodeParams("filePath", firstFrame, lastFrame, "0")).get();
        assertThat(offsetsStatus.getOffsetsResult(), is(OffsetsResult.Succeeded));
        assertThat(offsetsStatus.getInBytes(), is("0x0060000"));
        assertThat(offsetsStatus.getOutBytes(), is("0x0080000"));
    }

    @Test
    public void succeededQuestionTimecodeAskingForLastFrame() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(ReadFileHelper.readFile("xml/fileOffset/GoodFileOffsetsCallAskingForLastFrame.xml")));

        final TimeCode firstFrame = TimeCode.Companion.getTimeCodeForDropFrameRates(LocalTime.of(0, 0, 0), FrameRate.Companion.of("00"));
        final TimeCode lastFrame = TimeCode.Companion.getTimeCodeForDropFrameRates(LocalTime.of(0, 0, 10), FrameRate.Companion.of("00"));
        final OffsetsStatus offsetsStatus = client.questionTimecode(new QuestionTimecodeParams("filePath", firstFrame, lastFrame, "0")).get();
        assertThat(offsetsStatus.getOffsetsResult(), is(OffsetsResult.Succeeded));
        assertThat(offsetsStatus.getInBytes(), is("0x7c28014"));
        assertThat(offsetsStatus.getOutBytes(), is("0xffffffffffffffff"));
    }

    @Test
    public void fileNotFoundQuestionTimecode() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(ReadFileHelper.readFile("xml/fileOffset/FileNotFoundOffsetsCall.xml")));

        final TimeCode firstFrame = TimeCode.Companion.getTimeCodeForDropFrameRates(LocalTime.of(0, 0, 0), FrameRate.Companion.of("00"));
        final TimeCode lastFrame = TimeCode.Companion.getTimeCodeForDropFrameRates(LocalTime.of(0, 0, 10), FrameRate.Companion.of("00"));
        final OffsetsStatus offsetsStatus = client.questionTimecode(new QuestionTimecodeParams("filePath", firstFrame, lastFrame, "0")).get();
        assertThat(offsetsStatus.getOffsetsResult(), is(OffsetsResult.ErrorFileNotFound));
    }

    @Test
    public void reWrap() {
        final Map<String, ReWrapResult> testSource = new ImmutableMap.Builder<String, ReWrapResult>()
                .put("SuccessfulReWrap.xml", ReWrapResult.Succeeded)
                .put("DuplicateParameter.xml", ReWrapResult.ErrorDuplicateParameter)
                .put("MissingParameter.xml", ReWrapResult.ErrorMissingParameter)
                .put("IncorrectFrameRate.xml", ReWrapResult.ErrorBadFramerate)
                .build();

        testSource.forEach((k, v) -> {
            server.enqueue(new MockResponse()
                    .setResponseCode(200)
                    .setBody(ReadFileHelper.readFile("xml/reWrap/" + k)));

            try {
                final TimeCode firstFrame = TimeCode.Companion.getTimeCodeForDropFrameRates(LocalTime.of(0, 0, 0), FrameRate.Companion.of("00"));
                final TimeCode lastFrame = TimeCode.Companion.getTimeCodeForDropFrameRates(LocalTime.of(0, 0, 10), FrameRate.Companion.of("00"));
                final ReWrapResponse reWrapResponse = client.reWrap(new ReWrapParams("filePath", firstFrame, lastFrame, "0", "partialFilePath", "outputFileName")).get();
                assertThat(reWrapResponse.getReWrapResult(), is(v));
            } catch (final Exception e) {
                fail(String.format("%s failed with error: %s", k, e.getMessage()));
            }
        });
    }

    @Test
    public void reWrapStatus() {
        final Map<String, ReWrapStatusExpected> testSource = new ImmutableMap.Builder<String, ReWrapStatusExpected>()
                .put("JobPending.xml", new ReWrapStatusExpected(Phase.Pending, 0, null, null))
                .put("JobParsing.xml", new ReWrapStatusExpected(Phase.Parsing, 25, null, null))
                .put("JobTransferring.xml", new ReWrapStatusExpected(Phase.Transferring, 50, null, null))
                .put("JobComplete.xml", new ReWrapStatusExpected(Phase.Complete, 100, null, null))
                .put("JobFailed.xml", new ReWrapStatusExpected(Phase.Failed, 0, "-2132778983", "Failed to create file"))
                .build();

        testSource.forEach((k, v) -> {
            server.enqueue(new MockResponse()
                    .setResponseCode(200)
                    .setBody(ReadFileHelper.readFile("xml/reWrapStatus/" + k)));

            try {
                final ReWrapStatus reWrapStatus = client.reWrapStatus("outputFileName").get();
                assertThat(reWrapStatus.getPhase(), is(v.getPhase()));
                assertThat(reWrapStatus.getPercentComplete(), is(v.getPercentComplete()));
                assertThat(reWrapStatus.getErrorCode(), is(v.getErrorCode()));
                assertThat(reWrapStatus.getErrorMessage(), is(v.getErrorMessage()));
            } catch (final Exception e) {
                fail(String.format("%s failed with error: %s", k, e.getMessage()));
            }
        });
    }

    @Test
    public void reWrapError() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(ReadFileHelper.readFile("xml/reWrapStatus/PartialFileStatusError.xml")));

        final ReWrapStatus reWrapStatus = client.reWrapStatus("outFileName").get();
        assertThat(reWrapStatus.getError(), is("Job not found"));
    }
}
