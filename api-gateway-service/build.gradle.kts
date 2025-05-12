plugins {
    id("java")
    id("org.springframework.boot") version "3.3.3" // ✅ Spring Boot version
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
    // ✅ Core dependencies
    implementation("org.springframework.boot:spring-boot-starter-webflux") // For WebFlux support
    implementation("org.springframework.cloud:spring-cloud-starter-gateway") // API Gateway
    implementation("org.springframework.boot:spring-boot-starter-actuator") // Health, metrics
    implementation("org.springframework.boot:spring-boot-starter-hateoas") // HATEOAS
    implementation("com.fasterxml.jackson.core:jackson-databind") // JSON binding

    // ✅ Lombok support
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
    testCompileOnly("org.projectlombok:lombok:1.18.30")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.30")

    // ✅ Testing dependencies
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("org.mockito:mockito-core:5.11.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.11.0")
    testImplementation("org.springframework:spring-webflux") // For WebTestClient
}

tasks.named<Test>("test") {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed") // ✅ Show test results clearly
    }
}

// ✅ Make the final JAR name 'app.jar'
tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    archiveFileName.set("app.jar")
}
