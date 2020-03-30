package dk.fitfit.solitaire.controller

import dk.fitfit.solitaire.model.Point
import dk.fitfit.solitaire.model.SolitaireEngine
import dk.fitfit.solitaire.view.SolitaireView

private const val SIZE = 7

// this should also be an interface if you want to swap the controller
class SolitaireController(private val view : SolitaireView) {

    private val engine = SolitaireEngine(SIZE)
    private var from : Point? = null

    init {
        view.createView(engine.board, SIZE)
    }

    fun click(point : Point) {
        val fromPoint = from
        if (fromPoint == null) {
            from = point
            return
        }

        // reset "from"
        from = null

        // try to move from "fromPoint" to "point"
        when (val result = engine.move(fromPoint, point)) {
            is SolitaireEngine.MoveResult.Error -> view.showError(result.msg)
            SolitaireEngine.MoveResult.Ok -> view.update(engine.board)
            SolitaireEngine.MoveResult.Win -> {
                view.update(engine.board)
                view.showVictory()
            }
            is SolitaireEngine.MoveResult.Stalled -> {
                view.update(engine.board)
                view.showGameOver(result.piecesLeft)
            }
        }
    }
}