package com.hyperdesign.myapplication.presentation.utilies

import org.gradle.api.Project
import java.io.File
import java.util.Properties

object BuildUtils {

    fun getSecretProperty(project: Project, key: String, default: String = ""): String {
        return try {
            val secretsFile = File(project.rootProject.rootDir, "secrets.properties")
            if (secretsFile.exists()) {
                val properties = Properties().apply { load(secretsFile.inputStream()) }
                properties.getProperty(key) ?: (System.getenv(key) ?: default)
            } else {
                System.getenv(key) ?: default
            }
        } catch (e: Exception) {
            println("Failed to read secret '$key': $e") // Updated for better logging
            default
        }
    }

    fun Project.getSecret(key: String, default: String = ""): String =
        getSecretProperty(this, key, default)

    fun getRequiredSecret(project: Project, key: String): String {
        val value = getSecretProperty(project, key)
        if (value.isEmpty()) {
            throw IllegalStateException(
                "Required secret property '$key' not found. " +
                        "Please add it to secrets.properties or set as environment variable."
            )
        }
        return value
    }

    fun hasSecret(project: Project, key: String): Boolean =
        getSecretProperty(project, key).isNotEmpty()

    fun getSecrets(project: Project, keys: List<String>, defaults: Map<String, String> = emptyMap()): Map<String, String> {
        return keys.associateWith { key ->
            getSecretProperty(project, key, defaults[key] ?: "")
        }
    }
}

fun Project.getSecret(key: String, default: String = ""): String =
    BuildUtils.getSecretProperty(this, key, default)

fun Project.getSecretOrThrow(key: String): String =
    BuildUtils.getRequiredSecret(this, key)

fun Project.hasSecret(key: String): Boolean =
    BuildUtils.hasSecret(this, key)