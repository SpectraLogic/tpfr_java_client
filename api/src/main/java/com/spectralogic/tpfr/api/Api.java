package com.spectralogic.tpfr.api;

import com.spectralogic.tpfr.api.response.IndexStatus;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

interface Api {

    @GET("indexfile")
    Call<IndexStatus> indexFile(@Query("filepath") final String filePath);

    @GET("filestatus")
    Call<IndexStatus> fileStatus(@Query("filepath") final String filePath);

}

