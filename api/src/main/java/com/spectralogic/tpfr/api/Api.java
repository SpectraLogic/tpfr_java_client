package com.spectralogic.tpfr.api;

import com.spectralogic.tpfr.api.response.IndexStatusResponse;
import com.spectralogic.tpfr.api.response.OffsetsStatusResponse;
import com.spectralogic.tpfr.api.response.ReWrapResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

import java.util.Map;

interface Api {

    @GET("indexfile")
    Call<IndexStatusResponse> indexFile(@Query("filepath") final String filePath);

    @GET("filestatus")
    Call<IndexStatusResponse> fileStatus(@Query("filepath") final String filePath);

    @GET("fileoffsets")
    Call<OffsetsStatusResponse> questionTimecode(@QueryMap Map<String, String> params);

    @PUT("partialfile")
    Call<ReWrapResponse> reWrap(@QueryMap Map<String, String> params);

}

