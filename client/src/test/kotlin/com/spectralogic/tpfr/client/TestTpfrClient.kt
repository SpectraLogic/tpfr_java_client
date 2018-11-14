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
import kotlinx.coroutines.experimental.runBlocking
import org.assertj.core.api.Fail.fail
import org.assertj.core.api.KotlinAssertions.assertThat
import org.junit.BeforeClass
import org.junit.Test
import java.time.LocalTime
import java.util.*

class TestTpfrClient {

    companion object {

        private lateinit var tpfrClient: TpfrClient
        private var server: MockWebServer = MockWebServer()

        @BeforeClass
        @JvmStatic
        fun startup() {
            server.start()

            val endpoint = server.url("/").toString()
            val proxyHost = ""
            val proxyPort = 0

            val serverServiceFactory = ServerServiceFactoryImpl(endpoint, proxyHost, proxyPort)
            val serverService = serverServiceFactory.createServerService()

            tpfrClient = TpfrClientImpl(serverService)
        }
    }

    @Test
    fun successfulIndexFile() {
        server.enqueue(MockResponse()
                .setResponseCode(200)
                .setBody(ReadFileFromResources.readFile("xml/index/SuccessfulIndexFileOrFileStatusCall.xml")))

        runBlocking {
            val indexStatus = tpfrClient.indexFile("filePath", UUID.randomUUID().toString())
            assertThat(indexStatus.indexResult).isEqualTo(IndexResult.Succeeded)
            assertThat(indexStatus.indexTime).isEqualTo("2011/10/21 11:40:53")
            assertThat(indexStatus.fileStartTc).isEqualTo("01:00:00;00")
            assertThat(indexStatus.fileDuration).isEqualTo("1800")
            assertThat(indexStatus.fileFrameRate).isEqualTo("29.97")
            assertThat(indexStatus.originalFile).isEqualTo("\\\\Server\\share\\File.mxf")
            assertThat(indexStatus.indexID).isEqualTo("288fc700-b52c-11e8-96f8-529269fb1459")
        }
    }

    @Test
    fun notSuccessfulIndexFile() {
        server.enqueue(MockResponse()
                .setResponseCode(404))

        runBlocking {
            val indexStatus = tpfrClient.indexFile("filePath", UUID.randomUUID().toString())
            assertThat(indexStatus.indexResult).isEqualTo(IndexResult.Unknown)
            assertThat(indexStatus.errorCode).isEqualTo("404")
            assertThat(indexStatus.errorMessage).isEqualTo("OK")
        }
    }

    @Test
    fun successfulFileStatus() {
        server.enqueue(MockResponse()
                .setResponseCode(200)
                .setBody(ReadFileFromResources.readFile("xml/index/SuccessfulIndexFileOrFileStatusCall.xml")))

        runBlocking {
            val indexStatus = tpfrClient.fileStatus(UUID.randomUUID().toString())
            assertThat(indexStatus.indexResult).isEqualTo(IndexResult.Succeeded)
            assertThat(indexStatus.indexTime).isEqualTo("2011/10/21 11:40:53")
            assertThat(indexStatus.fileStartTc).isEqualTo("01:00:00;00")
            assertThat(indexStatus.fileDuration).isEqualTo("1800")
            assertThat(indexStatus.originalFile).isEqualTo("\\\\Server\\share\\File.mxf")
            assertThat(indexStatus.indexID).isEqualTo("288fc700-b52c-11e8-96f8-529269fb1459")
        }
    }

    @Test
    fun notSuccessfulFileStatus() {
        server.enqueue(MockResponse()
                .setResponseCode(404))

        runBlocking {
            val indexStatus = tpfrClient.fileStatus(UUID.randomUUID().toString())
            assertThat(indexStatus.indexResult).isEqualTo(IndexResult.Unknown)
            assertThat(indexStatus.errorCode).isEqualTo("404")
            assertThat(indexStatus.errorMessage).isEqualTo("OK")
        }
    }

    @Test
    fun failedIndexFile() {
        server.enqueue(MockResponse()
                .setResponseCode(200)
                .setBody(ReadFileFromResources.readFile("xml/index/FailedToIndex.xml")))

        runBlocking {
            val indexStatus = tpfrClient.indexFile("filePath", UUID.randomUUID().toString())
            assertThat(indexStatus.indexResult).isEqualTo(IndexResult.Failed)
            assertThat(indexStatus.indexTime).isEqualTo("2011/10/21 15:30:15")
            assertThat(indexStatus.errorCode).isEqualTo("400")
            assertThat(indexStatus.errorMessage).isEqualTo("Failed to index")
        }
    }

