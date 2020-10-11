package com.lordxarus.ffd.state

import com.lordxarus.ffd.state.Status

abstract class State {
    abstract val id: Status
    abstract fun execute()
}