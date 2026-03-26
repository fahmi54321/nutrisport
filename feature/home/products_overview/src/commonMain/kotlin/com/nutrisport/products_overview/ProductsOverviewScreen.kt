package com.nutrisport.products_overview

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nutrisport.products_overview.component.MainProductCard
import com.nutrisport.shared.Alpha
import com.nutrisport.shared.FontSize
import com.nutrisport.shared.Resources
import com.nutrisport.shared.TextPrimary
import com.nutrisport.shared.component.InfoCard
import com.nutrisport.shared.component.LoadingCard
import com.nutrisport.shared.component.ProductCard
import com.nutrisport.shared.util.DisplayResult
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProductsOverviewScreen(
    navigateToDetails: (String) -> Unit,
) {
    val viewModel = koinViewModel<ProductsOverviewViewModel>()
    val products by viewModel.products.collectAsState()

    val horizontalListState = rememberLazyListState()
    val verticalListState = rememberLazyListState()

    val centeredIndex: Int? by remember {
        derivedStateOf {
            val layoutInfo = horizontalListState.layoutInfo
            val viewportCenter =
                layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset / 2

            layoutInfo.visibleItemsInfo.minByOrNull { item ->
                val itemCenter = item.offset + item.size / 2
                kotlin.math.abs(itemCenter - viewportCenter)
            }?.index
        }
    }

    val scrollOffset by remember {
        derivedStateOf {
            if (verticalListState.firstVisibleItemIndex > 0) {
                1f
            } else {
                (verticalListState.firstVisibleItemScrollOffset / 600f)
                    .coerceIn(0f, 1f)
            }
        }
    }

    val scale by animateFloatAsState(
        targetValue = 1f - (0.2f * scrollOffset),
        label = "scale"
    )
    val alpha by animateFloatAsState(
        targetValue = 1f - (0.5f * scrollOffset),
        label = "alpha"
    )

    products.DisplayResult(
        onLoading = {
            LoadingCard(
                modifier = Modifier.fillMaxSize()
            )
        },
        onSuccess = { productList ->
            AnimatedContent(
                targetState = productList.distinctBy { it.id }
            ) { products ->
                if (products.isNotEmpty()) {
                    LazyColumn(
                        state = verticalListState,
                        modifier = Modifier.fillMaxSize(),
                    ) {

                        item {
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        item {
                            LazyRow(
                                state = horizontalListState,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .graphicsLayer {
                                        scaleX = scale
                                        scaleY = scale
                                        this.alpha = alpha
                                    },
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                            ) {
                                itemsIndexed(
                                    items = products
                                        .filter { it.isNew }
                                        .sortedBy { it.createdAt }
                                        .take(6),
                                    key = { _, item -> item.id }
                                ) { index, product ->

                                    val isLarge = index == centeredIndex

                                    val animatedScale by animateFloatAsState(
                                        targetValue = if (isLarge) 1f else 0.8f,
                                        animationSpec = tween(300),
                                        label = "itemScale"
                                    )

                                    MainProductCard(
                                        modifier = Modifier
                                            .scale(animatedScale)
                                            .height(300.dp)
                                            .fillParentMaxWidth(0.6f),
                                        isLarge = isLarge,
                                        product = product,
                                        onClick = { id ->
                                            navigateToDetails(id)
                                        }
                                    )
                                }
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(24.dp))
                        }

                        item {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .alpha(Alpha.HALF),
                                text = "Discounted Products",
                                fontSize = FontSize.EXTRA_REGULAR,
                                color = TextPrimary,
                                textAlign = TextAlign.Center,
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        items(
                            items = products
                                .filter { it.isDiscounted }
                                .sortedBy { it.createdAt }
                                .take(10),
                            key = { it.id }
                        ) { product ->
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 12.dp)
                                    .padding(bottom = 12.dp)
                            ){
                                ProductCard(
                                    product = product,
                                    onClick = { id ->
                                        navigateToDetails(id)
                                    }
                                )
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }

                } else {
                    InfoCard(
                        image = Resources.Image.Cat,
                        title = "Nothing here",
                        subtitle = "Empty product list.",
                    )
                }
            }

        },
        onError = { message ->
            InfoCard(
                image = Resources.Image.Cat,
                title = "Oops!",
                subtitle = message,
            )
        }
    )
}