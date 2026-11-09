package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Handyman
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ScreenTab
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.CharcoalBackground
import com.example.ui.theme.InterFontFamily
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary

data class NavTabItem(
  val tab: ScreenTab,
  val label: String,
  val icon: ImageVector,
  val tag: String
)

@Composable
fun FieldBottomNav(
  currentTab: ScreenTab,
  onSelectTab: (ScreenTab) -> Unit,
  modifier: Modifier = Modifier
) {
  val tabs = listOf(
    NavTabItem(ScreenTab.CHAT, "Chat", Icons.Outlined.ChatBubbleOutline, "tab_chat"),
    NavTabItem(ScreenTab.MAP, "Mapa", Icons.Outlined.Explore, "tab_map"),
    NavTabItem(ScreenTab.LIBRARY, "Biblioteca", Icons.Outlined.AutoStories, "tab_library"),
    NavTabItem(ScreenTab.KIT, "Kit", Icons.Outlined.Handyman, "tab_kit")
  )

  Surface(
    modifier = modifier
      .fillMaxWidth()
      .navigationBarsPadding(),
    color = CharcoalBackground,
    tonalElevation = 8.dp
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .height(64.dp)
        .padding(horizontal = 12.dp, vertical = 6.dp),
      horizontalArrangement = Arrangement.SpaceAround,
      verticalAlignment = Alignment.CenterVertically
    ) {
      tabs.forEach { item ->
        val isSelected = currentTab == item.tab
        val iconColor by animateColorAsState(
          targetValue = if (isSelected) AmberAccent else TextTertiary,
          animationSpec = tween(durationMillis = 180),
          label = "nav_icon_color"
        )
        val backgroundColor by animateColorAsState(
          targetValue = if (isSelected) Color(0xFF2E2416) else Color.Transparent,
          animationSpec = tween(durationMillis = 180),
          label = "nav_bg_color"
        )

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .clickable { onSelectTab(item.tab) }
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .testTag(item.tag),
          contentAlignment = Alignment.Center
        ) {
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
          ) {
            Icon(
              imageVector = item.icon,
              contentDescription = item.label,
              tint = iconColor,
              modifier = Modifier.size(20.dp)
            )
            Text(
              text = item.label,
              fontFamily = InterFontFamily,
              fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
              fontSize = 11.sp,
              color = iconColor,
              modifier = Modifier.padding(top = 2.dp)
            )
          }
        }
      }
    }
  }
}
