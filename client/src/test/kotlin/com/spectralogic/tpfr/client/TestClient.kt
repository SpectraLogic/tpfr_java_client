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

package com.spectralogic.tpfr.client

import com.google.common.collect.ImmutableMap
import com.spectralogic.tpfr.api.ServerServiceFactoryImpl
import com.spectralogic.tpfr.client.model.*
import com.squareup.okhttp.mockwebserver.MockResponse
import com.squareup.okhttp.mockwebserver.MockWebServer
import org.assertj.core.api.Fail.fail
import org.assertj.core.api.KotlinAssertions.assertThat
import org.junit.BeforeClass
import org.junit.Test
import java.time.LocalTime
class TestClient {

    companion object {

        private var client: Client? = null
        private var server: MockWebServer = MockWebServer()

        @BeforeClass @JvmStatic
        fun startup() {
            server.start()

            val endpoint = server.url("/").toString()
            val proxyHost = ""
            val proxyPort = 0

            val serverServiceFactory = ServerServiceFactoryImpl(endpoint, proxyHost, proxyPort)
            val serverService = serverServiceFactory.createServerService()

            client = ClientImpl(serverService)
        }
    }

    @Test
    fun successfulIndexFile() {
        server.enqueue(MockResponse()
                .setResponseCode(200)
                .setBody(ReadFileFromResources.readFile("xml/index/SuccessfulIndexFileOrFileStatusCall.xml")))

        val indexStatus = client!!.indexFile("filePath").get()
        assertThat(indexStatus.indexResult).isEqualTo(IndexResult.Succeeded)
        assertThat(indexStatus.indexTime).isEqualTo("2011/10/21 11:40:53")
        assertThat(indexStatus.fileStartTc).isEqualTo("01:00:00;00")
        assertThat(indexStatus.fileDuration).isEqualTo("1800")
        assertThat(indexStatus.fileFrameRate).isEqualTo("29.97")
    }

    @Test
    fun successfulFileStatus() {
        server.enqueue(MockResponse()
                .setResponseCode(200)
                .setBody(ReadFileFromResources.readFile("xml/index/SuccessfulIndexFileOrFileStatusCall.xml")))

        val indexStatus = client!!.fileStatus("filePath").get()
        assertThat(indexStatus.indexResult).isEqualTo(IndexResult.Succeeded)
        assertThat(indexStatus.indexTime).isEqualTo("2011/10/21 11:40:53")
        assertThat(indexStatus.fileStartTc).isEqualTo("01:00:00;00")
        assertThat(indexStatus.fileDuration).isEqualTo("1800")
        assertThat(indexStatus.fileFrameRate).isEqualTo("29.97")
    }

    @Test
    @Throws(Exception::class)
    fun failedIndexFile() {
        server.enqueue(MockResponse()
                .setResponseCode(200)
                .setBody(ReadFileFromResources.readFile("xml/index/FailedToIndex.xml")))

        val indexStatus = client!!.indexFile("filePath").get()
        assertThat(indexStatus.indexResult).isEqualTo(IndexResult.Failed)
        assertThat(indexStatus.indexTime).isEqualTo("2011/10/21 15:30:15")
        assertThat(indexStatus.errorCode).isEqualTo("400")
        assertThat(indexStatus.errorMessage).isEqualTo("Failed to index")
    }

    @Test
    @Throws(Exception::class)
    fun failedIndexStatus() {
        server.enqueue(MockResponse()
                .setResponseCode(200)
                .setBody(ReadFileFromResources.readFile("xml/index/FailedToIndex.xml")))

        val indexStatus = client!!.fileStatus("filePath").get()
        assertThat(indexStatus.indexResult).isEqualTo(IndexResult.Failed)
        assertThat(indexStatus.indexTime).isEqualTo("2011/10/21 15:30:15")
        assertThat(indexStatus.errorCode).isEqualTo("400")
        assertThat(indexStatus.errorMessage).isEqualTo("Failed to index")
    }

    @Test
    @Throws(Exception::class)
    fun fileStatusIndexing() {
        server.enqueue(MockResponse()
                .setResponseCode(200)
                .setBody(ReadFileFromResources.readFile("xml/index/FileStatusIndexing.xml")))

        val indexStatus = client!!.fileStatus("filePath").get()
        assertThat(indexStatus.indexResult).isEqualTo(IndexResult.Indexing)
    }

    @Test
    @Throws(Exception::class)
    fun fileNotIndexedFileStatus() {
        server.enqueue(MockResponse()
                .setResponseCode(200)
                .setBody(ReadFileFromResources.readFile("xml/index/FileStatusWhenFileNotIndexed.xml")))

        val indexStatus = client!!.fileStatus("filePath").get()
        assertThat(indexStatus.indexResult).isEqualTo(IndexResult.NotIndexed)
    }

    @Test
    @Throws(Exception::class)
    fun fileNotFoundFileStatus() {
        server.enqueue(MockResponse()
                .setResponseCode(200)
                .setBody(ReadFileFromResources.readFile("xml/index/FileStatusWhenFileNotPresent.xml")))

        val indexStatus = client!!.fileStatus("filePath").get()
        assertThat(indexStatus.indexResult).isEqualTo(IndexResult.ErrorFileNotFound)
    }

