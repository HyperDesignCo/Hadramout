package com.hyperdesign.myapplication.presentation.menu.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import com.hyperdesign.myapplication.R
import com.hyperdesign.myapplication.presentation.home.mvi.HomeViewModel
import com.hyperdesign.myapplication.presentation.main.navcontroller.LocalNavController
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IslamicDateTimePicker(homeViewModel: HomeViewModel = koinViewModel()) {

    val navController = LocalNavController.current

    // Get Egypt timezone
    val egyptTimeZone = TimeZone.getTimeZone("Africa/Cairo")
    val calendar = Calendar.getInstance(egyptTimeZone)
    val today = calendar.get(Calendar.DAY_OF_MONTH)
    val currentMonth = calendar.get(Calendar.MONTH)
    val currentYear = calendar.get(Calendar.YEAR)
    val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
    val currentMinute = calendar.get(Calendar.MINUTE)

    var selectedYear by remember { mutableStateOf(currentYear) }
    var selectedMonth by remember { mutableStateOf(currentMonth) }
    var selectedDate by remember { mutableStateOf<Int?>(null) }
    var selectedHour by remember { mutableStateOf(currentHour) }
    var selectedMinute by remember { mutableStateOf(currentMinute) }
    var showTimePicker by remember { mutableStateOf(false) }

    val openTime = homeViewModel.tokenManager.getOpenTimeBranch()
    val closeTime = homeViewModel.tokenManager.getCloseTimeBranch()

    val scrollState = rememberScrollState()

    val daysOfWeek = listOf("الأحد", "الإثنين", "الثلاثاء", "الأربعاء", "الخميس", "الجمعة", "السبت")
    val monthsArabic = listOf(
        "يناير", "فبراير", "مارس", "أبريل", "مايو", "يونيو",
        "يوليو", "أغسطس", "سبتمبر", "أكتوبر", "نوفمبر", "ديسمبر"
    )

    // Parse branch hours
    val (openHour, openMinute) = parseTime(openTime ?: "00:00")
    val (closeHour, closeMinute) = parseTime(closeTime ?: "23:59")

    Log.d("HomeScreen", "open branch=$openTime -> $openHour:$openMinute")
    Log.d("HomeScreen", "close branch=$closeTime -> $closeHour:$closeMinute")

    // Get days in selected month
    val daysInMonth = getDaysInMonth(selectedYear, selectedMonth)
    val firstDayOfWeek = getFirstDayOfWeek(selectedYear, selectedMonth)

    // Check if date is in the past
    fun isDateInPast(day: Int): Boolean {
        if (selectedYear < currentYear) return true
        if (selectedYear > currentYear) return false
        if (selectedMonth < currentMonth) return true
        if (selectedMonth > currentMonth) return false
        return day < today
    }

    // Check if date is today
    fun isToday(day: Int): Boolean {
        return selectedYear == currentYear &&
                selectedMonth == currentMonth &&
                day == today
    }

    // Check if selected time is valid
    fun isTimeValid(hour: Int, minute: Int): Boolean {
        val selectedTimeMinutes = hour * 60 + minute
        val openTimeMinutes = openHour * 60 + openMinute
        val closeTimeMinutes = closeHour * 60 + closeMinute

        // If selecting today, also check against current time
        if (selectedDate == today && selectedMonth == currentMonth && selectedYear == currentYear) {
            val currentTimeMinutes = currentHour * 60 + currentMinute
            return selectedTimeMinutes >= maxOf(openTimeMinutes, currentTimeMinutes) &&
                    selectedTimeMinutes <= closeTimeMinutes
        }

        return selectedTimeMinutes >= openTimeMinutes && selectedTimeMinutes <= closeTimeMinutes
    }

    // Get minimum allowed hour for today
    fun getMinimumAllowedHour(): Int {
        if (selectedDate == today && selectedMonth == currentMonth && selectedYear == currentYear) {
            return maxOf(openHour, currentHour)
        }
        return openHour
    }

    // Get minimum allowed minute for selected hour
    fun getMinimumAllowedMinute(hour: Int): Int {
        if (selectedDate == today && selectedMonth == currentMonth && selectedYear == currentYear) {
            if (hour == currentHour) {
                return maxOf(openMinute, currentMinute)
            }
            if (hour == openHour) {
                return openMinute
            }
        } else if (hour == openHour) {
            return openMinute
        }
        return 0
    }

    // Get maximum allowed minute for selected hour
    fun getMaximumAllowedMinute(hour: Int): Int {
        if (hour == closeHour) {
            return closeMinute
        }
        return 59
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .verticalScroll(scrollState)
    ) {
        // Header with wooden background
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.menueheader),
                contentDescription = "",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(vertical = 24.dp)
            ) {
                Text(
                    text = "اختر الوقت و التاريخ",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                if (!openTime.isNullOrEmpty()) {
                    Text(
                        text = "الرجاء العلم الفرع يفتح الساعة $openTime",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }

                if (!closeTime.isNullOrEmpty()) {
                    Text(
                        text = "و يغلق في الساعة $closeTime",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Calendar Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            // Month/Year Navigation
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    if (selectedMonth == 0) {
                        selectedMonth = 11
                        selectedYear--
                    } else {
                        selectedMonth--
                    }
                    selectedDate = null
                }) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "Previous Month",
                        tint = Color(0xFF4A3428)
                    )
                }

                Text(
                    text = "${monthsArabic[selectedMonth]} $selectedYear",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                IconButton(onClick = {
                    if (selectedMonth == 11) {
                        selectedMonth = 0
                        selectedYear++
                    } else {
                        selectedMonth++
                    }
                    selectedDate = null
                }) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowLeft,
                        contentDescription = "Next Month",
                        tint = Color(0xFF4A3428)
                    )
                }
            }

            // Days of week
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                daysOfWeek.forEach { day ->
                    Text(
                        text = day,
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Calendar Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                modifier = Modifier.height((daysInMonth + firstDayOfWeek) / 7 * 50.dp + 50.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                userScrollEnabled = false
            ) {
                // Empty cells for days before first day of month
                items(firstDayOfWeek) {
                    Box(modifier = Modifier.size(45.dp))
                }

                // Actual days
                items((1..daysInMonth).toList()) { day ->
                    val isPast = isDateInPast(day)
                    val isTodayDate = isToday(day)
                    val isSelected = day == selectedDate

                    DayItem(
                        day = day,
                        isSelected = isSelected,
                        isToday = isTodayDate,
                        isPast = isPast,
                        onClick = {
                            if (!isPast) {
                                selectedDate = day
                                // Reset time to valid range when date changes
                                selectedHour = getMinimumAllowedHour()
                                selectedMinute = getMinimumAllowedMinute(selectedHour)
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Time Picker Section
            if (selectedDate != null) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "اختر الوقت",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Time Display/Picker
                    OutlinedButton(
                        onClick = { showTimePicker = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxWidth()
                            .height(60.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = String.format(
                                "%02d:%02d %s",
                                if (selectedHour > 12) selectedHour - 12 else if (selectedHour == 0) 12 else selectedHour,
                                selectedMinute,
                                if (selectedHour >= 12) "PM" else "AM"
                            ),
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF6B9BD1)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Time Picker Controls
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // Hour Picker
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("الساعة", fontSize = 14.sp, color = Color.Gray)
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(
                                    onClick = {
                                        val minHour = getMinimumAllowedHour()
                                        if (selectedHour > minHour) {
                                            selectedHour--
                                            // Adjust minute if needed
                                            val minMinute = getMinimumAllowedMinute(selectedHour)
                                            val maxMinute = getMaximumAllowedMinute(selectedHour)
                                            if (selectedMinute < minMinute) {
                                                selectedMinute = minMinute
                                            } else if (selectedMinute > maxMinute) {
                                                selectedMinute = maxMinute
                                            }
                                        }
                                    },
                                    enabled = selectedHour > getMinimumAllowedHour()
                                ) {
                                    Text(
                                        "-",
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (selectedHour > getMinimumAllowedHour()) Color.Black else Color.Gray
                                    )
                                }
                                Text(
                                    text = String.format("%02d", selectedHour),
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                                IconButton(
                                    onClick = {
                                        if (selectedHour < closeHour) {
                                            selectedHour++
                                            // Adjust minute if needed
                                            val minMinute = getMinimumAllowedMinute(selectedHour)
                                            val maxMinute = getMaximumAllowedMinute(selectedHour)
                                            if (selectedMinute < minMinute) {
                                                selectedMinute = minMinute
                                            } else if (selectedMinute > maxMinute) {
                                                selectedMinute = maxMinute
                                            }
                                        }
                                    },
                                    enabled = selectedHour < closeHour
                                ) {
                                    Text(
                                        "+",
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (selectedHour < closeHour) Color.Black else Color.Gray
                                    )
                                }
                            }
                        }

                        // Minute Picker
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("الدقيقة", fontSize = 14.sp, color = Color.Gray)
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(
                                    onClick = {
                                        val minMinute = getMinimumAllowedMinute(selectedHour)
                                        if (selectedMinute > minMinute) {
                                            selectedMinute--
                                        }
                                    },
                                    enabled = selectedMinute > getMinimumAllowedMinute(selectedHour)
                                ) {
                                    Text(
                                        "-",
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (selectedMinute > getMinimumAllowedMinute(selectedHour)) Color.Black else Color.Gray
                                    )
                                }
                                Text(
                                    text = String.format("%02d", selectedMinute),
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                                IconButton(
                                    onClick = {
                                        val maxMinute = getMaximumAllowedMinute(selectedHour)
                                        if (selectedMinute < maxMinute) {
                                            selectedMinute++
                                        }
                                    },
                                    enabled = selectedMinute < getMaximumAllowedMinute(selectedHour)
                                ) {
                                    Text(
                                        "+",
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (selectedMinute < getMaximumAllowedMinute(selectedHour)) Color.Black else Color.Gray
                                    )
                                }
                            }
                        }
                    }

                    // Show validation message
                    if (!isTimeValid(selectedHour, selectedMinute)) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "الوقت المختار خارج ساعات العمل",
                            fontSize = 12.sp,
                            color = Color.Red
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Confirm Button
                Button(
                    onClick = {
                        // Format the selected date and time
                        val selectedDateTime = String.format(
                            "%02d-%02d-%d %02d:%02d",
                            selectedDate,
                            selectedMonth + 1,
                            selectedYear,
                            selectedHour,
                            selectedMinute
                        )

                        // Format for display (Arabic format)
                        val displayDateTime = String.format(
                            "%s %02d %s %d - %02d:%02d %s",
                            daysOfWeek[Calendar.getInstance().apply {
                                set(selectedYear, selectedMonth, selectedDate!!)
                            }.get(Calendar.DAY_OF_WEEK) - 1],
                            selectedDate,
                            monthsArabic[selectedMonth],
                            selectedYear,
                            if (selectedHour > 12) selectedHour - 12 else if (selectedHour == 0) 12 else selectedHour,
                            selectedMinute,
                            if (selectedHour >= 12) "م" else "ص"
                        )

                        Log.d("DateTimePicker", "Selected: $selectedDateTime")
                        Log.d("DateTimePicker", "Display: $displayDateTime")

                        // Pass the selected date/time back to CheckOutScreen
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("selected_delivery_time", displayDateTime)

                        navController.popBackStack()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4A3428)
                    ),
                    shape = RoundedCornerShape(28.dp),
                    enabled = selectedDate != null && isTimeValid(selectedHour, selectedMinute)
                ) {
                    Text(
                        text = "اختر الوقت و التاريخ",
                        fontSize = 18.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                // Placeholder when no date selected
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "الرجاء اختيار التاريخ",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun DayItem(
    day: Int,
    isSelected: Boolean,
    isToday: Boolean,
    isPast: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(45.dp)
            .clip(CircleShape)
            .background(
                when {
                    isSelected -> Color(0xFF4A3428)
                    isPast -> Color(0xFFE0E0E0)
                    else -> Color.Transparent
                }
            )
            .then(
                if (isToday && !isSelected) {
                    Modifier.border(2.dp, Color(0xFF4A3428), CircleShape)
                } else {
                    Modifier
                }
            )
            .clickable(enabled = !isPast, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.toString(),
            color = when {
                isSelected -> Color.White
                isPast -> Color.Gray
                else -> Color.Black
            },
            fontSize = 16.sp,
            fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal
        )
    }
}

fun getDaysInMonth(year: Int, month: Int): Int {
    val calendar = Calendar.getInstance()
    calendar.set(year, month, 1)
    return calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
}

fun getFirstDayOfWeek(year: Int, month: Int): Int {
    val calendar = Calendar.getInstance()
    calendar.set(year, month, 1)
    return calendar.get(Calendar.DAY_OF_WEEK) - 1
}

fun parseTime(time: String): Pair<Int, Int> {
    return try {
        // Handle different time formats: "HH:mm:ss" or "HH:mm"
        val parts = time.split(":")
        val hour = parts[0].toIntOrNull() ?: 0
        val minute = parts.getOrNull(1)?.toIntOrNull() ?: 0
        Pair(hour, minute)
    } catch (e: Exception) {
        Pair(0, 0)
    }
}