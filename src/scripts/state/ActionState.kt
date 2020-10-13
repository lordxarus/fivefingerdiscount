package scripts.state

import org.tribot.api.General
import org.tribot.api2007.Inventory
import org.tribot.api2007.NPCs
import org.tribot.api2007.Player
import scripts.util.isAnimating
import scripts.util.isStunned

object ActionState: State() {

    override val id = Status.ACTION

    override fun execute() {

        // TODO Want to change this: (need to make sure that the location isn't always the same for getClickableSpot())
        // 1. If not animating and right click menu isn't open then,
        // 2. Right click on clickable spot on NPC, mouse over to random spot within "Pickpocket" widget area,
        // 3. Wait until not animating, and then click again

        val cPouch = Inventory.find("Coin pouch").singleOrNull()
        if (cPouch != null) {
            if (cPouch.stack == 28) {
                cPouch.click("Open-all")
            }
        }


        val target = NPCs.sortByDistance(Player.getPosition(), NPCs.find("Man")).firstOrNull()


        General.println("Action1")
        if (target != null) {
            General.println("Action2")
            // Don't think this is needed?
            /*if (!target.isOnScreen) {
                Camera.mouseRotateToEntity(target)
            }*/

            if (!Player.getRSPlayer().isAnimating() && !Player.getRSPlayer().isStunned() && !Player.getRSPlayer().isMoving) {
                target.click("Pickpocket")
            /*    sleepUntil({
                    !getLocalPlayer().isAnimating && !getLocalPlayer().isStunned()
                }, Calculations.random(5000, 10000).toLong())*/
            }
          //  sleep(Calculations.random(550, 725))
        }



    }
}