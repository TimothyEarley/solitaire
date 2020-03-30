package dk.fitfit.solitaire.view

import dk.fitfit.solitaire.controller.SolitaireController
import dk.fitfit.solitaire.model.Board
import dk.fitfit.solitaire.model.Point
import dk.fitfit.solitaire.model.Tile
import dk.fitfit.solitaire.model.get
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.layout.Pane
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color.*
import javafx.scene.paint.Paint
import javafx.scene.shape.Rectangle
import javafx.stage.Stage

interface SolitaireView {
    fun createView(board : Board, size : Int)
    fun showError(msg: String)
    fun showVictory()
    fun showGameOver(piecesLeft: Int)
    fun update(board: Board)
}

private const val TILE_SIZE: Double = 80.toDouble()

class FXApplication(
    private val stage: Stage
) : SolitaireView {
    private val controller: SolitaireController = SolitaireController(this)
    private lateinit var tiles: List<TileView>

    override fun createView(board: Board, size : Int) {
        val root = Pane().apply {
            setPrefSize(size * TILE_SIZE, size * TILE_SIZE)
        }
        tiles = createTiles(root, board)
        val scene = Scene(StackPane(root))
        stage.scene = scene
        stage.show()
    }

    private fun createTiles(root: Pane, board: Board): List<TileView> =
        board.withIndex().flatMap { (rowIndex, row) ->
            row.mapIndexed { colIndex, col ->
                TileView(rowIndex, colIndex).apply {
                    setPaint(col.color())
                    root.children.add(this)
                }
            }
        }

    private fun Tile.color() = when (this) {
        Tile.ILLEGAL -> BLACK
        Tile.EMPTY -> BLUE
        Tile.FULL -> YELLOW
    }

    override fun update(board: Board) {
        tiles.forEach { tile ->
            tile.setPaint(board[tile.point].color())
        }
    }

    override fun showError(msg: String) {
        println(msg)
    }

    override fun showVictory() {
        println("Victory!!!")
    }

    override fun showGameOver(piecesLeft: Int) {
        println("Game over! $piecesLeft pieces left")
    }

    inner class TileView(x: Int, y: Int) : StackPane() {
        val point = Point(x, y)

        init {
            translateX = x * TILE_SIZE
            translateY = y * TILE_SIZE
            onMouseClicked = EventHandler { controller.click(point) }
        }

        fun setPaint(paint: Paint) {
            children.addAll(Rectangle(TILE_SIZE - 2, TILE_SIZE - 2, paint))
        }
    }
}
