package com.kr8ne.mensMorris.ui.impl.tutorial.domain

import android.content.res.Resources
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.kr8ne.mensMorris.BUTTON_WIDTH
import com.kr8ne.mensMorris.Position
import com.kr8ne.mensMorris.R
import com.kr8ne.mensMorris.common.BLUE_
import com.kr8ne.mensMorris.common.EMPTY
import com.kr8ne.mensMorris.common.GREEN
import com.kr8ne.mensMorris.ui.impl.game.GameBoardScreen
import com.kr8ne.mensMorris.ui.interfaces.ScreenModel
import com.kr8ne.mensMorris.viewModel.impl.game.GameBoardViewModel
import com.kr8ne.mensMorris.viewModel.interfaces.ViewModelI

/**
 * this screen tells about information indicators provide
 */
class NormalMovesTutorialScreen(val resources: Resources) : ScreenModel {
    private val position = Position(
        // @formatter:off
        arrayOf(
            BLUE_,                  EMPTY,                  EMPTY,
                    GREEN,          EMPTY,          EMPTY,
                            EMPTY,  EMPTY,  BLUE_,
            EMPTY,  GREEN,  EMPTY,          EMPTY,  EMPTY,  EMPTY,
                            EMPTY,  EMPTY,  EMPTY,
                    GREEN,          EMPTY,          GREEN,
            EMPTY,                  BLUE_,                  BLUE_
        ),
        // @formatter:on
        freePieces = Pair(0u, 0u), pieceToMove = false, removalCount = 0
    )

    private val gameBoard = GameBoardScreen(
        position,
        //selectedButton = mutableStateOf(22),
        navController = null
    )

    @Composable
    override fun InvokeRender() {
        // TODO: add animations
        gameBoard.RenderPieceCount()
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(15))
                .padding(bottom = BUTTON_WIDTH * 3),
            Alignment.BottomCenter
        ) {
            Column {
                gameBoard.viewModel.data.handleHighLighting()
                gameBoard.InvokeRender()
                Text(text = resources.getString(R.string.tutorial_normal_moves_condition))
                Text(text = resources.getString(R.string.tutorial_normal_moves_highlighting))
            }
        }
    }

    override val viewModel: ViewModelI
        get() = TODO("Not yet implemented")
}
