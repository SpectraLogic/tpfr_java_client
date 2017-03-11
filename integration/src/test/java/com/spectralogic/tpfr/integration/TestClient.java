package com.spectralogic.tpfr.integration;

import com.spectralogic.tpfr.client.ClientImpl;
import com.spectralogic.tpfr.client.model.*;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

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
    public void indexFile() throws Exception {
        final IndexStatus indexStatus = client.indexFile(path + "sample.mov");
        assertThat(indexStatus.getIndexResult(), is(IndexResult.Succeeded));
    }

    @Ignore("We are ignoring this test due to a a bug in Marquis")
    @Test
    public void indexFileNotFound() throws Exception {
        final IndexStatus indexStatus = client.indexFile(path + "not_found.mov");
        assertThat(indexStatus.getIndexResult(), is(IndexResult.ErrorFileNotFound));
    }

    @Test
    public void fileStatus() throws Exception {
        final IndexStatus indexStatus = client.indexFile(path + "sample.mov");
        assertThat(indexStatus.getIndexResult(), is(IndexResult.Succeeded));
    }

    @Test
    public void fileStatusNotFound() throws Exception {
        final IndexStatus indexStatus = client.indexFile(path + "not_found.mov");
        assertThat(indexStatus.getIndexResult(), is(IndexResult.Failed));
    }

    @Test
    public void questionTimecode() throws Exception {
        final QuestionTimecodeParams params = new QuestionTimecodeParams(
                path + "sample.mov",
                new TimeCode("00:00:00:00"),
                new TimeCode("00:00:10:00"),
                "29.97");

        final OffsetsStatus offsetsStatus = client.QuestionTimecode(params);
        assertThat(offsetsStatus.getOffsetsResult(), is(OffsetsResult.Succeeded));
        assertThat(offsetsStatus.getInBytes(), is("0x0"));
        assertThat(offsetsStatus.getOutBytes(), is("0x3647974"));
    }

    @Test
    public void reWrap() throws Exception {
        final ReWrapParams params = new ReWrapParams(
                path + "sample.mov",
                new TimeCode("00:00:00:00"),
                new TimeCode("00:00:10:00"),
                "29.97",
                path + "sample_10sec.mov",
                "sampleRestore");

        final ReWrapResponse reWrapResponse = client.ReWrap(params);
        assertThat(reWrapResponse.getReWrapResult(), is(ReWrapResult.Succeeded));
    }
}
