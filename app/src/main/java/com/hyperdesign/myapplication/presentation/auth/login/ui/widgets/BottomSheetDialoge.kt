package com.hyperdesign.myapplication.presentation.auth.login.ui.widgets

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hyperdesign.myapplication.R
import com.hyperdesign.myapplication.presentation.common.wedgits.CustomButton
import com.hyperdesign.myapplication.presentation.main.theme.ui.Secondry

@Composable
fun BottomSheetContent(
    phoneNumber: String,
    onContinueClick: () -> Unit,
    onChangeNumberClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
        ,verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Phone number
        Text(
            text = phoneNumber,
            color = Secondry,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )

        // Message
        Text(
            text = stringResource(R.string.we_will_send_an_authentication_code_to_the_phone_number_above),
            color = Secondry,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = stringResource(R.string.do_you_want_to_continue),
            color = Secondry,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Continue Button
        CustomButton(
            text = stringResource(R.string.coninue),
            onClick = onContinueClick,
            modifier = Modifier.fillMaxWidth(),
            startColor = Color(0xFFF15A25),
            endColor = Color(0xFFFCB203),
        )

        Spacer(modifier = Modifier.padding(vertical = 8.dp))

        Button(
            onClick = {
                onChangeNumberClick()
            },
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(26.dp)
                )
            ,
            shape = RoundedCornerShape(26.dp),
            colors =ButtonDefaults.buttonColors(

                containerColor = Color.Transparent,
                contentColor = Color.LightGray
            )
        ) {
            Text(
                text = stringResource(R.string.change_Number),
                fontSize = 16.sp
            )
        }


    }
}