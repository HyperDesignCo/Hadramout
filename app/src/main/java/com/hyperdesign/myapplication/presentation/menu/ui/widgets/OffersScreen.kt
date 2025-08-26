package com.hyperdesign.myapplication.presentation.menu.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.common.Feature
import com.hyperdesign.myapplication.R
import com.hyperdesign.myapplication.presentation.common.wedgits.MainHeader
import com.hyperdesign.myapplication.presentation.home.ui.wedgit.FeaturedWedgits
import com.hyperdesign.myapplication.presentation.home.ui.wedgit.OffersList
import com.hyperdesign.myapplication.presentation.home.ui.wedgit.offers
import com.hyperdesign.myapplication.presentation.main.theme.ui.Gray
import com.hyperdesign.myapplication.presentation.main.theme.ui.Primary
import com.hyperdesign.myapplication.presentation.main.theme.ui.Secondry

@Composable
fun OffersScreen() {

    OffersScreenContent(offers = listOf(
//        offers(R.drawable.test_food, 1),
//        offers(R.drawable.test_food, 2),
//        offers(R.drawable.test_food, 3),
//        offers(R.drawable.test_food, 4),
//        offers(R.drawable.test_food, 5)
    ),
        fetured = listOf(
//            Featured(R.drawable.featured, 1, "Charcoal Grills", "Lorem ipsum dolor sitamet, Lorem ipsum dolor sitamet,", "$10.00"),
//            Featured(R.drawable.featured, 2, "Charcoal Grills", "Lorem ipsum dolor sitamet, Lorem ipsum dolor sitamet", "$12.00"),
//            Featured(R.drawable.featured, 3, "Charcoal Grills", "Lorem ipsum dolor sitamet, Lorem ipsum dolor sitamet", "$15.00")

        )
    )

}



@Composable
fun OffersScreenContent(offers:List<offers>,fetured: List<Feature>) {



        LazyColumn(
            modifier = Modifier.fillMaxSize()
                .background(Color.White)
        ) {
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 10.dp)
                ) {
                    Spacer(modifier = Modifier.height(15.dp))

                    Text(
                        stringResource(R.string.offers),
                        color = Secondry,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            item {
//                OffersList(offers = offers)
            }

            item {
                Column(
                    modifier = Modifier.padding(horizontal = 10.dp)
                ) {
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        stringResource(R.string.featured),
                        color = Secondry,
                        fontSize = 17.sp,
                        modifier = Modifier.padding(top = 10.dp),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

//            items(items = fetured, key = { featured -> featured.id }) { featured ->
//                FeaturedWedgits(
////                    featured = featured,
////                    onItemClick = { /* Handle click */ }
////                )
//                Spacer(modifier = Modifier.height(10.dp))
//            }
        }



}