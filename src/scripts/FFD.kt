package scripts

import org.tribot.api.General
import org.tribot.api2007.Login
import org.tribot.api2007.NPCs
import org.tribot.api2007.Player
import org.tribot.api2007.Skills
import org.tribot.script.Script
import org.tribot.script.ScriptManifest
import org.tribot.script.interfaces.Ending
import org.tribot.script.interfaces.Painting
import org.tribot.script.interfaces.Starting
import scripts.state.ActionState
import scripts.state.Status
import scripts.state.WalkingState
import scripts.util.JTimer
import scripts.util.SkillTracker
import scripts.util.isAnimating
import scripts.util.readImageUrl
import java.awt.Color
import java.awt.Graphics
import java.text.SimpleDateFormat
import java.util.*

@ScriptManifest(
    name = "FiveFingerDiscount",
    description = "AIO Thiever?",
    authors = ["jaz"],
    category = "Thieving",
    version = 0.1,
    gameMode = 1
)
class FFD : Script(), Painting, Starting, Ending {

    // TODO Convert to TaskScript API
    // TODO Think about how I want to handle locations, check out other scripts

    val bannerImage = readImageUrl("https://i.ibb.co/jvF67vz/image.png")

    val sdf = SimpleDateFormat("mm:ss", Locale.US)


    var status: Status = Status.IDLING
    var states = arrayOf(ActionState, WalkingState)
    var originalAttackOption: Int = 0

    var startTime: Long = 0

    lateinit var timer: JTimer

    companion object {
        val supportedNpcs = arrayListOf(
            "Man"
        )
        
     //   var getSelectedNpc = { GuiView.selectedNpc.get() }

    }


    override fun onStart() {
        // TODO map out widgets to attack options and then reset option afterwards


        //  GuiApp()

        timer = JTimer()
        SkillTracker.start(Skills.SKILLS.THIEVING)

/*        // TODO, redo
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

        }*/

    }

    override fun onPaint(g: Graphics) {

        val origin = Pair(0, 339)

        g.drawImage(bannerImage, origin.first, origin.second, null)

        val textOrigin = Pair(origin.first + 15, origin.second + 35)

        g.color = Color.getColor("7a7a7a")
        // TODO Exp to next level
        g.drawString(
            "Exp Gained: ${SkillTracker.getGainedExp(Skills.SKILLS.THIEVING)}",
            textOrigin.first,
            textOrigin.second + 60
        )
        // g.drawString("Exp / HR: ${SkillTracker.getGainedExperiencePerHour(Skill.THIEVING)}", textOrigin.first, textOrigin.second + 80)
        g.drawString(
            "Exp Till Next Level: ${Skills.getXPToNextLevel(Skills.SKILLS.THIEVING)}",
            textOrigin.first,
            textOrigin.second + 100
        )

        // g.drawString("Time Till Next Level: ${sdf.format(Date(SkillTracker.getTimeToLevel(Skill.THIEVING)))}", textOrigin.first + 100, textOrigin.second + 100)


    }

    override fun run() {


        General.println(1)
        if (Login.getLoginState() == Login.STATE.LOGINSCREEN) {
            if (!Login.login()) {
                return
            }
        }

        while (true) {

            General.println(status)

            when (status) {

                Status.IDLING -> {
                    status = if (!Location.LUMBRIDGE_CASTLE.area.contains(Player.getPosition()) || NPCs.find("Man").isEmpty()) {
                        General.println(1)
                        Status.WALKING
                    } else {
                        General.println(2)
                        Status.ACTION
                    }
                }

                Status.WALKING -> {
                    if (!Location.LUMBRIDGE_CASTLE.area.contains(Player.getPosition())) {
                        status = Status.ACTION
                    }
                }

                Status.ACTION -> {
                    if (!Location.LUMBRIDGE_CASTLE.area.contains(Player.getPosition()) && !Player.isMoving() &&
                        !Player.getRSPlayer().isAnimating()
                    ) {
                        status = Status.IDLING
                    }
                }
            }

            states.singleOrNull { it.id == status }?.execute()

            General.sleep(50, 70);

        }

    }

    override fun onEnd() {
        //abcUtil.close()
    }


}
