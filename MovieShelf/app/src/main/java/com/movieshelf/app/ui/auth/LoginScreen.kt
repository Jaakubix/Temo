package com.movieshelf.app.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.movieshelf.app.ui.components.BrandLabel
import com.movieshelf.app.ui.components.MsGhostButton
import com.movieshelf.app.ui.components.MsPrimaryButton
import com.movieshelf.app.ui.components.ScreenTitle
import com.movieshelf.app.ui.theme.MsAmber
import com.movieshelf.app.ui.theme.MsBg
import com.movieshelf.app.ui.theme.MsBgElevated
import com.movieshelf.app.ui.theme.MsDanger
import com.movieshelf.app.ui.theme.MsInk
import com.movieshelf.app.ui.theme.MsInkSoft
import com.movieshelf.app.ui.theme.MsLine
import com.movieshelf.app.ui.theme.Outfit

/** Ekran logowania / rejestracji Firebase (e-mail + hasło) */
@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isRegister by remember { mutableStateOf(false) }
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MsBg)
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BrandLabel()
        Spacer(modifier = Modifier.height(6.dp))
        ScreenTitle(if (isRegister) "Załóż konto" else "Zaloguj się")
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Firebase Auth — po zalogowaniu półka działa też offline.",
            fontFamily = Outfit,
            fontSize = 14.sp,
            color = MsInkSoft
        )

        Spacer(modifier = Modifier.height(24.dp))

        AuthField(
            value = email,
            onValueChange = {
                email = it
                viewModel.clearError()
            },
            label = "E-mail",
            keyboardType = KeyboardType.Email
        )
        Spacer(modifier = Modifier.height(12.dp))
        AuthField(
            value = password,
            onValueChange = {
                password = it
                viewModel.clearError()
            },
            label = "Hasło",
            keyboardType = KeyboardType.Password,
            isPassword = true
        )

        val error = (uiState as? AuthUiState.Error)?.message
        if (error != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = error,
                color = MsDanger,
                fontFamily = Outfit,
                fontSize = 13.sp
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (uiState is AuthUiState.Loading) {
            CircularProgressIndicator(color = MsAmber)
        } else {
            MsPrimaryButton(
                text = if (isRegister) "Zarejestruj" else "Zaloguj",
                onClick = {
                    if (isRegister) viewModel.register(email, password)
                    else viewModel.signIn(email, password)
                }
            )
            Spacer(modifier = Modifier.height(10.dp))
            MsGhostButton(
                text = if (isRegister) "Mam już konto" else "Nie mam konta — rejestracja",
                onClick = {
                    isRegister = !isRegister
                    viewModel.clearError()
                }
            )
            Spacer(modifier = Modifier.height(10.dp))
            MsGhostButton(
                text = "Wejdź jako gość (tylko lokalna baza)",
                onClick = viewModel::continueAsGuest
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Firebase: podmień app/google-services.json z konsoli Firebase.",
            fontFamily = Outfit,
            fontSize = 11.sp,
            color = MsInkSoft,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
private fun AuthField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType,
    isPassword: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label, fontFamily = Outfit) },
        singleLine = true,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MsBgElevated,
            unfocusedContainerColor = MsBgElevated,
            focusedBorderColor = MsAmber,
            unfocusedBorderColor = MsLine,
            cursorColor = MsAmber,
            focusedTextColor = MsInk,
            unfocusedTextColor = MsInk
        )
    )
}
