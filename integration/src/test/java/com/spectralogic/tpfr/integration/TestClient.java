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

package com.spectralogic.tpfr.integration;

import com.spectralogic.tpfr.api.ServerService;
import com.spectralogic.tpfr.api.ServerServiceFactory;
import com.spectralogic.tpfr.api.ServerServiceFactoryImpl;
import com.spectralogic.tpfr.client.Client;
import com.spectralogic.tpfr.client.ClientImpl;
import com.spectralogic.tpfr.client.model.*;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

public class TestClient {

    private static final String origFilesPath = "\\\\ISV_RETROSPECT1\\Users\\orig\\";
    private static final String restoredFilesPath = "\\\\ISV_RETROSPECT1\\Users\\restored\\";
    private static Client client;
    private static final int numberOfThreads = 12;
    private static final Executor executor = Executors.newFixedThreadPool(numberOfThreads);

    @BeforeClass
    public static void startup() {
        final String endpoint = "http://10.85.41.78:60792";
        final String proxyHost = "";
        final int proxyPort = 0;

        final ServerServiceFactory serverServiceFactory = new ServerServiceFactoryImpl(endpoint, proxyHost, proxyPort);
        final ServerService serverService = serverServiceFactory.createServerService();

        client = new ClientImpl(serverService, executor);
    }

    @Test
    public void happyPath() throws Exception {

        // Index the file
        final IndexStatus indexFileStatus = client.indexFile(origFilesPath + "sample.mov").get();
        assertThat(indexFileStatus.getIndexResult(), is(IndexResult.Succeeded));

        // Get the index status
        final IndexStatus indexStatus = client.fileStatus(origFilesPath + "sample.mov").get();
        assertThat(indexStatus.getIndexResult(), is(IndexResult.Succeeded));

        // Question the timecode
        final QuestionTimecodeParams questionTimecodeParams = new QuestionTimecodeParams(
                origFilesPath + "sample.mov",
                TimeCode.Companion.getTimeCodeForDropFrameRates(LocalTime.of(0, 0, 0), FrameRate.Companion.of("00")),
                TimeCode.Companion.getTimeCodeForDropFrameRates(LocalTime.of(0, 0, 10), FrameRate.Companion.of("00")),
                "29.97");

        final OffsetsStatus offsetsStatus = client.questionTimecode(questionTimecodeParams).get();
        assertThat(offsetsStatus.getOffsetsResult(), is(OffsetsResult.Succeeded));
        assertThat(offsetsStatus.getInBytes(), is("0x0"));
        assertThat(offsetsStatus.getOutBytes(), is("0x3647974"));

        final String outFileName = java.util.UUID.randomUUID().toString();
        // reWrap the partial file
        final ReWrapParams reWrapParams = new ReWrapParams(
                origFilesPath + "sample.mov",
                TimeCode.Companion.getTimeCodeForDropFrameRates(LocalTime.of(1, 0, 0), FrameRate.Companion.of("00")),
                TimeCode.Companion.getTimeCodeForDropFrameRates(LocalTime.of(1, 0, 10), FrameRate.Companion.of("00")),
                "29.97",
                restoredFilesPath + "sample_10sec.mov",
                outFileName);

        final ReWrapResponse reWrapResponse = client.reWrap(reWrapParams).get();
        assertThat(reWrapResponse.getReWrapResult(), is(ReWrapResult.Succeeded));

        // Get the reWrap status
        ReWrapStatus reWrapStatus = client.reWrapStatus(outFileName).get();
        while (reWrapStatus.getPhase() == Phase.Pending  ||
                reWrapStatus.getPhase() == Phase.Parsing ||
                reWrapStatus.getPhase() == Phase.Transferring) {
            TimeUnit.SECONDS.sleep(5);
            reWrapStatus = client.reWrapStatus(outFileName).get();
        }
        assertThat(reWrapStatus.getPhase(), is(Phase.Complete));
    }

    @Test
    public void failedIndexFile() throws Exception {
        final IndexStatus indexStatus = client.indexFile(origFilesPath + "error.mov").get();
        assertThat(indexStatus.getIndexResult(), is(IndexResult.Failed));
        assertThat(indexStatus.getErrorCode(), is("-2132778877"));
        assertThat(indexStatus.getErrorMessage(), is("Failed to parse MOV file [\\\\ISV_RETROSPECT1\\Users\\orig\\error.mov] Error [Null atom discovered in QT movie.]"));
    }

