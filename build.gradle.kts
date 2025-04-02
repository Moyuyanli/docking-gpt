plugins {
    val kotlinVersion = "1.8.10"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion

    id("net.mamoe.mirai-console") version "2.16.0"
    id("com.github.gmazzo.buildconfig") version "3.1.0"
}

group = "cn.chahuyun"
version = "1.2.0"

repositories {
    maven("https://repo1.maven.org/maven2")
    maven("https://maven.aliyun.com/repository/public")
    mavenCentral()
}

dependencies {
    //依赖
    compileOnly("net.mamoe.yamlkt:yamlkt:0.12.0")
    compileOnly("cn.chahuyun:HuYanAuthorize:1.2.0")


    //hutool
    implementation("cn.hutool:hutool-all:5.8.20")

    implementation("cn.chahuyun:hibernate-plus:1.0.16")

    //retrofit2
    implementation("com.squareup.retrofit2:retrofit:2.7.2")
    implementation("com.squareup.retrofit2:converter-gson:2.7.2")

    //lombok
    implementation("org.projectlombok:lombok:1.18.26")
    annotationProcessor("org.projectlombok:lombok:1.18.26")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

mirai {
    jvmTarget = JavaVersion.VERSION_11
}

buildConfig {
    className("BuildConstants")
    packageName("cn.chahuyun.docking")
    useKotlinOutput()
    buildConfigField("String", "VERSION", "\"${project.version}\"")
    buildConfigField(
        "java.time.Instant",
        "BUILD_TIME",
        "java.time.Instant.ofEpochSecond(${System.currentTimeMillis() / 1000L}L)"
    )
}