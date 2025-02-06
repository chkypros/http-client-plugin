package com.github.chkypros.httpclientplugin.services

import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.github.chkypros.httpclientplugin.MessageBundle
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Service(Service.Level.PROJECT)
class HttpClientService(project: Project) {
    val httpClient = HttpClient.newHttpClient()

    init {
        thisLogger().info(MessageBundle.message("projectService", project.name))
    }

    fun getRandomNumber() = (1..100).random()

    fun sendRequest(httpVerb: String, uri: String): String {
        val request = HttpRequest.newBuilder(URI.create(uri))
            .method(httpVerb, HttpRequest.BodyPublishers.noBody())
            .build()

        val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())
        return response.body()
    }
}