    @Test
    public void indexFileNotFound() throws Exception {
        final IndexStatus indexStatus = client.indexFile(origFilesPath + "not_found_index.mov").get();
        assertThat(indexStatus.getIndexResult(), is(IndexResult.Failed));
        assertThat(indexStatus.getErrorCode(), is("-2132778994"));
        assertThat(indexStatus.getErrorMessage(), is("Failed to parse MOV file [\\\\ISV_RETROSPECT1\\Users\\orig\\not_found_index.mov] Error [Source could not be opened.]"));
    }

    @Test
    public void fileStatusNotFound() throws Exception {
        final IndexStatus indexStatus = client.fileStatus(origFilesPath + "not_found_status.mov").get();
        assertThat(indexStatus.getIndexResult(), is(IndexResult.ErrorFileNotFound));
    }

    @Test
    public void fileStatusNotIndexed() throws Exception {
        final IndexStatus indexStatus = client.fileStatus(origFilesPath + "not_indexed.mov").get();
        assertThat(indexStatus.getIndexResult(), is(IndexResult.NotIndexed));
    }

    @Test
    public void questionTimecodeFileNotFound() throws Exception {
        final TimeCode firstFrame = TimeCode.Companion.getTimeCodeForDropFrameRates(LocalTime.of(0, 0, 0), FrameRate.Companion.of("00"));
        final TimeCode lastFrame = TimeCode.Companion.getTimeCodeForDropFrameRates(LocalTime.of(0, 0, 10), FrameRate.Companion.of("00"));
        final OffsetsStatus offsetsStatus = client.questionTimecode(
                new QuestionTimecodeParams(origFilesPath + "not_found_questionTimecode", firstFrame, lastFrame, "29.97")).get();

        assertThat(offsetsStatus.getOffsetsResult(), is(OffsetsResult.ErrorFileNotFound));
    }

    @Test
    public void reWrapWithBadRestoreFile() throws Exception {
        final TimeCode firstFrame = TimeCode.Companion.getTimeCodeForDropFrameRates(LocalTime.of(0, 0, 0), FrameRate.Companion.of("00"));
        final TimeCode lastFrame = TimeCode.Companion.getTimeCodeForDropFrameRates(LocalTime.of(0, 0, 10), FrameRate.Companion.of("00"));
        final CompletableFuture<ReWrapResponse> errorSampleRestore = client.reWrap(new ReWrapParams(origFilesPath + "sample.mov", firstFrame, lastFrame, "29.97",
                restoredFilesPath + "sample_10sec.mov", "errorSampleRestore"));

        while (!errorSampleRestore.isDone()){
            TimeUnit.SECONDS.sleep(10);
        }

        final ReWrapStatus reWrapStatus = client.reWrapStatus("errorSampleRestore").get();
        assertThat(reWrapStatus.getPhase(), is(Phase.Failed));
        assertThat(reWrapStatus.getPercentComplete(), is(0));
        assertThat(reWrapStatus.getErrorCode(), is("-2132778927"));
        assertThat(reWrapStatus.getErrorMessage(), is("Requested subclip out of bounds."));
    }

    @Test
    public void reWrapErrorBadFrameRate() throws Exception {
        final TimeCode firstFrame = TimeCode.Companion.getTimeCodeForDropFrameRates(LocalTime.of(0, 0, 0), FrameRate.Companion.of("00"));
        final TimeCode lastFrame = TimeCode.Companion.getTimeCodeForDropFrameRates(LocalTime.of(0, 0, 10), FrameRate.Companion.of("00"));
        final ReWrapResponse reWrapResponse = client.reWrap(new ReWrapParams(origFilesPath + "sample.mov", firstFrame, lastFrame, "0",
                restoredFilesPath + "sample_10sec.mov", "sampleRestore")).get();
        assertThat(reWrapResponse.getReWrapResult(), is(ReWrapResult.ErrorBadFramerate));
    }

    @Test
    public void errorReWrapStatus() throws Exception {
        final ReWrapStatus reWrapStatus = client.reWrapStatus(origFilesPath + "not_found_reWrap").get();
        assertNull(reWrapStatus.getPhase());
        assertThat(reWrapStatus.getError(), is("Job not found"));
    }
}
