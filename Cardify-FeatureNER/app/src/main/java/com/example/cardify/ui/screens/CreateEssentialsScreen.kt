// Create a file: app/src/main/java/com/example/cardify/ui/screens/CreateEssentialsScreen.kt
package com.example.cardify.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cardify.models.BusinessCardInfo
import com.example.cardify.ui.components.BilingualTextField
import com.example.cardify.ui.components.CardifyButton
import com.example.cardify.ui.components.KoreanTextField
import com.example.cardify.ui.theme.PrimaryTeal

@Composable
fun CreateEssentialsScreen(
    cardInfo: BusinessCardInfo,
    onCardInfoChange: (BusinessCardInfo) -> Unit,
    onNextClick: () -> Unit,
    onBackClick: () -> Unit
) {
    var nameEnabled by remember { mutableStateOf(true) }
    var companyEnabled by remember { mutableStateOf(false) }
    var positionEnabled by remember { mutableStateOf(false) }
    var phoneEnabled by remember { mutableStateOf(false) }
    var emailEnabled by remember { mutableStateOf(false) }
    var snsEnabled by remember { mutableStateOf(false) }
    
    // Validate name field
    val isNameValid = remember { derivedStateOf { cardInfo.name.korean.isNotBlank() } }
    //BusinessCardInfo <<format>> : name, company, position, phone, email, sns,
    //imagerUrl, answers 두 개는 빼기

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Title
        Text(
            text = "새로운 명함 생성하기",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryTeal
        )
        
        // Language Mode Toggle
        var isBilingualMode by remember { mutableStateOf(false) }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "양면 명함",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(end = 8.dp)
            )
            Switch(
                checked = isBilingualMode,
                onCheckedChange = {
                    isBilingualMode = it
                    // Update card info to match mode
                    if (!it) {
                        // Convert to single language if switching from bilingual
                        onCardInfoChange(cardInfo.toSingleLanguage())
                    }
                }
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        // Subtitle
        Text(
            text = "명함에 넣을 정보를 입력해주세요.",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )

        Text(
            text = "보다 상세한 내용은 명함 생성 이후 별도로 추가할 수 있습니다.",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )

        Spacer(
            modifier = Modifier.height(32.dp)
        )

        // Form fields with toggle switches
        BilingualTextField(
            label = "이름(필수항목)",
            value = cardInfo.name,
            onValueChange = { 
                onCardInfoChange(cardInfo.copy(name = it))
            },
            enabled = nameEnabled,
            isBilingual = isBilingualMode
        )
        
        Switch(
            checked = nameEnabled,
            onCheckedChange = { nameEnabled = it },
            modifier = Modifier.align(Alignment.End)
        )
        
        Spacer(modifier = Modifier.height(16.dp))

        BilingualTextField(
            label = "회사(필수항목)",
            value = cardInfo.company,
            onValueChange = { onCardInfoChange(cardInfo.copy(company = it)) },
            enabled = companyEnabled,
            isBilingual = isBilingualMode
        )
        
        Switch(
            checked = companyEnabled,
            onCheckedChange = { companyEnabled = it },
            modifier = Modifier.align(Alignment.End)
        )
        
        Spacer(modifier = Modifier.height(16.dp))

        BilingualTextField(
            label = "직책(선택항목)",
            value = cardInfo.position,
            onValueChange = { onCardInfoChange(cardInfo.copy(position = it)) },
            enabled = positionEnabled,
            isBilingual = isBilingualMode
        )
        
        Switch(
            checked = positionEnabled,
            onCheckedChange = { positionEnabled = it },
            modifier = Modifier.align(Alignment.End)
        )
        
        Spacer(modifier = Modifier.height(16.dp))


        InfoField(
            label = "전화번호",
            value = cardInfo.phone,
            onValueChange = { onCardInfoChange(cardInfo.copy(phone = it)) },
            isEnabled = phoneEnabled,
            onEnabledChange = { phoneEnabled = it }
        )

        InfoField(
            label = "이메일",
            value = cardInfo.email,
            onValueChange = { onCardInfoChange(cardInfo.copy(email = it)) },
            isEnabled = emailEnabled,
            onEnabledChange = { emailEnabled = it }
        )

        InfoField(
            label = "SNS주소",
            value = cardInfo.sns,
            onValueChange = { onCardInfoChange(cardInfo.copy(sns = it)) },
            isEnabled = snsEnabled,
            onEnabledChange = { snsEnabled = it }
        )

        Spacer(
            modifier = Modifier.height(40.dp)
        )

        // Buttons
        CardifyButton(
            text = "AI와 함께 명함 디자인하기",
            onClick = {
                if (isNameValid.value) {
                    onNextClick()
                } else {
                    // Show error toast or message
                }
            },
            isPrimary = true
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        CardifyButton(
            text = "내 명함으로 돌아가기",
            onClick = onBackClick,
            isPrimary = false
        )
    }
}

@Composable
fun InfoField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isEnabled: Boolean,
    onEnabledChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            if (isEnabled) {
                // Replace CardifyTextField with KoreanTextField
                KoreanTextField(
                    value = value,
                    onValueChange = onValueChange,
                    label = label,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                Text(
                    text = label,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Switch(
            checked = isEnabled,
            onCheckedChange = onEnabledChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.primary,
                checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        )
    }
}
