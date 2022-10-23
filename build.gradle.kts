import org.jetbrains.kotlin.utils.addToStdlib.applyIf
import java.io.ByteArrayOutputStream
import java.util.*

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("org.jetbrains.kotlinx.benchmark")
    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
    `maven-publish`
    signing
}

val localPropsFile = project.rootProject.file("local.properties")
if (localPropsFile.exists()) {
    val p = Properties()
    localPropsFile.inputStream().use { p.load(it) }
    p.forEach { name, value -> ext.set(name.toString(), value) }
}

allprojects {
    group = "org.ton"
    version = version.applyIf(version == "unspecified") {
//        System.getenv("GITHUB_REF").takeIf {
//            !it.isNullOrEmpty()
//        }?.substring(11) ?:
        ByteArrayOutputStream().use {
            exec {
                commandLine("git", "rev-parse", "--short", "head")
                standardOutput = it
            }
            it.toString().trim()
        }
    }

    apply(plugin = "kotlin-multiplatform")
    apply(plugin = "kotlinx-serialization")
    apply(plugin = "maven-publish")
    apply(plugin = "signing")

    repositories {
        mavenCentral()
        maven("https://jitpack.io")
    }

    kotlin {
        jvm {
            withJava()
            compilations.all {
                kotlinOptions.jvmTarget = "1.8"
            }
            testRuns["test"].executionTask.configure {
                useJUnitPlatform()
            }
        }
//        linuxX64()
//        macosX64()
//        js {
//            useCommonJs()
//            browser()
//        }
        sourceSets {
            val commonMain by getting {
                dependencies {
                    subprojects {
                        api(this)
                    }
                }
            }
            val commonTest by getting {
                dependencies {
                    implementation("io.mockk:mockk:1.12.4")
                    implementation(kotlin("test"))
                }
            }
            val jvmMain by getting {

            }
            val jvmTest by getting {
                dependencies {
                    implementation(kotlin("test"))
                    implementation("junit:junit:4.13.1")
                }
            }
        }
    }

    val javadocJar by tasks.registering(Jar::class) {
        archiveClassifier.set("javadoc")
    }

    publishing {
        publications.withType<MavenPublication> {
            artifact(javadocJar.get())
            pom {
                name.set("ton-kotlin")
                description.set("Kotlin/Multiplatform SDK for The Open Network")
                url.set("https://github.com/andreypfau/ton-kotlin")
                licenses {
                    license {
                        name.set("GNU General Public License v3.0")
                        url.set("https://www.gnu.org/licenses/gpl-3.0.html")
                    }
                }
                developers {
                    developer {
                        id.set("andreypfau")
                        name.set("Andrey Pfau")
                        email.set("andreypfau@ton.org")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/andreypfau/ton-kotlin.git")
                    developerConnection.set("scm:git:ssh://github.com/andreypfau/ton-kotlin.git")
                    url.set("https://github.com/andreypfau/ton-kotlin")
                }
            }
        }
//        repositories {
//            maven {
//                name = "GitHubPackages"
//                url = uri("https://maven.pkg.github.com/andreypfau/ton-kotlin")
//                credentials {
//                    username = System.getenv("GITHUB_ACTOR")
//                    password = System.getenv("GITHUB_TOKEN")
//                }
//            }
//        }
    }

    signing {
        val secretKey = project.findProperty("signing.secretKey") as? String ?: System.getenv("SIGNING_SECRET_KEY")
        val password = project.findProperty("signing.password") as? String ?: System.getenv("SIGNING_PASSWORD")
        isRequired = secretKey != null && password != null
        useInMemoryPgpKeys(secretKey, password)
        sign(publishing.publications)
    }
}

nexusPublishing {
    repositories {
        sonatype {
            username.set(project.findProperty("ossrhUsername") as? String ?: System.getenv("OSSRH_USERNAME"))
            password.set(project.findProperty("ossrhPassword") as? String ?: System.getenv("OSSRH_PASSWORD"))
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
        }
    }
}
