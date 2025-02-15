plugins {
    `java-library`
    `eclipse`
    // Plugin for creating FAT Jars
    id("com.gradleup.shadow") version libs.versions.shadow.get()
    // Plugion for version numbering
    id("com.palantir.git-version") version libs.versions.version.get()
}

val gitVersion: groovy.lang.Closure<String> by extra
version = gitVersion()

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.guava)

    // Use JUnit Jupiter for testing.
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    
    
    // PDF viewer
    implementation(libs.icepdf.core)
    implementation(libs.icepdf.viewer)
    implementation(libs.jai.imageio.jpeg2000)

    // Logging
    implementation(libs.slf4j.api)
    implementation(libs.log4j.slf4j2.impl)

    // JSON for parsing configuration
    implementation(libs.gson)

    // LangChain for integration with LLM providers
    implementation(libs.langchain4j)
    implementation(libs.langchain4j.easy.rag)
    implementation(libs.langchain4j.ollama)
}

tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
    archiveBaseName.set("file-rename-helper")
    archiveVersion.set(project.version.toString())
    archiveClassifier.set("")
    manifest {
        attributes(mapOf("Main-Class" to "com.github.koen_mulder.file_rename_helper.application.Application"))
    }
    mergeServiceFiles()
}

tasks.named("build") {
    dependsOn(tasks.named("shadowJar"))
}

java {
    // Use Java version 21 because this is the latest Long Term Support release
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}
