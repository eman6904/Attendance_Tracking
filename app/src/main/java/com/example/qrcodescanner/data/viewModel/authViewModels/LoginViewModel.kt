package com.example.qrcodescanner.data.viewModel.authViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qrcodescanner.data.apis.ApiConnection
import com.example.qrcodescanner.data.apis.UIState
import com.example.qrcodescanner.data.model.LoginRequirements
import com.example.qrcodescanner.data.model.LoginResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val connect2: ApiConnection
):ViewModel() {

    private  val _state= MutableStateFlow<UIState<LoginResponse>?>(null)
    val state=_state.asStateFlow()

    fun login(loginRequirements: LoginRequirements)=viewModelScope.launch {
        _state.emit(UIState.Loading)
        try {
            val response = connect2.connect().login(loginRequirements)
            if (response.isSuccessful) {
                val loginResponse = response.body()
                if (loginResponse != null) {
                    _state.emit(UIState.Success(loginResponse))

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