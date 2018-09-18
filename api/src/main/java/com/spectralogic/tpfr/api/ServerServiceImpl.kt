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

package com.spectralogic.tpfr.api

import com.spectralogic.tpfr.api.response.IndexStatusResponse
import com.spectralogic.tpfr.api.response.OffsetsStatusResponse
import com.spectralogic.tpfr.api.response.ReWrapResponse
import com.spectralogic.tpfr.api.response.ReWrapStatusResponse
import okhttp3.ResponseBody
import org.slf4j.LoggerFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException
import kotlin.coroutines.experimental.suspendCoroutine

internal class ServerServiceImpl(private val retrofit: Retrofit, private val api: Api) : ServerService {

    companion object {
        private val LOG = LoggerFactory.getLogger(ServerServiceImpl::class.java)
    }

    override suspend fun indexFile(filePath: String, indexId: String): IndexStatusResponse {
        val response: Response<IndexStatusResponse>
        try {
            response = api.indexFile(filePath, indexId).await()
        } catch (e: IOException) {
            LOG.error("indexFile api call failed with exception", e)
            return IndexStatusResponse("Exception", e)
        }

        if (!response.isSuccessful) {
            LOG.error("indexFile api call failed ({}, {})", response.code(), response.message())
            return IndexStatusResponse("Unknown", response.code().toString(), response.message())
        }

        return response.body()!!
    }

    override suspend fun fileStatus(indexId: String): IndexStatusResponse {
        val response: Response<IndexStatusResponse>
        try {
            response = api.fileStatus(indexId).await()
        } catch (e: IOException) {
            LOG.error("fileStatus api call failed with exception", e)
            return IndexStatusResponse("Exception", e)
        }

        if (!response.isSuccessful) {
            LOG.error("fileStatus api call failed ({}, {})", response.code(), response.message())
            return IndexStatusResponse("Unknown", response.code().toString(), response.message())
        }

        return response.body()!!
    }

    override suspend fun questionTimecode(params: Map<String, String>): OffsetsStatusResponse {
        val response: Response<OffsetsStatusResponse>
        try {
            response = api.questionTimecode(params).await()
        } catch (e: IOException) {
            LOG.error("questionTimecode api call failed with exception", e)
            return OffsetsStatusResponse("Exception", e)
        }

        if (!response.isSuccessful) {
            LOG.error("questionTimecode failed ({}, {})", response.code(), response.message())
            return OffsetsStatusResponse("Unknown", response.code().toString(), response.message())
        }

        return response.body()!!
    }

    override suspend fun reWrap(params: Map<String, String>): ReWrapResponse {
        val response: Response<ReWrapResponse>
        try {
            response = api.reWrap(params).await()
        } catch (e: IOException) {
            LOG.error("reWrap api call failed with exception", e)
            return ReWrapResponse("Exception", e)
        }

        if (!response.isSuccessful) {
            // Due to bad design in Marquis
            if (response.code() == 400 && response.message() == "OK") {
                return try {
                    getErrorBody(response.errorBody()!!, ReWrapResponse::class.java)
                } catch (e: IOException) {
                    LOG.error("Failed to get error body for reWrap api call", e)
                    ReWrapResponse("Exception", e)
                }

            }

            LOG.error("reWrap failed ({}, {})", response.code(), response.message())
            return ReWrapResponse("Unknown", response.code().toString(), response.message())
        }

        return response.body()!!
    }

    override suspend fun reWrapStatus(targetFileName: String): ReWrapStatusResponse {
        val response: Response<ReWrapStatusResponse>
        try {
            response = api.reWrapStatus(targetFileName).await()
        } catch (e: IOException) {
            LOG.error("reWrapStatus api call failed with exception", e)
            return ReWrapStatusResponse("Exception", e)
        }

        if (!response.isSuccessful) {
            LOG.error("reWrapStatus failed ({}, {})", response.code(), response.message())
            return ReWrapStatusResponse("Unknown", response.code().toString(), response.message())
        }

        return response.body()!!
    }


    @Throws(IOException::class)
    private fun <T> getErrorBody(responseBody: ResponseBody, clazz: Class<T>): T {
        val errorConverter = retrofit.responseBodyConverter<T>(clazz, arrayOfNulls(0))

        return errorConverter.convert(responseBody)
    }

    private suspend fun <T> Call<T>.await(): Response<T> =
            suspendCoroutine { cont ->
                this.enqueue(object : Callback<T> {
                    override fun onResponse(call: Call<T>, response: Response<T>) {
                        cont.resume(response)
                    }
                    override fun onFailure(call: Call<T>, t: Throwable) {
                        cont.resumeWithException(t)
                    }
                })
            }
}