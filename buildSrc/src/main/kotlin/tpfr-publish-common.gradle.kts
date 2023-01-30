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

publishing {
    repositories {
        maven {
            name = "internal"
            val releasesRepoUrl = "http://artifacts.eng.sldomain.com/repository/spectra-releases/"
            val snapshotsRepoUrl = "http://artifacts.eng.sldomain.com/repository/spectra-snapshots/"
            url = uri(if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)
            isAllowInsecureProtocol = true
            credentials {
                username = extra.has("artifactsUsername").let {
                    if (it) extra.get("artifactsUsername") as String else null
                }
                password = extra.has("artifactsPassword").let {
                    if (it) extra.get("artifactsPassword") as String else null
                }
            }
        }
    }
}

tasks.register("publishToInternalRepository") {
    group = "publishing"
    description = "Publishes all Maven publications to the internal Maven repository."
    dependsOn(tasks.withType<PublishToMavenRepository>().matching {
        it.repository == publishing.repositories["internal"]
    })
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
