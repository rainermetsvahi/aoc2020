import java.io.File

fun adv8(filename: String) {
    println("Adv8: Reading from file $filename")
    val lines = mutableListOf<Instruction>()
    File(filename).forEachLine { line ->
        run {
            val parts = line.split(" ")
            lines.add(Instruction(parts[0], parts[1].toInt(), false))
        }
    }

    fun repair(startLine: Int, instructions: MutableList<Instruction>) {
        var repaired = false
        for ((i, x) in instructions.withIndex()) {
            if (!repaired && i >= startLine) {
                if (x.command == "jmp") {
                    x.command = "nop"
                    repaired = true
                } else if (x.command == "nop") {
                    x.command = "jmp"
                    repaired = true
                }
            }
        }
    }

    // return (last instruction executed, counter value, nextStep index)
    fun execute(instructions: MutableList<Instruction>): Triple<Instruction, Int, Int> {
        var nextStep = 0
        var counter = 0
        var instruction = instructions[nextStep]
        while (!instruction.visited) {
            instruction.visited = true
            when (instruction.command) {
                "nop" -> {
                    nextStep += 1
                }
                "acc" -> {
                    counter += instruction.value
                    nextStep += 1
                }
                "jmp" -> {
                    nextStep += instruction.value
                }
                else -> {
                    throw Exception("Unknown operation ${instruction.command}")
                }
            }
            if (nextStep < instructions.size) {
                instruction = instructions[nextStep]
            } else {
                break
            }
        }
        return Triple(instruction, counter, nextStep)
    }

    for (i in 0..lines.size) {
        val instructions = mutableListOf<Instruction>()
        lines.forEach { l -> instructions.add(Instruction(l.command, l.value, false)) }
        repair(i, instructions)
        val result = execute(instructions)
        if (result.third == lines.size) {
            println("Execution finished, counter = ${result.second}")
            break
        }
    }
}

class Instruction(var command: String, val value: Int, var visited: Boolean) {
    override fun toString(): String {
        return "$command, $value $visited"
    }
}
