import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.1.5"
    id("io.spring.dependency-management") version "1.1.3"
    id("org.jmailen.kotlinter") version "3.14.0"
    id("com.avast.gradle.docker-compose") version "0.17.4"
    kotlin("jvm") version "1.9.10"
    kotlin("plugin.spring") version "1.9.10"
    kotlin("plugin.jpa") version "1.9.10"
    kotlin("plugin.allopen") version "1.9.10"
    kotlin("plugin.noarg") version "1.9.10"
    kotlin("kapt") version "1.9.10"
}

group = "io.mustelidae.smoothcoatedotter"
version = "1.0-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenLocal()
    mavenCentral()
}

ext["log4j2.version"] = "2.17.1"

dependencies {
    implementation(kotlin("stdlib:1.9.10"))
    implementation(kotlin("reflect:1.9.10"))

    implementation("org.springframework.boot:spring-boot-starter-actuator:3.1.5")
    implementation("org.springframework.boot:spring-boot-starter-web:3.1.5")
    implementation("org.springframework.boot:spring-boot-starter-webflux:3.1.5")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.1.5")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb:3.1.5")
    implementation("org.springframework.boot:spring-boot-starter-data-redis:3.1.5")
    implementation("org.springframework.boot:spring-boot-starter-validation:3.1.5")
    implementation("org.springframework.boot:spring-boot-starter-undertow:3.1.5") {
        exclude("io.undertow", "undertow-websockets-jsr")
    }

    testImplementation("org.springframework.boot:spring-boot-starter-test:3.1.5") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
        exclude(group = "junit", module = "junit")
    }
    testImplementation("org.springframework.boot:spring-boot-starter-hateoas:3.1.5")
    kapt("org.springframework.boot:spring-boot-configuration-processor:3.1.5")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.2")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.15.2")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jdk8:2.15.2")

    kapt("com.querydsl:querydsl-apt:${dependencyManagement.importedProperties["querydsl.version"]}:jakarta")
    implementation("com.querydsl:querydsl-jpa:${dependencyManagement.importedProperties["querydsl.version"]}:jakarta")

    testImplementation("io.kotest:kotest-runner-junit5-jvm:5.6.2")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")

    testImplementation("com.h2database:h2:2.2.224")
    implementation("org.mariadb.jdbc:mariadb-java-client:3.3.0")
    testImplementation("de.flapdoodle.embed:de.flapdoodle.embed.mongo:4.9.0")
    testImplementation("com.asarkar.spring:embedded-redis-spring:1.1.1")

    implementation("org.apache.httpcomponents.client5:httpclient5:5.1.4")
    implementation("org.yaml:snakeyaml:2.0")

}

configurations {
    all {
        exclude("org.springframework.boot", "spring-boot-starter-tomcat")
    }
}

noArg {
    invokeInitializers = true
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

tasks.build {
    dependsOn("installKotlinterPrePushHook")
}

tasks.withType<KotlinCompile>().all {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()

    addTestListener(object : TestListener {
        override fun beforeSuite(suite: TestDescriptor) {}
        override fun beforeTest(testDescriptor: TestDescriptor) {}
        override fun afterTest(testDescriptor: TestDescriptor, result: TestResult) {}
        override fun afterSuite(suite: TestDescriptor, result: TestResult) {
            if (suite.parent == null) {
                val output =
                    "Results: ${result.resultType} (${result.testCount} tests, ${result.successfulTestCount} successes, ${result.failedTestCount} failures, ${result.skippedTestCount} skipped)"
                val startItem = "|  "
                val endItem = "  |"
                val repeatLength = startItem.length + output.length + endItem.length
                println("\n${"-".repeat(repeatLength)}\n|  $output  |\n${"-".repeat(repeatLength)}")
                println("\nElapsed: ${(result.endTime - result.startTime) / 1000} sec\n ")
            }
        }
    })
}
