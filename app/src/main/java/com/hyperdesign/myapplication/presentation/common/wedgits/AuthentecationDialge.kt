package com.hyperdesign.myapplication.presentation.common.wedgits

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hyperdesign.myapplication.R
import com.hyperdesign.myapplication.presentation.main.theme.ui.Secondry

@Composable
fun ShowAuthentaionDialge(
    onNavToLogin:()->Unit,
    onCancel:()->Unit

){
    var showDialog by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = { showDialog = false },
        modifier = Modifier
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(16.dp),
        title = {
            Text(
                text = stringResource(
                    R.string.you_should_loginin_firstly,

                    ),
                fontSize = 16.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
        },
        confirmButton = {
            Row(modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth(), horizontalArrangement =Arrangement.SpaceBetween ) {
                Text(
                    text = stringResource(R.string.login),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Secondry,
                    modifier = Modifier
                        .clickable {
                            onNavToLogin()
                            showDialog = false
                        }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )

                Text(
                    text = stringResource(R.string.cancel),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Secondry,
                    modifier = Modifier
                        .clickable {
                            showDialog = false
                            onCancel()
                        }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

        },
        dismissButton = {

        },
        containerColor = Color.White,
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 8.dp
    )
}