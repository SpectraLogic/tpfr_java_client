package com.spectralogic.tpfr.api;

import com.spectralogic.tpfr.api.response.IndexStatus;
import com.spectralogic.tpfr.api.response.OffsetsStatus;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

import java.util.Map;

interface Api {

    @GET("indexfile")
    Call<IndexStatus> indexFile(@Query("filepath") final String filePath);

    @GET("filestatus")
    Call<IndexStatus> fileStatus(@Query("filepath") final String filePath);

    @GET("fileoffsets")
    Call<OffsetsStatus> questionTimecode(@QueryMap Map<String, String> params);

}

