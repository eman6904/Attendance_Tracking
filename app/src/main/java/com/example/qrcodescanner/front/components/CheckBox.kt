package com.example.qrcodescanner.front.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.example.qrcodescanner.MainActivity
import com.example.qrcodescanner.R

@Composable
fun checkBox(item: String) {

    val oldState = MainActivity.rememberMe_sharedPref.getString(MainActivity.REMEMBER_ME, null)
    var myState = remember { mutableStateOf(false) }
    if (oldState == "false" || oldState == null)
        myState.value = false
    else
        myState.value = true

    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            modifier = Modifier.clip(CircleShape),
            checked = myState.value,
            onCheckedChange = {
                myState.value = it
                MainActivity.rememberMe_sharedPref.edit()
                    .putString(MainActivity.REMEMBER_ME, it.toString()).apply()
            },
            colors = CheckboxDefaults.colors(
                uncheckedColor = colorResource(id = R.color.mainColor),
                checkedColor = colorResource(id = R.color.mainColor),
                checkmarkColor = Color.White,
            )
        )
        Text(text = item)
    }
}