import java.io.File
import kotlin.math.max
import kotlin.math.pow

fun adv5(filename: String) {
    println("Adv5: Reading from file $filename")

    fun getNumber(value: String, low: Char, high: Char): Int {
        var range = IntRange(0, 2.toDouble().pow(value.length).toInt() - 1).toList()
        for (element in value) {
            when (element) {
                low -> {
                    range = IntRange(range[0], range[range.size / 2] - 1).toList()
                }
                high -> {
                    range = IntRange(range[range.size / 2], range.last()).toList()
                }
            }
        }
        return range.first()
    }

    var maxSeatId = 0
    var seatIds = mutableListOf<Int>()
    File(filename).forEachLine {
            line -> run {
        val row = getNumber(line.substring(0, 7), 'F', 'B')
        val col = getNumber(line.substring(7), 'L', 'R')
        val seatId = row * 8 + col
        //println ("$line: row = $row, col = $col, seatId = $seatId")
        seatIds.add(seatId)
        maxSeatId = max(maxSeatId, seatId)
    }
    }
    println("Max seat Id = $maxSeatId")
    seatIds.sort()
    for (i in 1..seatIds.size - 1) {
        if (seatIds[i - 1] + 1 != seatIds[i]) {
            println("The seat: ${seatIds[i]}")
        }
    }
    println(seatIds)
}
