package scripts.state

import scripts.Location

import org.tribot.api2007.WebWalking

object WalkingState: State() {

    override val id = Status.WALKING

    override fun execute() {
        WebWalking.walkTo(Location.LUMBRIDGE_CASTLE.area.randomTile)
    }
}