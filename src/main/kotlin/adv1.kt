import java.io.File

fun adv1(filename: String) {
    println("Adv1: Reading from file $filename")
    val numbers = readNumbers(filename)
    val targetSum = 2020
    var answerFound = false;
    for (num1 in numbers) {
        for (num2 in numbers) {
            for (num3 in numbers) {
                if (num1 + num2 + num3 == targetSum) {
                    println("Found numbers: $num1 $num2 $num3")
                    println("Sum = " + (num1 + num2 + num3) + " multiply = " + (num1 * num2 * num3))
                    answerFound = true
                    break
                }
            }
        }
    }
}

fun readNumbers(fileName: String): List<Int> {
    val numbers = mutableListOf<Int>()
    File(fileName).forEachLine {
        line -> numbers.add(line.toInt())
    }
    return numbers
}

