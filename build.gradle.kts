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
    testImplementation("junit:junit:4.12")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    withType<Jar> {
        baseName = "realme_one"
        manifest {
            attributes["Main-Class"] = application.mainClassName
        }
        from(configurations.runtime.map { if (it.isDirectory) it else zipTree(it) })
    }
}