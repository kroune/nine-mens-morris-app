package com.kroune.nineMensMorrisApp

/**
 * provides a clean way to access need element of the pair
 */
operator fun <A> Pair<A, A>.get(first: Boolean): A {
    return if (first) this.first
    else this.second
}

/**
 * sums int with UByte
 * @return Byte - sum of int and UByte
 */
operator fun Int.plus(uByte: UByte): UByte {
    return (this + uByte.toInt()).toUByte()
}
