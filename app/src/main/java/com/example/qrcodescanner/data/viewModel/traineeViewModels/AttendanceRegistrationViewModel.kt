package com.example.qrcodescanner.data.viewModel.traineeViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qrcodescanner.MainActivity.Companion.token
import com.example.qrcodescanner.data.apis.ApiConnection
import com.example.qrcodescanner.data.apis.UIState
import com.example.qrcodescanner.data.model.AttendanceRegistrationRequirements
import com.example.qrcodescanner.data.model.AttendanceRegistrationResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class AttendanceRegistrationViewModel@Inject constructor(
    private val connect2: ApiConnection
): ViewModel() {

    private  val _state= MutableStateFlow<UIState<AttendanceRegistrationResponse>?>(null)
    val state=_state.asStateFlow()

    fun addTraineeToAttendance(traineeId:AttendanceRegistrationRequirements)=viewModelScope.launch {
        _state.emit(UIState.Loading)
        try {
            val response = connect2.connect().addTraineeToAttendance(traineeId,"Bearer $token")
            if (response.isSuccessful) {
                val attendanceResponse = response.body()
                if (attendanceResponse != null) {
                    _state.emit(UIState.Success(attendanceResponse))

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