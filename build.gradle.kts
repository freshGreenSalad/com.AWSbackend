val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val commons_codec_version: String by project

plugins {
    application
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

repositories {
    mavenCentral()
}
val sshAntTask = configurations.create("sshAntTask")

dependencies {
    //aws
    implementation("aws.sdk.kotlin:s3:0.17.1-beta")
    implementation("aws.sdk.kotlin:dynamodb:0.17.1-beta")

    //ktor
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jwt-jvm:$ktor_version")
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")

    //logging
    implementation("ch.qos.logback:logback-classic:$logback_version")

    //kotlin tests
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")

    //coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")

    implementation("commons-codec:commons-codec:1.15")

    sshAntTask("org.apache.ant:ant-jsch:1.10.12")
}