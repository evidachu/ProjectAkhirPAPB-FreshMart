package com.papb.projectakhirandroid.presentation.screen.explore

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import com.papb.projectakhirandroid.navigation.screen.Screen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.papb.projectakhirandroid.R
import com.papb.projectakhirandroid.presentation.common.card.CategoryCard
import com.papb.projectakhirandroid.presentation.component.SearchViewBar
import com.papb.projectakhirandroid.ui.theme.*
import com.papb.projectakhirandroid.utils.DataDummy

@Composable
fun ExploreScreen(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController() // Default parameter untuk preview
) {
    Scaffold { paddingValues ->
        Column(
            modifier = modifier.padding(paddingValues)
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = DIMENS_16dp),
                text = stringResource(R.string.find_products),
                fontFamily = GilroyFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = TEXT_SIZE_18sp,
                color = Black
            )

            SearchViewBar(hint = stringResource(id = R.string.search_category))

            LazyVerticalGrid(
                modifier = Modifier.padding(start = DIMENS_16dp, end = DIMENS_16dp),
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(DIMENS_10dp),
                horizontalArrangement = Arrangement.spacedBy(DIMENS_12dp),
            ) {
                items(DataDummy.generateDummyCategories()) { categoryItem ->
                    CategoryCard(
                        categoryItem = categoryItem,
                        onItemClick = {
                            val cleanTitle = categoryItem.title.replace("\n", " ")
                            navController.navigate(Screen.ProductList.passTitle(cleanTitle))
                        }
                    )
                }
            }
        }
    }
}