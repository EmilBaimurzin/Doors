package com.lucmon.keyk.game.ui.bonus

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lucmon.keyk.game.domain.bonus.GameState
import kotlinx.coroutines.*

class DialogViewModel: ViewModel() {
    private val _currentPage = MutableLiveData(0)
    val currentPage: LiveData<Int> = _currentPage

    private val _threeSecondsTimer = MutableLiveData(3)
    val threeSecondsTimer: LiveData<Int> = _threeSecondsTimer

    private val _tenSecondsTimer = MutableLiveData(10)
    val tenSecondsTimer: LiveData<Int> = _tenSecondsTimer

    private val _progress = MutableLiveData(0)
    val progress: LiveData<Int> = _progress

    private val _gameValue = MutableLiveData(GameState.GAME_HAVE_NOT_STARTED)
    val gameValue: LiveData<GameState> = _gameValue

    var winCallback: (()-> Unit)? = null
    var loseCallback: (()-> Unit)? = null

    private val allScope = CoroutineScope(Dispatchers.Default)

    fun changePage(page: Int) {
        _currentPage.postValue(page)
    }

    fun startGame() {
        allScope.launch {
            changePage(1)
            startThreeSecondsTimer()
            delay(3000)
            _gameValue.postValue(GameState.GAME_STARTED)
            startCycle()
            startTenSecondsTimer()
            delay(10000)
            _gameValue.postValue(GameState.GAME_ENDED)
            lose()
        }
    }

    fun win() {
        allScope.cancel()
        changePage(2)
        reset()
    }

    fun reset() {
        _progress.postValue(0)
        _gameValue.postValue(GameState.GAME_HAVE_NOT_STARTED)
        _tenSecondsTimer.postValue(10)
        _threeSecondsTimer.postValue(3)
    }

    private fun lose() {
        loseCallback?.invoke()
    }

    private fun startThreeSecondsTimer() {
        allScope.launch {
            repeat(3) {
                delay(1000)
                _threeSecondsTimer.postValue(_threeSecondsTimer.value!! - 1)
            }
        }
    }

    private fun startTenSecondsTimer() {
        allScope.launch {
            repeat(10) {
                delay(1000)
                _tenSecondsTimer.postValue(_tenSecondsTimer.value!! - 1)
            }
        }
    }

    private fun startCycle() {
        allScope.launch {
            while (true) {
                delay(35)
                if ((_progress.value!! - 5) >= 0) {
                    _progress.postValue(_progress.value!! - 5)
                }
            }
        }
    }

    fun increaseProgress() {
        _progress.postValue(_progress.value!! + 50)
    }
}