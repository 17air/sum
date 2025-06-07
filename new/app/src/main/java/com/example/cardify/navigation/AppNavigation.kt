package com.example.cardify.navigation

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cardify.auth.TokenManager
import com.example.cardify.models.CardCreationViewModel
import com.example.cardify.models.LoginViewModel
import com.example.cardify.models.MainScreenViewModel
import com.example.cardify.ui.screens.LoginScreen
import com.example.cardify.ui.screens.MainEmptyScreen
import com.example.cardify.ui.screens.MainExistScreen
import com.example.cardify.ui.screens.RegisterCompleteScreen
import com.example.cardify.ui.screens.RegisterScreen
import com.example.cardify.ui.screens.SplashScreen
import com.example.cardify.ui.screens.CardListScreen

sealed class Screen(val route: String) {
    object AddAutoClassify : Screen("add_auto_classify/{imageUri}") {
        fun createRoute(imageUri: String) = "add_auto_classify/$imageUri"
    }
    object AddClassified : Screen("add_classified_screen")
    //object AddConfirm : Screen("add_confirm_screen")
    object AddExisting : Screen("add_existing_screen")
    object AddFromCamera : Screen("add_from_camera")
    object AddFromFile : Screen("add_from_file")
    object AddFromGallery : Screen("add_from_gallery")
    object AddImageSelect : Screen("add_image_select_screen/{imageUri}") {
        fun createRoute(imageUri: String) = "add_image_select_screen/$imageUri"
    }
    object CreateConfirm : Screen("create_confirm")
    object CreateDesign : Screen("create_design")
    object CreateEssentials : Screen("create_essentials")
    object CreateProgress : Screen("create_progress")
    object CreateQuestion : Screen("create_question")
    object Login : Screen("login")
    object Main : Screen("main_empty_or_exist")
    //QuestionScreen 처리해야해
    object RegisterComplete : Screen("register_complete")
    object Register : Screen("register")
    object Splash : Screen("splash")
    object CardList : Screen("card_list_screen")
    object Settings : Screen("settings_screen") //미구현
    object CardDetail : Screen("card_detail/{cardId}") { //미구현
       fun createRoute(cardId: String) = "card_detail/$cardId" //미구현
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current

    val cardCreationViewModel: CardCreationViewModel = viewModel()
    val uiState by cardCreationViewModel.uiState.collectAsState()
    val currentQuestion by cardCreationViewModel.currentQuestion.collectAsState()
    val loginViewModel: LoginViewModel = viewModel()
    val mainScreenViewModel: MainScreenViewModel = viewModel()

