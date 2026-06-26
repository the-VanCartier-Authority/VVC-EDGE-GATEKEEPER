package com.vvc.edge.gatekeeper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color
import com.vvc.edge.gatekeeper.presentation.auth.AuthScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                colorScheme = darkColorScheme(
                    primary = Color(0xFF00FF00),
                    background = Color.Black,
                    surface = Color.Black,
                    onPrimary = Color.Black,
                    onBackground = Color(0xFF00FF00),
                    onSurface = Color(0xFF00FF00)
                )
            ) {
                AuthScreen()
            }
        }
    }
}
