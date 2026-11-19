plugins {
    application
}

group = "interpreter"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

application {
    mainClass = "interpreter.Main"
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
    workingDir = rootDir // new: test.in and log files in the project root
}

tasks.test {
    useJUnitPlatform()
}