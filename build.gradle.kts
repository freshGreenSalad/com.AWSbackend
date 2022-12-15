val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val commons_codec_version: String by project

plugins {
    application
    id("io.ktor.plugin") version "2.1.2"
    id("com.github.johnrengelman.shadow") version "6.1.0"
    kotlin("jvm") version "1.6.21"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.6.21"
}

group = "com.example"

version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = application.mainClass
    }
}

repositories {
    mavenCentral()
}
val sshAntTask = configurations.create("sshAntTask")

dependencies {
    //aws
    implementation("aws.sdk.kotlin:s3:0.17.1-beta")
    implementation("aws.sdk.kotlin:dynamodb:0.17.1-beta")
    implementation("aws.sdk.kotlin:ses:0.17.1-beta")

    //ktor
    implementation("io.ktor:ktor-server-content-negotiation-jvm:2.1.3")
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jwt-jvm:$ktor_version")
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")

    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-serialization:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlin_version")

    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("io.ktor:ktor-client-serialization:$ktor_version")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")

    //logging
    implementation("ch.qos.logback:logback-classic:$logback_version")

    //kotlin tests
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")

    //coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")

    // Google Maps
    implementation("com.google.maps:google-maps-services:2.1.0")

    implementation("commons-codec:commons-codec:1.15")

    sshAntTask("org.apache.ant:ant-jsch:1.10.12")
}