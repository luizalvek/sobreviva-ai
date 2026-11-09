package com.example.ui.components

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.EmergencyRed
import com.example.ui.theme.InterFontFamily
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Botão SOS discreto em tom neutro.
 * Exige toque contínuo de 2 segundos para abrir o Modo de Emergência, prevenindo acionamento acidental.
 */
@Composable
fun SosButton(
  modifier: Modifier = Modifier,
  size: Dp = 44.dp,
  onSosTriggered: () -> Unit,
  onSingleTapHint: (() -> Unit)? = null
) {
  val context = LocalContext.current
  var isPressing by remember { mutableStateOf(false) }
  var pressProgress by remember { mutableFloatStateOf(0f) }
  val coroutineScope = rememberCoroutineScope()

  val animatedProgress by animateFloatAsState(
    targetValue = pressProgress,
    animationSpec = tween(durationMillis = 50),
    label = "sos_press_progress"
  )

  fun triggerHaptic(durationMs: Long = 60) {
    try {
      val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
      if (vibrator != null && vibrator.hasVibrator()) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
          vibrator.vibrate(VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
          @Suppress("DEPRECATION")
          vibrator.vibrate(durationMs)
        }
      }
    } catch (_: Exception) {}
  }

  LaunchedEffect(isPressing) {
    if (isPressing) {
      val startTime = System.currentTimeMillis()
      val targetDuration = 2000L
      while (isPressing && (System.currentTimeMillis() - startTime) < targetDuration) {
        val elapsed = System.currentTimeMillis() - startTime
        pressProgress = (elapsed.toFloat() / targetDuration).coerceIn(0f, 1f)
        delay(30)
      }
      if (isPressing && pressProgress >= 0.98f) {
        triggerHaptic(200)
        pressProgress = 0f
        isPressing = false
        onSosTriggered()
      }
    } else {
      pressProgress = 0f
    }
  }

  Box(
    modifier = modifier
      .size(size)
      .testTag("sos_emergency_button")
      .pointerInput(Unit) {
        detectTapGestures(
          onPress = {
            isPressing = true
            triggerHaptic(30)
            tryAwaitRelease()
            isPressing = false
            pressProgress = 0f
          },
          onTap = {
            onSingleTapHint?.invoke()
          }
        )
      },
    contentAlignment = Alignment.Center
  ) {
    // Canvas for subtle neutral circle + charging emergency arc
    Canvas(modifier = Modifier.size(size)) {
      val w = this.size.width
      val h = this.size.height
      val radius = (w / 2f) - 4.dp.toPx()

      // Subtle neutral background ring
      drawCircle(
        color = Color(0xFF262F29),
        radius = radius,
        center = Offset(w / 2f, h / 2f),
        style = Stroke(width = 2.dp.toPx())
      )

      // Inner soft neutral fill
      drawCircle(
        color = Color(0xFF1E2621),
        radius = radius - 1.dp.toPx(),
        center = Offset(w / 2f, h / 2f)
      )

      // If pressing, draw progress arc in Emergency Red
      if (animatedProgress > 0f) {
        val strokeW = 3.5.dp.toPx()
        drawArc(
          color = EmergencyRed,
          startAngle = -90f,
          sweepAngle = animatedProgress * 360f,
          useCenter = false,
          topLeft = Offset(4.dp.toPx(), 4.dp.toPx()),
          size = Size(w - 8.dp.toPx(), h - 8.dp.toPx()),
          style = Stroke(width = strokeW, cap = StrokeCap.Round)
        )
      }
    }

    Text(
      text = "SOS",
      fontFamily = InterFontFamily,
      fontWeight = FontWeight.Bold,
      fontSize = 11.sp,
      letterSpacing = 1.sp,
      color = if (animatedProgress > 0.3f) EmergencyRed else TextSecondary
    )
  }
}
