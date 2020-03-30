package dk.fitfit.solitaire.model

import dk.fitfit.solitaire.model.Tile.*

class SolitaireEngine(size: Int) {

    val board: Board = generateBoard(size)

    private fun generateBoard(size: Int): Board {
        if (size < 7 && size % 2 != 0) {
            throw IllegalArgumentException("Size must be bigger than or equal to 7 and odd")
        }

        val board: Board = Array(size) { Array(size) { FULL } }

        board[0][0] = ILLEGAL
        board[1][0] = ILLEGAL
        board[0][1] = ILLEGAL
        board[1][1] = ILLEGAL

        board[size - 1][0] = ILLEGAL
        board[size - 2][0] = ILLEGAL
        board[size - 1][1] = ILLEGAL
        board[size - 2][1] = ILLEGAL

        board[0][size - 1] = ILLEGAL
        board[0][size - 2] = ILLEGAL
        board[1][size - 1] = ILLEGAL
        board[1][size - 2] = ILLEGAL

        board[size - 1][size - 1] = ILLEGAL
        board[size - 2][size - 1] = ILLEGAL
        board[size - 2][size - 2] = ILLEGAL
        board[size - 1][size - 2] = ILLEGAL

        val center = size / 2
        board[center][center] = EMPTY

        return board
    }

    sealed class MoveResult {
        object Ok : MoveResult()
        object Win : MoveResult()
        class Stalled(val piecesLeft : Int) : MoveResult()
        class Error(val msg : String) : MoveResult()
    }

    fun move(from : Point, to : Point) : MoveResult {
        val fromTile = board[from]
        val toTile = board[to]

        if (fromTile != FULL) {
            return MoveResult.Error("From not full")
        }
        if (toTile != EMPTY) {
            return MoveResult.Error("To not empty")
        }

        val neighbour = neighbourMap[to - from] ?: return MoveResult.Error("Illegal move")
        val remove = to - neighbour
        if (board[remove] != FULL) {
            return MoveResult.Error("Illegal move, empty... Remove piece")
        }

        board[from] = EMPTY
        board[remove] = EMPTY
        board[to] = FULL

        val piecesLeft = board.flatten().count { it == FULL }

        if (piecesLeft == 1) {
            return MoveResult.Win
        }

        if (isStalled()) {
            return MoveResult.Stalled(piecesLeft)
        }

        return MoveResult.Ok
    }

    private val neighbourMap =  mapOf(
        Point(-2, 0) to Point(-1, 0),
        Point(0, -2) to Point(0, -1),
        Point(2, 0) to Point(1, 0),
        Point(0, 2) to Point(0, 1)
    )

    private fun isStalled(): Boolean {
        for ((x, row) in board.withIndex()) {
            for ((y, _) in row.withIndex()) {
                val point = Point(x, y)
                val tile = board[point]
                if (tile == FULL) {
                    neighbourMap.forEach { (targetOffset, toOffset) ->
                        val new = point - toOffset
                        val target = point - targetOffset
                        // Neighbour within bounds
                        if (board.isInBounds(new)
                            // Assert neighbour is full
                            && board[new] == FULL
                            // Target within bounds
                            && board.isInBounds(target)
                            // Assert target is empty
                            && board[target] == EMPTY) {
                            return false
                        }
                    }
                }
            }
        }
        return true
    }
}
