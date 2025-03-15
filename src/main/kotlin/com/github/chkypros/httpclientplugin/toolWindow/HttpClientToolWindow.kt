package com.github.chkypros.httpclientplugin.toolWindow

import com.github.chkypros.httpclientplugin.services.HttpClientService
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.wm.ToolWindow
import com.intellij.ui.dsl.builder.*
import com.intellij.ui.dsl.gridLayout.GridLayout
import com.intellij.ui.dsl.gridLayout.HorizontalAlign
import com.intellij.ui.dsl.gridLayout.UnscaledGapsX
import com.intellij.ui.dsl.gridLayout.builders.RowsGridBuilder
import com.jetbrains.rd.util.first
import org.jetbrains.annotations.ApiStatus
import java.awt.Color
import java.util.stream.IntStream
import javax.swing.BorderFactory
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel

class HttpClientToolWindow(toolWindow: ToolWindow) : DumbAware {

    private val service = toolWindow.project.service<HttpClientService>()

    private val model = Model()

    fun getContent(): DialogPanel {

        lateinit var panel: DialogPanel

        panel = panel {
            commandRow { panel }
            requestGroup()
            responseGroup()
        }

        return panel
    }

    private fun Panel.commandRow(panel: () -> DialogPanel) {
        row {
            button("SEND") {
                panel().apply()

                model.responseBody = service.sendRequest(model.httpVerb, model.url, model.requestBody)
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

    private fun Panel.requestGroup() {
        collapsibleGroup("Request") {

            panel {
                row { label("Headers") }
                requestHeadersGrid()
            }

            row {
                textArea()
                    .label("Body", LabelPosition.TOP)
                    .rows(5)
                    .align(AlignX.FILL)
                    .bindText(model::requestBody.toMutableProperty())
            }
        }.expanded = true
    }

    private fun Panel.requestHeadersGrid() {
        // TODO Remove: Dummy setup
        model.requestHeaders.put("Referer", "Sam Guy")

        val headers = model.requestHeaders
        val tableHeaders = listOf("Name", "Value")

        row {
            val columnGaps = IntStream.range(0, tableHeaders.size).mapToObj { UnscaledGapsX(5, 5) } .toList()

            val panel = JPanel(GridLayout())
            panel.border = BorderFactory.createLineBorder(Color.DARK_GRAY, 2, true)

            val builder = RowsGridBuilder(panel)
                .columnsGaps(columnGaps)
            builder.cellHeader(JLabel(tableHeaders.get(0)))
            builder.cellHeader(JLabel(tableHeaders.get(1)))
            builder.row()
            builder.cell(JLabel(headers.first().key))
            builder.cell(JLabel(headers.first().value))
            builder.row()
            builder.cell(JLabel("1"))
            builder.cell(JLabel("2"))
            cell(panel)

        }
    }

    private fun RowsGridBuilder.cellHeader(component: JComponent): RowsGridBuilder {
        component.background = Color.LIGHT_GRAY
        component.isOpaque = true
        component.border = BorderFactory.createLineBorder(Color.DARK_GRAY, 2)
        cell(component, horizontalAlign = HorizontalAlign.FILL)
        return this
    }

    private fun Panel.responseGroup() {
        collapsibleGroup("Response") {
            row {
                textArea()
                    .label("Body", LabelPosition.TOP)
                    .rows(5)
                    .align(AlignX.FILL)
                    .bindText(model::responseBody.toMutableProperty())
                    .component.isEditable = false
            }
        }
    }

    private fun getHttpVerbs() = listOf("GET", "POST")

    @ApiStatus.Internal
    internal data class Model (
        var httpVerb: String = "GET",
        var url: String = "",
        var requestBody: String = "",
        var requestHeaders: MutableMap<String, String> = HashMap(),
        var responseBody: String = ""
    )
}