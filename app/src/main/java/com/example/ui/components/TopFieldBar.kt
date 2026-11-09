package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CharcoalBackground
import com.example.ui.theme.InterFontFamily
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary

@Composable
fun TopFieldBar(
  title: String,
  subtitle: String? = null,
  onOpenDrawer: () -> Unit,
  onSosTriggered: () -> Unit,
  onSosHint: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .background(CharcoalBackground)
      .statusBarsPadding()
      .padding(horizontal = 16.dp, vertical = 8.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    // Menu lateral icon
    IconButton(
      onClick = onOpenDrawer,
      modifier = Modifier
        .size(44.dp)
        .testTag("drawer_menu_button")
    ) {
      Icon(
        imageVector = Icons.Outlined.Menu,
        contentDescription = "Abrir menu lateral",
        tint = TextPrimary,
        modifier = Modifier.size(24.dp)
      )
    }

    // Title / App Status
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier.weight(1f).padding(horizontal = 8.dp)
    ) {
      Text(
        text = title.uppercase(),
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        letterSpacing = 1.5.sp,
        color = TextPrimary
      )
      if (subtitle != null) {
        Text(
          text = subtitle,
          fontFamily = InterFontFamily,
          fontWeight = FontWeight.Normal,
          fontSize = 11.sp,
          color = TextTertiary,
          letterSpacing = 0.4.sp
        )
      }
    }

    // SOS discreet button
    SosButton(
      onSosTriggered = onSosTriggered,
      onSingleTapHint = onSosHint
    )
  }
}
