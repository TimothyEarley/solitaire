package dk.fitfit.solitaire

import dk.fitfit.solitaire.SolitaireEngine.TILE.*

class SolitaireEngine(size: Int) {
    var stall = false
    var win = false

    enum class TILE(val symbol: String) {
        ILLEGAL("I"), EMPTY("E"), FULL("F")
    }

    val board: Array<Array<TILE>> = generateBoard(size)

    private fun generateBoard(size: Int): Array<Array<TILE>> {
        if (size < 7 && size % 2 != 0) {
            throw IllegalArgumentException("Size must be bigger than or equal to 7 and odd")
        }

        val board: Array<Array<TILE>> = Array(size) { Array(size) { FULL } }

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

    fun move(row: Int, col: Int, trow: Int, tcol: Int) {
        val from = board[row][col]
        val to = board[trow][tcol]

        if (from != FULL) {
            throw IllegalAccessException("From not full")
        }
        if (to != EMPTY) {
            throw IllegalAccessException("To not empty")
        }

        val moveToNeighbour = mapOf(
            Pair(-2, 0) to Pair(-1, 0),
            Pair(0, -2) to Pair(0, -1),
            Pair(2, 0) to Pair(1, 0),
            Pair(0, 2) to Pair(0, 1)
        )
        val move = Pair(tcol - col, trow - row)
        val neighbour = moveToNeighbour[move] ?: throw IllegalAccessException("Illegal move")
        val rowRemove = trow - neighbour.second
        val colRemove = tcol - neighbour.first

        if (board[rowRemove][colRemove] != FULL) {
            throw IllegalAccessException("Illegal move, empty... Remove piece")
        } else {
            board[row][col] = EMPTY
            board[rowRemove][colRemove] = EMPTY
            board[trow][tcol] = FULL
        }

        detectWin()
        detectStall()
    }

    private fun detectWin() {
        win = board.flatten().count { it == FULL } == 1
    }

    private fun detectStall() {
        stall = true

        for ((i, r) in board.withIndex()) {
            for ((j, _) in r.withIndex()) {
                val tile = board[i][j]
                if (tile == FULL) {
                    mapOf(Pair(0, -1) to Pair(0, -2),
                        Pair(1, 0) to Pair(2, 0),
                        Pair(0, 1) to Pair(0, 2),
                        Pair(-1, 0) to Pair(-2, 0)).forEach {
                        // Get neighbour row, col
                        val newRow = i - it.key.first
                        val newCol = j - it.key.second
                        // Get target row, col
                        val newTargetRow = i - it.value.first
                        val newTargetCol = j - it.value.second
                        // Neighbour within bounds
                        if (newRow >= 0 && newRow < board.size
                            && newCol >= 0 && newCol < board.size
                            // Assert neighbour is full
                            && board[newRow][newCol] == FULL
                            // Target within bounds
                            && newTargetRow >= 0 && newTargetRow < board.size
                            && newTargetCol >= 0 && newTargetCol < board.size
                            // Assert target is empty
                            && board[i - it.value.first][j - it.value.second] == EMPTY) {
                            stall = false
                            return
                        }
                    }
                }
            }
        }
    }
}
