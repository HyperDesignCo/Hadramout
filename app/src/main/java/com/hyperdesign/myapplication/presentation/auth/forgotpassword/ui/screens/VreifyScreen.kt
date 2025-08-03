package com.hyperdesign.myapplication.presentation.auth.forgotpassword.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hyperdesign.myapplication.R
import com.hyperdesign.myapplication.presentation.common.wedgits.CustomButton
import com.hyperdesign.myapplication.presentation.common.wedgits.CustomTextField
import com.hyperdesign.myapplication.presentation.common.wedgits.MainHeader
import com.hyperdesign.myapplication.presentation.main.navcontroller.LocalNavController
import com.hyperdesign.myapplication.presentation.main.navcontroller.Screen
import com.hyperdesign.myapplication.presentation.main.theme.ui.Secondry


@Composable
fun VerifyScreen() {
    val navController = LocalNavController.current
    VerifyScreenContent(onBackPressed = {
        navController.popBackStack()

    }, navToResetPasswordScreen = {
        navController.navigate(Screen.ResetPasswordScreen.route)
    })
}


@Composable
fun VerifyScreenContent(onBackPressed: () -> Unit ,navToResetPasswordScreen: () -> Unit ) {


    Column(
        modifier = Modifier.fillMaxSize().background(Color.White)
    ) {
        MainHeader(
            title = stringResource(id = R.string.forgot_my_password),
            onBackPressesd = onBackPressed
        )

        Column(
            modifier = Modifier.padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(5.dp))

            Text(
                stringResource(id = R.string.verify),
                color = Secondry,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                stringResource(id = R.string.your_identity),
                color = Secondry,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(15.dp))

            Text(
                stringResource(id = R.string.Enter_your_code),
                color = Secondry,
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal
            )

            Text(
                "01151828780",
                color = Secondry,
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal
            )

            Spacer(modifier = Modifier.height(20.dp))


            Row(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
            ) {
                Box(modifier = Modifier.size(40.dp)) {
                    CustomTextField(
                        value = "",
                        onValueChange = {},
                        textColor = Color.Black,
                        borderWidth = 2f,
                        modifier = Modifier.fillMaxWidth(),
                        isPassword = false,
                        keyboardType = KeyboardType.Number
                    )

                }
                Spacer(modifier = Modifier.width(5.dp))

                Box(modifier = Modifier.size(40.dp)) {
                    CustomTextField(
                        value = "",
                        onValueChange = {},
                        textColor = Color.Black,
                        borderWidth = 2f,
                        modifier = Modifier.fillMaxWidth(),
                        isPassword = false,
                        keyboardType = KeyboardType.Number
                    )

                }

                Spacer(modifier = Modifier.width(5.dp))
                Box(modifier = Modifier.size(40.dp)) {
                    CustomTextField(
                        value = "",
                        onValueChange = {},
                        textColor = Color.Black,
                        borderWidth = 2f,
                        modifier = Modifier.fillMaxWidth(),
                        isPassword = false,
                        keyboardType = KeyboardType.Number
                    )

                }

                Spacer(modifier = Modifier.width(5.dp))

                Box(modifier = Modifier.size(40.dp)) {
                    CustomTextField(
                        value = "",
                        onValueChange = {},
                        textColor = Color.Black,
                        borderWidth = 2f,
                        modifier = Modifier.fillMaxWidth(),
                        isPassword = false,
                        keyboardType = KeyboardType.Number
                    )

                }

            }

            Spacer(modifier = Modifier.height(35.dp))

            CustomButton(
                text = stringResource(R.string.coninue),
                onClick = {

                    navToResetPasswordScreen()
                },
                modifier = Modifier.fillMaxWidth(),
                startColor = Color(0xFFF15A25),
                endColor = Color(0xFFFCB203),
            )

            Spacer(modifier = Modifier.padding(vertical = 8.dp))

            Button(
                onClick = {
//                    onChangeNumberClick()
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
                    text = stringResource(R.string.dont_receive_code),
                    fontSize = 16.sp
                )
            }








        }


    }

}