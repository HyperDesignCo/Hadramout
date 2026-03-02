package com.hyperdesign.myapplication.presentation.profile.points.ui.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hyperdesign.myapplication.R
import com.hyperdesign.myapplication.domain.Entity.WalletTransactionEntity
import com.hyperdesign.myapplication.presentation.common.wedgits.MainHeader
import com.hyperdesign.myapplication.presentation.profile.points.ui.viewmodel.PointsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun PointsScreen(
    viewModel: PointsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    val pointsCount = remember(
        state.profileData?.pointValue,
        state.profileData?.user?.balance
    ) {
        state.profileData?.pointValue?.toDoubleOrNull()?.let { pointValue ->
            if (pointValue == 0.0) return@let 0
            state.profileData?.user?.balance?.toDoubleOrNull()?.div(pointValue)?.toInt() ?: 0
        }
    }
    PointsContent(
        isLoading = state.isLoading,
        pointValue = state.profileData?.user?.balance?:"0",
        transactions = state.profileData?.walletTransactions ?: emptyList(),
        pointsCount = pointsCount.toString(),
        onBackPressesd = {
            // handle back press in navigation if needed, or pass from parent
        }
    )
}

@Composable
fun PointsContent(
    isLoading: Boolean,
    pointValue: String,
    pointsCount:String,
    transactions: List<WalletTransactionEntity>,
    onBackPressesd: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        MainHeader(
            showTitle = true,
            height = 90,
            showBackPressedIcon = true,
            title = stringResource(R.string.points),
            onBackPressesd = { onBackPressesd() },
            cardCount = 0
        )

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFFF2A141))
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF6F3F3))
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                Text(
                    text = stringResource(R.string.get_points_every_day),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.points_count),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )

                    Text(
                        text = stringResource(R.string.point, pointsCount),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6B4226)
                    )
                }
                if (pointValue!="0"){
                    Log.e("PointsScreen", "pointValue: $pointValue")
                    Row(
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.points_value),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.Black
                        )

                        Text(
                            text = stringResource(R.string.egy, pointValue),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF6B4226)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (transactions.isNotEmpty()) {
                Text(
                    text = stringResource(R.string.transactions_points),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),

                    )


                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(transactions) { transaction ->
                        TransactionItem(transaction)
                    }
                    item {
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun TransactionItem(transaction: WalletTransactionEntity) {
    val isWon = transaction.status?.lowercase() == "won"
    val alignment = if (isWon) Alignment.End else Alignment.Start

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5EFEF)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "#${transaction.orderId ?: transaction.id}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = transaction.createdAt ?: "",
                        fontSize = 13.sp,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        painter = painterResource(id = android.R.drawable.ic_menu_my_calendar),
                        contentDescription = "Date",
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                }


            }

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                color = Color.White,
                thickness = 1.dp
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = alignment
            ) {
                Text(
                    text = transaction.points ?: "0",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = if (isWon) stringResource(R.string.eraned_pointes) else stringResource(R.string.used_points),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isWon) Color(0xFF4CAF50) else Color(0xFF6B4226)
                )
            }
        }
    }
}
