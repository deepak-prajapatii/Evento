package com.evento.ui.slotselection

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.evento.domain.model.TimeSlot

@Composable
fun SlotSelectionScreen(
    onBackClick: () -> Unit, onContinueClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    val slots = listOf(
        TimeSlot("1", "Morning Slot", "09:00", "10:00"),
        TimeSlot("2", "Late Morning Slot", "10:00", "11:00"),
        TimeSlot("3", "Afternoon Slot", "14:00", "15:00"),
        TimeSlot("4", "Evening Slot", "16:00", "17:00"),
    )

    Scaffold(containerColor = colorScheme.background, topBar = {
        SelectTimeSlotTopBar(onBackClick = onBackClick)
    }, bottomBar = {
        SelectTimeSlotBottomBar(
            enabled = true, onClick = onContinueClick
        )
    }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "${slots.size} slots available",
                style = MaterialTheme.typography.bodyMedium,
                color = colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(slots, key = { it.slotId }) { slot ->
                    TimeSlotGridItem(
                        slot = slot, selected = true, onClick = { })
                }
            }
        }
    }
}

@Composable
private fun TimeSlotGridItem(
    slot: TimeSlot, selected: Boolean, onClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    val configuration = LocalConfiguration.current
    val isSmallScreen = configuration.screenWidthDp < 360

    val borderColor = if (selected) colorScheme.primary else colorScheme.outline
    val radioColor = if (selected) colorScheme.primary else colorScheme.outline

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 112.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        border = BorderStroke(1.dp, borderColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = slot.name,
                    style = if (isSmallScreen) MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold)
                    else MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(12.dp))

                RadioButton(
                    selected = selected, onClick = onClick, colors = RadioButtonDefaults.colors(
                        selectedColor = radioColor, unselectedColor = radioColor
                    )
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.AccessTime,
                    contentDescription = null,
                    tint = colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(18.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "${slot.startTime} - ${slot.endTime}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorScheme.onSurfaceVariant
                )
            }
        }
    }
}


@Composable
private fun SelectTimeSlotTopBar(onBackClick: () -> Unit) {
    val colorScheme = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(
                    text = "Select Time Slot", style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    ), color = colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Choose your preferred slot",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorScheme.onSurfaceVariant
                )
            }
        }

        Divider(
            color = colorScheme.outline.copy(alpha = 0.4f), thickness = 0.5.dp
        )
    }
}

@Composable
private fun SelectTimeSlotBottomBar(
    enabled: Boolean, onClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    Surface(
        tonalElevation = 4.dp,
        color = colorScheme.background,
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Button(
                onClick = onClick,
                enabled = enabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(26.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorScheme.primary.copy(alpha = 0.85f),
                    contentColor = colorScheme.onPrimary,
                    disabledContainerColor = colorScheme.primary.copy(alpha = 0.3f),
                    disabledContentColor = colorScheme.onPrimary.copy(alpha = 0.6f)
                )
            ) {
                Text(
                    text = "Continue", style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}