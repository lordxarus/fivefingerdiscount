package com.lordxarus.ffd.state

import com.lordxarus.ffd.Location
import org.dreambot.api.Client.*
import org.dreambot.api.methods.Calculations
import org.dreambot.api.methods.MethodProvider.sleepUntil
import org.dreambot.api.methods.walking.impl.Walking
import org.dreambot.api.methods.walking.impl.Walking.getWalking
import org.dreambot.api.utilities.impl.Condition
import org.dreambot.api.wrappers.interactive.NPC

object WalkingState: State() {

    override val id = Status.WALKING

    override fun execute() {
        Walking.walk(Location.LUMBRIDGE_CASTLE.area.randomTile)
        sleepUntil ( {
            Walking.getDestinationDistance() < Calculations.random(5, 7)
        }, Calculations.random(3400, 4350).toLong())
    }
}