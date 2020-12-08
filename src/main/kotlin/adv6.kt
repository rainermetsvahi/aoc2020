import java.io.File

fun adv6(filename: String) {
    println("Adv6: Reading from file $filename")

    val answers = mutableMapOf<Char, Int>()
    val list = mutableListOf<Int>()
    var members = 0
    File(filename).forEachLine { line ->
        if (line.isNotEmpty()) {
            for (ch in line) {
                val count = answers.computeIfAbsent(ch) { 0 }
                answers[ch] = count + 1
            }
            members++
        } else {
            val allYes = answers.filter { entry -> entry.value == members }.size
            //println("${allYes}: $answers members: $members")
            list.add(allYes)
            answers.clear()
            members = 0
        }
    }
    val allYes = answers.filter { entry -> entry.value == members }.size
    //println("${allYes}: $answers members $members")
    list.add(allYes)
    println("Sum is ${list.sum()}")
}
