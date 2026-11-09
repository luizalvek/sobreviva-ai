package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.FlashlightOn
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.MascotState
import com.example.ui.theme.EmergencyRed
import com.example.ui.theme.EmergencyRedDark
import com.example.ui.theme.InterFontFamily
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary

@Composable
fun EmergencyScreen(
  lat: Double = -22.41320,
  lng: Double = -44.96210,
  elevationMeters: Int = 2120,
  isTorchActive: Boolean = false,
  onToggleTorch: () -> Unit,
  onDismiss: () -> Unit
) {
  BackHandler {
    onDismiss()
  }

  val context = LocalContext.current
  var isMorseActive by remember { mutableStateOf(false) }
  var isSirenPlaying by remember { mutableStateOf(false) }

  val formattedCoords = String.format(java.util.Locale.US, "%.5f, %.5f", lat, lng)
  val radioMessage = "SOLICITO RESGATE IMEDIATO. Minhas coordenadas são: Latitude $formattedCoords, Altitude $elevationMeters metros."

  fun copyCoordinates() {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("Coordenadas SOS", formattedCoords)
    clipboard.setPrimaryClip(clip)
    Toast.makeText(context, "Coordenadas copiadas para a área de transferência", Toast.LENGTH_SHORT).show()
  }

  fun dialNumber(number: String) {
    try {
      val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number"))
      context.startActivity(intent)
    } catch (_: Exception) {
      Toast.makeText(context, "Não foi possível abrir o discador", Toast.LENGTH_SHORT).show()
    }
  }

  fun sendEmergencySms() {
    try {
      val smsText = "SOS EMERGÊNCIA: Preciso de resgate. Coordenadas: Lat ${String.format(java.util.Locale.US, "%.5f", lat)}, Lng ${String.format(java.util.Locale.US, "%.5f", lng)}, Alt: ${elevationMeters}m. Bateria restante calculada. Por favor envie socorro."
      val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("smsto:")
        putExtra("sms_body", smsText)
      }
      context.startActivity(intent)
    } catch (_: Exception) {
      Toast.makeText(context, "Não foi possível abrir o aplicativo de SMS", Toast.LENGTH_SHORT).show()
    }
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(Color(0xFF0F0B0B))
      .statusBarsPadding()
      .navigationBarsPadding()
      .testTag("emergency_screen_container")
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 20.dp, vertical = 12.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Top header with Close
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(10.dp)
              .clip(CircleShape)
              .background(EmergencyRed)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "MODO EMERGÊNCIA ATIVO",
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            letterSpacing = 1.2.sp,
            color = EmergencyRed
          )
        }

        IconButton(
          onClick = onDismiss,
          modifier = Modifier.testTag("emergency_close_button")
        ) {
          Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Encerrar emergência",
            tint = TextSecondary
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Mascot in Alert Mode guiding step-by-step
      FieldMascot(
        size = 80.dp,
        state = MascotState.ALERT
      )

      Spacer(modifier = Modifier.height(12.dp))

      Text(
        text = "Mantenha a calma. Estamos com você.",
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        color = TextPrimary,
        textAlign = TextAlign.Center
      )
      Text(
        text = "Siga os passos abaixo para acionar resgate e sinalizar sua posição.",
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        color = TextSecondary,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(top = 4.dp, bottom = 18.dp)
      )

      // Location Card (Dominant, clean, precise)
      Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color(0xFF1D1212),
        modifier = Modifier
          .fillMaxWidth()
          .border(1.dp, Color(0xFF3B1818), RoundedCornerShape(20.dp))
      ) {
        Column(
          modifier = Modifier.padding(18.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "SUA POSIÇÃO EXATA",
              fontFamily = InterFontFamily,
              fontWeight = FontWeight.SemiBold,
              fontSize = 11.sp,
              letterSpacing = 1.sp,
              color = EmergencyRed
            )

            IconButton(
              onClick = { copyCoordinates() },
              modifier = Modifier.size(36.dp)
            ) {
              Icon(
                imageVector = Icons.Default.ContentCopy,
                contentDescription = "Copiar coordenadas",
                tint = TextPrimary,
                modifier = Modifier.size(18.dp)
              )
            }
          }

          Text(
            text = formattedCoords,
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = TextPrimary,
            letterSpacing = 0.5.sp,
            modifier = Modifier.padding(vertical = 4.dp)
          )

          Text(
            text = "Altitude: ~${elevationMeters}m | Precisão GPS: ±4m (Alta)",
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            color = TextSecondary
          )

          Spacer(modifier = Modifier.height(12.dp))

          Box(
            modifier = Modifier
              .fillMaxWidth()
              .background(Color(0xFF281313), RoundedCornerShape(12.dp))
              .padding(12.dp)
          ) {
            Text(
              text = "Mensagem para rádio:\n\"$radioMessage\"",
              fontFamily = InterFontFamily,
              fontWeight = FontWeight.Normal,
              fontSize = 12.sp,
              color = Color(0xFFFFD5D2),
              lineHeight = 17.sp
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(18.dp))

      // Direct SMS Dispatch
      Button(
        onClick = { sendEmergencySms() },
        colors = ButtonDefaults.buttonColors(
          containerColor = EmergencyRed,
          contentColor = Color.White
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
          .fillMaxWidth()
          .height(52.dp)
          .testTag("send_emergency_sms_button")
      ) {
        Icon(
          imageVector = Icons.Default.Message,
          contentDescription = null,
          modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
          text = "Enviar SMS com Minha Posição",
          fontFamily = InterFontFamily,
          fontWeight = FontWeight.SemiBold,
          fontSize = 14.sp
        )
      }

      Spacer(modifier = Modifier.height(18.dp))

      // Emergency Numbers Section
      Text(
        text = "LIGAÇÃO RÁPIDA DE SOCORRO",
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 11.sp,
        letterSpacing = 1.sp,
        color = TextTertiary,
        modifier = Modifier.fillMaxWidth()
      )

      Spacer(modifier = Modifier.height(10.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        // 192 SAMU
        EmergencyCallCard(
          number = "192",
          title = "SAMU",
          subtitle = "Médico",
          modifier = Modifier.weight(1f),
          onClick = { dialNumber("192") }
        )

        // 193 Bombeiros
        EmergencyCallCard(
          number = "193",
          title = "BOMBEIROS",
          subtitle = "Resgate",
          modifier = Modifier.weight(1f),
          onClick = { dialNumber("193") }
        )

        // 199 Defesa Civil
        EmergencyCallCard(
          number = "199",
          title = "DEFESA CIVIL",
          subtitle = "Emergências",
          modifier = Modifier.weight(1f),
          onClick = { dialNumber("199") }
        )
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Signal Tools (Lanterna SOS / Apito)
      Text(
        text = "SINALIZAÇÃO DE EMERGÊNCIA",
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 11.sp,
        letterSpacing = 1.sp,
        color = TextTertiary,
        modifier = Modifier.fillMaxWidth()
      )

      Spacer(modifier = Modifier.height(10.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        // Flashlight / SOS Morse
        Surface(
          shape = RoundedCornerShape(16.dp),
          color = if (isTorchActive) Color(0xFF4A1818) else Color(0xFF1D1212),
          modifier = Modifier
            .weight(1f)
            .height(76.dp)
            .clickable { onToggleTorch() }
            .border(
              1.dp,
              if (isTorchActive) EmergencyRed else Color(0xFF2E1A1A),
              RoundedCornerShape(16.dp)
            )
        ) {
          Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.FlashlightOn,
              contentDescription = "Lanterna",
              tint = if (isTorchActive) EmergencyRed else TextSecondary,
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "Lanterna SOS",
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                color = TextPrimary
              )
              Text(
                text = if (isTorchActive) "LIGADA (Ativa)" else "Toque p/ Ligar",
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 11.sp,
                color = if (isTorchActive) EmergencyRed else TextTertiary
              )
            }
          }
        }

        // Whistle / Audio Alarm
        Surface(
          shape = RoundedCornerShape(16.dp),
          color = if (isSirenPlaying) Color(0xFF4A1818) else Color(0xFF1D1212),
          modifier = Modifier
            .weight(1f)
            .height(76.dp)
            .clickable { isSirenPlaying = !isSirenPlaying }
            .border(
              1.dp,
              if (isSirenPlaying) EmergencyRed else Color(0xFF2E1A1A),
              RoundedCornerShape(16.dp)
            )
        ) {
          Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.VolumeUp,
              contentDescription = "Alarme sonoro",
              tint = if (isSirenPlaying) EmergencyRed else TextSecondary,
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "Sinal Sonoro",
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                color = TextPrimary
              )
              Text(
                text = if (isSirenPlaying) "TOCANDO SOS" else "Pulsos de 3 apitos",
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 11.sp,
                color = if (isSirenPlaying) EmergencyRed else TextTertiary
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(24.dp))

      // Calm instructions list
      Surface(
        shape = RoundedCornerShape(18.dp),
        color = Color(0xFF161010),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "PROTOCOLO DE SEGURANÇA",
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 11.sp,
            letterSpacing = 1.sp,
            color = EmergencyRed
          )
          Spacer(modifier = Modifier.height(10.dp))

          EmergencyStepItem(step = "1", text = "Permaneça onde está. Resgate localiza pessoas paradas com mais facilidade.")
          EmergencyStepItem(step = "2", text = "Isole seu corpo do chão frio com mochila ou folhas secas.")
          EmergencyStepItem(step = "3", text = "Economize bateria do celular reduzindo o brilho ao mínimo.")
          EmergencyStepItem(step = "4", text = "Emita 3 sinais sonoros ou luminosos regulares a cada 10 minutos.")
        }
      }

      Spacer(modifier = Modifier.height(24.dp))

      // Exit Emergency Button
      OutlinedButton(
        onClick = onDismiss,
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
          .fillMaxWidth()
          .height(48.dp)
      ) {
        Text(
          text = "Encerrar Modo Emergência",
          fontFamily = InterFontFamily,
          fontWeight = FontWeight.Medium,
          fontSize = 13.sp,
          color = TextSecondary
        )
      }

      Spacer(modifier = Modifier.height(16.dp))
    }
  }
}

