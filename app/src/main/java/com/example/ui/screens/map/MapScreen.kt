package com.example.ui.screens.map

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.MapWaypoint
import com.example.model.OfflineMapRegion
import com.example.model.WaypointType
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.CharcoalBackground
import com.example.ui.theme.CharcoalCard
import com.example.ui.theme.CharcoalCardElevated
import com.example.ui.theme.EmergencyRed
import com.example.ui.theme.InterFontFamily
import com.example.ui.theme.TacticalOlive
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
  userLat: Double = -22.4132,
  userLng: Double = -44.9621,
  waypoints: List<MapWaypoint>,
  regions: List<OfflineMapRegion>,
  onAskGuide: (query: String) -> Unit,
  onDownloadRegion: (regionId: String) -> Unit,
  modifier: Modifier = Modifier
) {
  var searchQuery by remember { mutableStateOf("") }
  var showControls by remember { mutableStateOf(true) }
  var showDownloadsSheet by remember { mutableStateOf(false) }
  var selectedWaypoint by remember { mutableStateOf<MapWaypoint?>(null) }
  var mapScale by remember { mutableFloatStateOf(1.0f) }
  var mapPanOffset by remember { mutableStateOf(Offset.Zero) }

  val coroutineScope = rememberCoroutineScope()
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  val filteredWaypoints = remember(searchQuery, waypoints) {
    if (searchQuery.isBlank()) waypoints
    else waypoints.filter { it.name.contains(searchQuery, ignoreCase = true) || it.description.contains(searchQuery, ignoreCase = true) }
  }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0xFF141916))
      .testTag("map_screen")
  ) {
    // 1. Clean Full-screen Vector Topographic Map Canvas
    Canvas(
      modifier = Modifier
        .fillMaxSize()
        .pointerInput(Unit) {
          detectTransformGestures { _, pan, zoom, _ ->
            mapScale = (mapScale * zoom).coerceIn(0.6f, 3.5f)
            mapPanOffset += pan
            showControls = true
          }
        }
    ) {
      val w = size.width
      val h = size.height
      val centerX = (w / 2f) + mapPanOffset.x
      val centerY = (h / 2f) + mapPanOffset.y

      // Tactical field topo grid lines
      val gridSpacing = 60.dp.toPx() * mapScale
      val startX = (mapPanOffset.x % gridSpacing)
      val startY = (mapPanOffset.y % gridSpacing)

      var x = startX
      while (x < w) {
        drawLine(
          color = Color(0xFF1C241F),
          start = Offset(x, 0f),
          end = Offset(x, h),
          strokeWidth = 1.dp.toPx()
        )
        x += gridSpacing
      }

      var y = startY
      while (y < h) {
        drawLine(
          color = Color(0xFF1C241F),
          start = Offset(0f, y),
          end = Offset(w, y),
          strokeWidth = 1.dp.toPx()
        )
        y += gridSpacing
      }

      // Contour elevation lines (Curvas de nível estilizadas em relevo)
      for (r in listOf(120f, 220f, 340f, 460f, 580f)) {
        val scaledRadius = r * mapScale
        val path = Path().apply {
          addOval(
            androidx.compose.ui.geometry.Rect(
              centerX - scaledRadius * 1.3f,
              centerY - scaledRadius * 0.9f,
              centerX + scaledRadius * 1.3f,
              centerY + scaledRadius * 0.9f
            )
          )
        }
        drawPath(
          path = path,
          color = Color(0xFF222C25),
          style = Stroke(width = 1.5.dp.toPx())
        )
      }

      // Main River stream path (Leito do Córrego)
      val riverPath = Path().apply {
        moveTo(centerX - 300f * mapScale, centerY + 450f * mapScale)
        cubicTo(
          centerX - 120f * mapScale, centerY + 200f * mapScale,
          centerX - 80f * mapScale, centerY - 60f * mapScale,
          centerX + 180f * mapScale, centerY - 400f * mapScale
        )
      }
      drawPath(
        path = riverPath,
        color = Color(0xFF2B473A),
        style = Stroke(width = 4.dp.toPx())
      )

      // Trail path (Trilha principal tracejada)
      val trailPath = Path().apply {
        moveTo(centerX - 240f * mapScale, centerY + 400f * mapScale)
        lineTo(centerX - 100f * mapScale, centerY + 180f * mapScale)
        lineTo(centerX, centerY)
        lineTo(centerX + 110f * mapScale, centerY - 140f * mapScale)
        lineTo(centerX + 260f * mapScale, centerY - 320f * mapScale)
      }
      drawPath(
        path = trailPath,
        color = AmberAccent.copy(alpha = 0.6f),
        style = Stroke(width = 2.dp.toPx())
      )

      // Draw waypoints on the map
      filteredWaypoints.forEachIndexed { index, wp ->
        // Relative mock offsets for visualization around current position
        val offsetX = when (wp.type) {
          WaypointType.WATER -> (index * 70f - 110f) * mapScale
          WaypointType.SHELTER -> 130f * mapScale
          WaypointType.CAMP -> -150f * mapScale
          WaypointType.TRAILHEAD -> -180f * mapScale
          WaypointType.HAZARD -> 180f * mapScale
        }
        val offsetY = when (wp.type) {
          WaypointType.WATER -> (index * -80f + 90f) * mapScale
          WaypointType.SHELTER -> -120f * mapScale
          WaypointType.CAMP -> 140f * mapScale
          WaypointType.TRAILHEAD -> 260f * mapScale
          WaypointType.HAZARD -> -220f * mapScale
        }

        val pointCenter = Offset(centerX + offsetX, centerY + offsetY)
        val pointColor = when (wp.type) {
          WaypointType.WATER -> TacticalOlive
          WaypointType.SHELTER -> AmberAccent
          WaypointType.HAZARD -> EmergencyRed
          else -> Color(0xFF6C8C7C)
        }

        // Waypoint ring
        drawCircle(
          color = pointColor.copy(alpha = 0.25f),
          radius = 16.dp.toPx(),
          center = pointCenter
        )
        drawCircle(
          color = pointColor,
          radius = 5.dp.toPx(),
          center = pointCenter
        )
      }

      // Draw User Current Position (Radar pulse & center dot)
      drawCircle(
        color = AmberAccent.copy(alpha = 0.18f),
        radius = 28.dp.toPx() * (1f + (mapScale * 0.1f)),
        center = Offset(centerX, centerY)
      )
      drawCircle(
        color = Color(0xFF161917),
        radius = 9.dp.toPx(),
        center = Offset(centerX, centerY)
      )
      drawCircle(
        color = AmberAccent,
        radius = 6.dp.toPx(),
        center = Offset(centerX, centerY)
      )

      // Heading directional pointer cone
      val headingPath = Path().apply {
        moveTo(centerX, centerY - 18.dp.toPx())
        lineTo(centerX - 6.dp.toPx(), centerY - 6.dp.toPx())
        lineTo(centerX + 6.dp.toPx(), centerY - 6.dp.toPx())
        close()
      }
      drawPath(path = headingPath, color = AmberAccent)
    }

    // 2. Floating Search Bar at the Top
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 12.dp)
        .align(Alignment.TopCenter)
    ) {
      Surface(
        shape = RoundedCornerShape(20.dp),
        color = CharcoalCard.copy(alpha = 0.95f),
        tonalElevation = 6.dp,
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            tint = TextSecondary,
            modifier = Modifier.size(20.dp)
          )

          OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = {
              Text(
                text = "Buscar água, abrigo, cume...",
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 13.sp,
                color = TextTertiary
              )
            },
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = Color.Transparent,
              unfocusedBorderColor = Color.Transparent,
              focusedTextColor = TextPrimary,
              unfocusedTextColor = TextPrimary,
              cursorColor = AmberAccent
            ),
            singleLine = true,
            modifier = Modifier
              .weight(1f)
              .testTag("map_search_field")
          )

          if (searchQuery.isNotBlank()) {
            IconButton(
              onClick = { searchQuery = "" },
              modifier = Modifier.size(32.dp)
            ) {
              Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Limpar busca",
                tint = TextSecondary,
                modifier = Modifier.size(16.dp)
              )
            }
          }

          // Offline Maps Manager button
          IconButton(
            onClick = { showDownloadsSheet = true },
            modifier = Modifier
              .size(36.dp)
              .testTag("open_map_downloads_button")
          ) {
            Icon(
              imageVector = Icons.Default.CloudDownload,
              contentDescription = "Baixar mapas de regiões",
              tint = TacticalOlive,
              modifier = Modifier.size(20.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // 3. Floating Guide Action: "Onde há água perto?"
      Surface(
        shape = RoundedCornerShape(24.dp),
        color = Color(0xFF1D2821).copy(alpha = 0.92f),
        modifier = Modifier
          .clip(RoundedCornerShape(24.dp))
          .clickable {
            onAskGuide("Onde há água perto?")
          }
          .border(0.8.dp, TacticalOlive.copy(alpha = 0.5f), RoundedCornerShape(24.dp))
          .testTag("ask_water_guide_button")
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
          Icon(
            imageVector = Icons.Default.WaterDrop,
            contentDescription = null,
            tint = TacticalOlive,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Guia: \"Onde há água perto?\"",
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            color = TextPrimary
          )
        }
      }
    }

    // 4. Contextual Controls (Appear when needed on the right edge)
    AnimatedVisibility(
      visible = showControls,
      enter = fadeIn(),
      exit = fadeOut(),
      modifier = Modifier
        .align(Alignment.CenterEnd)
        .padding(end = 16.dp)
    ) {
      Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        // Center on GPS
        MapControlIconButton(
          icon = Icons.Default.MyLocation,
          contentDescription = "Minha posição",
          onClick = {
            mapPanOffset = Offset.Zero
            mapScale = 1.0f
          }
        )

        // Zoom In
        MapControlIconButton(
          icon = Icons.Default.Add,
          contentDescription = "Zoom mais",
          onClick = {
            mapScale = (mapScale + 0.3f).coerceAtMost(3.5f)
          }
        )

        // Zoom Out
        MapControlIconButton(
          icon = Icons.Default.Remove,
          contentDescription = "Zoom menos",
          onClick = {
            mapScale = (mapScale - 0.3f).coerceAtLeast(0.6f)
          }
        )
      }
    }

    // Bottom telemetry strip (Coordinates, Altitude, Heading)
    Surface(
      shape = RoundedCornerShape(16.dp),
      color = CharcoalCard.copy(alpha = 0.90f),
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .padding(horizontal = 16.dp, vertical = 12.dp)
        .fillMaxWidth()
    ) {
      Row(
        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "POSIÇÃO GPS (WGS84)",
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 9.sp,
            letterSpacing = 0.8.sp,
            color = TextTertiary
          )
          Text(
            text = String.format(java.util.Locale.US, "%.4f, %.4f", userLat, userLng),
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            color = TextPrimary
          )
        }

        Column(horizontalAlignment = Alignment.End) {
          Text(
            text = "ALTITUDE / RUMO",
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 9.sp,
            letterSpacing = 0.8.sp,
            color = TextTertiary
          )
          Text(
            text = "2.120m | 032° NNE",
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            color = AmberAccent
          )
        }
      }
    }

    // 5. Offline Regions Bottom Sheet ("Local Para baixar mapas de regiões")
    if (showDownloadsSheet) {
      ModalBottomSheet(
        onDismissRequest = { showDownloadsSheet = false },
        sheetState = sheetState,
        containerColor = CharcoalBackground,
        dragHandle = { BottomSheetDefaults.DragHandle(color = Color(0xFF333E37)) }
      ) {
        OfflineMapsSheetContent(
          regions = regions,
          onDownload = { regionId ->
            onDownloadRegion(regionId)
          },
          onClose = {
            coroutineScope.launch {
              sheetState.hide()
              showDownloadsSheet = false
            }
          }
        )
      }
    }
  }
}

