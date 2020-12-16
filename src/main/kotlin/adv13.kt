import java.io.File

fun adv13(filename: String) {
    println("Adv13: Reading from file $filename")

    var timestamp: Long = 0
    val busTimes = mutableMapOf<Long, Long>()
    val allBusTimes = mutableMapOf<Long, List<Long>>()
    val buses = mutableListOf<Long>()
    var lines = 0
    File(filename).forEachLine { it ->
        if (lines == 0) {
            timestamp = it.toLong()
            lines++
        } else {
            it.split(",").forEach {
                if (it == "x") {
                    buses.add(0)
                } else {
                    var busId = it.toLong()
                    buses.add(busId)
                    var time = busId
                    val times = mutableListOf<Long>()
                    times.add(time)
                    while (time <= timestamp) {
                        time += busId
                        times.add(time)
                    }
                    busTimes[busId] = time
                    allBusTimes[busId] = times
                }
            }
        }
    }
    println("TS = $timestamp")

    //busTimes.forEach { it -> println("bus ${it.key} = ${it.value}")}
    //allBusTimes.forEach { it -> println("bus ${it.key} = ${it.value}")}
    println(buses)

    fun puzzle1() {
        var earliestBus: Long = buses.first()
        var bestInterval = busTimes[earliestBus]!! - timestamp
        println("First interval is $bestInterval, bus = $earliestBus, TS=${busTimes[earliestBus]}")
        busTimes.forEach { bus ->
            run {
                val interval = bus.value - timestamp
                if (interval < bestInterval) {
                    bestInterval = interval
                    earliestBus = bus.key
                    println("new best interval is $bestInterval, bus = $earliestBus, TS=${bus.value}")
                }
            }
        }
        val waitTime = earliestBus * bestInterval
        println("Earliest bus is $earliestBus with diff $bestInterval, wait time $waitTime")
    }

    fun puzzle2() {
        val busIdWithIndex = mutableListOf<Pair<Long, Int>>()
        for ((i, busId) in buses.withIndex()) {
            if (busId != 0L) {
                busIdWithIndex.add(Pair(busId, i))
                println("Bus ID $busId is at index $i")
            }
        }

        fun isMatch(t: Long): Boolean {
            var matches = true
            for (x in busIdWithIndex) {
                val busId = x.first
                val idx = x.second
                matches = matches && (t + idx) % busId == 0L

                if (!matches)
                    return false
            }
            return matches
        }

        val firstT = busIdWithIndex.first().first
        var t: Long = firstT
        while (true) {
            if (isMatch(t)) {
                println("Found t: $t")
                break
            }
            t += firstT
        }
    }

    puzzle2()
}
