plugins {
    val kotlinVersion = "1.9.23"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion

    id("net.mamoe.mirai-console") version "2.16.0"
    id("com.github.gmazzo.buildconfig") version "3.1.0"
}

group = "cn.chahuyun"
version = "2.0.0"

repositories {
    maven("https://nexus.jsdu.cn/repository/maven-public/")
    mavenCentral()
}

dependencies {
    //依赖
    compileOnly("net.mamoe.yamlkt:yamlkt:0.12.0")
    compileOnly("cn.chahuyun:HuYanAuthorize:1.2.6")

    //数据库工具
    implementation("cn.chahuyun:hibernate-plus:1.0.16")

    //kotlin日志
    implementation("io.github.oshai:kotlin-logging:6.0.9")

    val ktorVersion = "2.3.13"
    //http工具
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    // 使用 kotlinx.serialization 作为 JSON 转换器
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
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