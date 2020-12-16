import java.io.File

enum class Direction(val degrees: Int) {
    N(270),
    E(0),
    W(180),
    S(90);
}

enum class Action {
    N, E, W, S, L, R, F
}

fun rotateShip(value: Int): Direction {
    val degrees = if (value >= 0) value else value + 360
    return when (degrees % 360) {
        Direction.N.degrees -> Direction.N
        Direction.E.degrees -> Direction.E
        Direction.W.degrees -> Direction.W
        Direction.S.degrees -> Direction.S
        else -> throw IllegalArgumentException("Unknown degrees: $degrees")
    }
}

fun rotateWayPoint(shipLoc: ShipLocation, wpLoc: WayPointLocation, degrees: Int): Pair<Int, Int> {
    val diffX = wpLoc.x - shipLoc.x
    val diffY = wpLoc.y - shipLoc.y
    val newDiff = when (if (degrees >= 0) degrees else degrees + 360) {
        Direction.N.degrees -> Pair(-1 * diffY, diffX)
        Direction.E.degrees -> Pair(diffX, diffY)
        Direction.W.degrees -> Pair(-1 * diffX, -1 * diffY)
        Direction.S.degrees -> Pair(diffY, -1 * diffX)
        else -> throw IllegalArgumentException("Unknown degrees: $degrees")
    }
    val newWpLoc = updateWayPointLocation(shipLoc, WayPointLocation(newDiff.first, newDiff.second))
    return Pair(newWpLoc.x, newWpLoc.y)
}

fun movePoint(location: Pair<Int, Int>, direction: Direction, distance: Int):
        Pair<Int, Int> {
    var x = location.first
    var y = location.second
    when (direction) {
        Direction.N -> y += distance
        Direction.E -> x += distance
        Direction.W -> x -= distance
        Direction.S -> y -= distance
    }
    return Pair(x, y)
}

class ShipLocation(val x: Int, val y: Int, val direction: Direction) {
    override fun toString(): String {
        return "Ship(east=$x, north=$y), direction=${direction.name}"
    }
}
class WayPointLocation(val x: Int, val y: Int) {
    override fun toString(): String {
        return "WayP(east=$x, north=$y)"
    }
}

fun puzzle1(action: Action, value: Int, shipLocation: ShipLocation): ShipLocation {
    var shipCoord = Pair(shipLocation.x, shipLocation.y)
    var shipDirection = shipLocation.direction
    when (action) {
        Action.N -> shipCoord = movePoint(shipCoord, Direction.N, value)
        Action.E -> shipCoord = movePoint(shipCoord, Direction.E, value)
        Action.W -> shipCoord = movePoint(shipCoord, Direction.W, value)
        Action.S -> shipCoord = movePoint(shipCoord, Direction.S, value)
        Action.L -> shipDirection = rotateShip(shipDirection.degrees - value)
        Action.R -> shipDirection = rotateShip(shipDirection.degrees + value)
        Action.F -> shipCoord = movePoint(shipCoord, shipDirection, value)
    }
    return ShipLocation(shipCoord.first, shipCoord.second, shipDirection)
}

fun puzzle2(action: Action, value: Int,
             shipLocation: ShipLocation,
             wayPointLocation: WayPointLocation):
        Pair<ShipLocation, WayPointLocation> {
    var shipCoord: Pair<Int, Int> = Pair(shipLocation.x, shipLocation.y)
    var wayPointCoord: Pair<Int, Int> = Pair(wayPointLocation.x, wayPointLocation.y)
    var shipDirection = shipLocation.direction
    when (action) {
        Action.N -> wayPointCoord = movePoint(wayPointCoord, Direction.N, value)
        Action.E -> wayPointCoord = movePoint(wayPointCoord, Direction.E, value)
        Action.W -> wayPointCoord = movePoint(wayPointCoord, Direction.W, value)
        Action.S -> wayPointCoord = movePoint(wayPointCoord, Direction.S, value)
        Action.L -> wayPointCoord = rotateWayPoint(shipLocation, wayPointLocation, -1 * value)
        Action.R -> wayPointCoord = rotateWayPoint(shipLocation, wayPointLocation, value)
        Action.F -> {
            val wpFromShip = Pair(
                wayPointCoord.first - shipCoord.first,
                wayPointCoord.second - shipCoord.second
            )
            println("\t moving ship $shipCoord, wp = $wayPointCoord, wpFromShip $wpFromShip")
            val moveEast = value * wpFromShip.first
            val moveNorth = value * wpFromShip.second
            println("\t need to move ship $moveEast east and $moveNorth north")
            shipCoord = Pair(shipCoord.first + moveEast, shipCoord.second + moveNorth)
            println("\t\t moved ship to $shipCoord")
            val newWpLoc = updateWayPointLocation(
                ShipLocation(shipCoord.first, shipCoord.second, shipDirection),
                WayPointLocation(wpFromShip.first, wpFromShip.second)
            )
            wayPointCoord = Pair(newWpLoc.x, newWpLoc.y)
        }
    }
    return Pair(
        ShipLocation(shipCoord.first, shipCoord.second, shipDirection),
        WayPointLocation(wayPointCoord.first, wayPointCoord.second)
    )
}


fun updateWayPointLocation(shipLocation: ShipLocation, wayPointLocation: WayPointLocation):
        WayPointLocation {
    return WayPointLocation(
        shipLocation.x + wayPointLocation.x,
        shipLocation.y + wayPointLocation.y
    )
}

fun adv12(filename: String) {
    println("Adv12: Reading from file $filename")

    var shipLocation = ShipLocation(0, 0, Direction.E)
    var wayPointLocation = WayPointLocation(shipLocation.x + 10, shipLocation.y + 1)

    println(shipLocation)
    File(filename).forEachLine {
        val action = Action.valueOf(it[0].toString())
        val value = it.substring(1).toInt()
        //shipLocation = puzzle1(action, value, shipLocation)
        //println(shipLocation)
        println(it)
        val result = puzzle2(action, value, shipLocation, wayPointLocation)
        shipLocation = result.first
        wayPointLocation = result.second

        println("\t after move: $shipLocation")
        println("\t after move: $wayPointLocation")
    }

    println("Manhattan distance ${kotlin.math.abs(shipLocation.x) + kotlin.math.abs(shipLocation.y)}")
}
