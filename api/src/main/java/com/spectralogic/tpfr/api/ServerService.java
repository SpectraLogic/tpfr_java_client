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

package com.spectralogic.tpfr.api;

import com.spectralogic.tpfr.api.response.IndexStatusResponse;
import com.spectralogic.tpfr.api.response.OffsetsStatusResponse;
import com.spectralogic.tpfr.api.response.ReWrapResponse;
import com.spectralogic.tpfr.api.response.ReWrapStatusResponse;

import java.io.IOException;
import java.util.Map;

public interface ServerService {
    IndexStatusResponse indexFile(final String filePath) throws IOException;
    IndexStatusResponse fileStatus(final String filePath) throws IOException;
    OffsetsStatusResponse questionTimecode(final Map<String, String> params) throws IOException;
    ReWrapResponse reWrap(final Map<String, String> params) throws IOException;
    ReWrapStatusResponse reWrapStatus(final String targetFileName) throws IOException;
}