@Composable
private fun EmergencyCallCard(
  number: String,
  title: String,
  subtitle: String,
  modifier: Modifier = Modifier,
  onClick: () -> Unit
) {
  Surface(
    shape = RoundedCornerShape(16.dp),
    color = Color(0xFF1E1313),
    modifier = modifier
      .clickable { onClick() }
      .border(1.dp, Color(0xFF3D1919), RoundedCornerShape(16.dp))
  ) {
    Column(
      modifier = Modifier.padding(12.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Icon(
        imageVector = Icons.Default.Phone,
        contentDescription = null,
        tint = EmergencyRed,
        modifier = Modifier.size(18.dp)
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = number,
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        color = TextPrimary
      )
      Text(
        text = title,
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 10.sp,
        color = EmergencyRed,
        letterSpacing = 0.5.sp
      )
      Text(
        text = subtitle,
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 9.sp,
        color = TextTertiary
      )
    }
  }
}

@Composable
private fun EmergencyStepItem(step: String, text: String) {
  Row(
    modifier = Modifier.padding(vertical = 4.dp),
    verticalAlignment = Alignment.Top
  ) {
    Box(
      modifier = Modifier
        .size(18.dp)
        .clip(CircleShape)
        .background(Color(0xFF331616)),
      contentAlignment = Alignment.Center
    ) {
      Text(
        text = step,
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 10.sp,
        color = EmergencyRed
      )
    }
    Spacer(modifier = Modifier.width(10.dp))
    Text(
      text = text,
      fontFamily = InterFontFamily,
      fontWeight = FontWeight.Normal,
      fontSize = 12.sp,
      color = TextSecondary,
      lineHeight = 17.sp
    )
  }
}
