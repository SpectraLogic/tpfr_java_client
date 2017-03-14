package com.spectralogic.tpfr.integration;

import com.spectralogic.tpfr.client.ClientImpl;
import com.spectralogic.tpfr.client.model.*;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TestClient {

    private static final String path = "\\\\ISV_RETROSPECT1\\Users\\spectra\\";
    private static ClientImpl client;
    @BeforeClass
    public static void startup() {
        final String ENDPOINT = "http://10.85.41.78:60792";
        client = new ClientImpl(ENDPOINT);
    }

    @Test
    public void happyPath() throws Exception {

        // Index the file
        final IndexStatus indexFileStatus = client.indexFile(path + "sample.mov");
        assertThat(indexFileStatus.getIndexResult(), is(IndexResult.Succeeded));

        // Get the index status
        final IndexStatus indexStatus = client.fileStatus(path + "sample.mov");
        assertThat(indexStatus.getIndexResult(), is(IndexResult.Succeeded));

        // Question the timecode
        final QuestionTimecodeParams questionTimecodeParams = new QuestionTimecodeParams(
                path + "sample.mov",
                new TimeCode("00:00:00:00"),
                new TimeCode("00:00:10:00"),
                "29.97");

        final OffsetsStatus offsetsStatus = client.questionTimecode(questionTimecodeParams);
        assertThat(offsetsStatus.getOffsetsResult(), is(OffsetsResult.Succeeded));
        assertThat(offsetsStatus.getInBytes(), is("0x0"));
        assertThat(offsetsStatus.getOutBytes(), is("0x3647974"));

        final String outFileName = java.util.UUID.randomUUID().toString();
        // reWrap the partial file
        final ReWrapParams reWrapParams = new ReWrapParams(
                path + "sample.mov",
                new TimeCode("01:00:00:00"),
                new TimeCode("01:00:10:00"),
                "29.97",
                path + "sample_10sec.mov",
                outFileName);

        final ReWrapResponse reWrapResponse = client.reWrap(reWrapParams);
        assertThat(reWrapResponse.getReWrapResult(), is(ReWrapResult.Succeeded));

        // Get the reWrap status
        ReWrapStatus reWrapStatus = client.reWrapStatus(outFileName);
        while (reWrapStatus.getPhase() == Phase.Pending) {
            TimeUnit.SECONDS.sleep(5);
            reWrapStatus = client.reWrapStatus(outFileName);
        }
        assertThat(reWrapStatus.getPhase(), is(Phase.Complete));
    }

    @Ignore("We are ignoring this test due to a a bug in Marquis")
    @Test
    public void indexFileNotFound() throws Exception {
        final IndexStatus indexStatus = client.indexFile(path + "not_found.mov");
        assertThat(indexStatus.getIndexResult(), is(IndexResult.ErrorFileNotFound));
    }

    @Test
    public void fileStatusNotFound() throws Exception {
        final IndexStatus indexStatus = client.indexFile(path + "not_found.mov");
        assertThat(indexStatus.getIndexResult(), is(IndexResult.Failed));
    }
}
