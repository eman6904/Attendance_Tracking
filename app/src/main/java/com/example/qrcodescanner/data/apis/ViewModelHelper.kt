package com.example.qrcodescanner.data.apis

import android.util.Log
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qrcodescanner.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ViewModelHelper:ViewModel() {

    private val _userName = MutableStateFlow("")
    val userName = _userName.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _passwordConfirm = MutableStateFlow("")
    val passwordConfirm = _passwordConfirm.asStateFlow()

    private val _newPassword = MutableStateFlow("")
    val newPassword = _newPassword.asStateFlow()

    private val _selectedTrainee = MutableStateFlow("")
    val selectedTrainee = _selectedTrainee.asStateFlow()

    private val _searchedTrainee = MutableStateFlow("")
    val searchedTrainee = _searchedTrainee.asStateFlow()

    private val _traineePoints = MutableStateFlow(0.toLong())
    val traineePoints = _traineePoints.asStateFlow()

    private val _pointAction = MutableStateFlow("Points Action")
    val pointAction = _pointAction.asStateFlow()

    private val _pointsString = MutableStateFlow("")
    val pointsString = _pointsString.asStateFlow()


    fun setUserName(name: String) = viewModelScope.launch{
        _userName.value = name
    }
    fun setPassword(pass: String) = viewModelScope.launch{
        _password.value = pass
    }
    fun setEmail(email: String) = viewModelScope.launch{
        _email.value = email
    }
    fun setPassConfirm(pass: String) = viewModelScope.launch{
        _passwordConfirm.value = pass
    }
    fun setNewPassword(pass: String) = viewModelScope.launch{
        _newPassword.value = pass
    }
    fun setTrainee(trainee: String) = viewModelScope.launch{
        _selectedTrainee.value = trainee
    }
    fun settTrainee(trainee: String) = viewModelScope.launch{
        _searchedTrainee.value = trainee
    }
    fun setPoint(point: Long) = viewModelScope.launch{
        _traineePoints.value = point
    }
    fun setAction(action: String) = viewModelScope.launch{
        _pointAction.value = action
    }
    fun setPointsString(point: String) = viewModelScope.launch{
        _pointsString.value = point
    }
}