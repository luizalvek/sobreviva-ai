package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ChatSession
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.CharcoalBackground
import com.example.ui.theme.CharcoalCardElevated
import com.example.ui.theme.EmergencyRed
import com.example.ui.theme.InterFontFamily
import com.example.ui.theme.TacticalOlive
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerDestinationModal(
  destination: DrawerDestination,
  chatSessions: List<ChatSession>,
  onSelectSession: (ChatSession) -> Unit,
  onClearHistory: () -> Unit,
  onDismiss: () -> Unit
) {
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    containerColor = CharcoalBackground,
    dragHandle = { BottomSheetDefaults.DragHandle(color = Color(0xFF333E37)) }
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 8.dp)
        .padding(bottom = 32.dp)
    ) {
      // Header
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = when (destination) {
            DrawerDestination.CHAT_HISTORY -> "Histórico de Conversas"
            DrawerDestination.PACKAGES -> "Pacotes & Modelos Offline"
            DrawerDestination.ALERTS -> "Alertas e Notícias da Região"
            DrawerDestination.SETTINGS -> "Configurações de Campo"
            DrawerDestination.LEGAL -> "Aviso Legal & Responsabilidade"
            DrawerDestination.NONE -> ""
          },
          fontFamily = InterFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 18.sp,
          color = TextPrimary
        )

        IconButton(onClick = onDismiss) {
          Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Fechar",
            tint = TextSecondary
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      when (destination) {
        DrawerDestination.CHAT_HISTORY -> {
          if (chatSessions.isEmpty()) {
            Text(
              text = "Nenhuma conversa anterior salva.",
              fontFamily = InterFontFamily,
              fontSize = 13.sp,
              color = TextTertiary
            )
          } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
              items(chatSessions, key = { it.id }) { session ->
                Surface(
                  shape = RoundedCornerShape(14.dp),
                  color = CharcoalCardElevated,
                  modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                      onSelectSession(session)
                      onDismiss()
                    }
                ) {
                  Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                      text = session.title,
                      fontFamily = InterFontFamily,
                      fontWeight = FontWeight.SemiBold,
                      fontSize = 14.sp,
                      color = TextPrimary
                    )
                    Text(
                      text = session.lastMessage,
                      fontFamily = InterFontFamily,
                      fontSize = 12.sp,
                      color = TextSecondary,
                      maxLines = 1,
                      modifier = Modifier.padding(top = 2.dp)
                    )
                  }
                }
              }
            }
          }
        }

        DrawerDestination.PACKAGES -> {
          Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Surface(
              shape = RoundedCornerShape(16.dp),
              color = CharcoalCardElevated,
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(16.dp)) {
                Text(
                  text = "Modelo Neural de Sobrevivência (Offline)",
                  fontFamily = InterFontFamily,
                  fontWeight = FontWeight.SemiBold,
                  fontSize = 14.sp,
                  color = TextPrimary
                )
                Text(
                  text = "24.5 MB • Armazenado no dispositivo • Versão 2026.4",
                  fontFamily = InterFontFamily,
                  fontSize = 11.sp,
                  color = TacticalOlive,
                  modifier = Modifier.padding(top = 2.dp)
                )
                Text(
                  text = "Permite raciocínio de primeiros socorros, botânica, topografia e nós mesmo com o aparelho em Modo Avião sem internet.",
                  fontFamily = InterFontFamily,
                  fontSize = 12.sp,
                  color = TextSecondary,
                  modifier = Modifier.padding(top = 6.dp)
                )
              }
            }

            Surface(
              shape = RoundedCornerShape(16.dp),
              color = CharcoalCardElevated,
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(16.dp)) {
                Text(
                  text = "Banco Topográfico e Hidrográfico Nacional",
                  fontFamily = InterFontFamily,
                  fontWeight = FontWeight.SemiBold,
                  fontSize = 14.sp,
                  color = TextPrimary
                )
                Text(
                  text = "142 MB ativos • 5 regiões disponíveis",
                  fontFamily = InterFontFamily,
                  fontSize = 11.sp,
                  color = AmberAccent,
                  modifier = Modifier.padding(top = 2.dp)
                )
              }
            }
          }
        }

        DrawerDestination.ALERTS -> {
          Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Surface(
              shape = RoundedCornerShape(16.dp),
              color = Color(0xFF2B1C1C),
              modifier = Modifier.fillMaxWidth()
            ) {
              Row(modifier = Modifier.padding(14.dp)) {
                Icon(
                  imageVector = Icons.Default.Warning,
                  contentDescription = null,
                  tint = EmergencyRed,
                  modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                  Text(
                    text = "Frente Fria Severa na Mantiqueira",
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Color.White
                  )
                  Text(
                    text = "Queda brusca de temperatura prevista para as 19h (estimada em 3°C com geada nos cumes). Reforce isolamento térmico e abrigo.",
                    fontFamily = InterFontFamily,
                    fontSize = 11.sp,
                    color = Color(0xFFFFCDD2),
                    lineHeight = 16.sp,
                    modifier = Modifier.padding(top = 2.dp)
                  )
                }
              }
            }

            Surface(
              shape = RoundedCornerShape(16.dp),
              color = CharcoalCardElevated,
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(14.dp)) {
                Text(
                  text = "Índice UV Extremo (Cerrado e Cumes)",
                  fontFamily = InterFontFamily,
                  fontWeight = FontWeight.SemiBold,
                  fontSize = 13.sp,
                  color = AmberAccent
                )
                Text(
                  text = "Radiação máxima entre 11h e 15h. Mantenha hidratação constante de 500ml por hora de caminhada.",
                  fontFamily = InterFontFamily,
                  fontSize = 11.sp,
                  color = TextSecondary,
                  lineHeight = 16.sp,
                  modifier = Modifier.padding(top = 2.dp)
                )
              }
            }
          }
        }

        DrawerDestination.SETTINGS -> {
          Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            SettingRow(title = "Sistema de Unidades", value = "Métrico (km, metros, °C)")
            SettingRow(title = "Frequência de Atualização do GPS", value = "Alta Precisão (10m)")
            SettingRow(title = "Declinação Magnética Automática", value = "Ativa (-21° W)")
            SettingRow(title = "Modo Alta Altitude", value = "Sensibilidade Barométrica")
          }
        }

        DrawerDestination.LEGAL -> {
          Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
              text = "AVISO DE RESPONSABILIDADE EM ÁREAS SELVAGENS",
              fontFamily = InterFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 12.sp,
              color = AmberAccent
            )
            Text(
              text = "O aplicativo VEREDA é um instrumento de apoio em campo e consulta técnica offline. As técnicas de sobrevivência, primeiros socorros e navegação devem ser praticadas com responsabilidade. Condições climáticas, terrenos acidentados e comportamento animal podem variar drasticamente. Nunca adentre áreas remotas sem informar seu itinerário a terceiros, portar agasalho adequado e kit mínimo de segurança.",
              fontFamily = InterFontFamily,
              fontSize = 12.sp,
              color = TextSecondary,
              lineHeight = 18.sp
            )
          }
        }

        DrawerDestination.NONE -> {}
      }
    }
  }
}

@Composable
private fun SettingRow(title: String, value: String) {
  Surface(
    shape = RoundedCornerShape(14.dp),
    color = CharcoalCardElevated,
    modifier = Modifier.fillMaxWidth()
  ) {
    Row(
      modifier = Modifier.padding(14.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = title,
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 13.sp,
        color = TextPrimary
      )
      Text(
        text = value,
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        color = TacticalOlive
      )
    }
  }
}