    @Test
    fun failedIndexStatus() {
        server.enqueue(MockResponse()
                .setResponseCode(200)
                .setBody(ReadFileFromResources.readFile("xml/index/FailedToIndex.xml")))

        runBlocking {
            val indexStatus = tpfrClient.fileStatus(UUID.randomUUID().toString())
            assertThat(indexStatus.indexResult).isEqualTo(IndexResult.Failed)
            assertThat(indexStatus.indexTime).isEqualTo("2011/10/21 15:30:15")
            assertThat(indexStatus.errorCode).isEqualTo("400")
            assertThat(indexStatus.errorMessage).isEqualTo("Failed to index")
        }
    }

    @Test
    fun fileStatusIndexing() {
        server.enqueue(MockResponse()
                .setResponseCode(200)
                .setBody(ReadFileFromResources.readFile("xml/index/FileStatusIndexing.xml")))

        runBlocking {
            val indexStatus = tpfrClient.fileStatus(UUID.randomUUID().toString())
            assertThat(indexStatus.indexResult).isEqualTo(IndexResult.Indexing)
        }
    }

    @Test
    fun fileNotIndexedFileStatus() {
        server.enqueue(MockResponse()
                .setResponseCode(200)
                .setBody(ReadFileFromResources.readFile("xml/index/FileStatusWhenFileNotIndexed.xml")))

        runBlocking {
            val indexStatus = tpfrClient.fileStatus(UUID.randomUUID().toString())
            assertThat(indexStatus.indexResult).isEqualTo(IndexResult.NotIndexed)
        }
    }

    @Test
    fun succeededQuestionTimecode() {
        server.enqueue(MockResponse()
                .setResponseCode(200)
                .setBody(ReadFileFromResources.readFile("xml/fileOffset/GoodFileOffsetsCall.xml")))

        runBlocking {
            val firstFrame = TimeCode.getTimeCodeForDropFrameRates(LocalTime.of(0, 0, 0), FrameRate.of("00"))
            val lastFrame = TimeCode.getTimeCodeForDropFrameRates(LocalTime.of(0, 0, 10), FrameRate.of("00"))
            val offsetsStatus = tpfrClient.questionTimecode(QuestionTimecodeParams(UUID.randomUUID().toString(), firstFrame, lastFrame, "0"))
            assertThat(offsetsStatus.offsetsResult).isEqualTo(OffsetsResult.Succeeded)
            assertThat(offsetsStatus.inBytes).isEqualTo("0x0060000")
            assertThat(offsetsStatus.outBytes).isEqualTo("0x0080000")
        }
    }

    @Test
    fun didNotSucceededQuestionTimecode() {
        server.enqueue(MockResponse()
                .setResponseCode(404))

        runBlocking {
            val firstFrame = TimeCode.getTimeCodeForDropFrameRates(LocalTime.of(0, 0, 0), FrameRate.of("00"))
            val lastFrame = TimeCode.getTimeCodeForDropFrameRates(LocalTime.of(0, 0, 10), FrameRate.of("00"))
            val offsetsStatus = tpfrClient.questionTimecode(QuestionTimecodeParams(UUID.randomUUID().toString(), firstFrame, lastFrame, "0"))
            assertThat(offsetsStatus.offsetsResult).isEqualTo(OffsetsResult.Unknown)
            assertThat(offsetsStatus.errorCode).isEqualTo("404")
            assertThat(offsetsStatus.errorMessage).isEqualTo("OK")
        }
    }

    @Test
    fun succeededQuestionTimecodeAskingForLastFrame() {
        server.enqueue(MockResponse()
                .setResponseCode(200)
                .setBody(ReadFileFromResources.readFile("xml/fileOffset/GoodFileOffsetsCallAskingForLastFrame.xml")))

        runBlocking {
            val firstFrame = TimeCode.getTimeCodeForDropFrameRates(LocalTime.of(0, 0, 0), FrameRate.of("00"))
            val lastFrame = TimeCode.getTimeCodeForDropFrameRates(LocalTime.of(0, 0, 10), FrameRate.of("00"))
            val offsetsStatus = tpfrClient.questionTimecode(QuestionTimecodeParams(UUID.randomUUID().toString(), firstFrame, lastFrame, "0"))
            assertThat(offsetsStatus.offsetsResult).isEqualTo(OffsetsResult.Succeeded)
            assertThat(offsetsStatus.inBytes).isEqualTo("0x7c28014")
            assertThat(offsetsStatus.outBytes).isEqualTo("0xffffffffffffffff")
        }
    }

    @Test
    fun fileNotFoundQuestionTimecode() {
        server.enqueue(MockResponse()
                .setResponseCode(200)
                .setBody(ReadFileFromResources.readFile("xml/fileOffset/FileNotFoundOffsetsCall.xml")))

        runBlocking {
            val firstFrame = TimeCode.getTimeCodeForDropFrameRates(LocalTime.of(0, 0, 0), FrameRate.of("00"))
            val lastFrame = TimeCode.getTimeCodeForDropFrameRates(LocalTime.of(0, 0, 10), FrameRate.of("00"))
            val offsetsStatus = tpfrClient.questionTimecode(QuestionTimecodeParams(UUID.randomUUID().toString(), firstFrame, lastFrame, "0"))
            assertThat(offsetsStatus.offsetsResult).isEqualTo(OffsetsResult.ErrorFileNotFound)
        }
    }

