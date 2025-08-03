package com.hyperdesign.myapplication.presentation.auth.forgotpassword.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.hyperdesign.myapplication.presentation.auth.login.ui.widgets.HadramoutHeader
import com.hyperdesign.myapplication.presentation.common.wedgits.CustomButton
import com.hyperdesign.myapplication.presentation.common.wedgits.CustomTextField
import com.hyperdesign.myapplication.presentation.common.wedgits.MainHeader
import com.hyperdesign.myapplication.presentation.main.navcontroller.LocalNavController
import com.hyperdesign.myapplication.presentation.main.navcontroller.Screen
import com.hyperdesign.myapplication.presentation.main.theme.ui.Secondry

@Composable
fun ForgotPasswordScreen(){

    val navController = LocalNavController.current
    ForGotPasswordScreenContent(onBackPressed = {
        navController.popBackStack()
    }, onClickToVerifyScreen = {
        navController.navigate(Screen.VerifyScreen.route)
    })
}


@Composable
fun ForGotPasswordScreenContent(onBackPressed: () -> Unit,onClickToVerifyScreen: () -> Unit) {
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
                stringResource(id = R.string.phone_number),
                color = Secondry,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
            CustomTextField(
                value = "+2 ",
                onValueChange = {},
                textColor = Color.Black,
                borderWidth = 2f,
                keyboardType = KeyboardType.Number,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(25.dp))

            CustomButton(
                text = stringResource(R.string.coninue),
                onClick = {
                    onClickToVerifyScreen()
                },
                modifier = Modifier.fillMaxWidth(),
                startColor = Color(0xFFF15A25),
                endColor = Color(0xFFFCB203)
            )
        }
    }


}