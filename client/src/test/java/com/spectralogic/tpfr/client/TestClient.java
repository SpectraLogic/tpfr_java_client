package com.spectralogic.tpfr.client;

import com.spectralogic.tpfr.api.response.IndexResult;
import com.spectralogic.tpfr.api.response.IndexStatus;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;

import static org.junit.Assert.assertThat;

public class TestClient {

    private static final String Path = "\\\\ISV_RETROSPECT1\\Users\\spectra\\";
    private static ClientImpl client;
    @BeforeClass
    public static void startup() {
        final String ENDPOINT = "http://10.85.41.78:60792";
        client = new ClientImpl(ENDPOINT);
    }

    @Test
    public void indexFile() throws Exception {
        final IndexStatus indexStatus = client.indexFile(Path + "sample.mov");
        assertThat(indexStatus.indexResult, is(IndexResult.Succeeded));
    }

    @Test
    public void indexFileNotFound() throws Exception {
        final IndexStatus indexStatus = client.indexFile(Path + "not_found.mov");
        assertThat(indexStatus.indexResult, is(IndexResult.ErrorFileNotFound));
    }
}
