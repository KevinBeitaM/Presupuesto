package cr.ac.una.presupesto.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

@Composable
fun SaldoTotalCard(
    saldo: Double,
    modifier: Modifier = Modifier
) {
    val formatter = remember {
        NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-CR")).apply {
            currency = Currency.getInstance("CRC")
        }
    }
    val esPositivo = saldo >= 0.0
    val fondo = if (esPositivo) Color(0xFF9FDCA7) else Color(0xFFEFA3A3)
    val colorTexto = if (esPositivo) Color(0xFF2E7D32) else Color(0xFFC62828)
    val borde = if (esPositivo) Color(0xFF5BBE73) else Color(0xFFD96F6F)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = fondo),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        border = BorderStroke(1.dp, borde)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Text(
                text = "Saldo total",
                style = MaterialTheme.typography.labelLarge,
                color = colorTexto
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = formatter.format(saldo),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = colorTexto
            )
        }
    }
}
