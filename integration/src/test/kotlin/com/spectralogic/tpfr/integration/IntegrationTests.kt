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
import com.spectralogic.tpfr.client.TpfrClient
import com.spectralogic.tpfr.client.TpfrClientImpl
import com.spectralogic.tpfr.client.model.*
import kotlinx.coroutines.experimental.runBlocking
import org.assertj.core.api.KotlinAssertions.assertThat
import org.junit.BeforeClass
import org.junit.Test
import java.time.LocalTime
import java.util.*
import java.util.concurrent.TimeUnit

class IntegrationTests {

    companion object {

        private val origFilesPath = FromEnv.origFilesPath()
        private val restoredFilesPath = FromEnv.restoredFilesPath()
        private val restoredFragmentPath = FromEnv.restoredFragmentPath()

        private lateinit var tpfrClient: TpfrClient

        @BeforeClass
        @JvmStatic
        fun startup() {

            val endpoint = FromEnv.endpoint()
            val proxyHost = FromEnv.proxyHost()
            val proxyPort = FromEnv.proxyPort()

            val serverServiceFactory = ServerServiceFactoryImpl(endpoint, proxyHost, proxyPort)
            val serverService = serverServiceFactory.createServerService()

            tpfrClient = TpfrClientImpl(serverService)
        }
    }

    @Test
    fun happyPath() {
        runBlocking {
            val filePath = origFilesPath + "sample.mov"
            val indexId = UUID.randomUUID().toString()

            // Index the file
            val indexFileStatus = tpfrClient.indexFile(filePath, indexId)
            assertThat(indexFileStatus.indexResult).isEqualTo(IndexResult.Succeeded)

            // Get the index status
            val indexStatus = tpfrClient.fileStatus(indexId)
            assertThat(indexStatus.indexResult).isEqualTo(IndexResult.Succeeded)

            // Question the timecode
            val questionTimecodeParams = QuestionTimecodeParams(
                    indexId,
                    TimeCode.getTimeCodeForDropFrameRates(LocalTime.of(1, 0, 0), 0),
                    TimeCode.getTimeCodeForDropFrameRates(LocalTime.of(1, 0, 10), 0),
                    "29.97")

            val offsetsStatus = tpfrClient.questionTimecode(questionTimecodeParams)
            assertThat(offsetsStatus.offsetsResult).isEqualTo(OffsetsResult.Succeeded)
            assertThat(offsetsStatus.inBytes).isEqualTo("0x0")
            assertThat(offsetsStatus.outBytes).isEqualTo("0xc148110")

            // reWrap the partial file
            val partialFile = restoredFragmentPath + "partial_sample_10sec"
            val outFilePath = restoredFilesPath + "sample_10sec_" + UUID.randomUUID().toString() + ".mov"
            val reWrapParams = ReWrapParams(
                    indexId,
                    TimeCode.getTimeCodeForDropFrameRates(LocalTime.of(1, 0, 0), 0),
                    TimeCode.getTimeCodeForDropFrameRates(LocalTime.of(1, 0, 10), 0),
                    "29.97",
                    partialFile,
                    outFilePath)

            val reWrapResponse = tpfrClient.reWrap(reWrapParams)
            assertThat(reWrapResponse.reWrapResult).isEqualTo(ReWrapResult.Succeeded)

            // Get the reWrap status
            var reWrapStatus = tpfrClient.reWrapStatus(outFilePath)
            while (reWrapStatus.phase === Phase.Pending ||
                    reWrapStatus.phase === Phase.Parsing ||
                    reWrapStatus.phase === Phase.Transferring) {
                TimeUnit.SECONDS.sleep(5)
                reWrapStatus = tpfrClient.reWrapStatus(outFilePath)
            }
            assertThat(reWrapStatus.phase).isEqualTo(Phase.Complete)
        }
    }

    @Test
    fun failedIndexFile() {
        runBlocking {
            val indexStatus = tpfrClient.indexFile(origFilesPath + "error.mov", UUID.randomUUID().toString())
            assertThat(indexStatus.indexResult).isEqualTo(IndexResult.Failed)
            assertThat(indexStatus.errorCode).isEqualTo("-2132778877")
            assertThat(indexStatus.errorMessage).isEqualTo("Failed to parse MOV file [\\\\eng-dell-26\\temp\\media\\BP-PFR\\toIndex\\error.mov] Error [Null atom discovered in QT movie.]")
        }
    }

    @Test
    fun indexFileNotFound() {
        runBlocking {
            val indexStatus = tpfrClient.indexFile(origFilesPath + "not_found_index.mov", UUID.randomUUID().toString())
            assertThat(indexStatus.indexResult).isEqualTo(IndexResult.Failed)
            assertThat(indexStatus.errorCode).isEqualTo("-2132778994")
            assertThat(indexStatus.errorMessage).isEqualTo("Failed to parse MOV file [\\\\eng-dell-26\\temp\\media\\BP-PFR\\toIndex\\not_found_index.mov] Error [Source could not be opened.]")
        }
    }

