/*
 * ******************************************************************************
 *   Copyright 2002 Spectra Logic Corporation. All Rights Reserved.
 *   Licensed under the Apache License, Version 2.0 (the "License"). You may not use
 *   this file except in compliance with the License. A copy of the License is located at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   or in the "license" file accompanying this file.
 *   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 *   CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations under the License.
 * ****************************************************************************
 */

plugins {
    `tpfr-build-platform`
    `tpfr-modify-pom`
}

dependencies {
    constraints {
        api(project(":api"))
        api(project(":client"))
    }
}

publishing.publications.getByName<MavenPublication>(project.name) {
    pom {
        description.set("Bill of Materials (BOM) for the Time-based Partial File Restore Client Project.")
    }
}