@Composable
private fun MapControlIconButton(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  contentDescription: String,
  onClick: () -> Unit
) {
  Surface(
    shape = CircleShape,
    color = CharcoalCard.copy(alpha = 0.92f),
    tonalElevation = 4.dp,
    modifier = Modifier
      .size(44.dp)
      .clip(CircleShape)
      .clickable { onClick() }
      .border(0.8.dp, Color(0xFF28342D), CircleShape)
  ) {
    Box(contentAlignment = Alignment.Center) {
      Icon(
        imageVector = icon,
        contentDescription = contentDescription,
        tint = TextPrimary,
        modifier = Modifier.size(18.dp)
      )
    }
  }
}

@Composable
private fun OfflineMapsSheetContent(
  regions: List<OfflineMapRegion>,
  onDownload: (String) -> Unit,
  onClose: () -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 20.dp, vertical = 8.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text(
          text = "Pacotes de Mapas Offline",
          fontFamily = InterFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 18.sp,
          color = TextPrimary
        )
        Text(
          text = "Baixe antes de sair para autonomia sem sinal",
          fontFamily = InterFontFamily,
          fontWeight = FontWeight.Normal,
          fontSize = 12.sp,
          color = TextTertiary
        )
      }

      IconButton(onClick = onClose) {
        Icon(
          imageVector = Icons.Default.Close,
          contentDescription = "Fechar",
          tint = TextSecondary
        )
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    LazyColumn(
      verticalArrangement = Arrangement.spacedBy(12.dp),
      modifier = Modifier.padding(bottom = 24.dp)
    ) {
      items(regions, key = { it.id }) { reg ->
        OfflineRegionCard(
          region = reg,
          onDownload = { onDownload(reg.id) }
        )
      }
    }
  }
}

