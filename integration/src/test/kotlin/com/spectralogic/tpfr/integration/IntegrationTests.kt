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

package com.spectralogic.tpfr.integration

import com.spectralogic.tpfr.api.ServerServiceFactoryImpl
import com.spectralogic.tpfr.client.Client
import com.spectralogic.tpfr.client.ClientImpl
import com.spectralogic.tpfr.client.model.*
import org.assertj.core.api.KotlinAssertions.assertThat
import org.junit.BeforeClass
import org.junit.Test
import java.time.LocalTime
import java.util.concurrent.ForkJoinPool
import java.util.concurrent.TimeUnit
class IntegrationTests {

    companion object {

        private val origFilesPath = "\\\\ISV_RETROSPECT1\\Users\\orig\\"
        private val restoredFilesPath = "\\\\ISV_RETROSPECT1\\Users\\restored\\"
        private lateinit var client: Client
        private val numberOfThreads = 12

        @BeforeClass @JvmStatic
        fun startup() {
            val endpoint = "http://10.85.41.78:60792"
            val proxyHost = ""
            val proxyPort = 0

            val serverServiceFactory = ServerServiceFactoryImpl(endpoint, proxyHost, proxyPort)
            val serverService = serverServiceFactory.createServerService()

            val executor = ForkJoinPool(numberOfThreads)
            client = ClientImpl(serverService, executor)
        }
    }

    @Test
    fun happyPath() {

        // Index the file
        val indexFileStatus = client.indexFile(origFilesPath + "sample.mov").get()
        assertThat(indexFileStatus.indexResult).isEqualTo(IndexResult.Succeeded)

        // Get the index status
        val indexStatus = client.fileStatus(origFilesPath + "sample.mov").get()
        assertThat(indexStatus.indexResult).isEqualTo(IndexResult.Succeeded)

        // Question the timecode
        val questionTimecodeParams = QuestionTimecodeParams(
                origFilesPath + "sample.mov",
                TimeCode.getTimeCodeForDropFrameRates(LocalTime.of(0, 0, 0), FrameRate.of("00")),
                TimeCode.getTimeCodeForDropFrameRates(LocalTime.of(0, 0, 10), FrameRate.of("00")),
                "29.97")

        val offsetsStatus = client.questionTimecode(questionTimecodeParams).get()
        assertThat(offsetsStatus.offsetsResult).isEqualTo(OffsetsResult.Succeeded)
        assertThat(offsetsStatus.inBytes).isEqualTo("0x0")
        assertThat(offsetsStatus.outBytes).isEqualTo("0x3647974")

        val outFileName = java.util.UUID.randomUUID().toString()
        // reWrap the partial file
        val reWrapParams = ReWrapParams(
                origFilesPath + "sample.mov",
                TimeCode.getTimeCodeForDropFrameRates(LocalTime.of(1, 0, 0), FrameRate.of("00")),
                TimeCode.getTimeCodeForDropFrameRates(LocalTime.of(1, 0, 10), FrameRate.of("00")),
                "29.97",
                restoredFilesPath + "sample_10sec.mov",
                outFileName)

        val reWrapResponse = client.reWrap(reWrapParams).get()
        assertThat(reWrapResponse.reWrapResult).isEqualTo(ReWrapResult.Succeeded)

        // Get the reWrap status
        var reWrapStatus = client.reWrapStatus(outFileName).get()
        while (reWrapStatus.phase === Phase.Pending ||
                reWrapStatus.phase === Phase.Parsing ||
                reWrapStatus.phase === Phase.Transferring) {
            TimeUnit.SECONDS.sleep(5)
            reWrapStatus = client.reWrapStatus(outFileName).get()
        }
        assertThat(reWrapStatus.phase).isEqualTo(Phase.Complete)
    }

    @Test
    fun failedIndexFile() {
        val indexStatus = client.indexFile(origFilesPath + "error.mov").get()
        assertThat(indexStatus.indexResult).isEqualTo(IndexResult.Failed)
        assertThat(indexStatus.errorCode).isEqualTo("-2132778877")
        assertThat(indexStatus.errorMessage).isEqualTo("Failed to parse MOV file [\\\\ISV_RETROSPECT1\\Users\\orig\\error.mov] Error [Null atom discovered in QT movie.]")
    }

