import java.io.File

fun adv4(filename: String) {
    println("Adv4: Reading from file $filename")

    fun fromMap(m: Map<String, String>): Passport {
        return Passport(
            m.getOrDefault("byr", ""),
            m.getOrDefault("iyr", ""),
            m.getOrDefault("eyr", ""),
            m.getOrDefault("hgt", ""),
            m.getOrDefault("hcl", ""),
            m.getOrDefault("ecl", ""),
            m.getOrDefault("pid", ""),
            m.getOrDefault("cid", "")
        )
    }

    val list = mutableListOf<Passport>()
    val map = mutableMapOf<String, String>()
    File(filename).forEachLine { line ->
        if (line.isNotEmpty()) {
            line.split(" ").forEach { p ->
                val pair = p.split(":")
                val key = pair[0]
                val value = pair[1]
                map[key] = value
            }
        } else {
            list.add(fromMap(map))
            map.clear()
        }
    }
    println("Valid passports = " + list.filter{ p -> p.isValid() }.size)
}

class Passport(val byr: String, val iyr: String, val eyr: String, val hgt: String, val hcl: String, val ecl: String,
               val pid: String, val cid: String) {

    private fun isValidByr(): Boolean {
        if (this.byr.isNotEmpty()) {
            val year = this.byr.toInt()
            return year in 1920..2002
        }
        return false
    }

    private fun isValidIyr(): Boolean {
        if (this.iyr.isNotEmpty()) {
            val year = this.iyr.toInt()
            return year in 2010..2020
        }
        return false
    }

    private fun isValidEyr(): Boolean {
        if (this.eyr.isNotEmpty()) {
            val year = this.eyr.toInt()
            return year in 2020..2030
        }
        return false
    }

    private fun isValidHgt(): Boolean {
        if (this.hgt.isNotEmpty()) {
            if (this.hgt.endsWith("cm")) {
                val value = this.hgt.split("cm")[0].toInt()
                //println(" hgt ${this.hgt}: $value : ${value in 150..193} cm ")
                return value in 150..193
            } else if (this.hgt.endsWith("in")) {
                val value = this.hgt.split("in")[0].toInt()
                //println(" hgt ${this.hgt}: $value : ${value in 59..76} in ")
                return value in 59..76
            }
            return false
        }
        return false
    }

    private fun isValidHcl(): Boolean {
        val regex = """#([0-9a-f]){6}""".toRegex()
        //println("HCL ${this.hcl} ${regex.matches(this.hcl)}")
        return this.hcl.isNotEmpty() && regex.matches(this.hcl)
    }

    private fun isValidEcl(): Boolean {
        val values = listOf("amb", "blu", "brn", "gry",  "grn", "hzl", "oth")
        return this.ecl.isNotEmpty() && values.contains(this.ecl)
    }

    private fun isValidPid(): Boolean {
        val regex = """[0-9]{9}""".toRegex()
        return this.pid.isNotEmpty() && regex.matches(this.pid)
    }

    fun isValid(): Boolean {
        return this.isValidByr() &&  this.isValidIyr() && this.isValidEyr() && this.isValidHgt()
                && this.isValidHcl() && this.isValidEcl() && this.isValidPid()
    }
}

