package dk.fitfit.solitaire

import dk.fitfit.solitaire.SolitaireEngine.TILE.*
import javafx.event.EventHandler
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.layout.Pane
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color.*
import javafx.scene.paint.Paint
import javafx.scene.shape.Rectangle
import javafx.stage.Stage

class FXApplication {
    private val SIZE = 7
    private val TILE_SIZE: Double = 80.toDouble()
    private val WIDTH: Double = TILE_SIZE * SIZE
    private val HEIGHT: Double = TILE_SIZE * SIZE
    private val engine = SolitaireEngine(SIZE)

    var from : Pair<Int, Int>? = null
    var to : Pair<Int, Int>? = null

    fun start(stage: Stage) {
        val root = createContent()
        val scene = Scene(StackPane(root))
        stage.scene = scene
        stage.show()
    }

    private fun createContent(): Parent {
        val root = Pane()
        root.setPrefSize(WIDTH, HEIGHT)

        for ((rowIndex, row) in engine.board.withIndex()) {
            for ((colIndex, col) in row.withIndex()) {
                val paint = when (col) {
                    ILLEGAL -> BLACK
                    EMPTY -> BLUE
                    FULL -> YELLOW
                }
                val tile = Tile(rowIndex, colIndex, paint)
                root.children.add(tile)
            }
        }

        return root
    }

    inner class Tile(private val x: Int, private val y: Int, paint: Paint) : StackPane() {
        private val border = Rectangle(TILE_SIZE - 2, TILE_SIZE - 2, paint)

        init {
            children.addAll(border)
            translateX = x * TILE_SIZE
            translateY = y * TILE_SIZE
            onMouseClicked = EventHandler { click() }
        }

        private fun click() {
            if (from == null) {
                from = Pair(x, y)
            } else {
                to = Pair(x, y)
            }
            if (from != null && to != null) {
                val row = from!!.first
                val col = from!!.second
                val trow = to!!.first
                val tcol = to!!.second
                from = null
                to = null
                try {
                    engine.move(row, col, trow, tcol)
                } catch (e: Exception) {
                    println(e.message)
                }

                if (engine.stalled) {
                    println("Game over! ${engine.piecesLeft} pieces left")
                }

                if (engine.win) {
                    println("Victory!!!")
                }

                scene.root = createContent()
            }
        }
    }
}
