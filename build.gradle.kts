import com.github.spotbugs.snom.Confidence
import com.github.spotbugs.snom.Effort
import com.github.spotbugs.snom.SpotBugsTask

plugins {
    java
    application
    checkstyle
    alias(libs.plugins.spotbugs)
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

checkstyle {
    toolVersion = libs.versions.checkstyle.get()
    configFile = file("config/checkstyle/checkstyle.xml")
    isIgnoreFailures = false
    maxErrors = 0
    maxWarnings = 0
}

tasks.test {
    useJUnitPlatform()
}

spotbugs {
    ignoreFailures = false
    effort = Effort.MAX
    reportLevel = Confidence.MEDIUM
}

tasks.withType<SpotBugsTask>().configureEach {
    reports.create("html") {
        required = true
        outputLocation = layout.buildDirectory
            .file("reports/spotbugs/${this@configureEach.name}.html")
        setStylesheet("fancy-hist.xsl")
    }
}