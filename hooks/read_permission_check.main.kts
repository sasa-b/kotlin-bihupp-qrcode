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
    @JsonIgnoreProperties(ignoreUnknown = true)
    data class ToolInput(
        @param:JsonProperty("file_path") val filePath: String? = null,
        @param:JsonProperty("path") val path: String? = null,
        @param:JsonProperty("command") val command: String? = null,
    ) {
        fun resolvedPath(): String? = filePath ?: path
    }
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

    // Block Bash commands that reference local.properties
    val command = toolCallData.toolInput.command
    if (command != null) {
        // Match local.properties appearing as a standalone token (preceded/followed by whitespace, quotes, or string boundary)
        val localPropertiesPattern = Regex("""(?:^|[\s'"])local\.properties(?=$|[\s'"])""")
        if (localPropertiesPattern.containsMatchIn(command)) {
            println("Attempting to read local.properties via Bash: $command")
            exitProcess(2)
        }
    }

    val resolvedPath =
        toolCallData.toolInput.resolvedPath() ?: run {
            // No path provided — Grep/Glob default to CWD, which is within the project root
            exitProcess(0)
        }

    val readPath = Paths.get(resolvedPath).normalize()

    if (!readPath.startsWith(projectRoot)) {
        println("Attempting to read a file outside of the project root: $readPath")
        exitProcess(2)
    }

    val fileName = readPath.fileName.toString()
    if (fileName == "local.properties") {
        println("Attempting to read local.properties: $readPath")
        exitProcess(2)
    }
}

main()
