package com.hyperdesign.myapplication.presentation.profile.common.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hyperdesign.myapplication.R
import com.hyperdesign.myapplication.presentation.common.wedgits.CustomButton
import com.hyperdesign.myapplication.presentation.common.wedgits.CustomTextField
import com.hyperdesign.myapplication.presentation.main.navcontroller.LocalNavController
import com.hyperdesign.myapplication.presentation.main.theme.ui.Secondry
import com.hyperdesign.myapplication.presentation.profile.common.mvi.ProfileIntents
import com.hyperdesign.myapplication.presentation.profile.common.mvi.ProfileState
import com.hyperdesign.myapplication.presentation.profile.common.mvi.ProfileViewModel
import com.hyperdesign.myapplication.presentation.profile.common.ui.Widgets.UserProfileHeader
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun UserProfileScreen(profileViewModel: ProfileViewModel = koinViewModel()) {
    val navController = LocalNavController.current
    val profileState by profileViewModel.profileState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    // Show success/error snackbar
    LaunchedEffect(profileState.updateSuccess) {
        if (profileState.updateSuccess) {
            Toast.makeText(context, context.getString(R.string.profile_updated_successfully), Toast.LENGTH_SHORT).show()
            profileViewModel.resetUpdateState()
            navController.popBackStack()

        }
    }

    LaunchedEffect(profileState.errorMessage) {
        profileState.errorMessage?.let { error ->
//            snackbarHostState.showSnackbar(error)
            Toast.makeText(context,error, Toast.LENGTH_SHORT).show()

        }
    }

    UserProfileScreenContent(
        profileState = profileState,
        onChangeName = { name -> profileViewModel.handleIntents(ProfileIntents.OnChangeName(name)) },
        onChangeEmail = { email -> profileViewModel.handleIntents(ProfileIntents.OnChangeEmail(email)) },
        onChangePhone = { phone -> profileViewModel.handleIntents(ProfileIntents.OnChangePhone(phone)) },
        onImageSelected = { imageUri -> profileViewModel.handleIntents(ProfileIntents.OnChangeImage(imageUri)) },
        onUpdateData = { profileViewModel.handleIntents(ProfileIntents.UpdateDataClick) },
        onBackPressed = { navController.popBackStack() }
    )
}

@Composable
fun UserProfileScreenContent(
    profileState: ProfileState,
    onChangeName: (String) -> Unit,
    onChangeEmail: (String) -> Unit,
    onChangePhone: (String) -> Unit,
    onImageSelected: (String) -> Unit,
    onUpdateData: () -> Unit,
    onBackPressed: () -> Unit
) {
    val verticalScroll = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(verticalScroll)
    ) {
        UserProfileHeader(
            onBackPressed = onBackPressed,
            onImageSelected = onImageSelected,
            currentImageUri = profileState.image
        )

        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Spacer(modifier = Modifier.height(20.dp))

            if (profileState.isUpdating) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Secondry)
                }
                Spacer(modifier = Modifier.height(20.dp))
            }

            Text(
                text = stringResource(id = R.string.user_name),
                color = Secondry,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            CustomTextField(
                value = profileState.nameState,
                onValueChange = onChangeName,
                textColor = Color.Black,
                borderWidth = 2f,
                placeholder = stringResource(R.string.enter_your_name),
                modifier = Modifier.fillMaxWidth(),
                keyboardType = androidx.compose.ui.text.input.KeyboardType.Text,
//                enabled = !profileState.isUpdating
            )

            Spacer(modifier = Modifier.height(25.dp))

            Text(
                text = stringResource(id = R.string.email_address),
                color = Secondry,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            CustomTextField(
                value = profileState.emailState,
                onValueChange = onChangeEmail,
                textColor = Color.Black,
                borderWidth = 2f,
                placeholder = stringResource(R.string.enter_email_address),
                modifier = Modifier.fillMaxWidth(),
                keyboardType = androidx.compose.ui.text.input.KeyboardType.Email,
//                enabled = !profileState.isUpdating
            )

            Spacer(modifier = Modifier.height(25.dp))

            Text(
                text = stringResource(id = R.string.phone_number),
                color = Secondry,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            CustomTextField(
                value = profileState.phoneNumber,
                onValueChange = onChangePhone,
                textColor = Color.Black,
                borderWidth = 2f,
                placeholder = stringResource(R.string.enter_phone_number),
                modifier = Modifier.fillMaxWidth(),
                keyboardType = androidx.compose.ui.text.input.KeyboardType.Phone,
//                enabled = !profileState.isUpdating
            )

            Spacer(modifier = Modifier.height(45.dp))

            CustomButton(
                text = stringResource(R.string.update_data),
                onClick = onUpdateData,
                modifier = Modifier.fillMaxWidth(),
                startColor = Color(0xFFF15A25),
                endColor = Color(0xFFFCB203),
//                enabled = !profileState.isUpdating
            )
        }
    }
}