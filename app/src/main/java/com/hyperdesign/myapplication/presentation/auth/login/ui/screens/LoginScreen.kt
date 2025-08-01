package com.hyperdesign.myapplication.presentation.auth.login.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hyperdesign.myapplication.R
import com.hyperdesign.myapplication.presentation.auth.login.ui.widgets.BottomSheetContent
import com.hyperdesign.myapplication.presentation.auth.login.ui.widgets.HadramoutHeader
import com.hyperdesign.myapplication.presentation.auth.login.ui.widgets.SocialLoginButtons
import com.hyperdesign.myapplication.presentation.common.wedgits.CustomButton
import com.hyperdesign.myapplication.presentation.common.wedgits.CustomTextField
import com.hyperdesign.myapplication.presentation.main.theme.ui.Secondry

@Composable
fun LoginScreen(onClickToScreenLoginTwo: () -> Unit) {
    LoginScreenContent(onClickToScreenLoginTwo = {
        onClickToScreenLoginTwo()

    })
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("ResourceAsColor")
@Composable
fun LoginScreenContent(onClickToScreenLoginTwo:()->Unit) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState()

    Column(
        modifier = Modifier.fillMaxSize().background(Color.White)
    ) {
        HadramoutHeader()

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
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(25.dp))

            CustomButton(
                text = stringResource(R.string.coninue),
                onClick = {
                    onClickToScreenLoginTwo()
//                    showBottomSheet = true
                          },
                modifier = Modifier.fillMaxWidth(),
                startColor = Color(0xFFF15A25),
                endColor = Color(0xFFFCB203)
            )

            Spacer(modifier = Modifier.height(30.dp))

            SocialLoginButtons(
                onGoogleClick = { /* Handle Google login */ },
                onFacebookClick = { /* Handle Facebook login */ },
                onLoginAsGuestClick = {},
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = bottomSheetState
        ) {
            BottomSheetContent(
                phoneNumber = "01001234567",
                onContinueClick = { showBottomSheet = false },
                onChangeNumberClick = { showBottomSheet = false }
            )
        }
    }
}



