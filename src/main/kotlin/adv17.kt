import java.io.File

const val ACTIVE = '#'
const val INACTIVE = '.'

fun getMapKey(x: Int, y: Int, z: Int): String {
    return "${x}_${y}_${z}"
}

fun getMapKey4(x: Int, y: Int, z: Int, w: Int): String {
    return "${x}_${y}_${z}_${w}"
}

fun getCoords(mapKey: String): Triple<Int, Int, Int> {
    val parts = mapKey.split("_")
    return Triple(parts[0].toInt(), parts[1].toInt(), parts[2].toInt())
}

fun getCoords4(mapKey: String): List<Int> {
    val parts = mapKey.split("_")
    return listOf(parts[0].toInt(), parts[1].toInt(), parts[2].toInt(), parts[3].toInt())
}

fun getNeighbors(key: String): List<String> {
    val coords = getCoords(key)
    val x = coords.first
    val y = coords.second
    val z = coords.third
    val neighbours = mutableListOf<String>()
    val diffs = listOf(-1, 0, 1)
    for (xx in diffs) {
        for (yy in diffs) {
            for (zz in diffs) {
                neighbours.add(getMapKey(x + xx, y + yy, z + zz))
            }
        }
    }
    neighbours.remove(key) // remove itself from its neighbours
    //println("Got ${neighbours.size} neighbours: $neighbours")
    return neighbours
}

fun getNeighbors4(key: String): List<String> {
    val coords = getCoords4(key)
    val x = coords[0]
    val y = coords[1]
    val z = coords[2]
    val w = coords[3]
    val neighbours = mutableListOf<String>()
    val diffs = listOf(-1, 0, 1)
    for (xx in diffs) {
        for (yy in diffs) {
            for (zz in diffs) {
                for (ww in diffs) {
                    neighbours.add(getMapKey4(x + xx, y + yy, z + zz, w + ww))
                }
            }
        }
    }
    neighbours.remove(key) // remove itself from its neighbours
    //println("Got ${neighbours.size} neighbours: $neighbours")
    return neighbours
}

fun getActiveCubes(map: Map<String, Char>): Int {
    return map.filter { e -> e.value == ACTIVE }.size
}

fun executeCycle(map: Map<String, Char>): Map<String, Char> {
    val updated = map.toMutableMap()
    println("entries before ${map.size}")

    for (entry in map) {
        val neighbors = getNeighbors(entry.key)
        neighbors.forEach {
            if (!map.containsKey(it)) {
                updated[it] = INACTIVE
            }
        }
    }

    val keys = updated.keys.toList()
    for (key in keys) {
        val neighbors = getNeighbors(key)
        val activeNeighbours = neighbors
            .filter { n -> map.getOrDefault(n, INACTIVE) == ACTIVE }
            .size
        if (map[key] == ACTIVE) {
            if (activeNeighbours == 2 || activeNeighbours == 3) {
                updated[key] = ACTIVE
            } else {
                updated[key] = INACTIVE
            }
        } else {
            // inactive cube
            if (activeNeighbours == 3) {
                updated[key] = ACTIVE
            } else {
                updated[key] = INACTIVE
            }
        }
    }
    println("\t entries after ${updated.size}")
    return updated
}

fun executeCycle4(map: Map<String, Char>): Map<String, Char> {
    val updated = map.toMutableMap()
    println("entries before ${map.size}")

    for (entry in map) {
        val neighbors = getNeighbors4(entry.key)
        neighbors.forEach {
            if (!map.containsKey(it)) {
                updated[it] = INACTIVE
            }
        }
    }

    val keys = updated.keys.toList()
    for (key in keys) {
        val neighbors = getNeighbors4(key)
        val activeNeighbours = neighbors
            .filter { n -> map.getOrDefault(n, INACTIVE) == ACTIVE }
            .size
        if (map[key] == ACTIVE) {
            if (activeNeighbours == 2 || activeNeighbours == 3) {
                updated[key] = ACTIVE
            } else {
                updated[key] = INACTIVE
            }
        } else {
            // inactive cube
            if (activeNeighbours == 3) {
                updated[key] = ACTIVE
            } else {
                updated[key] = INACTIVE
            }
        }
    }
    println("\t entries after ${updated.size}")
    return updated
}

fun printGrid(map: Map<String, Char>, z: Int) {
    var active = 0
    println("Grid size=${map.size}, z = $z")
    getGridRows(map, z).forEach { row -> run {
        row.forEach{ run {
            print(map[it])
            if (map[it] == ACTIVE) {
                active++
            }
        } }
        println()
    }}
    println("\tactive cubes =  $active")
}


fun getGridRows(map: Map<String, Char>, z: Int): List<List<String>> {
    val rows = mutableListOf<List<String>>()
    val zmap = mutableListOf<Triple<Int, Int, Int>>()

    var zValues = mutableSetOf<Int>()
    map.keys.forEach{ run {
        val coords = getCoords(it)
        if (coords.third == z) {
            zmap.add(coords)
        }
        zValues.add(coords.third)
    }}
    var yValues = mutableSetOf<Int>()
    var xValues = mutableSetOf<Int>()

    zmap.forEach { run {
        xValues.add(it.first)
        yValues.add(it.second)
    } }
    xValues = xValues.toSortedSet()
    yValues = yValues.toSortedSet()
    zValues = zValues.toSortedSet()
    println("\tX values $xValues")
    println("\tY values $yValues")
    println("\tZ values ${zValues}")

    yValues.reversed().forEach { y ->
        run {
            val row = mutableListOf<String>()
            xValues.forEach { x -> row.add(getMapKey(x, y, z)) }
            rows.add(row)
        }
    }

    return rows
}

fun puzzle1(map: Map<String, Char>) {
    var cycles = 0
    var grid = map.toMutableMap()
    //printActiveCubes(grid)
    printGrid(grid, -1)
    printGrid(grid, 0)
    printGrid(grid, 1)
    while (cycles++ < 6) {
        //printGrid(grid)
        //grid = executeCycle(grid).toMutableMap()
        grid = executeCycle4(grid).toMutableMap()
        println("Cycle $cycles finished, active cubes ${getActiveCubes(grid)}")
        //printActiveCubes(grid)
        printGrid(grid, -1)
        printGrid(grid, 0)
        printGrid(grid, 1)
    }
    println("Active cubes: ${getActiveCubes(grid)}")
}

fun adv17(filename: String) {
    println("Adv17: Reading from file $filename")

    val map = mutableMapOf<String, Char>()

    var x = 0
    var y = 0
    File(filename).forEachLine { line ->
        line.forEach {
            map[getMapKey4(x, y, 0, 0)] = it
            map[getMapKey4(x, y, -1,0)] = INACTIVE
            map[getMapKey4(x, y, 1, 0)] = INACTIVE
            map[getMapKey4(x, y, 0, -1)] = INACTIVE
            map[getMapKey4(x, y, -1,-1)] = INACTIVE
            map[getMapKey4(x, y, 1, -1)] = INACTIVE
            map[getMapKey4(x, y, 0, 1)] = INACTIVE
            map[getMapKey4(x, y, -1,1)] = INACTIVE
            map[getMapKey4(x, y, 1, 1)] = INACTIVE
            x++
        }
        x = 0
        y--
    }

    puzzle1(map)
}
