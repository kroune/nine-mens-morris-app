package com.kroune.nineMensMorrisApp.data.local

import com.kroune.nineMensMorrisApp.viewModel.useCases.GameAnalyzeUseCase
import com.kroune.nineMensMorrisLib.gameStartPosition
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.Duration

class GameAnalyzeUseCaseTest {
    @Test
    fun depthValue() {
        val instance = GameAnalyzeUseCase()
        for (i in instance.depthValue.value downTo 0) {
            assert(instance.depthValue.value == i)
            instance.decreaseDepth()
        }
        assert(instance.depthValue.value == 0)
        instance.decreaseDepth()
        assert(instance.depthValue.value == 0)
        for (i in 0..20) {
            assert(instance.depthValue.value == i)
            instance.increaseDepth()
        }
    }

    @Test
    fun analyzeWorks() {
        val inst = GameAnalyzeUseCase()
        inst.increaseDepth()
        // check if it stops
        inst.startAnalyze(gameStartPosition)
        assert(inst.analyzeJob?.isActive == true)
        runBlocking {
            delay(15000)
        }
        assert(inst.analyzeJob?.isActive == false)
    }

    @Test
    fun analyzeStops1() {
        val inst = GameAnalyzeUseCase()
        inst.increaseDepth()
        inst.startAnalyze(gameStartPosition)
        assert(inst.analyzeJob?.isActive == true)
        inst.decreaseDepth()
        assert(inst.analyzeJob?.isActive == false)
    }

    @Test
    fun analyzeStops2() {
        val inst = GameAnalyzeUseCase()
        inst.increaseDepth()
        inst.increaseDepth()
        inst.startAnalyze(gameStartPosition)
        runBlocking {
            delay(100)
        }
        assert(inst.analyzeJob?.isActive == true)
        inst.increaseDepth()
        assert(inst.analyzeJob?.isActive == false)
    }

    @Test
    fun analyze4() {
        val inst = GameAnalyzeUseCase()
        Assertions.assertTimeout(Duration.ofSeconds(10)) {
            runBlocking {
                inst.startAnalyze(gameStartPosition)
                assert(inst.analyzeJob?.isActive == true)
                inst.analyzeJob?.join()
                inst.startAnalyze(gameStartPosition)
                assert(inst.analyzeJob?.isActive == true)
            }
        }
    }
}
