package com.example.qrcodescanner.data.viewModel.authViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qrcodescanner.data.apis.ApiConnection
import com.example.qrcodescanner.data.apis.UIState
import com.example.qrcodescanner.data.model.PasswordResetResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class CheckOtpViewModel @Inject constructor(
private val connect2: ApiConnection
): ViewModel() {

    private  val _state= MutableStateFlow<UIState<PasswordResetResponse>?>(null)
    val state=_state.asStateFlow()

    fun checkOtpCode(email:String,otpCode:Int)=viewModelScope.launch {

        _state.emit(UIState.Loading)
        try {
            val response = connect2.connect().checkOtp(email,otpCode)

            if (response.isSuccessful) {

                val checkOtpResponse = response.body()

                if (checkOtpResponse != null) {
                    _state.emit(UIState.Success(checkOtpResponse))

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