/*
 * ******************************************************************************
 *   Copyright 2022 Spectra Logic Corporation. All Rights Reserved.
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

rootProject.name = "com.spectralogic.tpfr"

include("api")
include("client")

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
    versionCatalogs {
        create("libs") {
            version("assertj-kotlin", "0.2.1")
            version("commons-io", "2.4")
            version("guava", "31.1-jre")
            version("jackson", "2.9.6")
            version("junit", "4.12")
            version("kotlin", "1.3.72")
            version("kotlinx-coroutines", "1.0.1")
            version("okhttp", "2.7.5")
            version("okhttp-logging-interceptor", "3.11.0")
            version("retrofit", "2.4.0")
            version("slf4j", "1.7.25")

            library("guava", "com.google.guava", "guava").versionRef("guava")
            library("jacksonBom", "com.fasterxml.jackson", "jackson-bom").versionRef("jackson")
            library("jacksonDatatypeGuava", "com.fasterxml.jackson.datatype", "jackson-datatype-guava").withoutVersion()
            library("kotlinBom", "org.jetbrains.kotlin", "kotlin-bom").versionRef("kotlin")
            library("kotlinStdLib", "org.jetbrains.kotlin", "kotlin-stdlib").withoutVersion()
            library("kotlinxCoroutines", "org.jetbrains.kotlinx", "kotlinx-coroutines-core").versionRef("kotlinx-coroutines")
            library("okhttpLoggingInterceptor", "com.squareup.okhttp3", "logging-interceptor").versionRef("okhttp-logging-interceptor")
            library("retrofit", "com.squareup.retrofit2", "retrofit").versionRef("retrofit")
            library("retrofitSimpleXml", "com.squareup.retrofit2", "converter-simplexml").versionRef("retrofit")
            library("slf4jApi", "org.slf4j", "slf4j-api").versionRef("slf4j")

            // test only libraries
            library("assertjCoreKotlin", "net.wuerl.kotlin", "assertj-core-kotlin").versionRef("assertj-kotlin")
            library("commonsIo", "commons-io","commons-io").versionRef("commons-io")
            library("junit", "junit", "junit").versionRef("junit")
            library("okhttpMockWebServer", "com.squareup.okhttp", "mockwebserver").versionRef("okhttp")
            library("slf4jSimple", "org.slf4j", "slf4j-simple").versionRef("slf4j")

            // gradle plugins
            plugin("kotlinJvmPlugin", "org.jetbrains.kotlin.jvm").versionRef("kotlin")
            // Looking for the owasp dependency check plug-in? It lives in buildSrc/build.gradle.kts
        }
    }
}