    @Test
    @Throws(Exception::class)
    fun succeededQuestionTimecode() {
        server.enqueue(MockResponse()
                .setResponseCode(200)
                .setBody(ReadFileFromResources.readFile("xml/fileOffset/GoodFileOffsetsCall.xml")))

        val firstFrame = TimeCode.getTimeCodeForDropFrameRates(LocalTime.of(0, 0, 0), FrameRate.of("00"))
        val lastFrame = TimeCode.getTimeCodeForDropFrameRates(LocalTime.of(0, 0, 10), FrameRate.of("00"))
        val offsetsStatus = client!!.questionTimecode(QuestionTimecodeParams("filePath", firstFrame, lastFrame, "0")).get()
        assertThat(offsetsStatus.offsetsResult).isEqualTo(OffsetsResult.Succeeded)
        assertThat(offsetsStatus.inBytes).isEqualTo("0x0060000")
        assertThat(offsetsStatus.outBytes).isEqualTo("0x0080000")
    }

    @Test
    @Throws(Exception::class)
    fun succeededQuestionTimecodeAskingForLastFrame() {
        server.enqueue(MockResponse()
                .setResponseCode(200)
                .setBody(ReadFileFromResources.readFile("xml/fileOffset/GoodFileOffsetsCallAskingForLastFrame.xml")))

        val firstFrame = TimeCode.getTimeCodeForDropFrameRates(LocalTime.of(0, 0, 0), FrameRate.of("00"))
        val lastFrame = TimeCode.getTimeCodeForDropFrameRates(LocalTime.of(0, 0, 10), FrameRate.of("00"))
        val offsetsStatus = client!!.questionTimecode(QuestionTimecodeParams("filePath", firstFrame, lastFrame, "0")).get()
        assertThat(offsetsStatus.offsetsResult).isEqualTo(OffsetsResult.Succeeded)
        assertThat(offsetsStatus.inBytes).isEqualTo("0x7c28014")
        assertThat(offsetsStatus.outBytes).isEqualTo("0xffffffffffffffff")
    }

    @Test
    @Throws(Exception::class)
    fun fileNotFoundQuestionTimecode() {
        server.enqueue(MockResponse()
                .setResponseCode(200)
                .setBody(ReadFileFromResources.readFile("xml/fileOffset/FileNotFoundOffsetsCall.xml")))

        val firstFrame = TimeCode.getTimeCodeForDropFrameRates(LocalTime.of(0, 0, 0), FrameRate.of("00"))
        val lastFrame = TimeCode.getTimeCodeForDropFrameRates(LocalTime.of(0, 0, 10), FrameRate.of("00"))
        val offsetsStatus = client!!.questionTimecode(QuestionTimecodeParams("filePath", firstFrame, lastFrame, "0")).get()
        assertThat(offsetsStatus.offsetsResult).isEqualTo(OffsetsResult.ErrorFileNotFound)
    }

    @Test
    fun reWrap() {
        val testSource = ImmutableMap.Builder<String, ReWrapResult>()
                .put("SuccessfulReWrap.xml", ReWrapResult.Succeeded)
                .put("DuplicateParameter.xml", ReWrapResult.ErrorDuplicateParameter)
                .put("MissingParameter.xml", ReWrapResult.ErrorMissingParameter)
                .put("IncorrectFrameRate.xml", ReWrapResult.ErrorBadFramerate)
                .build()

        testSource.forEach { k, v ->
            server.enqueue(MockResponse()
                    .setResponseCode(200)
                    .setBody(ReadFileFromResources.readFile("xml/reWrap/" + k)))

            try {
                val firstFrame = TimeCode.getTimeCodeForDropFrameRates(LocalTime.of(0, 0, 0), FrameRate.of("00"))
                val lastFrame = TimeCode.getTimeCodeForDropFrameRates(LocalTime.of(0, 0, 10), FrameRate.of("00"))
                val reWrapResponse = client!!.reWrap(ReWrapParams("filePath", firstFrame, lastFrame, "0", "partialFilePath", "outputFileName")).get()
                assertThat(reWrapResponse.reWrapResult).isEqualTo(v)
            } catch (e: Exception) {
                fail(String.format("%s failed with error: %s", k, e.message))
            }
        }
    }

    @Test
    fun reWrapStatus() {
        val testSource = ImmutableMap.Builder<String, ReWrapStatusExpected>()
                .put("JobPending.xml", ReWrapStatusExpected(Phase.Pending, 0, null, null))
                .put("JobParsing.xml", ReWrapStatusExpected(Phase.Parsing, 25, null, null))
                .put("JobTransferring.xml", ReWrapStatusExpected(Phase.Transferring, 50, null, null))
                .put("JobComplete.xml", ReWrapStatusExpected(Phase.Complete, 100, null, null))
                .put("JobFailed.xml", ReWrapStatusExpected(Phase.Failed, 0, "-2132778983", "Failed to create file"))
                .build()

        testSource.forEach { k, (phase, percentComplete, errorCode, errorMessage) ->
            server.enqueue(MockResponse()
                    .setResponseCode(200)
                    .setBody(ReadFileFromResources.readFile("xml/reWrapStatus/" + k)))

            try {
                val reWrapStatus = client!!.reWrapStatus("outputFileName").get()
                assertThat(reWrapStatus.phase).isEqualTo(phase)
                assertThat(reWrapStatus.percentComplete).isEqualTo(percentComplete)
                assertThat(reWrapStatus.errorCode).isEqualTo(errorCode)
                assertThat(reWrapStatus.errorMessage).isEqualTo(errorMessage)
            } catch (e: Exception) {
                fail(String.format("%s failed with error: %s", k, e.message))
            }
        }
    }

    @Test
    @Throws(Exception::class)
    fun reWrapError() {
        server.enqueue(MockResponse()
                .setResponseCode(200)
                .setBody(ReadFileFromResources.readFile("xml/reWrapStatus/PartialFileStatusError.xml")))

        val reWrapStatus = client!!.reWrapStatus("outFileName").get()
        assertThat(reWrapStatus.error).isEqualTo("Job not found")
    }
}