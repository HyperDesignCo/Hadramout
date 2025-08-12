package com.hyperdesign.myapplication.presentation.profile.ui.Widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun SettingItem(
    title: String,
    description: String? = null,
    icon: Int,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier

            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 16.dp)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
//        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
//        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp)
                    .size(24.dp) // Adjust size as needed
            )

            Text(
                text = title,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 5.dp, end = 8.dp),
                fontSize = 15.sp
            )

            description?.let {
                Text(
                    text = it,
                    modifier = Modifier.padding(end = 8.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }

        }
        Divider(
            modifier = Modifier.padding(top = 15.dp).fillMaxWidth(),
            thickness = 1.dp,
            color = androidx.compose.ui.graphics.Color.LightGray
        )
    }

}


@Composable
@Preview(
    showBackground = true,
    showSystemUi = true
)
fun SettingItemPreview() {
    SettingItem(
        title = "Settings",
        description = "Manage your preferences",
        icon = android.R.drawable.ic_menu_preferences,
        onClick = {}
    )
}