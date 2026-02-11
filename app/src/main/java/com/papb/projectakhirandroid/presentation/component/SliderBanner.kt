package com.papb.projectakhirandroid.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.util.lerp // <-- INI BARIS PERBAIKAN UTAMA
import com.google.accompanist.pager.*
import com.papb.projectakhirandroid.ui.theme.DIMENS_114dp
import com.papb.projectakhirandroid.ui.theme.DIMENS_12dp
import com.papb.projectakhirandroid.ui.theme.DIMENS_16dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.yield
import kotlin.math.absoluteValue
import com.papb.projectakhirandroid.R


@ExperimentalPagerApi
@Composable
fun SliderBanner(
    modifier: Modifier = Modifier
) {
    // Implementasi Autoscroll (dibiarkan seperti semula)
    val pagerState = rememberPagerState(initialPage = 0)
    LaunchedEffect(pagerState.currentPage) {
        while (true) {
            yield()
            delay(3000) // Tunda 3 detik
            val nextPage = (pagerState.currentPage + 1) % pagerState.pageCount
            pagerState.animateScrollToPage(nextPage)
        }
    }

    val imageSlider = listOf(
        painterResource(id = R.drawable.img_banner1),
        painterResource(id = R.drawable.img_banner2),
        painterResource(id = R.drawable.img_banner3)
    )

    Column {
        HorizontalPager(
            count = imageSlider.size,
            state = pagerState,
            contentPadding = PaddingValues(horizontal = DIMENS_16dp),
            modifier = modifier
                .height(DIMENS_114dp)
                .fillMaxWidth()
        ) { page ->
            Card(
                shape = RoundedCornerShape(DIMENS_12dp),
                modifier = Modifier
                    .graphicsLayer {
                        // Hitung offset halaman untuk efek zoom
                        val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue

                        // Terapkan efek scaling menggunakan lerp
                        lerp(
                            start = 0.85f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        ).also { scale ->
                            scaleX = scale
                            scaleY = scale
                        }

                        // Terapkan efek alpha menggunakan lerp
                        alpha = lerp(
                            start = 0.5f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        )
                    }
            ) {
                Image(
                    painter = imageSlider[page],
                    contentDescription = stringResource(R.string.image_slider),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(DIMENS_16dp)
        )
    }
}

@ExperimentalPagerApi
@Preview
@Composable
fun SliderBannerPreview() {
    SliderBanner()
}
