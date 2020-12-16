import java.io.File

const val floorSeat = '.'
const val emptySeat = 'L'
const val occupiedSeat = '#'

var columns = 0
var rows = 0

    enum class SeatDirection {
        N, E, W, S, NE, NW, SE, SW
    }

fun getMapKey(row: Int, col: Int): String {
    return "${row}_${col}"
}

fun getCoordinates(mapKey: String): Pair<Int, Int> {
    val parts = mapKey.split("_")
    return Pair(parts[0].toInt(), parts[1].toInt())
}

fun getAdjacentSeat(seat: String, map: Map<String, Char>, direction: SeatDirection): Pair<String, Char>? {
    val (row, col) = getCoordinates(seat)
    var adjacentKey: String? = null
    when (direction) {
        SeatDirection.N -> {
            if (row - 1 >= 0) { // up
                adjacentKey = getMapKey(row - 1, col)
            }
        }
        SeatDirection.S -> {
            if (row + 1 < rows) { // down
                adjacentKey  = getMapKey(row + 1, col)
            }
        }
        SeatDirection.W -> {
            if (col - 1 >= 0) { // left
                adjacentKey = getMapKey(row, col - 1)
            }
        }
        SeatDirection.E -> {
            if (col + 1 < columns) { // right
                adjacentKey = getMapKey(row, col + 1)
            }
        }
        SeatDirection.NW -> {
            if (row - 1 >= 0 && col - 1 >= 0) { // upper-left
                adjacentKey = getMapKey(row - 1, col - 1)
            }
        }
        SeatDirection.NE -> {
            if (row - 1 >= 0 && col + 1 < columns) { // upper-right
                adjacentKey = getMapKey(row - 1, col + 1)
            }
        }
        SeatDirection.SW -> {
            if (row + 1 < rows && col - 1 >= 0) { // lower-left
                adjacentKey = getMapKey(row + 1, col - 1)
            }
        }
        SeatDirection.SE -> {
            if (row + 1 < rows && col + 1 < columns) { // lower-right
                adjacentKey = getMapKey(row + 1, col + 1)
            }
        }
    }
    return adjacentKey?.let { Pair(it, map[it]!!) }
}

fun getVisibleSeat(seat: String, map: Map<String, Char>, direction: SeatDirection): Pair<String, Char>? {
    val adjacent = getAdjacentSeat(seat, map, direction)
    if (adjacent != null) {
        if (map[adjacent.first]!! == floorSeat) {
            return getVisibleSeat(adjacent.first, map, direction)
        }
        return adjacent
    }
    return null
}

fun getAdjacentSeats(seat: String, map: Map<String, Char>): Map<String, Char> {
    val adjacent = mutableMapOf<String, Char>()
    for (direction in SeatDirection.values()) {
        getAdjacentSeat(seat, map, direction)?.let { adjacent[it.first] = it.second }
    }
    return adjacent
}

fun getVisibleSeats(seat: String, map: Map<String, Char>): Map<String, Char> {
    val visible = mutableMapOf<String, Char>()
    for (direction in SeatDirection.values()) {
        getVisibleSeat(seat, map, direction)?.let { visible[it.first] = it.second }
    }
    //println("Visible seats for $seat: $visible")
    return visible
}


fun getSeats(type: Char, floorMap: Map<String, Char>): Map<String, Char> {
    return floorMap.filter { e -> e.value == type }
}

fun printMap(map: Map<String, Char>) {
    for (i in 0 until rows) {
        var r = ""
        for (j in 0 until columns) {
            r += map[getMapKey(i, j)]
        }
        println(r)
        r = ""
    }
    println()
}

fun getNewState(seat: String, map: Map<String, Char>): Char {
    val state = map[seat]!!
    if (state == floorSeat) {
        // do nothing
        return floorSeat
    }
    val occupied = getVisibleSeats(seat, map).filter { s -> s.value == occupiedSeat }.size
    if (state == emptySeat) {
        if (occupied == 0) {
            return occupiedSeat
        }
    } else if (state == occupiedSeat) {
        if (occupied >= 5) {
            return emptySeat
        }
    }
    return state
}

fun updateMap(map: Map<String, Char>): Map<String, Char> {
    val updateStates = mutableMapOf<String, Char>()
    for (entry in map) {
        updateStates[entry.key] = getNewState(entry.key, map)
    }
    return updateStates
}


fun adv11(filename: String) {
    println("Adv11: Reading from file $filename")

    var row: Int = 0
    var column: Int = 0

    var map = mutableMapOf<String, Char>()
    File(filename).forEachLine {
        for (ch in it) {
            map[getMapKey(row, column)] = ch
            //println("Wrote ${getMapKey(row, column)} = $ch")
            column++
        }
        row++
        columns = column
        column = 0
    }

    rows = row
    println("got $rows rows and $columns columns")
    row = 0
    column = 0
    val empty = getSeats(emptySeat, map)
    var occupied = getSeats(occupiedSeat, map)
    val floor = getSeats(floorSeat, map)
    println("empty seats ${empty.size}")
    println("occupied seats ${occupied.size}")
    println("floor seats ${floor.size}")

    var occupiedLast = -1
    while (occupiedLast != occupied.size) {
        occupiedLast = getSeats(occupiedSeat, map).size
        map = updateMap(map).toMutableMap()
        occupied = getSeats(occupiedSeat, map)
    }

    println("Finally occupied: ${occupied.size}")
    printMap(map)
    println()
}
