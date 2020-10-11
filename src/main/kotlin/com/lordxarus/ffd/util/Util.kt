package com.lordxarus.ffd.util

import org.dreambot.api.methods.MethodProvider.*
import org.dreambot.api.Client.getLocalPlayer
import org.dreambot.api.wrappers.cache.nodes.RenderableNode
import org.dreambot.api.wrappers.interactive.NPC
import org.dreambot.api.wrappers.interactive.Player

fun List<NPC>.getClosestNPC(): NPC? {
    var distance = Double.MAX_VALUE
    var closest: NPC? = null

    this.forEach {
        val candDis = it.tile.walkingDistance(getLocalPlayer().tile)
        if (candDis < distance) {
            distance = candDis
            closest = it
        }
    }

    return closest
}

fun Player.isStunned(): Boolean {
    val height = RenderableNode(getLocalPlayer().reference).height
     return height == 1000
}
