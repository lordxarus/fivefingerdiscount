package com.lordxarus.ffd

import org.dreambot.api.methods.map.Area
import org.dreambot.api.wrappers.interactive.Player

enum class Location(val area: Area, vararg val availNPCNames: String) {

    LUMBRIDGE_CASTLE(
        Area(3226, 3228, 3217, 3208),
        "Man");

    fun contains(player: Player): Boolean = this.area.contains(player.tile)

}