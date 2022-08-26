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

@Root(name = "partialfilestatus")
data class ReWrapStatusResponse(

    @set:Attribute(name = "phase", required = false)
    @get:Attribute(name = "phase", required = false)
    var phase: String? = null,

    @set:Attribute(name = "percentcomplete", required = false)
    @get:Attribute(name = "percentcomplete", required = false)
    var percentcomplete: String? = null,

    @set:Attribute(name = "error", required = false)
    @get:Attribute(name = "error", required = false)
    var error: String? = null,

    @set:Attribute(name = "errorCode", required = false)
    @get:Attribute(name = "errorCode", required = false)
    var errorCode: String? = null,

    @set:Attribute(name = "errorStr", required = false)
    @get:Attribute(name = "errorStr", required = false)
    var errorMessage: String? = null,

    var exception: Exception? = null
) {
    constructor(phase: String, errorCode: String, errorMessage: String) : this() {
        this.phase = phase
        this.errorCode = errorCode
        this.errorMessage = errorMessage
    }

    constructor(phase: String, exception: Exception) : this() {
        this.phase = phase
        this.exception = exception
    }
}