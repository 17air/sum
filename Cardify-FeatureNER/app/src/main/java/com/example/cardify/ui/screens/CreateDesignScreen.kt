package com.example.cardify.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cardify.R
import com.example.cardify.ui.theme.PrimaryTeal

@Composable
fun CreateDesignScreen(
    isFirst: Boolean = true,
    onCardSelected: (String) -> Unit,
    onCancelClick: () -> Unit
) {
    val cardDesigns = remember {
        listOf(
            CardDesign("card1", R.drawable.card_one),
            CardDesign("card2", R.drawable.card_two),
            CardDesign("card3", R.drawable.card_three),
            CardDesign("card4", R.drawable.card_four),
            CardDesign("card5", R.drawable.card_five),
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        // Top section with title and progress
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            // Back/Cancel button
            TextButton(
                onClick = onCancelClick,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Text(
                    text = if (isFirst) "Cancel" else "Back",
                    color = Color.Gray
                )
            }

            // Title
            Text(
                text = "Card Design",
                modifier = Modifier.align(Alignment.Center),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            // Progress indicator
            Text(
                text = "4/5",
                modifier = Modifier.align(Alignment.CenterEnd),
                color = PrimaryTeal
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Instructions
        Text(
            text = if (isFirst)
                "내 명함을 선택해주세요!"
            else
                "새로운 명함이 생성되었어요.\n" + "AI가 분석한 당신의 취향을 확인해보세요. ",
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Card grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(cardDesigns) { design ->
                CardDesignItem(
                    design = design,
                    onClick = { onCardSelected(design.id) }
                )
            }
        }
    }
}

@Composable
fun CardDesignItem(
    design: CardDesign,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .aspectRatio(0.63f) // Business card ratio
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
    ) {
        Image(
            painter = painterResource(id = design.imageResId),
            contentDescription = "Card design ${design.id}",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}

data class CardDesign(
    val id: String,
    val imageResId: Int
)