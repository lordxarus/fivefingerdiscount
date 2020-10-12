package com.lordxarus.ffd.state

import com.lordxarus.ffd.FFD
import com.lordxarus.ffd.util.getClosestNPC
import com.lordxarus.ffd.util.isStunned
import org.dreambot.api.Client.*
import org.dreambot.api.methods.Calculations
import org.dreambot.api.methods.MethodProvider.*
import org.dreambot.api.methods.container.impl.Inventory
import org.dreambot.api.methods.container.impl.Inventory.getInventory
import org.dreambot.api.methods.input.Camera
import org.dreambot.api.utilities.Logger
import org.dreambot.api.wrappers.cache.nodes.RenderableNode
import kotlin.math.log

object ActionState: State() {

    override val id = Status.ACTION

    override fun execute() {

        // TODO Want to change this: (need to make sure that the location isn't always the same for getClickableSpot())
        // 1. If not animating and right click menu isn't open then,
        // 2. Right click on clickable spot on NPC, mouse over to random spot within "Pickpocket" widget area,
        // 3. Wait until not animating, and then click again

        val cPouch = Inventory.get("Coin pouch")
        if (cPouch != null) {
            if (cPouch.amount == 28) {
                cPouch.interact("Open-all")
                sleep(Calculations.random(100, 250))
            }
        }


        val target = getNPCs().filter { it.name == FFD.getSelectedNpc() }.getClosestNPC()

        if (target != null) {

            if (!target.isOnScreen) {
                Camera.mouseRotateToEntity(target)
            }

            if (!getLocalPlayer().isAnimating && !getLocalPlayer().isStunned() && !getLocalPlayer().isMoving) {
                target.interact("Pickpocket")
                sleepUntil ( {
                    !getLocalPlayer().isAnimating && !getLocalPlayer().isStunned()
                }, Calculations.random(5000, 10000).toLong())
            }
            sleep(Calculations.random(550, 725))
        }



    }
}