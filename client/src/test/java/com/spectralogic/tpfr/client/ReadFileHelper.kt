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

import org.apache.commons.io.IOUtils
import org.slf4j.LoggerFactory
import java.io.IOException

internal object ReadFileHelper {
    private val LOG = LoggerFactory.getLogger(ClientImpl::class.java)

    fun readFile(fileName: String): String {

        val classLoader = Thread.currentThread().contextClassLoader
        try {
            classLoader.getResourceAsStream(fileName).use {
                resourceAsStream -> return IOUtils.toString(resourceAsStream)
            }
        } catch (e: IOException) {
            LOG.error("Failed to read xml from resource", e)
        }

        return ""
    }
}