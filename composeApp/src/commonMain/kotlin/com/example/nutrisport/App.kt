package com.example.nutrisport

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mmk.kmpauth.google.GoogleAuthCredentials
import com.mmk.kmpauth.google.GoogleAuthProvider
import com.nutrisport.data.domain.CustomerRepository
import com.nutrisport.shared.navigation.Screen
import com.nutrisport.navigation.SetupNavGraph
import com.nutrisport.shared.Constants
import org.koin.compose.koinInject

@Composable
@Preview
fun App() {
    val customerRepository = koinInject<CustomerRepository>()
    var appReady by remember { mutableStateOf(false) }
    val isUserAuthenticated = remember { customerRepository.getUserId() != null }
    val startDestination = remember {
        if(isUserAuthenticated){
            Screen.HomeGraph
        }else{
            Screen.Auth
        }
    }
    MaterialTheme {
        LaunchedEffect(Unit){
            GoogleAuthProvider.create(
                credentials = GoogleAuthCredentials(serverId = Constants.WEB_CLIENT_ID)
            )
            appReady = true
        }
        AnimatedVisibility(
            modifier = Modifier.fillMaxSize(),
            visible = appReady
        ){
            SetupNavGraph(
                startDestination = startDestination
            )
        }
    }
}