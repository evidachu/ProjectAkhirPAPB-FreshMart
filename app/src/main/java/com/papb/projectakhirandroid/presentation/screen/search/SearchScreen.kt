package com.papb.projectakhirandroid.presentation.screen.search

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.papb.projectakhirandroid.R
import com.papb.projectakhirandroid.presentation.common.content.EmptyContent
import com.papb.projectakhirandroid.presentation.common.content.ListContentProduct
import com.papb.projectakhirandroid.presentation.component.SearchViewBar
import com.papb.projectakhirandroid.ui.theme.DIMENS_16dp

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    searchViewModel: SearchViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val searchQuery by searchViewModel.searchQuery
    val productsList by searchViewModel.searchProductList.collectAsState()

    Scaffold { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            SearchViewBar(
                query = searchQuery,
                hint = stringResource(id = R.string.search_store),
                onValueChange = { newValue ->
                    // textfield menampilkan teks baru (membuat bisa diketik)
                    searchViewModel.updateSearchQuery(query = newValue)
                    searchViewModel.searchProduct(query = newValue)
                },
                onClickSearch = { query ->
                    searchViewModel.searchProduct(query = query)
                }
            )

            Spacer(modifier = Modifier.height(DIMENS_16dp))

            if (searchQuery.isNotEmpty()) {
                ListContentProduct(
                    title = "",
                    products = productsList,
                    navController = navController,
                    onClickToCart = {},
                    isVerticalList = true
                )
            }
            else {
                EmptyContent()
            }
        }
    }
}
