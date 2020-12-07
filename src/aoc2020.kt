import java.io.File

fun main(args: Array<String>) {
    adv7(args[0])
}

fun adv7(filename: String) {
    println("ADV7: Reading $filename")

    val containsSingle = Regex("^(\\w+ \\w+) bags contain (\\d+) (\\w+ \\w+) bag(s)?\\.")
    val containsMultiple = Regex("^(\\w+ \\w+) bags contain (.* bag[s]?, .*)\\.")
    val singleInMultiple = Regex("(\\d+) (\\w+ \\w+) bag[s]?")
    val containsNoOther = Regex("^(\\w+ \\w+) bags contain no other bags.")

    var colours = mutableMapOf<String, MutableList<ColourWithAmount>>()
    File(filename).forEachLine { line ->
        run {
            val match = containsSingle.find(line)
            if (match != null) {
                val (colour, containCount, containColour) = match.destructured
                colours.getOrPut(colour) { mutableListOf() }.add(ColourWithAmount(containColour, containCount.toInt()))
            } else {
                val multipleMatch = containsMultiple.find(line)
                if (multipleMatch != null) {
                    val (col, multiple) = multipleMatch.destructured
                    multiple.split(", ").forEach { part ->
                        run {
                            val (containCount, containColour) = singleInMultiple.find(part)!!.destructured
                            colours.getOrPut(col) { mutableListOf() }.add(ColourWithAmount(containColour, containCount.toInt()))
                        }
                    }
                } else {
                    val noOtherMatch = containsNoOther.find(line)
                    if (noOtherMatch != null) {
                        val col = noOtherMatch.destructured.component1()
                        colours.put(col, mutableListOf())
                    } else {
                        println("Nothing matched for $line")
                    }
                }
            }
        }
    }
    val colour = "shiny gold"

    fun getParents(child: String): MutableSet<String> {
        val parents = mutableSetOf<String>()
        val directParents = colours.filter { entry -> entry.value.any { b -> b.color == child } }.keys
        parents.addAll(directParents)
        directParents.forEach { parent ->
            run {
                val indirectParents = getParents(parent)
                parents.addAll(indirectParents)
            }
        }
        return parents
    }

    fun getChildBags(child: String): Int {
        var childBags = 0
        colours[child]?.forEach { c ->
            run {
                childBags += c.amount + c.amount * getChildBags(c.color)
            }
        }
        return childBags
    }

    println(getParents(colour).size)
    println(getChildBags(colour))
}

class ColourWithAmount(val color: String, val amount: Int) {
}
