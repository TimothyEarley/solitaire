package dk.fitfit.solitaire.model

enum class Tile(val symbol: String) {
    ILLEGAL("I"), EMPTY("E"), FULL("F")
}

typealias Board = Array<Array<Tile>>

operator fun Board.get(point : Point): Tile = this[point.x][point.y]
operator fun Board.set(point: Point, value : Tile) {
    this[point.x][point.y] = value
}
fun Board.isInBounds(point: Point) : Boolean =
    point.x >= 0 && point.x < this.size && point.y >= 0 && point.y < this[point.x].size