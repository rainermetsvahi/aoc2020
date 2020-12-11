import java.io.File
import kotlin.math.pow

fun adv10(filename: String) {
    println("Adv10: Reading from file $filename")

    val lines = mutableListOf<Int>()
    File(filename).forEachLine { line ->
        run {
            val num = line.toInt()
            lines.add(num)
        }
    }
    lines.sort()
    println(lines)

    val seatAdapter = 0
    val builtIn = lines.last() + 3
    //lines.add(0, seatAdapter)
    //lines.add(builtIn)

    println(lines)

    val diffs = mutableListOf<Int>()
    var previousAdapter = seatAdapter
    for (i in 1 until lines.size) {
        val adapter = lines[i]
        val diff = adapter - previousAdapter
        previousAdapter = adapter
        //println ("Adapter nr $i  = $adapter diff: $diff")
        diffs.add(diff)
    }
    val diffs1 = diffs.filter { d -> d == 1 }.size
    val diffs2 = diffs.filter { d -> d == 2 }.size
    val diffs3 = diffs.filter { d -> d == 3 }.size

    println("diff1 = $diffs1")
    println("diff2 = $diffs2")
    println("diff3 = $diffs3")

    val notRequired = mutableListOf<Int>()
    for (i in 1 until lines.size - 1) {
        val newList = mutableListOf<Int>(*lines.toTypedArray())
        val item = newList[i]
        newList.remove(item)
        if (isValidSequence(newList)) {
            notRequired.add(item)
        }
    }
    println("NotRequired = $notRequired, size = ${notRequired.size}}")
    val p = 2.toDouble().pow(notRequired.size)
    println("$p")
}

fun isValidSequence(seq: List<Int>): Boolean {
    val allowedDiffs = listOf(1, 2, 3)
    for (i in 1 until seq.size - 1) {
        val prev = seq[i - 1]
        val item = seq[i]
        val next = seq[i + 1]
        val diffLast = item - prev
        val diffNext =  next - item
        if (!allowedDiffs.contains(diffLast) || !allowedDiffs.contains(diffNext)) {
            return false
        }
    }
    return true
}
