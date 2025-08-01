package com.hyperdesign.myapplication.presentation.auth.login.ui.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Icon
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.hyperdesign.myapplication.R

@Composable
fun SocialLoginButtons(
    onGoogleClick: () -> Unit,
    onFacebookClick: () -> Unit,
    onLoginAsGuestClick: () -> Unit,
    modifier: Modifier = Modifier,
    orTextColor: Color = Color.Gray,
    buttonTextColor: Color = Color.Black,
    appleButtonColor: Color = Color.White,
    googleButtonColor: Color = Color.White,
    facebookButtonColor: Color = Color(0xFF1877F2),
    cornerRadius: Int = 8
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Or separator
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Divider(
                color = Color.Gray,
                thickness = 1.dp,
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp)
            )
            Text(
                text = stringResource(R.string.or),
                color = orTextColor,
                fontSize = 16.sp,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Divider(
                color = Color.Gray,
                thickness = 1.dp,
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { onLoginAsGuestClick() },
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(cornerRadius.dp)
                ),
            shape = RoundedCornerShape(cornerRadius.dp),
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.LightGray
            )
        ) {
            Text(
                text = stringResource(R.string.login_as_a_guest),
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(15.dp))

        Button(
            onClick = onGoogleClick,
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(cornerRadius.dp)
                ),
            shape = RoundedCornerShape(cornerRadius.dp),
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.LightGray
            )
        ) {
            Image(
                painter = painterResource(id = R.drawable.google_logo),
                contentDescription = "Google Icon",
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.sign_in_with_google),
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(15.dp))
        Button(
            onClick = onFacebookClick,
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = Color.Transparent,
                    shape = RoundedCornerShape(cornerRadius.dp)
                ),
            shape = RoundedCornerShape(cornerRadius.dp),
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                containerColor = facebookButtonColor
            )
        ) {
            Image(
                painter = painterResource(id = R.drawable.logos_facebook),
                contentDescription = "Facebook Icon",
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.continue_with_facebook),
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier.padding(vertical = 6.dp)
            )
        }
    }
}