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
    id("tpfr-version")
    `java-library`
    `jacoco`
    `maven-publish`
    id("tpfr-modify-pom")
    id("org.owasp.dependencycheck")
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    // since java 8 is the minimum version supported, make sure we always
    // produce java 8 bytecode
    if (org.gradle.api.JavaVersion.current() != org.gradle.api.JavaVersion.VERSION_1_8) {
        options.release.set(8)
    } else {
        // java 8 does not have a release option, so use source and target compatibility
        setSourceCompatibility(org.gradle.api.JavaVersion.VERSION_1_8.toString())
        setTargetCompatibility(org.gradle.api.JavaVersion.VERSION_1_8.toString())
    }
}

java {
    withJavadocJar()
    withSourcesJar()
}

tasks.jacocoTestReport {
    dependsOn(tasks.test) // report depends on running tests first
    reports {
        html.required.set(true)
    }
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
}

publishing {
    publications {
        create<MavenPublication>(project.name) {
            from(components["java"])
        }
    }
}

dependencyCheck {
    // fail the build if any vulnerable dependencies are identified (CVSS score > 0)
    failBuildOnCVSS = 0f;
    suppressionFile = "project_files/owasp/dependency-check-suppression.xml"
}
