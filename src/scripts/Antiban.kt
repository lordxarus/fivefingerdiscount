package scripts.delta.util.abc

import org.tribot.api.General
import org.tribot.api.Timing
import org.tribot.api.interfaces.Clickable07
import org.tribot.api.interfaces.Positionable
import org.tribot.api.types.generic.Filter
import org.tribot.api.util.abc.ABCUtil
import org.tribot.api2007.Objects
import org.tribot.api2007.Player
import org.tribot.api2007.Projection
import org.tribot.api2007.types.RSCharacter
import org.tribot.api2007.types.RSNPC
import org.tribot.api2007.types.RSObject

/**
 * An extension of ABCUtil, allowing for persistence with some of the
 * conditions, in replica of ABCv1.
 */

// This code... definitely needs re-written. Eek.
class Antiban : ABCUtil() {
    @Transient
    private var runAt = -1

    @Transient
    private var shouldHover: Boolean? = null

    @Transient
    private var shouldOpenMenu: Boolean? = null

    @Transient
    private var shouldMoveToAnticipated: Boolean? = null

    @Transient
    private var nextTarget: Positionable? = null

    @Transient
    private var nextTargetClosest: Positionable? = null

    @Transient
    private var anticipatedTime: Long = 0

    @Transient
    private var anticipatedTimeDelay = General.random(20000, 40000).toLong()

    /**
     * Gets the persistent value for the energy at which we should activate run,
     * or generates one if there is no persistent value already set.
     *
     * @return int
     */
    override fun generateRunActivation(): Int {
        return if (runAt != -1) runAt else super.generateRunActivation().also { runAt = it }
    }

    /**
     * Resets the run activation persistence.
     */
    fun resetRunActivation() {
        runAt = -1
    }

    /**
     * Determines whether or not we should hover the mouse over the next target,
     * using persistence.
     *
     * @return boolean
     */
    override fun shouldHover(): Boolean {
        return if (shouldHover != null) shouldHover!! else super.shouldHover().also { shouldHover = it }
    }

    /**
     * Resets the should hover persistence.
     */
    fun resetShouldHover() {
        shouldHover = null
    }

    /**
     * Determines whether or not we should open the menu for the next target,
     * using persistence.
     *
     * @return boolean
     */
    override fun shouldOpenMenu(): Boolean {
        return if (shouldOpenMenu != null) shouldOpenMenu!! else super.shouldOpenMenu()
            .also { shouldOpenMenu = it }
    }

    /**
     * Resets the should open menu persistence.
     */
    fun resetShouldOpenMenu() {
        shouldOpenMenu = null
    }

    /**
     * Determines if our current next target is still valid.
     *
     * @param possible_targets
     * The possible next targets. This is used just in-case a new
     * closest target is available. If that is the case, then we can
     * say he next target is invalid and let the script determine a
     * new next target.
     *
     * @return boolean
     */
    private fun isNextTargetValid(possible_targets: Array<Positionable>?): Boolean {
        val pos = nextTarget!!.position ?: return false
        if (!Projection.isInViewport(
                Projection.tileToScreen(
                    pos,
                    0
                )
            )
        ) return false
        if (nextTarget is Clickable07 && !(nextTarget as Clickable07?)!!.isClickable) return false
        if (nextTarget is RSNPC && !(nextTarget as RSNPC?)!!.isValid) return false
        if (nextTarget is RSCharacter) {
            val name = (nextTarget as RSCharacter?)!!.name
            if (name == null || name.trim { it <= ' ' }.equals("null", ignoreCase = true)) return false
        }
        if (nextTarget is RSObject) {
            if (!Objects.isAt(
                    nextTarget,
                    object : Filter<RSObject>() {
                        override fun accept(o: RSObject): Boolean {
                            return o.obj == (nextTarget as RSObject).obj
                        }
                    })
            ) return false
        }

        // Finally, we check if there is a new closest possible target.
        if (possible_targets != null && possible_targets.size > 0 && nextTargetClosest != null) {
            val new_closest_tile = possible_targets[0].position
            val orig_closest_tile = nextTargetClosest!!.position
            val player_pos = Player.getPosition()
            if (new_closest_tile != null && orig_closest_tile != null && player_pos != null) {
                val new_closest_dist = new_closest_tile.distanceToDouble(player_pos)
                val orig_closest_dist = orig_closest_tile.distanceToDouble(player_pos)

                // If there is a new closest object
                if (new_closest_dist < orig_closest_dist) return false
            }
        }
        return true
    }

    /**
     * Nullifies the next target.
     */
    fun resetNextTarget() {
        nextTarget = null
        nextTargetClosest = null
    }

    /**
     * Gets the next target.
     *
     * @return [Positionable], or null if we do not currently have a next
     * target.
     */
    fun getNextTarget(): Positionable? = if (nextTarget == null || !isNextTargetValid(null)) null else nextTarget

    /**
     * Selects the next target, using persistence.
     *
     * @return [Positionable]
     */
    override fun selectNextTarget(possible_targets: Array<Positionable>): Positionable {
        return try {
            if (nextTarget != null && isNextTargetValid(possible_targets)) nextTarget!! else super.selectNextTarget(
                possible_targets
            ).also { nextTarget = it }
        } finally {
            if (nextTarget != null && possible_targets.size > 0) nextTargetClosest =
                possible_targets[0]
        }
    }

    override fun shouldMoveToAnticipated(): Boolean {
        if (shouldMoveToAnticipated != null
            && Timing.timeFromMark(anticipatedTime) < anticipatedTimeDelay
        ) return shouldMoveToAnticipated!!
        anticipatedTime = Timing.currentTimeMillis()
        anticipatedTimeDelay = General.random(20000, 40000).toLong()
        return super.shouldMoveToAnticipated().also { shouldMoveToAnticipated = it }
    }
}