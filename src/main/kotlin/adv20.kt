import java.io.File
import kotlin.math.max

class Adv20 {

    fun run(filename: String) {
        println("Adv20: reading from $filename")

        val tiles = mutableListOf<Tile>()
        val edges = mutableMapOf<Int, List<String>>()
        val edgesFlippedDown = mutableMapOf<Int, List<String>>()
        val edgesFlippedRight = mutableMapOf<Int, List<String>>()
        File(filename).forEachLine { line ->
            if (line.startsWith("Tile")) {
                tiles.add(Tile(line.split(" ")[1].split(":")[0].toInt()))
                println("Added tile ${tiles.last().id}")
            } else if (line.isEmpty()) {
                val tile = tiles.last()
                edges[tile.id] = tile.edges()
                edgesFlippedDown[tile.id] = tile.flipDown().edges()
                edgesFlippedRight[tile.id] = tile.flipRight().edges()
            } else {
                tiles.last().addRow(line.toList())
            }
        }
        val tile = tiles.last()
        edges[tile.id] = tile.edges()
        edgesFlippedDown[tile.id] = tile.flipDown().edges()
        edgesFlippedRight[tile.id] = tile.flipRight().edges()

        var maxMatchesMap = mutableMapOf<Int, Int>()
        for (tile in tiles) {
            println("searching for tiles edge for ${tile.id}")
            println("\tedge[${tile.id}] = ${edges[tile.id]}")
            println("\tflippedDown[${tile.id}] = ${edgesFlippedDown[tile.id]}")
            println("\tflippedRight[${tile.id}] = ${edgesFlippedRight[tile.id]}")
            val tileEdges = listOf<List<String>>(
                edges[tile.id]!!, edgesFlippedDown[tile.id]!!, edgesFlippedRight[tile.id]!!
            )
            var matches = 0
            for (tileId in edges.keys) {
                if (tileId != tile.id) {
                    for (edg in tileEdges) {
                        val m1 = edg.intersect(edges[tileId]!!).size
                        val m2 = edg.intersect(edgesFlippedDown[tileId]!!).size
                        val m3 = edg.intersect(edgesFlippedRight[tileId]!!).size
                        //println("\t\t $m1, $m2, $m3, maxMatches = $maxMatches")
                        matches += (max(max(m1, m2), m3))
                    }
                }
            }
            maxMatchesMap[tile.id] = matches
        }
        val res = maxMatchesMap.toList().sortedBy { (_, value) -> value }.toMap()
        for (e in res) {
            println("${e.key}: ${e.value}")
        }
        val l = res.toList()
        val c = l[0].first.toLong() * l[1].first * l[2].first * l[3].first
        println(c)
    }
}

class Tile(val id: Int) {
    private val rows: MutableList<List<Char>> = mutableListOf()

    fun addRow(items: List<Char>) {
        rows.add(items)
    }

    fun edges(): List<String> {
        val edges = mutableListOf<String>()
        val upperEdge = rows.first().joinToString("")
        var rightEdge = ""
        for (row in rows) {
            rightEdge += row.last()
        }
        val lowerEdge = rows.last().joinToString("")
        var leftEdge = ""
        for (row in rows) {
            leftEdge += row.last()
        }
        return listOf(upperEdge, rightEdge, lowerEdge, leftEdge)
    }

    fun flipDown(): Tile {
        val tile = Tile(this.id)
        for (i in this.rows.size - 1 downTo 0) {
            tile.addRow(this.rows[i].toList())
        }
        return tile
    }

    fun flipRight(): Tile {
        val tile = Tile(this.id)
        for (row in rows) {
            tile.addRow(row.reversed().toList())
        }
        return tile
    }
}
