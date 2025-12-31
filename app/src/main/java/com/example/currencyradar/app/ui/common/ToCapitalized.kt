package com.example.currencyradar.app.ui.common

fun String.toCapitalized() = replaceFirstChar {
    if (it.isLowerCase()) {
        it.titlecase()
    } else {
        it.toString()
    }
}
