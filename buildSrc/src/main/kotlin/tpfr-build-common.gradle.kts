/*
 * ******************************************************************************
 *   Copyright 2002-2023 Spectra Logic Corporation. All Rights Reserved.
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
    id("tpfr-publish-common")
    id("org.owasp.dependencycheck")
    id("com.github.ben-manes.versions")
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
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
