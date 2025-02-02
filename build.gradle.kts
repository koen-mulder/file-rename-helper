plugins {
    // Apply the java-library plugin for API and implementation separation.
    `java-library`
    `eclipse`
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // Use JUnit Jupiter for testing.
    //testImplementation(libs.junit.jupiter)

    //testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // This dependency is exported to consumers, that is to say found on their compile classpath.
    //api(libs.commons.math3)

    // This dependency is used internally, and not exposed to consumers on their own compile classpath.
    implementation(libs.guava)
    

    // PDF viewer    
    implementation("com.github.pcorless.icepdf:icepdf-core:7.2.3")
    implementation("com.github.pcorless.icepdf:icepdf-viewer:7.2.3")
    implementation("com.github.jai-imageio:jai-imageio-jpeg2000:1.4.0")
    //implementation("com.github.jai-imageio:jai-imageio-core:1.4.0")
    //implementation("org.apache.pdfbox:jbig2-imageio:3.0.0")
    
        // Log4j2 dependencies
    implementation("org.apache.logging.log4j:log4j-api:2.24.3")
    implementation("org.apache.logging.log4j:log4j-core:2.24.3")

    // SLF4J binding for Log4j2 (optional, if you want to use SLF4J with Log4j2)
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.24.3")
}

// Apply a specific Java toolchain to ease working on different environments.
//java {
//    toolchain {
//        languageVersion = JavaLanguageVersion.of(23)
//    }
//}

//tasks.named<Test>("test") {
//    // Use JUnit Platform for unit tests.
//    useJUnitPlatform()
//}