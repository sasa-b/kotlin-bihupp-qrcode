#!/usr/bin/env kotlin

@file:DependsOn("com.fasterxml.jackson.module:jackson-module-kotlin:2.18.3")

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.nio.file.Paths
import kotlin.system.exitProcess

@JsonIgnoreProperties(ignoreUnknown = true)
data class ToolCallData(
    @param:JsonProperty("session_id") val sessionId: String,
    @param:JsonProperty("transcript_path") val transcriptPath: String,
    @param:JsonProperty("hook_event_name") val hookEventName: String,
    @param:JsonProperty("tool_name") val toolName: String,
    @param:JsonProperty("tool_input") val toolInput: ToolInput,
) {
    data class ToolInput(
        @param:JsonProperty("file_path") val filePath: String,
    )
}

fun main() {
    val mapper = jacksonObjectMapper()

    val jsonInput = System.`in`.bufferedReader().readText()

    val toolCallData =
        runCatching { mapper.readValue<ToolCallData>(jsonInput) }
            .getOrElse {
                System.err.println("Failed to parse hook input: ${it.message}")
                exitProcess(1)
            }

    val projectRoot =
        Paths
            .get(
                System.getenv("PWD") ?: run {
                    System.err.println("PWD env var is not set")
                    exitProcess(1)
                },
            ).normalize()

    val readPath = Paths.get(toolCallData.toolInput.filePath).normalize()

    if (!readPath.startsWith(projectRoot)) {
        println("Attempting to read a file outside of the project root: $readPath")
        exitProcess(2)
    }

    val fileName = readPath.toString().substringAfterLast("/")
    if (fileName == ".env" || fileName.startsWith(".env.")) {
        println("Attempting to read an env file: $readPath")
        exitProcess(2)
    }
}

main()
