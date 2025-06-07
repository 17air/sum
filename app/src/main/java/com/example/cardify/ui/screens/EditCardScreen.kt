package com.example.cardify.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.cardify.model.BusinessCard

@Composable
fun EditCardScreen(
    card: BusinessCard,
    onSave: (BusinessCard) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf(card.name) }
    var company by remember { mutableStateOf(card.company) }
    var position by remember { mutableStateOf(card.position) }
    var phone by remember { mutableStateOf(card.phone) }
    var email by remember { mutableStateOf(card.email) }
    var sns by remember { mutableStateOf(card.sns) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                onSave(
                    card.copy(
                        name = name,
                        company = company,
                        position = position,
                        phone = phone,
                        email = email,
                        sns = sns
                    )
                )
            }) { Text("저장") }
        },
        dismissButton = {
            Button(onClick = onDismiss) { Text("취소") }
        },
        text = {
            Column {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("이름") })
                OutlinedTextField(value = company, onValueChange = { company = it }, label = { Text("회사") })
                OutlinedTextField(value = position, onValueChange = { position = it }, label = { Text("직책") })
                OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("전화") })
                OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("이메일") })
                OutlinedTextField(value = sns, onValueChange = { sns = it }, label = { Text("SNS") })
            }
        }
    )
}