    @Test
    fun reWrap() {
        val testSource = ImmutableMap.Builder<String, ReWrapResult>()
                .put("SuccessfulReWrap.xml", ReWrapResult.Succeeded)
                .put("DuplicateParameter.xml", ReWrapResult.ErrorDuplicateParameter)
                .put("MissingParameter.xml", ReWrapResult.ErrorMissingParameter)
                .put("MustBeUNC.xml", ReWrapResult.ErrorOutFileNameMustBeUNC)
                .build()

        testSource.forEach { k, v ->
            server.enqueue(MockResponse()
                    .setResponseCode(200)
                    .setBody(ReadFileFromResources.readFile("xml/reWrap/$k")))

            try {
                val firstFrame = TimeCode.getTimeCodeForDropFrameRates(LocalTime.of(0, 0, 0), FrameRate.of("00"))
                val lastFrame = TimeCode.getTimeCodeForDropFrameRates(LocalTime.of(0, 0, 10), FrameRate.of("00"))
                runBlocking {
                    val reWrapResponse = tpfrClient.reWrap(ReWrapParams(UUID.randomUUID().toString(), firstFrame, lastFrame, "0", "partialFilePath", "outputFileName"))
                    assertThat(reWrapResponse.reWrapResult).isEqualTo(v)
                }
            } catch (e: Exception) {
                fail(String.format("%s failed with error: %s", k, e.message))
            }
        }
    }

    @Test
    fun reWrap400() {
        server.enqueue(MockResponse()
                .setResponseCode(400)
                .setBody(ReadFileFromResources.readFile("xml/reWrap/IncorrectFrameRate.xml")))

        runBlocking {
            val firstFrame = TimeCode.getTimeCodeForDropFrameRates(LocalTime.of(0, 0, 0), FrameRate.of("00"))
            val lastFrame = TimeCode.getTimeCodeForDropFrameRates(LocalTime.of(0, 0, 10), FrameRate.of("00"))
            val reWrapResponse = tpfrClient.reWrap(ReWrapParams(UUID.randomUUID().toString(), firstFrame, lastFrame, "0", "partialFilePath", "outputFileName"))
            assertThat(reWrapResponse.reWrapResult).isEqualTo(ReWrapResult.ErrorBadFramerate)
        }
    }

    @Test
    fun reWrap404() {
        server.enqueue(MockResponse()
                .setResponseCode(404))

        runBlocking {
            val firstFrame = TimeCode.getTimeCodeForDropFrameRates(LocalTime.of(0, 0, 0), FrameRate.of("00"))
            val lastFrame = TimeCode.getTimeCodeForDropFrameRates(LocalTime.of(0, 0, 10), FrameRate.of("00"))
            val reWrapResponse = tpfrClient.reWrap(ReWrapParams(UUID.randomUUID().toString(), firstFrame, lastFrame, "0", "partialFilePath", "outputFileName"))
            assertThat(reWrapResponse.reWrapResult).isEqualTo(ReWrapResult.Unknown)
            assertThat(reWrapResponse.errorCode).isEqualTo("404")
            assertThat(reWrapResponse.errorMessage).isEqualTo("OK")
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
                    .setBody(ReadFileFromResources.readFile("xml/reWrapStatus/$k")))

            try {
                runBlocking {
                    val reWrapStatus = tpfrClient.reWrapStatus("outputFileName")
                    assertThat(reWrapStatus.phase).isEqualTo(phase)
                    assertThat(reWrapStatus.percentComplete).isEqualTo(percentComplete)
                    assertThat(reWrapStatus.errorCode).isEqualTo(errorCode)
                    assertThat(reWrapStatus.errorMessage).isEqualTo(errorMessage)
                }
            } catch (e: Exception) {
                fail(String.format("%s failed with error: %s", k, e.message))
            }
        }
    }

    @Test
    fun reWrapStatusError() {
        server.enqueue(MockResponse()
                .setResponseCode(200)
                .setBody(ReadFileFromResources.readFile("xml/reWrapStatus/PartialFileStatusError.xml")))

        runBlocking {
            val reWrapStatus = tpfrClient.reWrapStatus("outFileName")
            assertThat(reWrapStatus.error).isEqualTo("Job not found")
        }
    }

    @Test
    fun reWrapStatus404() {
        server.enqueue(MockResponse()
                .setResponseCode(404))

        runBlocking {
            val reWrapStatus = tpfrClient.reWrapStatus("outFileName")
            assertThat(reWrapStatus.phase).isEqualTo(Phase.Unknown)
            assertThat(reWrapStatus.errorCode).isEqualTo("404")
            assertThat(reWrapStatus.errorMessage).isEqualTo("OK")
        }
    }
}