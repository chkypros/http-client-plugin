package com.github.chkypros.httpclientplugin.toolWindow

import com.github.chkypros.httpclientplugin.services.HttpClientService
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import com.intellij.ui.dsl.builder.*
import org.jetbrains.annotations.ApiStatus


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

        fun getContent(): DialogPanel {
            val model = Model()

            lateinit var panel: DialogPanel

            panel = panel {
                row {
                    button("SEND") {
                        panel.apply()

                        val response = service.sendRequest(model.httpVerb, model.url)
                        thisLogger().info("Got response: [$response]")
                    }

                    comboBox(getHttpVerbs())
                        .bindItem(model::httpVerb.toNullableProperty())

                    expandableTextField()
                        .align(AlignX.FILL)
                        .bindText(model::url)
                        .component.emptyText.text = "URL"
                }
            }

            return panel
        }

        private fun getHttpVerbs() = listOf("GET", "POST")
    }

    @ApiStatus.Internal
    internal data class Model (
        var httpVerb: String = "GET",
        var url: String = ""
    )
}
