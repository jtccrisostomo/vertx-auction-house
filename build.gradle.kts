
plugins {
    java
    application
}

group = "io.github.jccrisostomo"
version = "0.1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.vertx.core)
    implementation(libs.vertx.web)

    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform.launcher)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    mainClass = "io.github.jccrisostomo.auctionhouse.AuctionHouseApplication"
}

tasks.test {
    useJUnitPlatform()
}
