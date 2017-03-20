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

package com.spectralogic.tpfr.api;

import com.spectralogic.tpfr.api.response.*;
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

    @GET("partialfilestatus")
    Call<ReWrapStatusResponse> reWrapStatus(@Query("targetpartialname") final String targetFileName);

}

