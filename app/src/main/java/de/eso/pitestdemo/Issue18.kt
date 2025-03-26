package de.eso.pitestdemo

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

fun foo() {
    GlobalScope.launch {
        println("Hello, World!")
    }
}