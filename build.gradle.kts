import com.google.protobuf.gradle.ExecutableLocator
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

repositories {
    mavenCentral()
    jcenter()
}

plugins {
    base
    java
    application
    idea
    id("com.google.protobuf") version "0.8.8"
    kotlin("jvm") version "1.3.31"
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

application {
    group = "one.realme"
    version = "0.1.0"
    mainClassName = "one.realme.krot.program.Krot"
}

protobuf {
    protobuf.generatedFilesBaseDir = "src"
    protobuf.protoc(closureOf<ExecutableLocator> {
        artifact = "com.google.protobuf:protoc:3.6.1"
    })
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation("io.netty:netty-all:4.1.36.Final")
    implementation("com.google.guava:guava:27.1-jre")
    implementation("ch.qos.logback:logback-classic:1.2.3")
    implementation("org.bouncycastle:bcprov-jdk15on:1.61")
    implementation("org.rocksdb:rocksdbjni:6.0.1")
    implementation("com.github.ajalt:clikt:1.7.0")
    implementation("com.github.ajalt:mordant:1.2.1")
    implementation("com.typesafe:config:1.3.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.0.1")
    implementation("com.google.protobuf:protobuf-java:3.7.1")


    testImplementation("org.junit.jupiter:junit-jupiter-api:5.3.1")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.3.1")
    testRuntime("org.junit.jupiter:junit-jupiter-engine:5.3.1")
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    withType<Test> {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }

    withType<Tar> {
        compression = Compression.GZIP
        extension = "tar.gz"
    }
}
