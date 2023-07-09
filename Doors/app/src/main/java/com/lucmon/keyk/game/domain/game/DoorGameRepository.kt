package com.lucmon.keyk.game.domain.game

import com.lucmon.keyk.library.library.random


class DoorGameRepository {
    fun generateDoorList(doorAmount: Int): MutableList<DoorState> {
        val list = mutableListOf<DoorState>()
        repeat(doorAmount) {
            list.add(DoorState())
        }
        list[(0 random list.size - 1)].doorValue = true
        return list
    }
}