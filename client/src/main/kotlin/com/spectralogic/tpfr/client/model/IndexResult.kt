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

enum class IndexResult {
    Failed,
    Succeeded,
    NotIndexed,
    Indexing,
    Exception,
    Unknown;

    companion object {
        fun getIndexResult(result:String):IndexResult {
            return when (result) {
                "Succeeded" -> Succeeded
                "Failed" -> Failed
                "Not Indexed" -> NotIndexed
                "Indexing" -> Indexing
                "Exception" -> Exception
                else -> Unknown
            }
        }
    }
}