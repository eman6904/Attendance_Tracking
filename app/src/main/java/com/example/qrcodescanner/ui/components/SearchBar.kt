//package com.example.qrcodescanner.ui.components
//
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.Icon
//import androidx.compose.material.MaterialTheme
//import androidx.compose.material.Text
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Close
//import androidx.compose.material.icons.filled.Search
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.SearchBar
//import androidx.compose.material3.SearchBarDefaults
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.MutableState
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.unit.dp
//import com.example.qrcodescanner.data.utils.normalizeText
//import com.example.qrcodescanner.ui.screens.traineeModel
//import com.example.qrcodescanner.R
//import com.example.qrcodescanner.data.apis.ViewModel
//import com.example.qrcodescanner.ui.screens.sadNews
//import com.google.accompanist.swiperefresh.SwipeRefresh
//import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun searchAboutTraineeForUpdatePoints(
//    searchedTrainee: MutableState<String>,
//    traineeId: MutableState<String>
//) {
//
//    var active = remember { mutableStateOf(false) }
//    var isRefreshing = remember { mutableStateOf(false) }
//    val viewModel= ViewModel()
//    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing.value)
//
//
//    LaunchedEffect(isRefreshing.value)
//    {
//        if (isRefreshing.value)
//            viewModel.showProgress.value = false
//        else if (viewModel.trainees.value.isEmpty())
//            viewModel.showProgress.value = true
//
//        viewModel.getAllTrainees()
//        kotlinx.coroutines.delay(2000)
//        isRefreshing.value = false
//    }
//    SearchBar(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(start = 15.dp, end = 15.dp),
//        shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),
//        colors = SearchBarDefaults.colors(
//            containerColor = MaterialTheme.colors.surface,
//            inputFieldColors = androidx.compose.material3.TextFieldDefaults.colors(
//                focusedTextColor = MaterialTheme.colors.onSurface,
//                unfocusedTextColor = MaterialTheme.colors.onSurface
//            )
//        ),
//        query = searchedTrainee.value,
//        onQueryChange = {
//            searchedTrainee.value = it
//        },
//        onSearch = {
//            active.value = false
//        },
//        active = active.value,
//        onActiveChange = {
//            active.value = it
//        },
//        placeholder = {
//            Text(
//                text = stringResource(R.string.search_name),
//                color = MaterialTheme.colors.onSurface
//            )
//        },
//        leadingIcon = {
//            Icon(
//                imageVector = Icons.Default.Search,
//                contentDescription = "Search icon",
//                tint = MaterialTheme.colors.onSurface
//            )
//        },
//        trailingIcon = {
//
//            if (active.value) {
//                Icon(
//                    modifier = Modifier.clickable {
//                        if (searchedTrainee.value.isNotEmpty()) {
//                            searchedTrainee.value = ""
//                            active.value = false
//                        } else
//                            active.value = false
//                    },
//                    imageVector = Icons.Default.Close,
//                    tint = MaterialTheme.colors.onSurface,
//                    contentDescription = "Close icon"
//                )
//            }
//        }
//    ) {
//        progressBar(show = viewModel.showProgress)
//        SwipeRefresh(
//            state = swipeRefreshState,
//            onRefresh = {
//                isRefreshing.value = true
//            },
//        ) {
//            if (viewModel.shutDownError.value)
//                sadNews(message = viewModel.errorMessage)
//            else {
//                if (viewModel.noTrainee.value) {
//                    LazyColumn(
//                        modifier = Modifier.fillMaxSize(),
//                        verticalArrangement = Arrangement.Center,
//                        horizontalAlignment = Alignment.CenterHorizontally
//                    ) {
//                        item {
//                            Text(
//                                text = stringResource(R.string.no_trainees_in_this_camp),
//                                color = MaterialTheme.colors.onSurface
//                            )
//                        }
//                    }
//                } else {
//                    LazyColumn(
//                        modifier = Modifier
//                            .padding(10.dp)
//                            .fillMaxSize(),
//                    ) {
//
//                        for (trainee in viewModel.trainees.value) {
//
//                            trainee.name = normalizeText(trainee.name)
//                        }
//
//                        items(items = viewModel.trainees.value.filter {
//                            it.name.contains(normalizeText(searchedTrainee.value))
//                        }) {
//                            traineeModel(
//                                active = active,
//                                trainee = it,
//                                searchedText = searchedTrainee,
//                                traineeId
//                            )
//                        }
//                    }
//                }
//            }
//        }
//    }
//}