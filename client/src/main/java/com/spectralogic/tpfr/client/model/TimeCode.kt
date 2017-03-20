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

package com.spectralogic.tpfr.client.model

import java.util.regex.Pattern

class TimeCode(val time: String) {

    init {
        if (!isValidFormat(time)) {
            throw IllegalArgumentException("The format of the time code is not valid. Time code format should be in form hh:mm:ss:ff for non-drop framerates and hh:mm:ss;ff for drop framerates.")
        }
    }

    private fun isValidFormat(timeCode: String): Boolean {
        val pattern = Pattern.compile("[0-9][0-9]:[0-9][0-9]:[0-9][0-9][:;][0-9][0-9]")
        val matcher = pattern.matcher(timeCode)
        return matcher.find()
    }
}