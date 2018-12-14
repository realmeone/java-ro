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
    id("com.google.protobuf") version "0.8.6"
    kotlin("jvm") version "1.3.11"
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

application {
    group = "one.realme.krot"
    version = "0.1.0"
    mainClassName = "one.realme.krot.program.Main"
}

protobuf {
    protobuf.generatedFilesBaseDir = "src"
    protobuf.protoc(closureOf<ExecutableLocator> {
        artifact = "com.google.protobuf:protoc:3.6.1"
    })
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation("io.netty:netty-all:4.1.31.Final")
    implementation("com.google.guava:guava:27.0-jre")
    implementation("ch.qos.logback:logback-classic:1.2.3")
    implementation("org.bouncycastle:bcprov-jdk15on:1.60")
    implementation("org.rocksdb:rocksdbjni:5.15.10")
    implementation("com.github.ajalt:clikt:1.5.0")
    implementation("com.github.ajalt:mordant:1.2.0")
    implementation("com.typesafe:config:1.3.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.0.1")
    implementation("com.google.protobuf:protobuf-java:3.6.1")

    
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

    withType<Jar> {
        manifest {
            attributes["Main-Class"] = application.mainClassName
        }
        from(configurations.runtime.map { if (it.isDirectory) it else zipTree(it) })
    }

    withType<Tar> {
        compression = Compression.GZIP
        extension = "tar.gz"
    }
}