    @Test
    fun fileStatusNotIndexed() {
        runBlocking {
            val indexStatus = tpfrClient.fileStatus(UUID.randomUUID().toString())
            assertThat(indexStatus.indexResult).isEqualTo(IndexResult.NotIndexed)
        }
    }

    @Test
    fun questionTimecodeFileNotFound() {
        runBlocking {
            val firstFrame = TimeCode.getTimeCodeForDropFrameRates(LocalTime.of(0, 0, 0), 0)
            val lastFrame = TimeCode.getTimeCodeForDropFrameRates(LocalTime.of(0, 0, 10), 0)
            val offsetsStatus = tpfrClient.questionTimecode(
                    QuestionTimecodeParams(UUID.randomUUID().toString(), firstFrame, lastFrame, "29.97"))

            assertThat(offsetsStatus.offsetsResult).isEqualTo(OffsetsResult.ErrorFileNotFound)
        }
    }

    @Test
    fun reWrapWithBadRestoreFile() {
        runBlocking {
            val filePath = origFilesPath + "sample.mov"
            val indexId = UUID.randomUUID().toString()
            val partialFile = restoredFragmentPath + "bad_restored_file"
            val outFilePath = restoredFilesPath + "sample_10sec_" + UUID.randomUUID().toString() + ".mov"
            val firstFrame = TimeCode.getTimeCodeForDropFrameRates(LocalTime.of(0, 0, 0), 0)
            val lastFrame = TimeCode.getTimeCodeForDropFrameRates(LocalTime.of(0, 0, 10), 0)

            // Index the file
            val indexFileStatus = tpfrClient.indexFile(filePath, indexId)
            assertThat(indexFileStatus.indexResult).isEqualTo(IndexResult.Succeeded)

            // Get the index status
            val indexStatus = tpfrClient.fileStatus(indexId)
            assertThat(indexStatus.indexResult).isEqualTo(IndexResult.Succeeded)

            val reWrapResponse = tpfrClient.reWrap(ReWrapParams(indexId, firstFrame, lastFrame, "29.97",
                    partialFile, outFilePath))
            assertThat(reWrapResponse.reWrapResult).isEqualTo(ReWrapResult.Succeeded)

            var reWrapStatus = tpfrClient.reWrapStatus(outFilePath)
            while (reWrapStatus.phase === Phase.Pending ||
                    reWrapStatus.phase === Phase.Parsing ||
                    reWrapStatus.phase === Phase.Transferring) {
                TimeUnit.SECONDS.sleep(5)
                reWrapStatus = tpfrClient.reWrapStatus(outFilePath)
            }

            assertThat(reWrapStatus.phase).isEqualTo(Phase.Failed)
            assertThat(reWrapStatus.percentComplete).isEqualTo(0)
            assertThat(reWrapStatus.errorCode).isEqualTo("-2132778927")
            assertThat(reWrapStatus.errorMessage).isEqualTo("Requested subclip out of bounds.")
        }
    }

    @Test
    fun reWrapErrorBadFrameRate() {
        runBlocking {
            val firstFrame = TimeCode.getTimeCodeForDropFrameRates(LocalTime.of(0, 0, 0), 0)
            val lastFrame = TimeCode.getTimeCodeForDropFrameRates(LocalTime.of(0, 0, 10), 0)
            val reWrapResponse = tpfrClient.reWrap(ReWrapParams(UUID.randomUUID().toString(), firstFrame, lastFrame, "0",
                    restoredFilesPath + "sample_10sec.mov", "\\\\sampleRestore"))
            assertThat(reWrapResponse.reWrapResult).isEqualTo(ReWrapResult.ErrorBadFramerate)
        }
    }

    @Test
    fun reWrapErrorUNC() {
        runBlocking {
            val firstFrame = TimeCode.getTimeCodeForDropFrameRates(LocalTime.of(0, 0, 0), 0)
            val lastFrame = TimeCode.getTimeCodeForDropFrameRates(LocalTime.of(0, 0, 10), 0)
            val reWrapResponse = tpfrClient.reWrap(ReWrapParams(UUID.randomUUID().toString(), firstFrame, lastFrame, "0",
                    restoredFilesPath + "sample_10sec.mov", "must_be_UNC"))
            assertThat(reWrapResponse.reWrapResult).isEqualTo(ReWrapResult.ErrorOutFileNameMustBeUNC)
        }
    }

    @Test
    fun errorReWrapStatus() {
        runBlocking {
            val reWrapStatus = tpfrClient.reWrapStatus(origFilesPath + "not_found_reWrap")
            assertThat(reWrapStatus.phase).isNull()
            assertThat(reWrapStatus.error).isEqualTo("Job not found")
        }
    }
}