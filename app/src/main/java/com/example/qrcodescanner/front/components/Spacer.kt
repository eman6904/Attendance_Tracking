package com.example.qrcodescanner.front.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun space(h: Int) {
    Spacer(
        modifier = Modifier
            .height(h.dp)
            .fillMaxWidth()
    )
}