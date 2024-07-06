package com.example.qrcodescanner.coding.functions

import android.content.Context
import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.example.qrcodescanner.MainActivity.Companion.token
import com.example.qrcodescanner.MainActivity.Companion.viewModel
import com.example.qrcodescanner.coding.DataClasses.ExtraPoint
import com.example.qrcodescanner.coding.DataClasses.ItemDetails

@Composable
fun getAllTrainees(trainees:MutableList<ItemDetails>){

    val lifecycleOwner = LocalLifecycleOwner.current
    // Observe the ViewModel's LiveData
    viewModel.getAllTrainees(14,token).observe(lifecycleOwner, Observer { apiResponse ->
        // Clear the list and add new items
        trainees.clear()
        apiResponse?.data?.let {
            trainees.addAll(it)
        }
    })

}
@Composable
fun getPresentTrainees(trainees:MutableList<ItemDetails>,itemsCase:MutableState<String>) {

    val lifecycleOwner = LocalLifecycleOwner.current
    viewModel.getPresentTrainees(14).observe(lifecycleOwner, Observer { apiResponse ->

        trainees.clear()
        if(apiResponse?.data==null)
                itemsCase.value="No trainee has presented yet"

        apiResponse?.data?.let {

               trainees.addAll(it)
        }
    })
}
@Composable
fun getAllCamps(camps:MutableList<ItemDetails>, itemsCase:MutableState<String>) {

    val lifecycleOwner = LocalLifecycleOwner.current
    viewModel.getCamps(token).observe(lifecycleOwner, Observer { apiResponse ->

        camps.clear()
        if(apiResponse?.data==null)
            itemsCase.value="No Camps"

        apiResponse?.data?.let {

            camps.addAll(it)
        }
    })
}
@Composable
fun addTrainee(
    traineeId:String,
    isSuccess:MutableState<Boolean>,
    message:MutableState<String>
){

    val lifecycleOwner = LocalLifecycleOwner.current
    viewModel.addTraineeToAttendance(traineeId,token).observe(lifecycleOwner, Observer { apiResponse ->
        message.value=apiResponse.message
        apiResponse.isSuccess.let {

            isSuccess.value=it
        }
    })
}

fun updatePoints(
    extraPoint:ExtraPoint,
    isSuccess:MutableState<Boolean>,
    message: MutableState<String>,
    lifecycleOwner:LifecycleOwner
){

    viewModel.traineePointsUpdate(extraPoint,token).observe(lifecycleOwner, Observer { apiResponse ->
        message.value=apiResponse.message

        apiResponse.isSuccess.let {
            isSuccess.value=it
        }
    })
}