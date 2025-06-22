package com.deezer.exoapplication.testUtils

import kotlin.random.Random

private val lowerCaseAlphabet: List<Char> = ('a'..'z').toList()
private val upperCaseAlphabet: List<Char> = ('A'..'Z').toList()

fun Random.nextString(length: Int = nextInt(2, 20)): String {
    val alphabet = lowerCaseAlphabet + upperCaseAlphabet

    return (1..length)
        .map { alphabet.random() }
        .joinToString("")
}
