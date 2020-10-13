package scripts.state

import scripts.state.Status

abstract class State {
    abstract val id: Status
    abstract fun execute()
}