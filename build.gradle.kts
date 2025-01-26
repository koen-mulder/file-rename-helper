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
    //implementation(libs.guava)
    
    implementation("org.apache.pdfbox:pdfbox:3.0.0")
    
    implementation("com.github.jai-imageio:jai-imageio-core:1.4.0")
    implementation("com.github.jai-imageio:jai-imageio-jpeg2000:1.4.0")

    // Optional for you ; just to avoid the same error with JBIG2 images
    implementation("org.apache.pdfbox:jbig2-imageio:3.0.0")
    
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