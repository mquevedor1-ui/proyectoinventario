package com.example.inventario.ui

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import java.util.Calendar

@Composable
fun FechaIngresar(
    fecha: String,
    onFechaChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Fecha"
) {
    val context = LocalContext.current
    val calendario = Calendar.getInstance()

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, day ->
            onFechaChange("$day/${month + 1}/$year")
        },
        calendario.get(Calendar.YEAR),
        calendario.get(Calendar.MONTH),
        calendario.get(Calendar.DAY_OF_MONTH)
    )

    OutlinedTextField(
        value = fecha,
        onValueChange = {},
        readOnly = true,
        label = { Text(label) },
        modifier = modifier
            .fillMaxWidth()
            .clickable { datePickerDialog.show() },
        enabled = false,
        colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
            disabledTextColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurface,
            disabledBorderColor = androidx.compose.material3.MaterialTheme.colorScheme.outline,
            disabledLabelColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
            disabledPlaceholderColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
            disabledTrailingIconColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
        )
    )
}