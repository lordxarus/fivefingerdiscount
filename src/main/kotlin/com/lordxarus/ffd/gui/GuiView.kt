package com.lordxarus.ffd.gui

import com.lordxarus.ffd.FFD
import javafx.beans.property.SimpleStringProperty
import javafx.collections.ObservableList
import javafx.geometry.Orientation
import javafx.scene.Parent
import tornadofx.*

class GuiView: View() {

    companion object {
        val selectedNpc = SimpleStringProperty()
    }

    override val root = form {
        fieldset("Script Settings", labelPosition = Orientation.VERTICAL) {
            field("Target NPC") {
                combobox<String> {
                    items = FFD.supportedNpcs.asObservable()
                }
            }
        }
    }
}