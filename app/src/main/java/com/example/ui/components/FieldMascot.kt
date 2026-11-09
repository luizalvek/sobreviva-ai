package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.model.MascotState
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.EmergencyRed
import com.example.ui.theme.TacticalOlive

/**
 * Mascote minimalista e plano (Guará - Lobo-Guará de Campo)
 * Design moderno em arte vetorial geométrica plana, sem excesso de detalhes.
 */
@Composable
fun FieldMascot(
  modifier: Modifier = Modifier,
  size: Dp = 64.dp,
  state: MascotState = MascotState.CALM
) {
  val primaryColor = when (state) {
    MascotState.ALERT -> EmergencyRed
    MascotState.CALM -> AmberAccent
    MascotState.HELPING -> TacticalOlive
  }
  val innerColor = when (state) {
    MascotState.ALERT -> Color(0xFF5E1717)
    MascotState.CALM -> Color(0xFF2E2416)
    MascotState.HELPING -> Color(0xFF1E281C)
  }

  Canvas(modifier = modifier.size(size)) {
    val w = this.size.width
    val h = this.size.height

    // 1. Subtle aura halo
    drawCircle(
      color = primaryColor.copy(alpha = if (state == MascotState.ALERT) 0.25f else 0.12f),
      radius = w * 0.48f,
      center = Offset(w * 0.5f, h * 0.52f)
    )

    // 2. Geometric Face Base (Fox / Maned Wolf silhouette)
    val headPath = Path().apply {
      // Top center between ears
      moveTo(w * 0.5f, h * 0.32f)
      // Left ear tip
      lineTo(w * 0.22f, h * 0.14f)
      // Left cheek
      lineTo(w * 0.24f, h * 0.56f)
      // Chin / Snout point
      lineTo(w * 0.5f, h * 0.86f)
      // Right cheek
      lineTo(w * 0.76f, h * 0.56f)
      // Right ear tip
      lineTo(w * 0.78f, h * 0.14f)
      close()
    }

    drawPath(path = headPath, color = innerColor)
    drawPath(path = headPath, color = primaryColor, style = Stroke(width = w * 0.035f))

    // 3. Inner Ears
    val leftInnerEar = Path().apply {
      moveTo(w * 0.36f, h * 0.34f)
      lineTo(w * 0.26f, h * 0.20f)
      lineTo(w * 0.28f, h * 0.44f)
      close()
    }
    val rightInnerEar = Path().apply {
      moveTo(w * 0.64f, h * 0.34f)
      lineTo(w * 0.74f, h * 0.20f)
      lineTo(w * 0.72f, h * 0.44f)
      close()
    }
    drawPath(path = leftInnerEar, color = primaryColor.copy(alpha = 0.4f))
    drawPath(path = rightInnerEar, color = primaryColor.copy(alpha = 0.4f))

    // 4. Snout / Nose
    val nosePath = Path().apply {
      moveTo(w * 0.45f, h * 0.76f)
      lineTo(w * 0.55f, h * 0.76f)
      lineTo(w * 0.5f, h * 0.82f)
      close()
    }
    drawPath(path = nosePath, color = primaryColor)

    // 5. Eyes based on state
    when (state) {
      MascotState.CALM -> {
        // Serene, confident angled eye slits
        drawLine(
          color = primaryColor,
          start = Offset(w * 0.36f, h * 0.48f),
          end = Offset(w * 0.44f, h * 0.51f),
          strokeWidth = w * 0.04f
        )
        drawLine(
          color = primaryColor,
          start = Offset(w * 0.64f, h * 0.48f),
          end = Offset(w * 0.56f, h * 0.51f),
          strokeWidth = w * 0.04f
        )
      }
      MascotState.ALERT -> {
        // Wide vigilant alert eyes
        drawCircle(
          color = Color.White,
          radius = w * 0.065f,
          center = Offset(w * 0.39f, h * 0.49f)
        )
        drawCircle(
          color = EmergencyRed,
          radius = w * 0.038f,
          center = Offset(w * 0.39f, h * 0.49f)
        )

        drawCircle(
          color = Color.White,
          radius = w * 0.065f,
          center = Offset(w * 0.61f, h * 0.49f)
        )
        drawCircle(
          color = EmergencyRed,
          radius = w * 0.038f,
          center = Offset(w * 0.61f, h * 0.49f)
        )

        // Alert eyebrow marks
        drawLine(
          color = EmergencyRed,
          start = Offset(w * 0.33f, h * 0.41f),
          end = Offset(w * 0.45f, h * 0.44f),
          strokeWidth = w * 0.03f
        )
        drawLine(
          color = EmergencyRed,
          start = Offset(w * 0.67f, h * 0.41f),
          end = Offset(w * 0.55f, h * 0.44f),
          strokeWidth = w * 0.03f
        )
      }
      MascotState.HELPING -> {
        // Attentive friendly curved eyes
        drawArc(
          color = primaryColor,
          startAngle = 180f,
          sweepAngle = 180f,
          useCenter = false,
          topLeft = Offset(w * 0.35f, h * 0.47f),
          size = Size(w * 0.10f, w * 0.06f),
          style = Stroke(width = w * 0.035f)
        )
        drawArc(
          color = primaryColor,
          startAngle = 180f,
          sweepAngle = 180f,
          useCenter = false,
          topLeft = Offset(w * 0.55f, h * 0.47f),
          size = Size(w * 0.10f, w * 0.06f),
          style = Stroke(width = w * 0.035f)
        )
      }
    }
  }
}
