package com.lordxarus.ffd

import com.lordxarus.ffd.gui.GuiApp
import com.lordxarus.ffd.gui.GuiView
import com.lordxarus.ffd.state.ActionState
import com.lordxarus.ffd.state.Status
import com.lordxarus.ffd.state.WalkingState
import com.lordxarus.ffd.util.ClientOption
import com.lordxarus.ffd.util.ClientSettings
import com.lordxarus.ffd.util.readImageUrl
import org.dreambot.api.Client
import org.dreambot.api.input.Mouse
import org.dreambot.api.methods.Calculations
import org.dreambot.api.methods.MethodProvider
import org.dreambot.api.methods.settings.PlayerSettings
import org.dreambot.api.methods.skills.Skill
import org.dreambot.api.methods.skills.SkillTracker
import org.dreambot.api.methods.skills.Skills
import org.dreambot.api.methods.widget.Widgets
import org.dreambot.api.script.AbstractScript
import org.dreambot.api.script.Category
import org.dreambot.api.script.ScriptManifest
import org.dreambot.api.utilities.Timer
import org.dreambot.api.wrappers.cache.nodes.RenderableNode
import java.awt.Color
import java.awt.Graphics
import java.text.SimpleDateFormat
import java.util.*
import javax.swing.text.DateFormatter

@ScriptManifest(category = Category.THIEVING, name = "FiveFingerDiscount", description = "AIO Thiever?", author = "jaz", version = 0.1)
class FFD: AbstractScript() {

    // TODO Convert to TaskScript API
    // TODO Think about how I want to handle locations, check out other scripts

    val bannerImage = readImageUrl("https://i.ibb.co/jvF67vz/image.png")

    val sdf = SimpleDateFormat("mm:ss", Locale.US)

    var status: Status = Status.IDLING
    var states = arrayOf(ActionState, WalkingState)
    var originalAttackOption: Int = 0

    lateinit var timer: Timer


    companion object {
        val supportedNpcs = arrayListOf(
                "Man"
        )

        var getSelectedNpc = { GuiView.selectedNpc.get() }

    }


    override fun onStart() {
        // TODO map out widgets to attack options and then reset option afterwards

        GuiApp()

        timer = Timer()
        SkillTracker.start(Skill.THIEVING)

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

    }

    override fun onLoop(): Int {

        //log("State: $status")

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

    override fun onPaint(g: Graphics) {
        val origin = Pair(0, 339)

        g.drawImage(bannerImage, origin.first, origin.second, null)

        val textOrigin = Pair(origin.first + 15, origin.second + 35)

        g.color = Color.getColor("7a7a7a")
        // TODO Exp to next level
        g.drawString("Exp Gained: ${Skills.getExperience(Skill.THIEVING) - SkillTracker.getStartExperience(Skill.THIEVING)}", textOrigin.first, textOrigin.second + 60)
        g.drawString("Exp / HR: ${SkillTracker.getGainedExperiencePerHour(Skill.THIEVING)}", textOrigin.first, textOrigin.second + 80)
        g.drawString("Exp Till Next Level: ${Skills.getExperienceToLevel(Skill.THIEVING)}", textOrigin.first, textOrigin.second + 100)

        g.drawString("Time Till Next Level: ${sdf.format(Date(SkillTracker.getTimeToLevel(Skill.THIEVING)))}", textOrigin.first + 100, textOrigin.second + 100)

    }



}
