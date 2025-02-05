package com.github.chkypros.httpclientplugin.toolWindow

import com.github.chkypros.httpclientplugin.services.HttpClientService
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import com.intellij.ui.dsl.builder.AlignX
import com.intellij.ui.dsl.builder.panel


class HttpClientToolWindowFactory : ToolWindowFactory {

    init {
        thisLogger().warn("Don't forget to remove all non-needed sample code files with their corresponding registration entries in `plugin.xml`.")
    }

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val httpClientToolWindow = HttpClientToolWindow(toolWindow)
        val content = ContentFactory.getInstance().createContent(httpClientToolWindow.getContent(), null, false)
        toolWindow.contentManager.addContent(content)
    }

    override fun shouldBeAvailable(project: Project) = true

    class HttpClientToolWindow(toolWindow: ToolWindow) : DumbAware {

        private val service = toolWindow.project.service<HttpClientService>()

        fun getContent() = panel {
            row {
                button("SEND") {
                    // TODO Implement HTTP request invocation
                    val randomNumber = service.getRandomNumber()
                    thisLogger().info("Sending random number $randomNumber")
                }

                comboBox(getHttpVerbs())

                expandableTextField()
                    .align(AlignX.FILL)
                    .component.emptyText.text = "URL"
            }
        }

        private fun getHttpVerbs() = listOf("GET", "POST")
    }
}
