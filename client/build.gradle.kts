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
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    `tpfr-build-common`
    alias(libs.plugins.kotlinJvmPlugin)
}

dependencies {
    api(platform(libs.kotlinBom))

    implementation(platform(libs.okhttpBom))

    api(libs.kotlinStdLib)
    api(project(":api"))

    implementation(libs.guava)
    implementation(libs.slf4jApi)

    testImplementation(libs.assertjCoreKotlin)
    implementation(libs.commonsIo)
    testImplementation(libs.junit)
    testImplementation(libs.kotlinxCoroutines)
    testImplementation(libs.okhttpMockWebServer)
    
    testRuntimeOnly(libs.slf4jSimple)
}

sourceSets {
    create("integrationTest") {
        compileClasspath += sourceSets.main.get().output
        runtimeClasspath += sourceSets.main.get().output
    }
}

val integrationTest = task<Test>("integrationTest") {
    description = "Run integration tests."
    group = "verification"
    testClassesDirs = sourceSets["integrationTest"].output.classesDirs
    classpath = sourceSets["integrationTest"].runtimeClasspath
    shouldRunAfter("test")
}

tasks.check { dependsOn(integrationTest) }