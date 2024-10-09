package com.kroune.nineMensMorrisApp.viewModel.useCases

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import com.kroune.nineMensMorrisLib.Position
import com.kroune.nineMensMorrisLib.move.Movement
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlin.math.max

/**
 * game analyze use case
 * uses local analysis
 */
class GameAnalyzeUseCase {
    /**
     * depth at which search will be performed
     */
    var depthValue: MutableState<Int> = mutableIntStateOf(4)

    /**
     * best moves as a list of move
     */
    val movementsValue: MutableStateFlow<List<Movement>> = MutableStateFlow(listOf())

    /**
     * decreases search depth
     */
    fun decreaseDepth() {
        depthValue.value = max(0, depthValue.value - 1)
        stopAnalyze()
    }

    /**
     * increases search depth
     */
    fun increaseDepth() {
        depthValue.value++
        stopAnalyze()
    }

    /**
     * current analyze job
     */
    var analyzeJob: Job? = null

    /**
     * starts board analyze
     */
    fun startAnalyze(pos: Position) {
        analyzeJob = CoroutineScope(Dispatchers.Default).launch {
            val newMoves = mutableListOf<Movement>()
            var currentPos = pos
            // see https://github.com/detekt/detekt/issues/3566
            // however we can't exit repeat with break
            @Suppress("UnusedPrivateProperty")
            for (i in 1..depthValue.value) {
                val move = currentPos.findBestMove(depthValue.value.toUByte()) ?: break
                newMoves.add(move)
                currentPos = move.producePosition(currentPos)
            }
            movementsValue.value = newMoves.asReversed()
        }
        analyzeJob?.start()
    }

    /**
     * hides analyze gui and delete it's result
     */
    private fun stopAnalyze() {
        analyzeJob?.cancel()
    }
}
