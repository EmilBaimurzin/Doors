package com.lucmon.keyk.game.ui.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lucmon.keyk.game.domain.game.DoorState
import com.lucmon.keyk.game.domain.game.DoorGameRepository

class DoorGameViewModel: ViewModel() {
    private val repository = DoorGameRepository()
    var canClick = true

    private val _coefficient = MutableLiveData(1.0)
    val coefficient: LiveData<Double> = _coefficient

    private val _attempts = MutableLiveData(3)
    val attempts: LiveData<Int> = _attempts

    private val _list = MutableLiveData(repository.generateDoorList(2))
    val list: LiveData<MutableList<DoorState>> = _list

    fun next() {
        _list.postValue(repository.generateDoorList(_list.value!!.size + 1))
    }

    fun minusAttempt() {
        _attempts.postValue(attempts.value!! - 1)
    }

    fun increaseRate() {
        _coefficient.postValue(_coefficient.value!! * 2)
    }

    fun addLive() {
        _attempts.postValue(attempts.value!! + 1)
    }
}