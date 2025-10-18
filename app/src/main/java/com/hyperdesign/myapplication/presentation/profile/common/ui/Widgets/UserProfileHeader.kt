package com.hyperdesign.myapplication.presentation.profile.common.ui.Widgets

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.hyperdesign.myapplication.R
import com.hyperdesign.myapplication.presentation.auth.login.mvi.LoginViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun UserProfileHeader(
    onBackPressed: () -> Unit,
    onImageSelected: (String) -> Unit,
    currentImageUri: String?
) {
    Box(modifier = Modifier) {
        // Header background
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.menueheader),
                    contentDescription = "",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Row(
                    modifier = Modifier.padding(top = 20.dp).fillMaxSize(),
                    verticalAlignment = Alignment.Top
                ) {
                    IconButton(
                        onClick = onBackPressed,
                        modifier = Modifier
                            .padding(start = 5.dp, top = 30.dp)
                            .size(30.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            tint = Color.White,
                            contentDescription = "Back",
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Text(
                        text = stringResource(R.string.profile),
                        color = Color.White,
                        modifier = Modifier
                            .padding(top = 35.dp, start = 10.dp)
                            .fillMaxWidth(0.8f),
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // Clickable profile image
        Box(
            modifier = Modifier.padding(top = 90.dp).fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            ClickableProfileImage(
                imageUri = currentImageUri,
                onImageSelected = onImageSelected
            )
        }
    }
}



@Composable
fun ClickableProfileImage(
    imageUri: String?,
    onImageSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.toString()?.let { imageUriString ->
            onImageSelected(imageUriString)
        }
    }

    Box(
        modifier = modifier
            .size(85.dp)
            .clip(CircleShape)
            .clickable { imagePickerLauncher.launch("image/*") },
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(imageUri ?: R.drawable.test_food)
                .crossfade(true)
                .error(R.drawable.test_food)
                .placeholder(R.drawable.test_food)
                .memoryCachePolicy(coil.request.CachePolicy.ENABLED)
                .diskCachePolicy(coil.request.CachePolicy.ENABLED)
                .build(),
            contentDescription = "Profile image",
            modifier = Modifier
                .size(85.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        // Camera icon overlay
        IconButton(
            onClick = { imagePickerLauncher.launch("image/*") },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(8.dp)
                .size(24.dp)
                .background(Color.White.copy(alpha = 0.8f), CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.CameraAlt,
                contentDescription = "Change photo",
                tint = Color.Gray,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

//@Composable
//@Preview(showBackground = true, showSystemUi = true)
//fun PreviewUserProfileHeader(){
//    UserProfileHeader(onBackPressesd = {
//
//    })
//}