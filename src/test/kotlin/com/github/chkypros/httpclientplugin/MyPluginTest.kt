package com.github.chkypros.httpclientplugin

import com.github.chkypros.httpclientplugin.services.MyProjectService
import com.intellij.openapi.components.service
import com.intellij.testFramework.TestDataPath
import com.intellij.testFramework.fixtures.BasePlatformTestCase

@TestDataPath("\$CONTENT_ROOT/src/test/testData")
class MyPluginTest : BasePlatformTestCase() {

    fun testProjectService() {
        val projectService = project.service<MyProjectService>()

        assertNotSame(projectService.getRandomNumber(), projectService.getRandomNumber())
    }

    override fun getTestDataPath() = "src/test/testData"
}
