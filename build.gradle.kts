import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    base
    java
    application
    kotlin("jvm") version "1.2.71"
}

repositories {
    maven { setUrl("http://dl.bintray.com/kotlin/kotlin-eap") }
    mavenCentral()
    jcenter()
}

application {
    group = "one.realme"
    version = "0.1.0"
    mainClassName = "one.realme.app.App"
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("io.netty:netty-all:4.1.30.Final")
    implementation("com.google.guava:guava:26.0-jre")
    implementation("ch.qos.logback:logback-classic:1.2.3")
    implementation("org.bouncycastle:bcprov-jdk15on:1.60")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.3.1")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.3.1")
    testRuntime("org.junit.jupiter:junit-jupiter-engine:5.3.1")

    testImplementation("com.typesafe:config:1.3.3")
    testImplementation("org.rocksdb:rocksdbjni:5.15.10")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
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
        baseName = "realme_one"
        manifest {
            attributes["Main-Class"] = application.mainClassName
        }
        from(configurations.runtime.map { if (it.isDirectory) it else zipTree(it) })
    }
}