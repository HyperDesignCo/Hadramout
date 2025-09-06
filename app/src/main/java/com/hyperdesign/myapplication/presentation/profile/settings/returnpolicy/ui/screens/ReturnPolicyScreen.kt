package com.hyperdesign.myapplication.presentation.profile.settings.returnpolicy.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hyperdesign.myapplication.R
import com.hyperdesign.myapplication.domain.Entity.PageEntity
import com.hyperdesign.myapplication.presentation.common.wedgits.MainHeader
import com.hyperdesign.myapplication.presentation.main.navcontroller.LocalNavController
import com.hyperdesign.myapplication.presentation.main.navcontroller.Screen
import com.hyperdesign.myapplication.presentation.profile.settings.common.mvi.StaticPagesIntents
import com.hyperdesign.myapplication.presentation.profile.settings.common.mvi.StaticPagesViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ReturnPolicyScreen(showStaticPagesViewModel: StaticPagesViewModel= koinViewModel()){

    val pageState by showStaticPagesViewModel.staticPageState.collectAsStateWithLifecycle()
    var page by remember { mutableStateOf<PageEntity?>(null) }
    LaunchedEffect(Unit) {
        showStaticPagesViewModel.handleIntents(StaticPagesIntents.DisplayPage(3))
    }
    LaunchedEffect(pageState) {
        page = pageState.pagesResponse?.pages
    }
    val navController= LocalNavController.current
    page?.let { ReturnPolicyScreenContent(page = it,onBackPressed = {navController.popBackStack()}) }
}


@Composable
fun ReturnPolicyScreenContent(page: PageEntity, onBackPressed:()->Unit){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        MainHeader(
            onBackPressesd = { onBackPressed() },
            showTitle = true,
            title = stringResource(R.string.return_policy),
            showBackPressedIcon = true,
            height = 90
        )

        LazyColumn(
            modifier = Modifier.padding(horizontal = 10.dp)
        ) {
            item {
                Spacer(modifier = Modifier.padding(vertical = 10.dp))
            }
            item {
                Text(AnnotatedString.fromHtml(page.text))
            }
        }
    }


}