    //Navhost maps object(e.g.Splash) to Screen.kt
    //Start Destination fixed to Splash, which indicates SplashScreen.
    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        //when Screen.Login.route called, this function executed.
        //LoginScreen.kt
        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                onNavigateToMain = { navController.navigate(Screen.Main.route) },
                loginViewModel = loginViewModel
            )
        }

        //RegisterScreen.kt
        composable(Screen.Register.route) {
            RegisterScreen(
                //회원가입 버튼
                onNavigateToRegisterComplete = {
                    navController.navigate(Screen.RegisterComplete.route)
                },
                //로그인 화면으로 버튼
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route)
                }
            )
        }

        //RegisterCompleteScreen.kt
        composable(Screen.RegisterComplete.route) {
            RegisterCompleteScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Main.route) {
            val tokenManager = remember { TokenManager(context) }
            val token = tokenManager.getToken()
            val mainViewModel: MainScreenViewModel = viewModel()
            val hasCards by mainViewModel.hasCards.collectAsStateWithLifecycle()
            val cards by mainViewModel.cards.collectAsStateWithLifecycle()
            val error by mainViewModel.error.collectAsStateWithLifecycle()

            // Fetch cards when screen is first shown
            LaunchedEffect(Unit) {
                token?.let { mainViewModel.fetchCards(it) }
            }

            // null - checking cards
            if (hasCards == null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("로딩중...")
                }
            }
            // error - show error
            else if (!error.isNullOrEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Error: $error")
                        Button(onClick = { token?.let { mainViewModel.fetchCards(it) } }) {
                            Text("Retry")
                        }
                    }
                }
            }

            // Show main content based on hasCards state
            else {
                if (hasCards == true) {
                    // MainExistScreen
                    MainExistScreen(
                        cardList = cards,
                        onCardClick = { card ->
                            navController.navigate(Screen.CardDetail.createRoute(card.cardId))
                        },
                        onAddExistingCard = { navController.navigate(Screen.AddExisting.route) },
                        onCreateNewCard = {
                            cardCreationViewModel.resetCreation()
                            navController.navigate(Screen.CreateEssentials.route)
                        },
                        onNavigateToCardList = { navController.navigate(Screen.CardList.route) },
                        onNavigateToSettings = { navController.navigate(Screen.Settings.route) }
                    )
                } else {
                    // MainEmptyScreen
                    MainEmptyScreen(
                        onCreateNewCard = { navController.navigate(Screen.CreateEssentials.route) },
                        onAddExistingCard = { navController.navigate(Screen.AddExisting.route) },
                        onNavigateToCardList = { navController.navigate(Screen.CardList.route) },
                        onNavigateToSettings = { navController.navigate(Screen.Settings.route) }
                    )
                }
            }
        }
    }
}
/*

            // Show error message if there's an error
            if (error != null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Error: $error")
                }
            } else {
                // Show empty state or cards list based on the data
                if (cards.isEmpty()) {
                    MainEmptyScreen(
                        onCreateNewCard = {
                            cardCreationViewModel.resetCreation()
                            navController.navigate(Screen.CreateEssentials.route)
                        },
                        onAddExistingCard = { navController.navigate(Screen.AddExisting.route) },
                        onNavigateToCardList = { navController.navigate(Screen.CardList.route) },
                        onNavigateToSettings = { navController.navigate(Screen.Settings.route) }
                    )
                } else {
                    MainExistScreen(
                        cardList = cards,
                        onCardClick = { card ->
                            navController.navigate(Screen.CardDetail.createRoute(card.cardId))
                        },
                        onAddExistingCard = { navController.navigate(Screen.AddExisting.route) },
                        onCreateNewCard = {
                            cardCreationViewModel.resetCreation()
                            navController.navigate(Screen.CreateEssentials.route)
                        },
                        onNavigateToCardList = { navController.navigate(Screen.CardList.route) },
                        onNavigateToSettings = { navController.navigate(Screen.Settings.route) }
                    )
                }
            }
        }


        composable(Screen.CreateEssentials.route) {
            val tokenManager = TokenManager(LocalContext.current)
            val token = tokenManager.getToken() ?: ""
            
            CreateEssentialsScreen(
                cardInfo = uiState.card,
                onCardInfoChange = { cardCreationViewModel.updateCardInfo(it) },
                onNextClick = { navController.navigate(Screen.CreateQuestion.route) },
                onBackClick = { navController.popBackStack() },
                viewModel = cardCreationViewModel,
                token = token
            )
        }

        composable(Screen.CreateQuestion.route) {
            CreateQuestionScreen(
                questionNumber = currentQuestion,
                onAnswerSelected = { answer ->
                    cardCreationViewModel.recordAnswer(currentQuestion, answer)
                    if (currentQuestion >= 5) {
                        navController.navigate(Screen.CreateProgress.route)
                    }
                },
                onCancelClick = {
                    cardCreationViewModel.resetCreation()
                    navController.popBackStack(Screen.CreateEssentials.route, inclusive = true)
                }
            )
        }

        composable(Screen.CreateProgress.route) {
            val tokenManager = TokenManager(LocalContext.current)
            val token = tokenManager.getToken() ?: ""
            
            CreateProgressScreen(
                cardInfo = uiState.card,
                userAnswers = cardCreationViewModel.answers.value as List<String>,
                viewModel = cardCreationViewModel,
                onProgressComplete = {
                    navController.navigate(Screen.CreateDesign.route)
                },
                onCancelClick = {
                    cardCreationViewModel.resetCreation()
                    navController.popBackStack(Screen.CreateEssentials.route, inclusive = true)
                },
                token = token
            )
        }

        composable(Screen.AddFromCamera.route) {
            val tokenManager = TokenManager(LocalContext.current)
            val token = tokenManager.getToken() ?: ""
            
            AddFromCameraScreen(
                navController = navController,
                onImageCaptured = { bitmap ->
                    cardCreationViewModel.analyzeCardImage(bitmap, token)
                    navController.navigate(Screen.AddAutoClassify.route)
                }
            )
        }

        composable(
            route = Screen.AddImageSelect.route,
            arguments = listOf(navArgument("imageUri") { type = NavType.StringType })
        ) { backStackEntry ->
            val imageUri = backStackEntry.arguments?.getString("imageUri")
            AddImageSelectScreen(
                navController = navController,
                imageUri = imageUri
            )
        }

        composable(
            route = Screen.AddAutoClassify.route,
            arguments = listOf(navArgument("imageUri") { type = NavType.StringType })
        ) { backStackEntry ->
            val imageUri = backStackEntry.arguments?.getString("imageUri") ?: ""
            val bitmap = loadBitmapFromUri(context, imageUri)
            bitmap?.let { capturedBitmap ->
                AddAutoClassifyScreen(
                    navController = navController,
                    viewModel = cardCreationViewModel,
                    capturedImage = capturedBitmap
                )
            } ?: run {
                // Handle case where bitmap couldn't be loaded
                LaunchedEffect(Unit) {
                    navController.popBackStack()
                }
            }
        }

        composable(Screen.AddClassified.route) {
            AddClassifiedScreen(
                navController = navController,
                viewModel = cardCreationViewModel
            )
        }

        composable(Screen.CardList.route) {
            CardListScreen(
                cards = emptyList(),
                onNavigateToMain = { navController.navigate(Screen.Main.route) },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) },
                onUpdateCard = { }
            )
        }

        composable(Screen.AddConfirm.route) {
            // Placeholder for AddConfirmScreen
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Add Confirm Screen")
            }
        }

        composable(Screen.CreateDesign.route) {
            CreateDesignScreen(
                isFirst = true,
                generatedCardImages = uiState.cardImages,
                onCardSelected = { cardId, base64Image ->
                    cardCreationViewModel.selectAndSaveCard(cardId, base64Image)
                    navController.navigate("${Screen.CreateDesign.route}?showOptions=true")
                },
                onCancelClick = {
                    cardCreationViewModel.resetCreation()
                    navController.popBackStack(Screen.CreateEssentials.route, inclusive = true)
                }
            )
        }
*/

@SuppressLint("UseKtx")
private fun loadBitmapFromUri(context: android.content.Context, uriString: String): Bitmap? {
    return try {
        val uri = uriString.toUri()
        val inputStream = context.contentResolver.openInputStream(uri)
        BitmapFactory.decodeStream(inputStream)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
