package com.example.ui.screens.kit

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AltRoute
import androidx.compose.material.icons.filled.Compress
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Sos
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.CharcoalBackground
import com.example.ui.theme.CharcoalCardElevated
import com.example.ui.theme.EmergencyRed
import com.example.ui.theme.InterFontFamily
import com.example.ui.theme.TacticalOlive
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary

enum class KitToolType {
  COMPASS,
  HEALTH,
  SUPPLEMENT_CALC,
  BAROMETER,
  SUN_LIGHT,
  BREADCRUMB_TRAIL
}

data class KitToolItem(
  val type: KitToolType,
  val name: String,
  val telemetrySubtitle: String,
  val description: String,
  val icon: ImageVector,
  val tintColor: Color,
  val bgColor: Color
)

@Composable
fun KitScreen(
  onOpenSos: () -> Unit,
  isTorchActive: Boolean,
  onToggleTorch: () -> Unit,
  modifier: Modifier = Modifier
) {
  var activeTool by remember { mutableStateOf<KitToolType?>(null) }

  if (activeTool != null) {
    BackHandler {
      activeTool = null
    }
  }

  AnimatedContent(
    targetState = activeTool,
    label = "kit_tool_transition"
  ) { tool ->
    if (tool != null) {
      KitToolDetail(
        toolType = tool,
        isTorchActive = isTorchActive,
        onToggleTorch = onToggleTorch,
        onBack = { activeTool = null }
      )
    } else {
      KitToolsGrid(
        onSelectTool = { activeTool = it },
        onOpenSos = onOpenSos
      )
    }
  }
}

/**
 * Ferramentas em Grid com Saúde & IMC no lugar de lanterna e Suplemento prático
 */
