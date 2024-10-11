package com.kroune.nineMensMorrisApp.data.remote.account
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class OnlineGameInfoUseCase {
    suspend fun getPlayerInfo(playerId: String): PlayerInfo {
        return withContext(Dispatchers.IO) {
            // Имитация запроса к серверу
            delay(150)  // Задержка для симуляции сетевого запроса
            PlayerInfo(playerId, "Player $playerId", 123)  // Вернуть данные
        }
    }
}

data class PlayerInfo(
    val id: String,
    val name: String,
    val rating: Int
)