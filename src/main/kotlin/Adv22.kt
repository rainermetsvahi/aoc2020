import java.io.File
import java.lang.Exception


class Adv22(fileName: String) {
    private val file = fileName

    fun run() {
        println("Adv20: reading from ${this.file}")

        var player1Cards = false
        var player2Cards = false
        val cards1 = mutableListOf<Int>()
        val cards2 = mutableListOf<Int>()
        File(this.file).forEachLine { line ->
            if (line.startsWith("Player 1")) {
                player1Cards = true
            } else if (line.startsWith("Player 2")) {
                player2Cards = true
                player1Cards = false
            } else if (line.isEmpty()) {
                // nothing
            } else {
                if (player1Cards) {
                    cards1.add(line.toInt())
                }
                if (player2Cards) {
                    cards2.add(line.toInt())
                }
            }
        }

        //play(cards1, cards2)
        val played = mutableListOf<Pair<List<Int>, List<Int>>>()
        val result = playRecursive(cards1, cards2, emptyList())
        if (result.first.isEmpty()) {
            println("Player 2 won ")
            println(calculateScore(result.second))
        } else {
            println("Player 1 won")
            println(calculateScore(result.first))
        }
    }

    fun printState(cards1: List<Int>, cards2: List<Int>) {
        println()
        println("P1 cards: $cards1")
        println("P2 cards: $cards2")
    }

    fun playRecursive(player1Deck: List<Int>, player2Deck: List<Int>,
                      previousStates: List<Pair<List<Int>, List<Int>>>): Pair<List<Int>, List<Int>> {
        println("play($player1Deck, $player2Deck, $previousStates)")
        val played = previousStates.toMutableList()
        val cards1 = player1Deck.toMutableList()
        val cards2 = player2Deck.toMutableList()
        while (cards1.isNotEmpty() && cards2.isNotEmpty()) {
            if (played.contains(Pair(cards1, cards2))) {
                println("State $cards1, $cards2 already played, $cards1 wins")
                //println("\t prev states $played")
                return Pair(cards1, emptyList())
            }
            played.add(Pair(cards1, cards2.toList()))
            //println("Added state $cards1, $cards2, new states = $played")
            val card1 = cards1.removeFirst()
            val card2 = cards2.removeFirst()

            //println("\t P1 card $card1")
            //println("\t P2 card $card2")
            if (card1 <= cards1.size && card2 <= cards2.size) {
                // winner of the round is determined by a new round
                val subDeck1 = mutableListOf<Int>()
                val subDeck2 = mutableListOf<Int>()
                for (i in 0 until card1) {
                    subDeck1.add(cards1[i])
                }
                for (i in 0 until card2) {
                    subDeck2.add(cards2[i])
                }
                println("subGame (p1 card = $card1, subdeck = $subDeck1")
                println("subGame (p2 card = $card2, subdeck = $subDeck2")
                val result = playRecursive(subDeck1.toList(), subDeck2.toList(), emptyList())
                println("\t subgame result $result")
                if (result.second.isEmpty()) {
                    println("Player1 won sub game")
                    cards1.add(card1)
                    cards1.add(card2)
                } else {
                    println("Player2 won sub game")
                    cards2.add(card2)
                    cards2.add(card1)
                }
            } else {
                if (card1 > card2) {
                    cards1.add(card1)
                    cards1.add(card2)
                    //println("\t $card1, $card2 added to p1 deck (normal round)")
                } else {
                    cards2.add(card2)
                    cards2.add(card1)
                    //println("\t $card1, $card2 added to p2 deck (normal round)")
                }
            }
        }
        return Pair(cards1, cards2)
    }

    fun play(cards1: MutableList<Int>, cards2: MutableList<Int>): Pair<List<Int>, List<Int>> {
        printState(cards1, cards2)
        while (cards1.isNotEmpty() && cards2.isNotEmpty()) {
            val card1 = cards1.removeFirst()
            val card2 = cards2.removeFirst()
            println("p1 card $card1, p2 card $card2, p1 won")
            if (card1 > card2) {
                cards1.add(card1)
                cards1.add(card2)
                println("\t added to p1 deck")
                printState(cards1, cards2)
            } else {
                cards2.add(card2)
                cards2.add(card1)
                println("\t added to p2 deck")
                printState(cards1, cards2)
            }
            printState(cards1, cards2)
        }

        if (cards1.isEmpty()) {
            println("Player 2 won normal game, score: ${calculateScore(cards2)}")
        } else {
            println("Player 1 won normal game, score: ${calculateScore(cards1)}")
        }
        return Pair(cards1, cards2)
    }

    fun calculateScore(cards: List<Int>): Int {
        var score = 0
        for (i in cards.indices) {
            score += cards[i] * (cards.size - i)
        }
        return score
    }
}
