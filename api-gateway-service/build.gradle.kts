plugins {
    id("java")
    id("org.springframework.boot") version "3.3.3" // ✅ Downgraded from 3.4.4
    id("io.spring.dependency-management") version "1.1.7"
    id("io.freefair.lombok") version "8.4" // ✅ Lombok plugin
}

group = "com.champsoft.services"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

repositories {
    mavenCentral()
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2023.0.1")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux") // ✅ Reactive Web
    implementation("org.springframework.cloud:spring-cloud-starter-gateway") // ✅ Spring Cloud Gateway
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("com.fasterxml.jackson.core:jackson-databind")

    // ✅ Lombok support
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
    testCompileOnly("org.projectlombok:lombok:1.18.30")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.30")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

// ✅ Make the final JAR name 'app.jar'
tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    archiveFileName.set("app.jar")
}
