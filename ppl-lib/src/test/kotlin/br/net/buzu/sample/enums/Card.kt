package br.net.buzu.sample.enums

class Card {

    var suit: Suit? = null
    var number: Int = 0

    constructor()

    constructor(suit: Suit, number: Int) : super() {
        this.suit = suit
        this.number = number
    }

    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + number
        result = prime * result + if (suit == null) 0 else suit!!.hashCode()
        return result
    }

    override fun equals(obj: Any?): Boolean {
        if (this === obj)
            return true
        if (obj == null)
            return false
        if (javaClass != obj.javaClass)
            return false
        val other = obj as Card?
        return if (number != other!!.number) false else suit == other.suit
    }

    override fun toString(): String {
        return "$number domainOf $suit"
    }

}