@Composable
private fun KitToolsGrid(
  onSelectTool: (KitToolType) -> Unit,
  onOpenSos: () -> Unit
) {
  val toolItems = listOf(
    KitToolItem(
      type = KitToolType.COMPASS,
      name = "Bússola",
      telemetrySubtitle = "032° NNE",
      description = "Azimute em tempo real e rumo magnético",
      icon = Icons.Filled.Explore,
      tintColor = TacticalOlive,
      bgColor = Color(0xFF1E2A20)
    ),
    KitToolItem(
      type = KitToolType.HEALTH,
      name = "Saúde & IMC",
      telemetrySubtitle = "IMC 22.9 • FC 125",
      description = "Biometria, IMC, TMB e zona de marcha segura",
      icon = Icons.Filled.MonitorHeart,
      tintColor = TacticalOlive,
      bgColor = Color(0xFF1E2A20)
    ),
    KitToolItem(
      type = KitToolType.SUPPLEMENT_CALC,
      name = "Suplemento",
      telemetrySubtitle = "Ração & Eletrólitos",
      description = "Água, calorias, gramas de ração e sais de marcha",
      icon = Icons.Filled.Restaurant,
      tintColor = AmberAccent,
      bgColor = Color(0xFF2E2416)
    ),
    KitToolItem(
      type = KitToolType.BAROMETER,
      name = "Barômetro",
      telemetrySubtitle = "1013.2 hPa",
      description = "Pressão atmosférica e alerta de tempestade",
      icon = Icons.Filled.Compress,
      tintColor = TacticalOlive,
      bgColor = Color(0xFF1E2A20)
    ),
    KitToolItem(
      type = KitToolType.SUN_LIGHT,
      name = "Sol e Luz",
      telemetrySubtitle = "2h 45m restantes",
      description = "Horário de poente e regra dos 4 dedos",
      icon = Icons.Filled.WbSunny,
      tintColor = AmberAccent,
      bgColor = Color(0xFF2E2416)
    ),
    KitToolItem(
      type = KitToolType.BREADCRUMB_TRAIL,
      name = "Trilha de Volta",
      telemetrySubtitle = "6 waypoints",
      description = "Rumo de retorno seguro ao ponto de partida",
      icon = Icons.Filled.AltRoute,
      tintColor = TacticalOlive,
      bgColor = Color(0xFF1E2A20)
    )
  )

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(CharcoalBackground)
      .padding(horizontal = 16.dp)
      .testTag("kit_screen")
  ) {
    Spacer(modifier = Modifier.height(12.dp))

    // Header info
    Text(
      text = "INSTRUMENTOS DE CAMPO",
      fontFamily = InterFontFamily,
      fontWeight = FontWeight.SemiBold,
      fontSize = 11.sp,
      letterSpacing = 1.2.sp,
      color = TextTertiary,
      modifier = Modifier.padding(start = 2.dp, bottom = 12.dp)
    )

    // 2-Column Grid
    LazyVerticalGrid(
      columns = GridCells.Fixed(2),
      horizontalArrangement = Arrangement.spacedBy(12.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp),
      contentPadding = PaddingValues(bottom = 24.dp),
      modifier = Modifier.fillMaxSize()
    ) {
      items(toolItems) { tool ->
        ToolGridCard(
          tool = tool,
          onClick = { onSelectTool(tool.type) }
        )
      }

      // SOS também fica dentro do Kit (Span 2 colunas para destaque com segurança)
      item(span = { GridItemSpan(2) }) {
        Spacer(modifier = Modifier.height(4.dp))
        Surface(
          shape = RoundedCornerShape(20.dp),
          color = Color(0xFF281313),
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable { onOpenSos() }
            .border(1.dp, Color(0xFF541C1C), RoundedCornerShape(20.dp))
            .testTag("kit_sos_entry_row")
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(46.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0xFF451717)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Filled.Sos,
                contentDescription = "SOS",
                tint = EmergencyRed,
                modifier = Modifier.size(24.dp)
              )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "Modo Emergência (SOS)",
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = EmergencyRed
              )
              Text(
                text = "Coordenadas GPS, resgate (192, 193, 199) e SMS",
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 11.sp,
                color = Color(0xFFFFCDD2),
                lineHeight = 15.sp
              )
            }

            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowForward,
              contentDescription = null,
              tint = EmergencyRed,
              modifier = Modifier.size(18.dp)
            )
          }
        }
      }
    }
  }
}

/**
 * Card em Grid para ferramenta do Kit com ícone intuitivo
 */
@Composable
private fun ToolGridCard(
  tool: KitToolItem,
  onClick: () -> Unit
) {
  Surface(
    shape = RoundedCornerShape(20.dp),
    color = CharcoalCardElevated,
    modifier = Modifier
      .fillMaxWidth()
      .height(160.dp)
      .clip(RoundedCornerShape(20.dp))
      .clickable { onClick() }
      .border(0.6.dp, Color(0xFF28342D), RoundedCornerShape(20.dp))
      .testTag("tool_card_${tool.type.name.lowercase()}")
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
      ) {
        // Intuitive Tool Icon
        Box(
          modifier = Modifier
            .size(46.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(tool.bgColor),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = tool.icon,
            contentDescription = tool.name,
            tint = tool.tintColor,
            modifier = Modifier.size(24.dp)
          )
        }

        Surface(
          shape = RoundedCornerShape(8.dp),
          color = Color(0xFF19221C)
        ) {
          Text(
            text = tool.telemetrySubtitle,
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 10.sp,
            color = tool.tintColor,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
          )
        }
      }

      Column {
        Text(
          text = tool.name,
          fontFamily = InterFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 14.sp,
          color = TextPrimary,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
        Text(
          text = tool.description,
          fontFamily = InterFontFamily,
          fontWeight = FontWeight.Normal,
          fontSize = 11.sp,
          color = TextSecondary,
          maxLines = 2,
          overflow = TextOverflow.Ellipsis,
          lineHeight = 15.sp,
          modifier = Modifier.padding(top = 2.dp)
        )
      }
    }
  }
}
