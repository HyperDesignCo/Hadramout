package com.hyperdesign.myapplication.presentation.auth.login.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hyperdesign.myapplication.R
import com.hyperdesign.myapplication.presentation.auth.login.ui.widgets.HadramoutHeader
import com.hyperdesign.myapplication.presentation.common.wedgits.CustomButton
import com.hyperdesign.myapplication.presentation.common.wedgits.CustomTextField
import com.hyperdesign.myapplication.presentation.main.navcontroller.LocalNavController
import com.hyperdesign.myapplication.presentation.main.navcontroller.Screen
import com.hyperdesign.myapplication.presentation.main.theme.ui.Secondry


@Composable
fun LoginScreenStepTwo() {

    val navController = LocalNavController.current
    LoginScreenStepTwoContent(onGoToForgePasswordScreen = {
        navController.navigate(Screen.ForgotPasswordScreen.route)

    }, onNavToHomeScreen = {
        navController.navigate(Screen.HomeScreen.route) {
            popUpTo(Screen.LoginStepTwoScreen.route) {
                inclusive = true
            }
        }
    })
}


@Composable
fun LoginScreenStepTwoContent(onGoToForgePasswordScreen:()->Unit,onNavToHomeScreen: () -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        HadramoutHeader()
        Column(
            modifier = Modifier.padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(5.dp))

            Text(
                stringResource(R.string.welcome_back_to_hadramout_shekh_elmandy),
                color = Secondry,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                stringResource(R.string.hello_muhammed_we_ve_missed_you),
                color = Color.LightGray,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                stringResource(R.string.password),
                color = Secondry,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
            CustomTextField(
                value = "",
                onValueChange = {},
                isPassword = true,
                textColor = Color.Black,
                borderWidth = 2f,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = stringResource(R.string.forgot_password),
                color = Secondry,
                fontSize = 15.sp,
                modifier = Modifier.fillMaxWidth().clickable{

                    onGoToForgePasswordScreen()
                },
                style = androidx.compose.ui.text.TextStyle(
                    textDecoration = TextDecoration.Underline
                ),
                textAlign = TextAlign.End,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(20.dp))

            CustomButton(
                text = stringResource(R.string.login),
                onClick = {
                    onNavToHomeScreen()

                },
                modifier = Modifier.fillMaxWidth(),
                startColor = Color(0xFFF15A25),
                endColor = Color(0xFFFCB203)
            )
        }





    }




}