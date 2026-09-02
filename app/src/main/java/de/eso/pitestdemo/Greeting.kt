package de.eso.pitestdemo

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Box(modifier) {
        if (name == "") {
            Text(
                text = "Hello!",
                modifier = modifier
            )
        } else {
            content()
        }

        // Fine
        Button(onClick = {  }) {
            Text(text = stringResource(id = R.string.app_name))
        }

        // Produces junk mutations
        Button(
            onClick = {  }
        ) { Text(text = stringResource(id = R.string.app_name)) }
    }
}
