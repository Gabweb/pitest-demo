package de.eso.pitestdemo

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    if (name == "") {
        Text(
            text = "Hello!",
            modifier = modifier
        )
    } else {
        content()
    }
}
