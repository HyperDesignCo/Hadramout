package com.hyperdesign.myapplication.presentation.menu.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hyperdesign.myapplication.R
import com.hyperdesign.myapplication.presentation.main.theme.ui.Gray
import com.hyperdesign.myapplication.presentation.menu.mvi.CheckOutStateModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpecialRequestEditText(state: CheckOutStateModel,onChangeSpecialRequest:(String)->Unit){

    Card (
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(Gray),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.special_request_optinal),
                color = Color.Black,
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
            )
            Text(
                text = stringResource(R.string.no_extras_are_allowed_as_a_special_request),
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth(),
                color = Color.Gray,
                textAlign = TextAlign.Start,


                )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = state.specialRequest,
                onValueChange = { onChangeSpecialRequest(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = RoundedCornerShape(20.dp))
                ,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    cursorColor = Color.Black
                ),
                placeholder = { Text(stringResource(R.string.enter_your_request_here), color = Color.LightGray) }
            )


        }
    }
}

@Composable
fun ShowAddress(district:String ,addressDetails:String){
    Card (
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(Gray),
        shape = RoundedCornerShape(10.dp)
    ) {
    Column(
        modifier = Modifier
            .padding(16.dp),
    ) {
        Text(
            text = stringResource(R.string.delevery_to),
            color = Color.Gray,
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Start,
            fontSize = 14.sp,
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = district,
            fontSize = 16.sp,
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .fillMaxWidth(),
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start,

            )
        Spacer(modifier = Modifier.height(5.dp))


        Text(
                text = addressDetails,
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth(),
                color = Color.Gray,
                textAlign = TextAlign.Start,

                )




    }
        }
}

@Composable
fun PaymentsOption(payment: String, selectedPayment: String, onSelected: (String) -> Unit) {


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp, horizontal = 12.dp)
                .background(color = Color.White)
                .border(
                    1.dp,
                    color = if (selectedPayment == payment) Color(0xFFFCB203) else Color.Transparent
                )
                .clip(RoundedCornerShape(8.dp))

                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                payment,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = if (selectedPayment == payment) Color(0xFFFCB203) else Color.Black
            )

            RadioButton(
                colors = RadioButtonDefaults.colors(if (selectedPayment == payment) Color(0xFFFCB203) else Color.Black),
                selected = selectedPayment == payment,
                onClick = { onSelected(payment) }
            )

        }

}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun Preview(){
    PaymentsOption(payment = "visa", selectedPayment = "visa",{

    })
}