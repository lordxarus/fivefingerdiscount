package scripts

import org.tribot.api2007.types.RSArea
import org.tribot.api2007.types.RSTile

enum class Location(val area: RSArea, vararg val availNPCNames: String) {

    LUMBRIDGE_CASTLE(
        RSArea(RSTile(3226, 3228), RSTile(3217, 3208)),
        "Man"
    );
}