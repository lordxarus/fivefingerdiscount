package com.lordxarus.ffd

import com.lordxarus.ffd.state.ActionState
import com.lordxarus.ffd.state.Status
import com.lordxarus.ffd.state.WalkingState
import com.lordxarus.ffd.util.ClientOption
import com.lordxarus.ffd.util.ClientSettings
import org.dreambot.api.Client
import org.dreambot.api.input.Mouse
import org.dreambot.api.methods.Calculations
import org.dreambot.api.methods.MethodProvider
import org.dreambot.api.methods.settings.PlayerSettings
import org.dreambot.api.methods.widget.Widgets
import org.dreambot.api.script.AbstractScript
import org.dreambot.api.script.Category
import org.dreambot.api.script.ScriptManifest
import org.dreambot.api.wrappers.cache.nodes.RenderableNode

@ScriptManifest(category = Category.THIEVING, name = "FiveFingerDiscount", description = "AIO Thiever?", author = "jaz", version = 0.1)
class FFD: AbstractScript() {

    var status: Status = Status.IDLING
    var states = arrayOf(ActionState, WalkingState)

    var originalAttackOption: Int = 0

    override fun onStart() {
        // TODO map out widgets to options and then reset option afterwards

        originalAttackOption = PlayerSettings.getConfig(ClientSettings.NPC_ATTACK_OPTIONS.id)

        log(originalAttackOption)

        if (originalAttackOption.and(ClientOption.HIDDEN.value) != ClientOption.HIDDEN.value) {
            Widgets.getWidget(161)?.getChild(42)?.interact()
            sleep(Calculations.random(200, 350))
            Widgets.getWidget(261)?.getChild(1)?.getChild(7)?.interact()
            sleep(Calculations.random(185, 310))
            Widgets.getWidget(261)?.getChild(92)?.getChild(0)?.interact()
            sleep(Calculations.random(230, 385))
            Widgets.getWidget(261)?.getChild(109)?.getChild(4)?.interact()
            sleep(Calculations.random(200, 350))

        }

        log("Started!")
    }

    override fun onLoop(): Int {

        log("State: $status")

        when (status) {

            Status.IDLING -> {
                status = if (!Location.LUMBRIDGE_CASTLE.contains(localPlayer)) {
                    Status.WALKING
                } else {
                    Status.ACTION
                }
            }

            Status.WALKING -> {
                if (Location.LUMBRIDGE_CASTLE.contains(localPlayer)) {
                    status = Status.ACTION
                }
            }

            Status.ACTION -> {
                if (!Location.LUMBRIDGE_CASTLE.contains(localPlayer) && localPlayer.isStandingStill &&
                    !localPlayer.isAnimating) {
                    status = Status.IDLING
                }
            }
        }

        states.singleOrNull { it.id == status }?.execute()


        return Calculations.random(300, 600);
    }

}
