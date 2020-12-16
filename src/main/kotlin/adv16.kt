import java.io.File

fun adv16(filename: String) {
    println("Adv16: Reading from file $filename")

    val fields = mutableMapOf<String, List<IntRange>>()
    val myTicket = mutableListOf<Int>()
    val tickets = mutableListOf<List<Int>>()
    var partsProcessed = 0
    File(filename).forEachLine { line ->
        if (line.isNotEmpty()) {
            if (partsProcessed == 0) {
                val fieldParts = line.split(":")
                val ranges = mutableListOf<IntRange>()
                val fieldName = fieldParts[0].trim()
                val fieldRanges = fieldParts[1].split("or")
                for (range in fieldRanges) {
                    val rangeParts = range.trim().split("-")
                    ranges.add(IntRange(rangeParts[0].toInt(), rangeParts[1].toInt()))
                }
                fields[fieldName] = ranges
                println("Added $ranges for $fieldName")
            } else if (partsProcessed == 1) {
                if (line.contains(":")) {
                    // your ticket header. nothing to do
                } else {
                    line.split(",").forEach { x -> myTicket.add(x.toInt()) }
                    println("Read my ticket: $myTicket")
                }
            } else {
                if (line.contains(":")) {
                    // nearby tickets header, nothing to do
                } else {
                    val ticket = mutableListOf<Int>()
                    line.split(",").forEach { x -> ticket.add(x.toInt()) }
                    tickets.add(ticket)
                    println("Read other ticket $ticket")
                }
            }
        } else {
            partsProcessed++
        }
    }

    val invalidValues = mutableListOf<Int>()
    val validTickets = mutableListOf<List<Int>>()
    tickets.forEach {
        var validTicket = true
        it.forEach { nr ->
            run {
                var valid = false
                for (fieldRanges in fields.values) {
                    if (valid) {
                        break
                    }
                    for (range in fieldRanges) {
                        if (!valid) {
                            if (range.contains(nr)) {
                                valid = true
                                break
                            }
                        }
                    }
                }
                if (!valid) {
                    invalidValues.add(nr)
                    validTicket = false
                }
            }
        }
        if (validTicket) {
            validTickets.add(it)
        }
    }
    //println("Invalid $invalidValues, sum: ${invalidValues.sum()}")
    println("Valid Tickets: $validTickets")
    val fieldPositions = mutableMapOf<String, List<Int>>()
    println("positions = ${myTicket.size}")
    fields.keys.forEach {
        run {
            fieldPositions[it] = getPositions(it, fields, validTickets)
        }
    }
    println("Found positions: $fieldPositions")

    val availablePositions = IntRange(0, myTicket.size - 1).toMutableList()
    val sorted = fieldPositions.toList().sortedBy { (_, value) -> value.size }.toMap()
    val posMap = mutableMapOf<String, Int>()
    sorted.forEach {
        run {
            val candidates = availablePositions.intersect(it.value)
            val position = candidates.first()
            println("Candidates for ${it.key} = $candidates, selecting $position")
            posMap[it.key] = position
            availablePositions.remove(position)
        }
    }

    var mult = 1L
    posMap.keys
        .filter { e -> e.contains("departure") }.toTypedArray()
        .forEach {
            run {
                val position = posMap[it]!!
                mult *= myTicket[position].toLong()
            }
        }
    println("departure sums on my ticket $mult")

}

fun getPositions(fieldName: String, fields: Map<String, List<IntRange>>, tickets: List<List<Int>>): List<Int> {
    val result = mutableListOf<Int>()
    for (position in tickets.first().indices) {
        println("Pos: $position")
        var matchesAllTickets = true
        for (ticket in tickets) {
            //println("\t ticket: $ticket")
            var matchesRange = false
            for (range in fields[fieldName]!!) { // for each range
                //println("\t\t range $range contains ${ticket[position]}: ${range.contains(ticket[position])}")
                if (range.contains(ticket[position])) {
                    matchesRange = true
                    break
                }
            }
            matchesAllTickets = matchesAllTickets && matchesRange
        }
        if (matchesAllTickets) {
            println("\t found pos $position for $fieldName")
            result.add(position)
        }
    }
    println("positions for $fieldName")
    return result
}
