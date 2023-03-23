package net.salig.tictactoe.domain

import net.salig.tictactoe.core.Constants.InitialGridSize

inline fun <reified T> Array<T>.containsOnly(element: T): Boolean {
    return this.contentEquals(Array(this.size) { element })
}

inline fun <reified T> Array<Array<T>>.colContainsOnly(colIndex: Int, element: T): Boolean {
    val a: Array<T?> = arrayOfNulls<T>(this.size)
    for (i in 0 until this.size)
        a[i] = this[i][colIndex]
    return a.containsOnly(element)
}

inline fun <reified T> Array<Array<T>>.diagonalTopLeftBottomRightContainsOnly(element: T): Boolean {
    val a: Array<T?> = arrayOfNulls<T>(this.size)
    for (i in 0 until this.size)
        a[i] = this[i][i]
    return a.containsOnly(element)
}

inline fun <reified T> Array<Array<T>>.diagonalTopRightBottomLeftContainsOnly(element: T): Boolean {
    val a: Array<T?> = arrayOfNulls<T>(this.size)
    for (i in 0 until this.size)
        a[i] = this[i][InitialGridSize - 1 - i]
    return a.containsOnly(element)
}

inline fun <reified T> Array<Array<T>>.diagonallyContainsOnly(element: T): Boolean {
    return this.diagonalTopRightBottomLeftContainsOnly(element) || this.diagonalTopLeftBottomRightContainsOnly(
        element)
}

inline fun <reified T> Array<Array<T>>.deepContainsOnly(element: T): Boolean {
    for (i in 0 until this.size)
        if (this[i].contains(element))
            return true
    return false
}