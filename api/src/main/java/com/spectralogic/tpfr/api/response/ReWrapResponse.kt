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

@Root(name = "partialfile")
data class ReWrapResponse(

        @set:Attribute(name = "partialfileResult")
        @get:Attribute(name = "partialfileResult")
        var reWrapResult: String,

        var errorCode: String?,
        var errorMessage: String?,
        var exception: Exception?) {

    constructor() : this("", null, null, null)
    constructor(reWrapResult: String, errorCode: String, errorMessage: String) : this(reWrapResult, errorCode, errorMessage, null)
    constructor(reWrapResult: String, exception: Exception) : this(reWrapResult, null, null, exception)
}
