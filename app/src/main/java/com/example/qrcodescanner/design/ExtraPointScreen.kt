package com.example.qrcodescanner.design

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.qrcodescanner.R

@Composable
fun extraPoints(navHostController: NavHostController){

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.mainColor)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){

        var pointType= listOf("Plus","Minus")
        var selectedItem by remember { mutableStateOf("Points Action")}
        var points = remember { mutableStateOf("")}
        var searchedText = remember { mutableStateOf("")}

           Text(
               text="Extra Points",
               fontSize = 25.sp,
               fontFamily = FontFamily(Font(R.font.bold2)),
               color= Color.White,
               modifier = Modifier.padding(bottom=40.dp)
           )
           space(h = 15)
           search(searchedText)
           space(h = 15)
           pointsField(points = points)
           space(h = 15)
           selectPointAction(
               itemList = pointType,
               selectedItem =selectedItem ,
               onItemSelected ={selectedItem=it}
           )
           space(h = 15)
           updatePointsButton()
       }

}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun search(searchedText:MutableState<String>){

    var list= mutableListOf<String>("eman","esraa","hoda","hend","rana","rola")
    var active = remember  { mutableStateOf(false)}
    SearchBar(
        modifier= Modifier
            .fillMaxWidth()
            .padding(15.dp),
        shape= RoundedCornerShape(10.dp,10.dp,10.dp,10.dp),
        colors=SearchBarDefaults.colors(
            containerColor = Color.White
        ),
        query=searchedText.value,
        onQueryChange={
            searchedText.value=it
        },
        onSearch={
            active.value=false
        },
        active=active.value,
        onActiveChange={
            active.value=it
        },
        placeholder = {Text(text="Search Name")},
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search icon")
        },
        trailingIcon = {

            if(active.value){
                Icon(
                    modifier=Modifier.clickable {
                        if(searchedText.value.isNotEmpty()) {
                            searchedText.value = ""
                            active.value=false
                        }
                        else
                            active.value=false
                    },
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close icon")
            }
        }
    ){
       LazyColumn(
           modifier = Modifier
               .padding(10.dp)
               .fillMaxSize(),
       ){
         items(items =list.filter {
             it.contains(searchedText.value)
         }){
           ColumnItem(active = active, item = it, searchedText = searchedText)
         }
       }
    }
}
@Composable
fun selectPointAction(
    itemList: List<String>,
    selectedItem: String,
    onItemSelected: (selectedItem: String) -> Unit,
    // through that we can change value of selectedItem,

) {
    var expanded by rememberSaveable() { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .clip(RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp))
    ) {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.background(Color.White)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = selectedItem,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .weight(8f),
                    color = Color.Gray,
                    fontFamily = FontFamily.Default,
                    fontSize = 15.sp
                )
                Icon(
                    modifier = Modifier.weight(1f),
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = Color.DarkGray
                )
            }
        }
    }
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        //to control dropDownMenu position
        offset = DpOffset(x = (-1).dp, y = (-250).dp)
    ) {
        itemList.forEach {
            DropdownMenuItem(
                onClick = {
                    expanded = false
                    onItemSelected(it)
                }
            ) {
                Text(text = it, fontSize = 20.sp, color = Color.DarkGray)
            }
        }
    }

}

@Composable
fun pointsField(points: MutableState<String>) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
        shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),
        elevation = 3.dp,
    ) {
        TextField(
            modifier = Modifier,
            value = points.value,
            onValueChange = { points.value = it },
            placeholder = { Text(text="Points") },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = colorResource(id = R.color.mainColor),
                disabledIndicatorColor = Color.Transparent,
                disabledTextColor = Color.Transparent

            ),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        )
    }
}
@Composable
fun updatePointsButton(){
    Button(
        onClick = {},
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = colorResource(id = R.color.mainColor2),
        ),
        shape= RoundedCornerShape(10.dp,10.dp,10.dp,10.dp),

    ) {
        Text(text="Update Points",
            modifier = Modifier.padding(10.dp),
            color = Color.White,
            fontSize = 17.sp,
            fontFamily = FontFamily(Font(R.font.bold2))
        )
    }
}
@Composable
fun ColumnItem(active:MutableState<Boolean>,item:String,searchedText:MutableState<String>){

    Spacer(modifier = Modifier
        .fillMaxWidth()
        .height(12.dp))
    Text(
        text=item,
        modifier= Modifier
            .clickable {
                searchedText.value = item
                active.value = false
            }
            .fillMaxWidth()
    )
    Spacer(modifier = Modifier
        .fillMaxWidth()
        .height(12.dp))
    Divider(modifier = Modifier
        .fillMaxWidth()
        .heightIn(1.dp))
}
@Composable
fun space(h:Int){
    Spacer(modifier = Modifier
        .height(h.dp)
        .fillMaxWidth())
}

