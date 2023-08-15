plugins {
    val kotlinVersion = "1.8.10"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion

    id("net.mamoe.mirai-console") version "2.15.0"
}

group = "cn.chahuyun"
version = "1.0.1"

repositories {
    maven("https://repo1.maven.org/maven2")
//    maven("https://maven.aliyun.com/repository/public")
//    mavenCentral()
}

dependencies{
    //依赖
    compileOnly("net.mamoe.yamlkt:yamlkt:0.12.0")
    compileOnly("cn.chahuyun:HuYanAuthorize:1.0.7")
    compileOnly("xyz.cssxsh.mirai:mirai-hibernate-plugin:2.7.1")

    //hutool
    implementation("cn.hutool:hutool-all:5.8.20")

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
