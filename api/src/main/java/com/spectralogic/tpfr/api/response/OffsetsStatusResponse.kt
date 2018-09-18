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

@Root(name = "fileoffsetvalues")
data class OffsetsStatusResponse(

        @set:Attribute(name = "fileoffsetsResult")
        @get:Attribute(name = "fileoffsetsResult")
        var offsetsResult: String = "",

        @set:Attribute(name = "in_bytes", required = false)
        @get:Attribute(name = "in_bytes", required = false)
        var inBytes: String? = null,

        @set:Attribute(name = "out_bytes", required = false)
        @get:Attribute(name = "out_bytes", required = false)
        var outBytes: String? = null,

        var errorCode: String? = null,
        var errorMessage: String? = null,
        var exception: Exception? = null
) {
    constructor(offsetsResult: String, errorCode: String, errorMessage: String) : this() {
        this.offsetsResult = offsetsResult
        this.errorCode = errorCode
        this.errorMessage = errorMessage
    }

    constructor(offsetsResult: String, exception: Exception) : this() {
        this.offsetsResult = offsetsResult
        this.exception = exception
    }
}
