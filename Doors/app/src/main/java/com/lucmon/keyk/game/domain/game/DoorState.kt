package com.lucmon.keyk.game.domain.game

import kotlin.random.Random

data class DoorState(
    val id: Int = Random.nextInt(),
    var isOpened: Boolean = false,
    var doorValue: Boolean = false
)
