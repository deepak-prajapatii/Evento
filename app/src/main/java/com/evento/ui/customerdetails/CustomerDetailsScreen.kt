package com.evento.ui.customerdetails

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.evento.R
import com.evento.domain.entities.TimeSlot
import com.evento.ui.components.ErrorDialog
import com.evento.ui.components.EventsLoadingOverlay
import com.evento.ui.components.SuccessDialog
import com.evento.utils.AppConstants


@Composable
fun CustomerDetailsScreen(
    onBackClick: () -> Unit,
    navigateToEventBooking: () -> Unit,
    viewModel: CustomerDetailsViewModel  = hiltViewModel()
) {
    val colorScheme = MaterialTheme.colorScheme
    val scrollState = rememberScrollState()
    val keyboardController = LocalSoftwareKeyboardController.current
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }

    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                CustomerDetailsUIEvent.NavigateToEventBooking -> {
                    showSuccessDialog = true
                }

                is CustomerDetailsUIEvent.CreateEventFailure -> {
                    showErrorDialog = true
                }
            }
        }
    }

    Scaffold(
        containerColor = colorScheme.background,
        topBar = {
            CustomerDetailsTopBar(onBackClick = onBackClick)
                 },
        bottomBar = {
            CustomerDetailsBottomBar(
                enabled = true,
                onClick = viewModel::bookEvent
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                SelectedSlotCard(selectedSlot = state.timeSlot)

                Spacer(modifier = Modifier.height(24.dp))

                RequiredFieldLabel(text = stringResource(R.string.customer_name))
                Spacer(modifier = Modifier.height(8.dp))
                InputField(
                    value = state.customerName,
                    onValueChange = viewModel::updateCustomerName,
                    placeholder = stringResource(R.string.enter_full_name),
                    error = state.nameErrorResId?.let { stringResource(it) },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        capitalization = KeyboardCapitalization.Words
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                RequiredFieldLabel(text = stringResource(R.string.phone_number))
                Spacer(modifier = Modifier.height(8.dp))
                InputField(
                    value = state.phoneNumber,
                    onValueChange = { newValue ->
                        val digitsOnly = newValue.filter { it.isDigit() }

                        if (digitsOnly.length <= AppConstants.MAX_PHONE_NUMBER_LENGTH) {
                            viewModel.updatePhoneNumber(digitsOnly)

                            if (digitsOnly.length == AppConstants.MAX_PHONE_NUMBER_LENGTH) {
                                keyboardController?.hide()
                            }
                        }
                    },
                    placeholder = stringResource(R.string.enter_phone_number),
                    error = state.phoneErrorResId?.let { stringResource(it) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone
                    )
                )

                Spacer(modifier = Modifier.height(32.dp))
            }

            if (state.isLoading) {
                EventsLoadingOverlay()
            }

            if (showSuccessDialog){
                SuccessDialog(
                    title = stringResource(R.string.booking_confirmed),
                    message = stringResource(R.string.event_booked_successfully),
                    onDismissRequest = {},
                    onDoneClick = {
                        showSuccessDialog = false
                        navigateToEventBooking()
                    },
                    dismissOnBackPress = false,
                    dismissOnClickOutside = false
                )
            }

            if (showErrorDialog){
                ErrorDialog(
                    title = state.uiErrorTitle?:"",
                    message = state.uiErrorMessage?:"",
                    onRetryClick = {
                        showErrorDialog = false
                        viewModel.bookEvent()
                    },
                    onCancelClick = {
                        showErrorDialog = false
                    },
                    onDismissRequest = {},
                    dismissOnBackPress = false,
                    dismissOnClickOutside = false
                )
            }
        }
    }
}

@Composable
private fun CustomerDetailsTopBar(onBackClick: () -> Unit) {
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
                    text = stringResource(R.string.customer_details),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = stringResource(R.string.enter_your_information),
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorScheme.onSurfaceVariant
                )
            }
        }

        Divider(
            color = colorScheme.outline.copy(alpha = 0.4f),
            thickness = 0.5.dp
        )
    }
}

@Composable
private fun CustomerDetailsBottomBar(
    enabled: Boolean,
    onClick: () -> Unit
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
                    text = stringResource(R.string.book_event),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
private fun SelectedSlotCard(selectedSlot: TimeSlot?) {
    val colorScheme = MaterialTheme.colorScheme

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, colorScheme.primary.copy(alpha = 0.18f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.AccessTime,
                    contentDescription = null,
                    tint = colorScheme.onPrimary
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(R.string.selected_slot),
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = selectedSlot?.name ?: "",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${selectedSlot?.startTime} - ${selectedSlot?.endTime}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun RequiredFieldLabel(text: String) {
    val colorScheme = MaterialTheme.colorScheme
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
            color = colorScheme.onBackground
        )
        Text(
            text = " *",
            style = MaterialTheme.typography.bodyLarge,
            color = colorScheme.error
        )
    }
}


@Composable
private fun InputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    error: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    val colorScheme = MaterialTheme.colorScheme

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        isError = error != null,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 54.dp),
        placeholder = {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.bodyMedium,
                color = colorScheme.onSurfaceVariant
            )
        },
        supportingText = {
            if (error != null) {
                Text(
                    text = error,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        shape = RoundedCornerShape(18.dp),
        singleLine = true,
        keyboardOptions = keyboardOptions,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = if (error != null) colorScheme.error else colorScheme.outline,
            unfocusedBorderColor = if (error != null) colorScheme.error else colorScheme.outline.copy(alpha = 0.7f),
            cursorColor = colorScheme.primary,
            focusedContainerColor = colorScheme.surface,
            unfocusedContainerColor = colorScheme.surface,
            disabledContainerColor = colorScheme.surface,
            focusedTextColor = colorScheme.onSurface,
            unfocusedTextColor = colorScheme.onSurface
        )
    )
}

