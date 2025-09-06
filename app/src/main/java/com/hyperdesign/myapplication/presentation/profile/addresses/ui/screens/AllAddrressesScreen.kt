package com.hyperdesign.myapplication.presentation.profile.addresses.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hyperdesign.myapplication.R
import com.hyperdesign.myapplication.domain.Entity.AddressEntity
import com.hyperdesign.myapplication.presentation.common.wedgits.CustomButton
import com.hyperdesign.myapplication.presentation.common.wedgits.MainHeader
import com.hyperdesign.myapplication.presentation.main.navcontroller.LocalNavController
import com.hyperdesign.myapplication.presentation.main.theme.ui.Secondry
import com.hyperdesign.myapplication.presentation.profile.addresses.mvi.AddressesViewModel
import com.hyperdesign.myapplication.presentation.profile.addresses.ui.widgets.AddressItem
import org.koin.androidx.compose.koinViewModel

@Composable
fun AllAddressesScreen(addressViewModel: AddressesViewModel = koinViewModel()) {
    val navController = LocalNavController.current
    val addressState by addressViewModel.addressState.collectAsStateWithLifecycle()

    var addresses by remember { mutableStateOf<List<AddressEntity>>(emptyList()) }

    LaunchedEffect(addressState) {
        addresses = addressState.addresses?.addresses.orEmpty()
    }
    Box(modifier = Modifier.fillMaxSize()) {
        AllAddressesScreenContent(
            onBackPressed = { navController.popBackStack() },
            addresses = addresses
        )
        if (addressState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Secondry)
            }
        }
    }
}

@Composable
fun AllAddressesScreenContent(onBackPressed: () -> Unit, addresses: List<AddressEntity>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        MainHeader(
            onBackPressesd = { onBackPressed() },
            showTitle = true,
            title = stringResource(R.string.Addresses),
            showBackPressedIcon = true,
            height = 90
        )

        // LazyColumn for addresses, taking available space
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .weight(1f) // Takes remaining space above the button
                .fillMaxWidth()
        ) {
            item {
                Spacer(modifier = Modifier.padding(vertical = 10.dp))
            }

            items(addresses, key = { address -> address.id }) { address ->
                AddressItem(address = address, onDeleteAddress = { /* Handle delete */ })
                Spacer(modifier = Modifier.height(10.dp))
                Divider(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.Gray,
                    thickness = 1.dp
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }

        // Fixed button at the bottom
        CustomButton(
            text = stringResource(R.string.add_new_address),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 10.dp),
            onClick = { /* Handle add new address */ }
        )
    }
}