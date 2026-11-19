package com.movieshelf.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.movieshelf.app.ui.theme.MsAmber
import com.movieshelf.app.ui.theme.MsBg
import com.movieshelf.app.ui.theme.MsBgElevated
import com.movieshelf.app.ui.theme.MsDanger
import com.movieshelf.app.ui.theme.MsInk
import com.movieshelf.app.ui.theme.MsInkSoft
import com.movieshelf.app.ui.theme.MsLine
import com.movieshelf.app.ui.theme.Outfit
import com.movieshelf.app.ui.theme.SpaceGrotesk

/** Napis brandowy jak `.brand` w makiecie */
@Composable
fun BrandLabel(modifier: Modifier = Modifier) {
    Text(
        text = "MovieShelf",
        modifier = modifier,
        color = MsAmber,
        fontFamily = SpaceGrotesk,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        letterSpacing = 0.6.sp
    )
}

/** Przycisk tekstowy w topbarze („← Wróć”, „Edytuj”) */
@Composable
fun MsTextButton(
    text: String,
    onClick: () -> Unit,
    accent: Boolean = false,
    modifier: Modifier = Modifier
) {
    TextButton(onClick = onClick, modifier = modifier) {
        Text(
            text = text,
            color = if (accent) MsAmber else MsInkSoft,
            fontFamily = Outfit,
            fontWeight = if (accent) FontWeight.SemiBold else FontWeight.Medium,
            fontSize = 14.sp
        )
    }
}

/** Dolny pasek: Półka / Shaker / Dodaj — jak `.bottom-nav` */
@Composable
fun MovieShelfBottomBar(
    active: BottomTab,
    onShelf: () -> Unit,
    onShaker: () -> Unit,
    onAdd: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // Tylko górna kreska — jak border-top w makiecie
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(MsLine)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MsBg.copy(alpha = 0.92f))
                .navigationBarsPadding()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            BottomItem(
                symbol = "▣",
                label = "Półka",
                selected = active == BottomTab.SHELF,
                onClick = onShelf
            )
            BottomItem(
                symbol = "⟳",
                label = "Shaker",
                selected = active == BottomTab.SHAKER,
                onClick = onShaker
            )
            BottomItem(
                symbol = "+",
                label = "Dodaj",
                selected = active == BottomTab.ADD,
                onClick = onAdd
            )
        }
    }
}

enum class BottomTab { SHELF, SHAKER, ADD }

@Composable
private fun RowScope.BottomItem(
    symbol: String,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = symbol,
            fontSize = 16.sp,
            color = if (selected) MsInk else MsInkSoft
        )
        Text(
            text = label,
            fontFamily = Outfit,
            fontWeight = FontWeight.SemiBold,
            fontSize = 11.sp,
            color = if (selected) MsInk else MsInkSoft
        )
    }
}

/** Zakładki półki: Do obejrzenia / Obejrzane / Ulubione */
@Composable
fun ShelfTabs(
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val labels = listOf("Do obejrzenia", "Obejrzane", "Ulubione")
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MsBgElevated.copy(alpha = 0.55f), RoundedCornerShape(14.dp))
            .border(1.dp, MsLine, RoundedCornerShape(14.dp))
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        labels.forEachIndexed { index, label ->
            val active = index == selectedIndex
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (active) MsInk else androidx.compose.ui.graphics.Color.Transparent)
                    .clickable { onSelect(index) }
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    fontFamily = Outfit,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 11.sp,
                    color = if (active) MsBgElevated else MsInkSoft
                )
            }
        }
    }
}

/** FAB „+” jak w makiecie */
@Composable
fun AmberFab(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(52.dp)
            .shadow(12.dp, RoundedCornerShape(16.dp), ambientColor = MsAmber.copy(alpha = 0.35f), spotColor = MsAmber.copy(alpha = 0.35f))
            .clip(RoundedCornerShape(16.dp))
            .background(MsAmber)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "+",
            color = MsBgElevated,
            fontSize = 26.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = Outfit
        )
    }
}

@Composable
fun MsPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(MsInk)
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = MsBgElevated,
            fontFamily = Outfit,
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp
        )
    }
}

@Composable
fun MsGhostButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, MsLine, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = MsInk,
            fontFamily = Outfit,
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp
        )
    }
}

@Composable
fun MsDangerButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(MsBgElevated)
            .border(1.dp, ColorDangerBorder, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = MsDanger,
            fontFamily = Outfit,
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp
        )
    }
}

private val ColorDangerBorder = Color(0xFFF0C4C0)

@Composable
fun ScreenTitle(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier,
        fontFamily = SpaceGrotesk,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        letterSpacing = (-0.3).sp,
        color = MsInk
    )
}

@Composable
fun VerticalSpacer(height: Int) {
    Spacer(modifier = Modifier.height(height.dp))
}
