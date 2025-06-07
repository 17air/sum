package com.example.cardify.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import coil.compose.AsyncImage
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cardify.models.BusinessCard
import com.example.cardify.ui.components.BottomNavBar
import com.example.cardify.ui.components.BusinessCardItem

@Composable
fun CardListScreen(
    cards: List<BusinessCard>,
    onNavigateToMain: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onUpdateCard: (BusinessCard) -> Unit,
) {
    val selectedImage = remember { mutableStateOf<String?>(null) }
    val editingCard = remember { mutableStateOf<BusinessCard?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 64.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "명함첩",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(16.dp)
            )

            LazyColumn {
                items(cards) { card ->
                    BusinessCardItem(
                        card = card,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        onImageClick = { url -> selectedImage.value = url },
                        onEditClick = { editingCard.value = card }
                    )
                }
            }
        }

        BottomNavBar(
            currentRoute = "cardbook",
            onNavigateToMain = onNavigateToMain,
            onNavigateToCardBook = {},
            onNavigateToSettings = onNavigateToSettings,
            modifier = Modifier.align(Alignment.BottomCenter)
        )

        selectedImage.value?.let { url ->
            AlertDialog(
                onDismissRequest = { selectedImage.value = null },
                confirmButton = {
                    Button(onClick = { selectedImage.value = null }) { Text("닫기") }
                },
                text = {
                    AsyncImage(
                        model = url,
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            )
        }

        editingCard.value?.let { card ->
            EditCardScreen(
                card = card,
                onSave = { updated ->
                    editingCard.value = null
                    onUpdateCard(updated)
                },
                onDismiss = { editingCard.value = null }
            )
        }
    }
}

