import java.io.File

fun adv3(filename: String) {
    fun getMapKey(row: Int, col: Int): String {
        return "${row}_${col}"
    }

    println("Adv3: Reading from file $filename")
    val map = mutableMapOf<String, Char>()
    var row: Int = 0
    var column: Int = 0
    var columns = 0
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

    val rows = row
    println("got $rows rows and $columns columns")
    row = 0
    column = 0
    var trees = 0
    while (row < rows) {
        val s = map[getMapKey(row, column)]
        //println("looking on map row $row column $column = $s")
        if (s == '#') {
            trees++
        }
        row += 2
        column = (column + 1) % columns
    }
    println("Found $trees on map")
}
