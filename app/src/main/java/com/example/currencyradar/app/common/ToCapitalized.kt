package com.example.currencyradar.app.common

fun String.toCapitalized() = replaceFirstChar {
    if (it.isLowerCase()) {
        it.titlecase()
    } else {
        it.toString()
    }
}
