package com.example.qrcodescanner.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.qrcodescanner.R
import com.example.qrcodescanner.data.apis.UIState
import com.example.qrcodescanner.data.viewModel.traineeViewModels.PresentTraineesViewModel

@Composable
fun TabR0w(
    selectedCase: MutableState<Int>,
    attendanceNumber: MutableState<Int>,
    absenceNumber: MutableState<Int>,
    tabs: List<String>,
) {

    val mainColor = colorResource(id = R.color.mainColor)
    val whiteColor = colorResource(id = R.color.white)
    val tabColor = remember { mutableStateOf(mainColor) }
    val contentColor = remember { mutableStateOf(whiteColor) }
    val attendanceViewModel:PresentTraineesViewModel= hiltViewModel()
    val attendanceState by attendanceViewModel.state.collectAsState()
    androidx.compose.material3.TabRow(
        selectedTabIndex = selectedCase.value,
        modifier = Modifier
            .clip(shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .height(32.dp)
            .border(
                2.dp,
                colorResource(id = R.color.mainColor),
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            )
    ) {
        tabs.forEachIndexed { index, title ->

            if (selectedCase.value == index) {
                tabColor.value = mainColor
                contentColor.value = whiteColor
            } else {
                tabColor.value = whiteColor
                contentColor.value = mainColor
            }
            Tab(
                text = {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(
                            text = title,
                            color = contentColor.value
                        )
                       if(index==0) {
                           Text(
                               text = " ${attendanceNumber.value}",
                               color = contentColor.value
                           )
                       }else{
                           Text(
                               text = " ${absenceNumber.value}",
                               color = contentColor.value
                           )
                       }
                    }
                },
                selected = selectedCase.value == index,
                onClick = {
                    if(index==1&&attendanceState!=UIState.Loading)
                         selectedCase.value = index
                    else if(index==0)
                        selectedCase.value = index
                },
                modifier = Modifier
                    .background(tabColor.value)
            )
        }
    }
}