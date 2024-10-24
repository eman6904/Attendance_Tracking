package com.example.qrcodescanner.data.viewModel.authViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qrcodescanner.data.apis.ApiConnection
import com.example.qrcodescanner.data.apis.UIState
import com.example.qrcodescanner.data.model.PasswordResetRequirements
import com.example.qrcodescanner.data.model.PasswordResetResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val connect2: ApiConnection
): ViewModel() {

    private  val _state= MutableStateFlow<UIState<PasswordResetResponse>?>(null)
    val state=_state.asStateFlow()

    fun resetPassword(resetPasswordRequirements: PasswordResetRequirements)=viewModelScope.launch {
        _state.emit(UIState.Loading)

        try {
            val response = connect2.connect().resetPassword(resetPasswordRequirements)

            if (response.isSuccessful) {

                val resetPasswordResponse = response.body()

                if (resetPasswordResponse != null) {
                    _state.emit(UIState.Success(resetPasswordResponse))

                } else {
                    _state.emit(UIState.Error("Empty response"))

                }
            } else {
                _state.emit(UIState.Error("Error ${response.code()}: ${response.message()}"))

            }
        } catch (e: IOException) {
            _state.emit(UIState.Error("Please check your Internet Connection."))

        } catch (e: HttpException) {
            _state.emit(UIState.Error("An error occurred while connecting to the server."))

        } catch (e: Exception) {
            _state.emit(UIState.Error(e.message ?: "Unknown error"))

        }
    }
}