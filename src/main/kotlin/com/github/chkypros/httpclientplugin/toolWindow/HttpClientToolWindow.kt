package com.github.chkypros.httpclientplugin.toolWindow

import com.github.chkypros.httpclientplugin.services.HttpClientService
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.wm.ToolWindow
import com.intellij.ui.dsl.builder.*
import org.jetbrains.annotations.ApiStatus

class HttpClientToolWindow(toolWindow: ToolWindow) : DumbAware {

    private val service = toolWindow.project.service<HttpClientService>()

    private val model = Model()

    fun getContent(): DialogPanel {

        lateinit var panel: DialogPanel

        panel = panel {
            commandRow { panel }
            requestResponseRow()
        }

        return panel
    }

    private fun Panel.commandRow(panel: () -> DialogPanel) {
        row {
            button("SEND") {
                panel().apply()

                model.responseBody = service.sendRequest(model.httpVerb, model.url)
                thisLogger().info("Got response: [${model.responseBody}]")

                panel().reset()
            }

            comboBox(getHttpVerbs())
                .bindItem(model::httpVerb.toNullableProperty())

            expandableTextField()
                .align(AlignX.FILL)
                .bindText(model::url)
                .component.emptyText.text = "URL"
        }
    }

    private fun Panel.requestResponseRow() {
        row {
            textArea()
                .label("Response", LabelPosition.TOP)
                .rows(5)
                .align(AlignX.FILL)
                .bindText(model::responseBody.toMutableProperty())
                .component.isEditable = false
        }
    }

    private fun getHttpVerbs() = listOf("GET", "POST")

    @ApiStatus.Internal
    internal data class Model (
        var httpVerb: String = "GET",
        var url: String = "",
        var responseBody: String = ""
    )
}