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
    
    // Logging    
    implementation("org.slf4j:slf4j-api:2.0.16")
    implementation("org.apache.logging.log4j:log4j-slf4j2-impl:2.24.3")
    
    // JSON for parsing configuration
    implementation("com.google.code.gson:gson:2.12.1")
    
    // LangChain for integration with LLM providers
    implementation("dev.langchain4j:langchain4j:1.0.0-alpha1")
    implementation("dev.langchain4j:langchain4j-easy-rag:1.0.0-alpha1")
    //implementation("dev.langchain4j:langchain4j-embedding-store-inmemory:1.0.0-alpha1")
    // Ollama integration
    implementation("dev.langchain4j:langchain4j-ollama:1.0.0-alpha1")
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