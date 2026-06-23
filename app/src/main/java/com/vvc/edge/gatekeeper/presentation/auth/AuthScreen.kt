package com.vvc.edge.gatekeeper.presentation.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.vvc.edge.gatekeeper.R

@Composable
fun AuthScreen() {
    val cyberpunkBlack = Color(0xFF000000)
    val cyberpunkGreen = Color(0xFF00FF00)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(cyberpunkBlack)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Image(
            painter = painterResource(id = R.drawable.vvc_logo),
            contentDescription = "VVC Logo",
            modifier = Modifier
                .size(200.dp)
                .padding(top = 48.dp)
        )

        Spacer(modifier = Modifier.height(64.dp))

        Text(
            text = "GATEKEEPER SYSTEM ACTIVE",
            color = cyberpunkGreen,
            style = MaterialTheme.typography.headlineMedium
        )
    }
}
