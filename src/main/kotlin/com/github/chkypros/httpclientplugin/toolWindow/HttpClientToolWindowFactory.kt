package com.github.chkypros.httpclientplugin.toolWindow

import com.github.chkypros.httpclientplugin.services.HttpClientService
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import com.intellij.ui.dsl.builder.Align
import com.intellij.ui.dsl.builder.panel
import java.util.*
import javax.swing.JSeparator


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
            align(Align.FILL)
            row {
                panel {
                    align(Align.FILL)
                    row {
                        button("SEND") {
                            // TODO Implement HTTP request invocation
                        }

                        comboBox(getHttpVerbs())

                        expandableTextField().component.emptyText.text = "URL"
                    }

                }
                cell(JSeparator())
                text("2")
            }
        }

        private fun getHttpVerbs(): List<String> = Arrays.asList("GET", "POST")
    }
}
