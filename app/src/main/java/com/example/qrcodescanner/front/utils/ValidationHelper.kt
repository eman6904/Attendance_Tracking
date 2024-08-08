package com.example.qrcodescanner.front.utils

import android.content.Context
import android.util.Patterns
import androidx.compose.runtime.MutableState
import com.example.qrcodescanner.R


fun String.isEmail(): Boolean = Patterns.EMAIL_ADDRESS.matcher(this).matches()
fun isDigitsOnly(text: String): Boolean {
    return text.all { it.isDigit() }
}

fun checkEmail(
    email: MutableState<String>,
    isEmailError: MutableState<Boolean>,
    emailError: MutableState<String>,
    context: Context
): Boolean {

    if (email.value.isEmpty()) {

        isEmailError.value = true
        emailError.value = context.getString(R.string.enter_e_mail)
    } else if (!email.value.isEmail()) {

        isEmailError.value = true
        emailError.value = context.getString(R.string.invalid_email)
    } else {

        isEmailError.value = false
        emailError.value = ""
    }
    return isEmailError.value
}

fun checkPassword(
    password: MutableState<String>,
    isPassError: MutableState<Boolean>,
    passError: MutableState<String>,
    context: Context
): Boolean {

    if (password.value.isEmpty()) {

        isPassError.value = true
        passError.value = context.getString(R.string.enter_your_password)
    } else {

        isPassError.value = false
        passError.value = ""
    }
    return isPassError.value
}

fun checkUserName(
    userName: MutableState<String>,
    isUserNameError: MutableState<Boolean>,
    userNameError: MutableState<String>,
    context: Context
): Boolean {

    if (userName.value.isEmpty()) {

        isUserNameError.value = true
        userNameError.value = context.getString(R.string.enter_your_user_name)
    } else {

        isUserNameError.value = false
        userNameError.value = ""
    }
    return isUserNameError.value
}

fun checkConfirmPassword(
    password: MutableState<String>,
    confirmPassword: MutableState<String>,
    isConfirmPassError: MutableState<Boolean>,
    confirmPassError: MutableState<String>,
    context: Context
): Boolean {

    if (confirmPassword.value.isEmpty()) {

        isConfirmPassError.value = true
        confirmPassError.value = context.getString(R.string.enter_new_password_again)
    } else if (password.value != confirmPassword.value) {

        isConfirmPassError.value = true
        confirmPassError.value = context.getString(R.string.password_doesn_t_match)
    } else {

        isConfirmPassError.value = false
        confirmPassError.value = ""
    }
    return isConfirmPassError.value
}

fun checkPoints(
    isInvalid: MutableState<Boolean>,
    error: MutableState<String>,
    points: String,
    context: Context
    ): Boolean {

    val largePoints = 92233720368547758

    when (isDigitsOnly(points)) {

        true -> {

            if (points.toLong() <= largePoints) {
                isInvalid.value = false
                error.value = ""
            } else {

                isInvalid.value = true
                error.value = context.getString(R.string.the_number_is_too_large)
            }
        }

        false -> {
            isInvalid.value = true
            error.value = context.getString(R.string.points_must_contain_only_digits)
        }
    }

    return isInvalid.value
}