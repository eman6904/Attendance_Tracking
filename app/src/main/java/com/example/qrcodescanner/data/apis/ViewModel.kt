package com.example.qrcodescanner.data.apis


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qrcodescanner.MainActivity.Companion.connect
import com.example.qrcodescanner.data.model.LoginRequirements
import com.example.qrcodescanner.data.model.LoginResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException


class ViewModel:ViewModel(){

   private  val _state=MutableStateFlow<LoadingState<LoginResponse>?>(null)
    val state=_state.asStateFlow()

    fun login(loginRequirements: LoginRequirements)=viewModelScope.launch {
        _state.emit(LoadingState.Loading)

        try {
            val response = connect.connect().login(loginRequirements)
            if (response.isSuccessful) {
                val loginResponse = response.body()
                if (loginResponse != null) {
                    _state.emit(LoadingState.Success(loginResponse))
                    Log.d("state",state.value.toString())

                } else {
                    _state.emit(LoadingState.Error("Empty response"))

                }
            } else {
                _state.emit(LoadingState.Error("Error ${response.code()}: ${response.message()}"))
            }
        } catch (e: IOException) {
           _state.emit(LoadingState.Error("Please check your Internet Connection."))
        } catch (e: HttpException) {
           _state.emit(LoadingState.Error("Server error: ${e.code()}"))
        } catch (e: Exception) {
            _state.emit(LoadingState.Error(e.message ?: "Unknown error")) // معالجة أي أخطاء أخرى
        }
    }
}