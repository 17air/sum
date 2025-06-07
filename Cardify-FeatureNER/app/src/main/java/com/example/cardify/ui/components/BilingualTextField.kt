package com.example.cardify.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cardify.models.BilingualData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BilingualTextField(
    label: String,
    value: BilingualData,
    onValueChange: (BilingualData) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isBilingual: Boolean = true
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        OutlinedTextField(
            value = value.korean,
            onValueChange = { 
                onValueChange(value.copy(korean = it, isBilingual = isBilingual))
            },
            label = { Text("한국어") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        OutlinedTextField(
            value = value.english,
            onValueChange = { 
                onValueChange(value.copy(english = it, isBilingual = isBilingual))
            },
            label = { Text("영어") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled && isBilingual,
            singleLine = true
        )
    }
}
