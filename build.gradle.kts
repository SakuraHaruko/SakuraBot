import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "2.0.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "moe.nekocafe"

version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    testImplementation(kotlin("test"))

    implementation("top.mrxiaom:overflow-core:2.16.0-dbd98d2-SNAPSHOT")
    implementation("net.mamoe:mirai-core-api:2.16.0")

    implementation("org.quartz-scheduler:quartz:2.3.2")
    implementation("org.apache.httpcomponents:httpclient:4.5.13")
    implementation("com.alibaba.fastjson2:fastjson2-kotlin:2.0.52")
    implementation("org.slf4j:slf4j-log4j12:2.0.16")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}

tasks.withType<ShadowJar> {
    archiveBaseName.set("SakuraBot")
    mergeServiceFiles()

    manifest {
        attributes(
            "Main-Class" to "moe.nekocafe.sakurabot.SakuraBotKt"  // 这里替换为你的启动类的完整路径
        )
    }
}

tasks.build {
    dependsOn(tasks.withType<ShadowJar>())
}
