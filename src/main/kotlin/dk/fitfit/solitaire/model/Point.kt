package dk.fitfit.solitaire.model

data class Point(val x : Int, val y : Int) {

    operator fun minus(other : Point) = Point(x - other.x, y - other.y)

}