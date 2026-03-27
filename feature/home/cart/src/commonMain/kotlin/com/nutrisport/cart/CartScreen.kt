package com.nutrisport.cart

import ContentWithMessageBar
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nutrisport.cart.component.CartItemCard
import com.nutrisport.shared.Resources
import com.nutrisport.shared.Surface
import com.nutrisport.shared.component.InfoCard
import com.nutrisport.shared.component.LoadingCard
import com.nutrisport.shared.util.DisplayResult
import com.nutrisport.shared.util.RequestState
import org.koin.compose.viewmodel.koinViewModel
import rememberMessageBarState

@Composable
fun CartScreen() {
    val viewModel = koinViewModel<CartViewModel>()
    val cartItemsWithProducts by viewModel.cartItemsWithProducts.collectAsState(RequestState.Loading)
    val messageBarState = rememberMessageBarState()

    ContentWithMessageBar(
        contentBackgroundColor = Surface,
        messageBarState = messageBarState,
        errorMaxLines = 2,
    ) {
        cartItemsWithProducts.DisplayResult(
            onError = { message ->
                InfoCard(
                    image = Resources.Image.Cat,
                    title = "Oops!",
                    subtitle = message,
                )
            },
            onLoading = {
                LoadingCard(
                    modifier = Modifier.fillMaxSize()
                )
            },
            onSuccess = { dataPair ->
                AnimatedContent(
                    targetState = dataPair
                ) { data ->
                    if (data.isNotEmpty()) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(
                                items = data,
                                key = { pair -> "${pair.first.id}_${pair.second.id}" }
                            ) { pair ->
                                CartItemCard(
                                    product = pair.second,
                                    cartItem = pair.first,
                                    onPlusClick = { value ->
                                        viewModel.updateCartItemQuantity(
                                            id = pair.first.id,
                                            quantity = value,
                                            onSuccess = {},
                                            onError = { message ->
                                                messageBarState.addError(message)
                                            },
                                        )
                                    },
                                    onMinusClick = { value ->
                                        viewModel.updateCartItemQuantity(
                                            id = pair.first.id,
                                            quantity = value,
                                            onSuccess = {},
                                            onError = { message ->
                                                messageBarState.addError(message)
                                            },
                                        )
                                    },
                                    onDeleteClick = {
                                        viewModel.deleteCartItem(
                                            id = pair.first.id,
                                            onSuccess = {},
                                            onError = { message ->
                                                messageBarState.addError(message)
                                            },
                                        )
                                    },
                                )
                            }
                        }
                    } else {
                        InfoCard(
                            image = Resources.Image.ShoppingCart,
                            title = "Empty Cart",
                            subtitle = "Check some of our products",
                        )
                    }
                }
            },
            transitionSpec = fadeIn() togetherWith fadeOut()
        )
    }
}