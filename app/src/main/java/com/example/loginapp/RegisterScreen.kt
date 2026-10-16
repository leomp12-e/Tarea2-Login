package com.example.loginapp

import android.util.Patterns
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import android.content.Context

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onRegisterSuccess: (String) -> Unit,   // Recibe el email para pasarlo a Home
    onBackToLogin: () -> Unit
) {

    val context = LocalContext.current
    // ---------------------------------------------------------
    // 1. ESTADO
    // ---------------------------------------------------------
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var acceptTerms by remember { mutableStateOf(false) }

    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmError by remember { mutableStateOf<String?>(null) }
    var termsError by remember { mutableStateOf<String?>(null) }

    var showPassword by remember { mutableStateOf(false) }
    var showConfirm by remember { mutableStateOf(false) }

    // ---------------------------------------------------------
    // 2. FOCO
    // ---------------------------------------------------------
    val focusManager = LocalFocusManager.current
    val emailFocusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        emailFocusRequester.requestFocus()
    }

    // ---------------------------------------------------------
    // 3. VALIDACIONES
    // ---------------------------------------------------------
    fun validarEmail(context: Context): Boolean {
        val ok = email.isNotBlank() &&
                Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()
        emailError = if (!ok) context.getString(R.string.valid_mail) else null
        return ok
    }

    fun validarPassword(context: Context): Boolean {
        val regex = "^(?=.*[A-Z])(?=.*[0-9]).{8,}$".toRegex()
        val ok = regex.matches(password)
        passwordError = if (!ok) {
            context.getString(R.string.unvalid_passwd)
        } else null
        return ok
    }

    fun validarConfirm(context: Context): Boolean {
        val ok = confirmPassword == password && confirmPassword.isNotEmpty()
        confirmError = if (!ok) context.getString(R.string.unmatch_passwd) else null
        return ok
    }

    fun validarTerminos(context: Context): Boolean {
        termsError = if (!acceptTerms) context.getString(R.string.must_accept_terms) else null
        return acceptTerms
    }

    // ---------------------------------------------------------
    // 4. UI
    // ---------------------------------------------------------
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.titulo_registro)) },
                navigationIcon = {
                    IconButton(onClick = onBackToLogin) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.volver)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.titulo_bienvenida),
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- Email ---
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    if (it.isNotEmpty()) validarEmail(context)
                },
                label = { Text(stringResource(R.string.hint_email)) },
                isError = emailError != null,
                supportingText = emailError?.let { { Text(it) } },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(emailFocusRequester)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // --- Contraseña ---
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    if (it.isNotEmpty()) validarPassword(context)
                    // Si ya había confirmación escrita, revalidamos
                    if (confirmPassword.isNotEmpty()) validarConfirm(context)
                },
                label = { Text(stringResource(R.string.hint_password)) },
                isError = passwordError != null,
                supportingText = passwordError?.let { { Text(it) } },
                visualTransformation = if (showPassword) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                trailingIcon = {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            imageVector = if (showPassword) {
                                Icons.Filled.Visibility
                            } else {
                                Icons.Filled.VisibilityOff
                            },
                            contentDescription = stringResource(R.string.toggle_password)
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // --- Confirmar contraseña ---
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    if (it.isNotEmpty()) validarConfirm(context)
                },
                label = { Text(stringResource(R.string.hint_confirm_password)) },
                isError = confirmError != null,
                supportingText = confirmError?.let { { Text(it) } },
                visualTransformation = if (showConfirm) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                trailingIcon = {
                    IconButton(onClick = { showConfirm = !showConfirm }) {
                        Icon(
                            imageVector = if (showConfirm) {
                                Icons.Filled.Visibility
                            } else {
                                Icons.Filled.VisibilityOff
                            },
                            contentDescription = stringResource(R.string.toggle_password)
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // --- Checkbox términos ---
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = acceptTerms,
                    onCheckedChange = {
                        acceptTerms = it
                        if (it) termsError = null
                    }
                )
                Text(stringResource(R.string.acepto_terminos))
            }
            termsError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- Botón Crear cuenta ---
            Button(
                onClick = {
                    val emailOk = validarEmail(context)
                    val passOk = validarPassword(context)
                    val confirmOk = validarConfirm(context)
                    val termsOk = validarTerminos(context)
                    if (emailOk && passOk && confirmOk && termsOk) {
                        focusManager.clearFocus()
                        onRegisterSuccess(email.trim())
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.boton_crear_cuenta))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- Volver a Login ---
            TextButton(onClick = onBackToLogin) {
                Text(stringResource(R.string.ya_tienes_cuenta))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    MaterialTheme {
        RegisterScreen(onRegisterSuccess = {}, onBackToLogin = {})
    }
}