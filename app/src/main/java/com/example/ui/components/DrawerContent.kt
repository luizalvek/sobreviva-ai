package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.outlined.BatteryChargingFull
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.DownloadForOffline
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.MascotState
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.CharcoalBackground
import com.example.ui.theme.CharcoalCard
import com.example.ui.theme.InterFontFamily
import com.example.ui.theme.TacticalOlive
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary

enum class DrawerDestination {
  NONE,
  CHAT_HISTORY,
  PACKAGES,
  ALERTS,
  SETTINGS,
  LEGAL
}

@Composable
fun DrawerContent(
  batterySaverActive: Boolean,
  onToggleBatterySaver: (Boolean) -> Unit,
  onSelectDestination: (DrawerDestination) -> Unit,
  onClose: () -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxWidth(0.85f)
      .fillMaxHeight()
      .background(CharcoalBackground)
      .statusBarsPadding()
      .navigationBarsPadding()
      .padding(horizontal = 20.dp, vertical = 16.dp)
      .verticalScroll(rememberScrollState())
      .testTag("lateral_drawer_content")
  ) {
    // Header
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.padding(bottom = 20.dp)
    ) {
      FieldMascot(size = 46.dp, state = MascotState.CALM)
      Spacer(modifier = Modifier.width(14.dp))
      Column {
        Text(
          text = "VEREDA",
          fontFamily = InterFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 18.sp,
          letterSpacing = 1.8.sp,
          color = TextPrimary
        )
        Text(
          text = "INSTRUMENTO DE CAMPO",
          fontFamily = InterFontFamily,
          fontWeight = FontWeight.Medium,
          fontSize = 10.sp,
          letterSpacing = 0.8.sp,
          color = AmberAccent
        )
      }
    }

    // System Status Card
    Surface(
      shape = RoundedCornerShape(16.dp),
      color = CharcoalCard,
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 20.dp)
    ) {
      Row(
        modifier = Modifier.padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Column {
          Text(
            text = "STATUS DO SISTEMA",
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 10.sp,
            letterSpacing = 0.8.sp,
            color = TextTertiary
          )
          Text(
            text = "100% Operacional Offline",
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            color = TacticalOlive
          )
        }
        Box(
          modifier = Modifier
            .size(8.dp)
            .clip(CircleShape)
            .background(TacticalOlive)
        )
      }
    }

    Text(
      text = "NAVEGAÇÃO",
      fontFamily = InterFontFamily,
      fontWeight = FontWeight.SemiBold,
      fontSize = 10.sp,
      letterSpacing = 1.sp,
      color = TextTertiary,
      modifier = Modifier.padding(start = 6.dp, bottom = 8.dp)
    )

    // 1. Histórico de conversas
    DrawerItem(
      icon = Icons.Outlined.History,
      title = "Histórico de conversas",
      subtitle = "Sessões e consultas salvas",
      onClick = {
        onSelectDestination(DrawerDestination.CHAT_HISTORY)
        onClose()
      }
    )

    // 2. Pacotes (modelos e mapas)
    DrawerItem(
      icon = Icons.Outlined.DownloadForOffline,
      title = "Pacotes",
      subtitle = "Mapas de relevo e modelos offline",
      badge = "5 Regiões",
      onClick = {
        onSelectDestination(DrawerDestination.PACKAGES)
        onClose()
      }
    )

    // 3. Alertas e notícias
    DrawerItem(
      icon = Icons.Outlined.NotificationsActive,
      title = "Alertas e notícias",
      subtitle = "Frente fria, vendavais e tempestades",
      badge = "2 novos",
      onClick = {
        onSelectDestination(DrawerDestination.ALERTS)
        onClose()
      }
    )

    Spacer(modifier = Modifier.height(14.dp))
    Divider(color = Color(0xFF263029), thickness = 0.8.dp)
    Spacer(modifier = Modifier.height(14.dp))

    Text(
      text = "SISTEMA & ENERGIA",
      fontFamily = InterFontFamily,
      fontWeight = FontWeight.SemiBold,
      fontSize = 10.sp,
      letterSpacing = 1.sp,
      color = TextTertiary,
      modifier = Modifier.padding(start = 6.dp, bottom = 8.dp)
    )

    // 4. Modo bateria
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(14.dp))
        .background(if (batterySaverActive) Color(0xFF1E2822) else Color.Transparent)
        .padding(horizontal = 10.dp, vertical = 8.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = Icons.Outlined.BatteryChargingFull,
          contentDescription = null,
          tint = if (batterySaverActive) TacticalOlive else TextSecondary,
          modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(14.dp))
        Column {
          Text(
            text = "Modo bateria",
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = TextPrimary
          )
          Text(
            text = "Reduz brilho e desliga efeitos",
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 11.sp,
            color = TextTertiary
          )
        }
      }

      Switch(
        checked = batterySaverActive,
        onCheckedChange = onToggleBatterySaver,
        colors = SwitchDefaults.colors(
          checkedThumbColor = AmberAccent,
          checkedTrackColor = Color(0xFF3B2F1C),
          uncheckedThumbColor = TextTertiary,
          uncheckedTrackColor = Color(0xFF1F2622)
        )
      )
    }

    // 5. Configurações
    DrawerItem(
      icon = Icons.Outlined.Settings,
      title = "Configurações",
      subtitle = "Sensores, unidades métricas e precisão",
      onClick = {
        onSelectDestination(DrawerDestination.SETTINGS)
        onClose()
      }
    )

    // 6. Aviso legal
    DrawerItem(
      icon = Icons.Outlined.Description,
      title = "Aviso legal",
      subtitle = "Responsabilidade e segurança remota",
      onClick = {
        onSelectDestination(DrawerDestination.LEGAL)
        onClose()
      }
    )

    Spacer(modifier = Modifier.weight(1f))

    // Footer
    Text(
      text = "Vereda Field Instrument v1.0\nProjetado para autonomia total sem sinal",
      fontFamily = InterFontFamily,
      fontWeight = FontWeight.Normal,
      fontSize = 11.sp,
      color = TextTertiary,
      lineHeight = 15.sp,
      modifier = Modifier.padding(top = 16.dp, start = 6.dp)
    )
  }
}

@Composable
private fun DrawerItem(
  icon: ImageVector,
  title: String,
  subtitle: String,
  badge: String? = null,
  onClick: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(14.dp))
      .clickable { onClick() }
      .padding(horizontal = 10.dp, vertical = 10.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Icon(
      imageVector = icon,
      contentDescription = null,
      tint = TextSecondary,
      modifier = Modifier.size(20.dp)
    )
    Spacer(modifier = Modifier.width(14.dp))
    Column(modifier = Modifier.weight(1f)) {
      Text(
        text = title,
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        color = TextPrimary
      )
      Text(
        text = subtitle,
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp,
        color = TextTertiary
      )
    }
    if (badge != null) {
      Surface(
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFF26322A)
      ) {
        Text(
          text = badge,
          fontFamily = InterFontFamily,
          fontWeight = FontWeight.SemiBold,
          fontSize = 10.sp,
          color = TacticalOlive,
          modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
        )
      }
    }
  }
}
