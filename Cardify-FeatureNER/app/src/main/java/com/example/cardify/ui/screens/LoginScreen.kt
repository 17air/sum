package com.example.cardify.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cardify.auth.TokenManager
import com.example.cardify.models.LoginViewModel
import com.example.cardify.ui.components.CardifyButton
import com.example.cardify.ui.components.CardifyTextField

@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
    onNavigateToMain: () -> Unit,
    loginViewModel: LoginViewModel
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val context = LocalContext.current
    val tokenManager = remember { TokenManager(context) }

    //API 응답 받아 성공/실패 처리
    val loginResult by loginViewModel.loginResult.collectAsState()
    LaunchedEffect(loginResult) {
        loginResult?.onSuccess {
            tokenManager.saveToken(it.token) // 여기서 JWT 토큰 저장
            loginViewModel.clearResult()
            onNavigateToMain()
        }?.onFailure {
            showError = true
            errorMessage = "로그인에 실패했어요. 이메일과 비밀번호를 다시 확인해주세요."
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Welcome Text
            Text(
                text = "안녕하세요!\n로그인 후 Cardify를 이용할 수 있어요.",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                lineHeight = 26.sp,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Email Field
            CardifyTextField(
                value = email,
                onValueChange = {
                    email = it
                    showError = false
                },
                placeholder = "이메일을 입력하세요.",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                isError = showError,
                errorMessage = if (showError) errorMessage else null,
            )

            // Password Field
            CardifyTextField(
                value = password,
                onValueChange = {
                    password = it
                    showError = false
                },
                placeholder = "비밀번호를 입력하세요.",
                isPassword = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                isError = showError,
                errorMessage = if (showError) errorMessage else null,
            )



            // Error Message
            if (showError) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Login Button
            CardifyButton(
                text = "로그인",
                onClick = {
                    if (email.isBlank() || password.isBlank()) {
                        showError = true
                        errorMessage = "이메일과 비밀번호를 모두 입력해주세요."
                    } else {
                        loginViewModel.login(email, password)
                    }
                }
            )


            Spacer(modifier = Modifier.height(16.dp))

            // Register Text
            Text(
                text = "Cardify가 처음이신가요?",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                modifier = Modifier.padding(top = 8.dp)
            )

            // Register Button
            CardifyButton(
                text = "회원가입",
                onClick = { onNavigateToRegister() },
                isPrimary = false
            )
        }
    }
}
