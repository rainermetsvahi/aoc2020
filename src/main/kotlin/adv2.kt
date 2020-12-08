import java.io.File

fun adv2(filename: String) {
    println("Adv2: Reading from file $filename")
    val passwords = readPasswords(filename)
    println(" Valid passwords: " + passwords.filter { p -> p.isValidByPosition() } .size)
}

fun readPasswords(fileName: String): List<PasswordWithPolicy> {
    val passwords = mutableListOf<PasswordWithPolicy>()
    File(fileName).forEachLine { line ->
        run {  // 1-3 a: abcde
            val parts = line.split(":")
            val policyParts = parts[0].trim() // 1-3 a
            val password = parts[1].trim()  // abcde
            val policy = PasswordPolicy(
                policyParts.split(" ")[0].split("-")[0].toInt(),
                policyParts.split(" ")[0].split("-")[1].toInt(),
                policyParts.split(" ")[1].get(0)
            )
            passwords.add(PasswordWithPolicy(policy, password))
        }
    }
    return passwords
}

class PasswordPolicy(val minOccurs: Int, val maxOccurs: Int, val symbol: Char) {
}

class PasswordWithPolicy(val policy: PasswordPolicy, val password: String) {
    fun isValid(): Boolean {
        val occurrences = this.password.filter{c -> this.policy.symbol.equals(c)}.length
        return occurrences >= this.policy.minOccurs && occurrences <= this.policy.maxOccurs
    }

    fun isValidByPosition(): Boolean {
        if (this.policy.minOccurs > this.password.length || this.policy.maxOccurs > this.password.length) {
            return false
        }
        val pos1: Char = this.password.get(this.policy.minOccurs - 1)
        val pos2: Char = this.password.get(this.policy.maxOccurs - 1)
        return (pos1.equals(this.policy.symbol) && !pos2.equals(this.policy.symbol))
            || (!pos1.equals(this.policy.symbol) && pos2.equals(this.policy.symbol))
    }
}
