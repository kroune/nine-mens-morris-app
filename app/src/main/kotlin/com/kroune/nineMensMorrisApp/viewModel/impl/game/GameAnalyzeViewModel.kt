package com.kroune.nineMensMorrisApp.viewModel.impl.game

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.kroune.nineMensMorrisApp.common.toPositions
import com.kroune.nineMensMorrisApp.viewModel.interfaces.ViewModelI
import com.kroune.nineMensMorrisApp.viewModel.useCases.GameAnalyzeUseCase
import com.kroune.nineMensMorrisLib.Position
import kotlinx.coroutines.launch

/**
 * game analyze model
 */
class GameAnalyzeViewModel(
    /**
     * winning positions consequence
     */
    val pos: MutableState<Position>
) : ViewModelI() {
    private val useCase = GameAnalyzeUseCase()

    /**
     * depth of the analysis
     */
    val depth: MutableState<Int> = useCase.depthValue

    /**
     * positions produced by the analysis
     */
    val positions: MutableState<List<Position>> = mutableStateOf(listOf())

    /**
     * quick access function
     */
    fun increaseDepth() {
        useCase.increaseDepth()
    }

    /**
     * quick access function
     */
    fun decreaseDepth() {
        useCase.decreaseDepth()
    }

    /**
     * quick access function
     */
    fun startAnalyze() {
        useCase.startAnalyze(pos.value)
    }

    init {
        viewModelScope.launch {
            useCase.movementsValue.collect {
                positions.value = it.toPositions(pos.value)
            }
        }
    }
}
