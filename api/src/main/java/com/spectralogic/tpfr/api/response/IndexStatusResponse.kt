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

package com.spectralogic.tpfr.api.response

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Root

@Root(name = "IndexerReport")
data class IndexStatusResponse(

        @set:Attribute(name = "IndexResult")
        @get:Attribute(name = "IndexResult")
        var indexResult: String,

        @set:Attribute(name = "IndexTime", required = false)
        @get:Attribute(name = "IndexTime", required = false)
        var indexTime: String?,

        @set:Attribute(name = "FileStartTC", required = false)
        @get:Attribute(name = "FileStartTC", required = false)
        var fileStartTc: String?,

        @set:Attribute(name = "FileDuration", required = false)
        @get:Attribute(name = "FileDuration", required = false)
        var fileDuration: String?,

        @set:Attribute(name = "FileFrameRate", required = false)
        @get:Attribute(name = "FileFrameRate", required = false)
        var fileFrameRate: String?,

        @set:Attribute(name = "errorCode", required = false)
        @get:Attribute(name = "errorCode", required = false)
        var errorCode: String?,

        @set:Attribute(name = "errorStr", required = false)
        @get:Attribute(name = "errorStr", required = false)
        var errorMessage: String?,

        var exception: Exception?) {

    constructor() : this("", null, null, null, null, null, null, null)
    constructor(indexResult: String, errorCode: String, errorMessage: String) : this(indexResult, null, null, null, null, errorCode, errorMessage, null)
    constructor(indexResult: String, exception: Exception) : this(indexResult, null, null, null, null, null, null, exception)
}