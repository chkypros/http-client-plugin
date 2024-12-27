package com.github.chkypros.httpclientplugin

import com.github.chkypros.httpclientplugin.services.HttpClientService
import com.intellij.openapi.components.service
import com.intellij.testFramework.TestDataPath
import com.intellij.testFramework.fixtures.BasePlatformTestCase

@TestDataPath("\$CONTENT_ROOT/src/test/testData")
class HttpClientPluginTest : BasePlatformTestCase() {

    fun testProjectService() {
        val projectService = project.service<HttpClientService>()

        assertNotSame(projectService.getRandomNumber(), projectService.getRandomNumber())
    }

    override fun getTestDataPath() = "src/test/testData"
}
