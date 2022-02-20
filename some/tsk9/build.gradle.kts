import java.io.FileWriter

plugins {
    java
    application
}

group = "org.example"

repositories {
    mavenCentral()
}

sourceSets {
    main {
        java {
            srcDirs("src")
        }
    }
}

application {
    mainClass.set("tsk9")
}

val resPath = projectDir.absolutePath + "/src/main/resources"

tasks.jar {
    manifest {
        attributes(mapOf(
            "Implementation-Title" to project.name,
            "Main-Class" to application.mainClass.get()))
    }
    destinationDirectory.set(file(resPath))
}

val dcrFl = "$resPath/Dockerfile"
val exc: (String) -> Process = Runtime.getRuntime()::exec

val Process.txt
    get() =
        inputStream
        .reader()
        .readText()

task("createContainer") {
    doLast {
        println("building jar file...")
        actions[0].execute(tasks.jar.get())

        println("writing Dockerfile ($dcrFl)...")
        File(dcrFl).apply {
            if (!exists())
                createNewFile()
        }
        FileWriter(dcrFl, false).apply {
            write("""
                # syntax=docker/dockerfile:1
                FROM openjdk:17-alpine
                WORKDIR /app
                COPY GrdlTsk.jar /app
                CMD ["java", "-jar", "GrdlTsk.jar"]
            """.trimIndent())

            flush()
            close()
        }

        println("building an image...")
        val b = exc("docker image build -t grd_tsk_dcr $resPath").txt
        println(b)

        println("running the image as a container...")
        val a = exc("docker run grd_tsk_dcr").txt

        println("application says: $a\n done")
    }
}

task("clearDocker") {
    doLast {
        val a = exc("docker ps -a").txt

        val b = a
            .substringAfter("NAMES")
            .substring(1)
            .substringBefore(' ')

        println(b)
        val c = exc("docker rm $b").txt
        assert(b === c)

        val d = exc("docker image rm grd_tsk_dcr").txt
        println(d)

        File(resPath).listFiles()?.forEach { e -> e.delete() }
    }
}
