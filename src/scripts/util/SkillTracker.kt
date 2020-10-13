package scripts.util

import org.tribot.api2007.Skills
import org.tribot.api2007.Skills.SKILLS as Skill

object SkillTracker {
    // TODO Should think about having this class start a timer for each skill so I can start the timer when I want to

    // Stores start exp
    val skills = hashMapOf<Skill, Int>()

    fun start(skill: Skill) {
        skills[skill] = Skills.getXP(skill)
    }

    fun reset(skill: Skill) = start(skill)

    fun getStartExp(skill: Skill): Int? {
        return if (skills.contains(skill)) {
            Skills.getXP(skill) - skills[skill]!!
        } else {
            null
        }
    }

    fun getGainedExp(skill: Skill): Int? {
        return if (skills.contains(skill)) {
            Skills.getXP(skill) - skills[skill]!!
        } else {
            null
        }
    }

    // TODO
    //fun getGainedExpPerHour(skill: Skill): Int? {}
    //fun getTimeUntilNextLevel(skill: Skill): Int? {}
}