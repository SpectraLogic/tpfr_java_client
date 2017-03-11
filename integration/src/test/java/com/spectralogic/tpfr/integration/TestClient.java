package com.spectralogic.tpfr.integration;

import com.spectralogic.tpfr.api.response.IndexResult;
import com.spectralogic.tpfr.api.response.IndexStatus;
import com.spectralogic.tpfr.api.response.OffsetsResult;
import com.spectralogic.tpfr.api.response.OffsetsStatus;
import com.spectralogic.tpfr.client.ClientImpl;
import com.spectralogic.tpfr.client.model.QuestionTimecodeParams;
import com.spectralogic.tpfr.client.model.TimeCode;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

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
        Assert.assertThat(indexStatus.indexResult, CoreMatchers.is(IndexResult.Succeeded));
    }

    @Ignore("We are ignoring this test due to a a bug in Marquis")
    @Test
    public void indexFileNotFound() throws Exception {
        final IndexStatus indexStatus = client.indexFile(path + "not_found.mov");
        Assert.assertThat(indexStatus.indexResult, CoreMatchers.is(IndexResult.ErrorFileNotFound));
    }

    @Test
    public void fileStatus() throws Exception {
        final IndexStatus indexStatus = client.indexFile(path + "sample.mov");
        Assert.assertThat(indexStatus.indexResult, CoreMatchers.is(IndexResult.Succeeded));
    }

    @Test
    public void fileStatusNotFound() throws Exception {
        final IndexStatus indexStatus = client.indexFile(path + "not_found.mov");
        Assert.assertThat(indexStatus.indexResult, CoreMatchers.is(IndexResult.Failed));
    }

    @Test
    public void questionTimecode() throws Exception {
        final QuestionTimecodeParams params = new QuestionTimecodeParams(
                path + "sample.move",
                new TimeCode("00:00:00:00"),
                new TimeCode("00:00:10:00"),
                "29.97");

        final OffsetsStatus offsetsStatus = client.QuestionTimecode(params);
        Assert.assertThat(offsetsStatus.offsetsResult, CoreMatchers.is(OffsetsResult.Succeeded));
        Assert.assertThat(offsetsStatus.inBytes, CoreMatchers.is("0x0"));
        Assert.assertThat(offsetsStatus.outBytes, CoreMatchers.is("0x3647974"));
    }
}
