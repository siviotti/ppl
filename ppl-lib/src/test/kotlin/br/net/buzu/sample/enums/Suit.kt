package br.net.buzu.sample.enums

/**
 * TODO
 *
 * @author Douglas Siviotti (073.116.317-69) #21 3509-7585
 * @since 12 de abr de 2018 - Construção da Duimp (Release 1)
 */
enum class Suit private constructor(val color: Color) {

    /**  */
    Hearts(Color.RED),
    /**  */
    Spades(Color.BLACK),
    /**  */
    Clubs(Color.BLACK),
    /**  */
    Diamonds(Color.RED)

}