    @Test
    fun indexFileNotFound() {
        val indexStatus = client.indexFile(origFilesPath + "not_found_index.mov").get()
        assertThat(indexStatus.indexResult).isEqualTo(IndexResult.Failed)
        assertThat(indexStatus.errorCode).isEqualTo("-2132778994")
        assertThat(indexStatus.errorMessage).isEqualTo("Failed to parse MOV file [\\\\ISV_RETROSPECT1\\Users\\orig\\not_found_index.mov] Error [Source could not be opened.]")
    }

    @Test
    fun fileStatusNotFound() {
        val indexStatus = client.fileStatus(origFilesPath + "not_found_status.mov").get()
        assertThat(indexStatus.indexResult).isEqualTo(IndexResult.ErrorFileNotFound)
    }

    @Test
    fun fileStatusNotIndexed() {
        val indexStatus = client.fileStatus(origFilesPath + "not_indexed.mov").get()
        assertThat(indexStatus.indexResult).isEqualTo(IndexResult.NotIndexed)
    }

    @Test
    fun questionTimecodeFileNotFound() {
        val firstFrame = TimeCode.getTimeCodeForDropFrameRates(LocalTime.of(0, 0, 0), FrameRate.of("00"))
        val lastFrame = TimeCode.getTimeCodeForDropFrameRates(LocalTime.of(0, 0, 10), FrameRate.of("00"))
        val offsetsStatus = client.questionTimecode(
                QuestionTimecodeParams(origFilesPath + "not_found_questionTimecode", firstFrame, lastFrame, "29.97")).get()

        assertThat(offsetsStatus.offsetsResult).isEqualTo(OffsetsResult.ErrorFileNotFound)
    }

    @Test
    fun reWrapWithBadRestoreFile() {
        val firstFrame = TimeCode.getTimeCodeForDropFrameRates(LocalTime.of(0, 0, 0), FrameRate.of("00"))
        val lastFrame = TimeCode.getTimeCodeForDropFrameRates(LocalTime.of(0, 0, 10), FrameRate.of("00"))
        val errorSampleRestore = client.reWrap(ReWrapParams(origFilesPath + "sample.mov", firstFrame, lastFrame, "29.97",
                restoredFilesPath + "sample_10sec.mov", "errorSampleRestore"))

        while (!errorSampleRestore.isDone) {
            TimeUnit.SECONDS.sleep(10)
        }

        val reWrapStatus = client.reWrapStatus("errorSampleRestore").get()
        assertThat(reWrapStatus.phase).isEqualTo(Phase.Failed)
        assertThat(reWrapStatus.percentComplete).isEqualTo(0)
        assertThat(reWrapStatus.errorCode).isEqualTo("-2132778927")
        assertThat(reWrapStatus.errorMessage).isEqualTo("Requested subclip out of bounds.")
    }

    @Test
    fun reWrapErrorBadFrameRate() {
        val firstFrame = TimeCode.getTimeCodeForDropFrameRates(LocalTime.of(0, 0, 0), FrameRate.of("00"))
        val lastFrame = TimeCode.getTimeCodeForDropFrameRates(LocalTime.of(0, 0, 10), FrameRate.of("00"))
        val reWrapResponse = client.reWrap(ReWrapParams(origFilesPath + "sample.mov", firstFrame, lastFrame, "0",
                restoredFilesPath + "sample_10sec.mov", "sampleRestore")).get()
        assertThat(reWrapResponse.reWrapResult).isEqualTo(ReWrapResult.ErrorBadFramerate)
    }

    @Test
    fun errorReWrapStatus() {
        val reWrapStatus = client.reWrapStatus(origFilesPath + "not_found_reWrap").get()
        assertThat(reWrapStatus.phase).isEqualTo(null)
        assertThat(reWrapStatus.error).isEqualTo("Job not found")
    }
}