@Composable
private fun OfflineRegionCard(
  region: OfflineMapRegion,
  onDownload: () -> Unit
) {
  Surface(
    shape = RoundedCornerShape(16.dp),
    color = CharcoalCardElevated,
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = region.name,
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp,
            color = TextPrimary
          )
          Text(
            text = region.subtitle,
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 11.sp,
            color = TextTertiary
          )
        }

        if (region.isDownloaded) {
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color(0xFF1E2E23)
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = TacticalOlive,
                modifier = Modifier.size(14.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "BAIXADO",
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                color = TacticalOlive
              )
            }
          }
        } else {
          Button(
            onClick = onDownload,
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AmberAccent),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 6.dp),
            modifier = Modifier.height(34.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Download,
              contentDescription = null,
              tint = Color(0xFF1F1504),
              modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "${region.sizeMb} MB",
              fontFamily = InterFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 11.sp,
              color = Color(0xFF1F1504)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        Text(
          text = "• ${region.trailsCount} trilhas",
          fontFamily = InterFontFamily,
          fontWeight = FontWeight.Normal,
          fontSize = 11.sp,
          color = TextSecondary
        )
        Text(
          text = "• ${region.waterPoints} nascentes",
          fontFamily = InterFontFamily,
          fontWeight = FontWeight.Normal,
          fontSize = 11.sp,
          color = TextSecondary
        )
        Text(
          text = "• Max ${region.elevationMax}m",
          fontFamily = InterFontFamily,
          fontWeight = FontWeight.Normal,
          fontSize = 11.sp,
          color = TextSecondary
        )
      }

      if (region.isDownloading) {
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
          progress = { region.downloadProgress },
          modifier = Modifier
            .fillMaxWidth()
            .height(4.dp)
            .clip(RoundedCornerShape(2.dp)),
          color = AmberAccent,
          trackColor = Color(0xFF263229),
        )
      }
    }
  }
}
