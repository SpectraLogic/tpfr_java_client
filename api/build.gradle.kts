/*
 * ******************************************************************************
 *   Copyright 2022-2023 Spectra Logic Corporation. All Rights Reserved.
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
    `tpfr-build-common`
}

dependencies {
    api(platform(libs.kotlinBom))

    implementation(platform(libs.jacksonBom))
    implementation(platform(libs.okhttpBom))

    api(libs.kotlinStdLib)

    implementation(libs.guava)
    implementation(libs.jacksonDatatypeGuava)
    implementation(libs.kotlinxCoroutines)
    implementation(libs.okhttpLoggingInterceptor)
    implementation(libs.retrofit)
    implementation(libs.retrofitSimpleXml) {
        exclude(group = "org.simpleframework", module = "simple-xml")
            .because("simple-xml contains critical CVEs")
    }
    implementation(libs.simpleXmlSafe)?.because("simple-xml-safe replaces the excluded simple-xml dependency of retrofit's converter-simplexml")
    implementation(libs.slf4jApi)
}

publishing.publications.getByName<MavenPublication>(project.name) {
    pom {
        description.set("The Time-based Partial File Restore Client API.")
    }
}
