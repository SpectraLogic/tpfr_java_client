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
    `maven-publish`
}

publishing.publications.filterIsInstance<MavenPublication>().forEach { pub ->
    pub.pom {
        name.set("${project.group}:${project.name}")
        url.set("https://github.com/SpectraLogic/tpfr_java_client")
        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }
        developers {
            developer {
                name.set("Spectra Logic Developers")
                email.set("developer@spectralogic.com")
                organization.set("Spectra Logic")
                organizationUrl.set("https://spectralogic.com/")
            }
        }
        scm {
            connection.set("scm:git:https://github.com/SpectraLogic/tpfr_java_client.git")
            developerConnection.set("scm:git:https://github.com/SpectraLogic/tpfr_java_client.git")
            url.set("https://github.com/SpectraLogic/tpfr_java_client")
        }
    }
}
