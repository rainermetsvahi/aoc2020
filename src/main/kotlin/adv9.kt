import java.io.File

fun adv9(filename: String) {
    println("Adv9: Reading from file $filename")

    //val size = 5
    val size = 25
    val fifo = Fifo(size)
    var linesRead = 0
    val invalid: Long = 138879426
    //val invalid: Long = 127
    var invalidReached = false
    val numbers = mutableListOf<Long>()
    File(filename).forEachLine { line ->
        run {
            val num = line.toLong()
            /*if (linesRead >= size) {
                println("Fifo filled, starting validating")
                if (!fifo.isValidNumber(num)) {
                    println("Not valid: $num")
                    throw Exception()
                }
            }
            fifo.add(num)
            println("Added $num, fifo = $fifo")

            linesRead++
             */
            if (num == invalid) {
                invalidReached = true
            } else if (!invalidReached) {
                numbers.add(num)
            }
        }
    }
    println("Numbers size ${numbers.size}")
    var currentStart = 0
    var next = 0
    val numSet = mutableListOf<Long>()
    while (currentStart < numbers.size) {
        val num = numbers[next]
        val newSum = numSet.sum() + num
        println("Current start $currentStart, read line: $next, num = $num, sum = $newSum")
        if (newSum == invalid) {
            numSet.add(num)
            println ("Contiguous set found: $numSet")
            numSet.sort()
            println("Sum of smallest and largest = ${numSet.first() + numSet.last()}")
            break
        } else if (newSum > invalid) {
            println("\t $newSum > $invalid")
            numSet.clear()
            currentStart++
            next = currentStart
        } else {
            numSet.add(num)
            next++
        }
    }
}

class Fifo(private val capacity: Int) {
    private var values: LongArray = LongArray(capacity)
    private var currentIndex = 0

    fun add(value: Long) {
        this.values[currentIndex++ % capacity] = value
    }

    fun isValidNumber(nr: Long): Boolean {
        for (i in 0 until capacity) {
            for (j in 1 until capacity) {
                if (values[i] != values[j] && values[i] + values[j] == nr)
                    return true
            }
        }
        return false
    }

    override fun toString(): String {
        var s = ""
        this.values.forEach { v -> s += ", $v" }
        return s
    }
}
