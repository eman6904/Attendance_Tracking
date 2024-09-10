package com.example.qrcodescanner.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Logout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.qrcodescanner.data.utils.getMode
import com.example.qrcodescanner.data.utils.logout
import com.example.qrcodescanner.data.utils.refresh
import com.example.qrcodescanner.MainActivity
import com.example.qrcodescanner.R


@Composable
fun menuItems(
    navController: NavHostController,
    showMenu: MutableState<Boolean>,
    errorMessage: MutableState<String>,
    shutDownError: MutableState<Boolean>
) {

    val mode = remember { mutableStateOf("") }
    val context = LocalContext.current
    mode.value = getMode().toString()

    if (showMenu.value) {
        Box() {
            DropdownMenu(
                expanded = showMenu.value,
                onDismissRequest = { showMenu.value = false },
                offset = DpOffset(x = (200).dp, y = (5).dp)
            )
            {
                DropdownMenuItem(
                    onClick = {

                        logout(
                            shutDownError = shutDownError,
                            errorMessage = errorMessage,
                            navController = navController
                        )
                        showMenu.value = false
                    }
                ) {
                    Row() {
                        Icon(
                            imageVector = Icons.Default.Logout,
                            contentDescription = null,
                            tint = colorResource(id = R.color.mainColor)
                        )
                        Text(
                            text = stringResource(R.string.logout),
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }
                DropdownMenuItem(
                    onClick = {

                        when (mode.value) {
                            context.getString(R.string.dark_mode) -> {
                                MainActivity.mode_sharedPref.edit()
                                    .putString(MainActivity.MODE,  context.getString(R.string.light_mode)).apply()
                                refresh(context)
                            }
                            context.getString(R.string.light_mode) -> {
                                MainActivity.mode_sharedPref.edit()
                                    .putString(MainActivity.MODE, context.getString(R.string.dark_mode)).apply()
                                refresh(context)
                            }
                        }
                        showMenu.value = false
                    }
                ) {
                    Row() {
                        when (mode.value) {
                            context.getString(R.string.dark_mode) -> {
                                Icon(
                                    imageVector = Icons.Default.DarkMode,
                                    contentDescription = null,
                                )
                            }
                            context.getString(R.string.light_mode) -> {
                                Icon(
                                    imageVector = Icons.Default.LightMode,
                                    contentDescription = null,
                                )
                            }
                        }
                        Text(
                            text = mode.value,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }
            }
        }
    }
}
