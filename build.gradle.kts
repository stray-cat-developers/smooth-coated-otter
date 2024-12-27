import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.4.1"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.jmailen.kotlinter") version "4.5.0"
    id("com.avast.gradle.docker-compose") version "0.17.12"
    kotlin("jvm") version "2.1.0"
    kotlin("plugin.spring") version "2.1.0"
    kotlin("plugin.jpa") version "2.1.0"
    kotlin("plugin.allopen") version "2.1.0"
    kotlin("plugin.noarg") version "2.1.0"
    kotlin("kapt") version "2.1.0"
}

group = "io.mustelidae.smoothcoatedotter"
version = "1.0-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_21

buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-compiler-embeddable:2.1.0")
    }
}

repositories {
    mavenLocal()
    mavenCentral()
}

ext["log4j2.version"] = "2.17.1"

dependencies {
    implementation(kotlin("stdlib:2.1.0"))
    implementation(kotlin("reflect:2.1.0"))
    compileOnly("org.jetbrains.kotlin:kotlin-compiler-embeddable:2.1.0")

    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-undertow") {
        exclude("io.undertow", "undertow-websockets-jsr")
    }

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
        exclude(group = "junit", module = "junit")
    }
    testImplementation("org.springframework.boot:spring-boot-starter-hateoas")
    kapt("org.springframework.boot:spring-boot-configuration-processor")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.18.2")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.18.2")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jdk8:2.18.2")

    kapt("com.querydsl:querydsl-apt:${dependencyManagement.importedProperties["querydsl.version"]}:jakarta")
    implementation("com.querydsl:querydsl-jpa:${dependencyManagement.importedProperties["querydsl.version"]}:jakarta")

    testImplementation("io.kotest:kotest-runner-junit5-jvm:5.9.1")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.7.0")

    testImplementation("com.h2database:h2:2.3.232")
    runtimeOnly("mysql:mysql-connector-java:8.0.33")
    testImplementation("de.flapdoodle.embed:de.flapdoodle.embed.mongo:4.18.1")
    testImplementation("com.asarkar.spring:embedded-redis-spring:1.1.1") {
        exclude("it.ozimov")
        testImplementation("org.signal:embedded-redis:0.8.3")
    }

    implementation("org.apache.httpcomponents.client5:httpclient5:5.4.1")
    implementation("org.yaml:snakeyaml:2.3")

}

configurations {
    all {
        exclude("org.springframework.boot", "spring-boot-starter-tomcat")
    }
}

tasks.build {
    dependsOn("installKotlinterPrePushHook")
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        freeCompilerArgs = arrayListOf("-Xjsr305=strict")
        jvmTarget = JvmTarget.JVM_21
    }
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()

    addTestListener(object : TestListener {
        override fun beforeSuite(suite: TestDescriptor) {}
        override fun beforeTest(testDescriptor: TestDescriptor) {}
        override fun afterTest(testDescriptor: TestDescriptor, result: TestResult) {
            val firstLog = "Test Fail. ${testDescriptor.displayName}(${testDescriptor.className})\n"
            @Suppress("UnstableApiUsage")
            val secondLog = result.failures.joinToString { it.rawFailure.localizedMessage}
            val bodyLength = if (firstLog.length > secondLog.length) firstLog.length else secondLog.length
            println("\n${"-".repeat(bodyLength)}\n$firstLog\n$secondLog\n${"-".repeat(bodyLength)}")
        }
        override fun afterSuite(suite: TestDescriptor, result: TestResult) {
            if (suite.parent == null) {
                val output =
                    "Test Summary: ${result.resultType} (${result.testCount} tests, ${result.successfulTestCount} successes, ${result.failedTestCount} failures, ${result.skippedTestCount} skipped)"
                val startItem = "|  "
                val endItem = "  |"
                val repeatLength = startItem.length + output.length + endItem.length
                println("\n${"-".repeat(repeatLength)}\n|  $output  |\n${"-".repeat(repeatLength)}")
                println("\nElapsed: ${(result.endTime - result.startTime) / 1000} sec\n ")
            }
        }
    })
}

tasks.register("version") {
    println(version)
}