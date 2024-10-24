package com.example.qrcodescanner.data.viewModel.traineeViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qrcodescanner.MainActivity.Companion.token
import com.example.qrcodescanner.data.apis.ApiConnection
import com.example.qrcodescanner.data.apis.UIState
import com.example.qrcodescanner.data.model.ApiResponse
import com.example.qrcodescanner.data.model.PresentedTraineesResponse
import com.example.qrcodescanner.data.utils.getCurrentCamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class PresentTraineesViewModel @Inject constructor(
    private val connect2: ApiConnection
): ViewModel() {

    private  val _state= MutableStateFlow<UIState<PresentedTraineesResponse>?>(null)
    val state=_state.asStateFlow()

    fun getPresentTrainees(keyWord:String)=viewModelScope.launch {
        _state.emit(UIState.Loading)
        try {
            val campId = getCurrentCamp()?.id?.toInt()
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
            var currentDate = sdf.format(Date())
            val response = connect2.connect().getPresentTrainees(campId!!,currentDate,keyWord,"Bearer $token")

            if (response.isSuccessful) {
                val traineesResponse = response.body()
                if (traineesResponse != null) {
                    _state.emit(UIState.Success(traineesResponse))

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