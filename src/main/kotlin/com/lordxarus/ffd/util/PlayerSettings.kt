package com.lordxarus.ffd.util

enum class ClientSettings(val id: Int) {

    NPC_ATTACK_OPTIONS(1306)

}

enum class ClientOption(val value: Int) {

    // Attack options
    DEPENDS_ON_LEVEL(0),
    RIGHT_CLICK(1),
    LEFT_CLICK(2),
    HIDDEN(3);

}