#!/usr/bin/env kotlin

@file:DependsOn("com.fasterxml.jackson.module:jackson-module-kotlin:2.18.3")

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlin.system.exitProcess

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
    val mapper =
        jacksonObjectMapper()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

    val jsonInput = System.`in`.bufferedReader().readText()

    val toolCallData =
        runCatching { mapper.readValue<ToolCallData>(jsonInput) }
            .getOrElse {
                System.err.println("Failed to parse hook input: ${it.message}")
                exitProcess(1)
            }

    val projectRoot = System.getenv("PWD") ?: "/Users/sasablagojevic/Developer/Projects/projectx"
    val readPath = toolCallData.toolInput.filePath

    if (!readPath.startsWith(projectRoot)) {
        println("Attempting to read a file outside of the project root: $readPath")
        exitProcess(2)
    }

    val fileName = readPath.substringAfterLast("/")
    if (fileName == "local.properties") {
        println("Attempting to read local.properties: $readPath")
        exitProcess(2)
    }
}

main()
