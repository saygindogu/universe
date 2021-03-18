package com.saygindogu.universe

import javafx.stage.Stage
import org.springframework.context.ApplicationEvent

class StageReady(source: Stage) : ApplicationEvent(source) {
    val stage = source
}