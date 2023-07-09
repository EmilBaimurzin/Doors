package com.lucmon.keyk.game.ui.bid

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BettingViewModel: ViewModel() {
    private val _bid = MutableLiveData(100L)
    val bid: LiveData<Long> = _bid

    fun changeBidValue(value: Long) {
        _bid.postValue(value)
    }
}