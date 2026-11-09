package com.example.ui.screens.kit

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BreadcrumbPoint
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.CharcoalBackground
import com.example.ui.theme.CharcoalCardElevated
import com.example.ui.theme.EmergencyRed
import com.example.ui.theme.InterFontFamily
import com.example.ui.theme.TacticalOlive
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

@Composable
fun KitToolDetail(
  toolType: KitToolType,
  isTorchActive: Boolean,
  onToggleTorch: () -> Unit,
  onBack: () -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(CharcoalBackground)
      .padding(horizontal = 16.dp, vertical = 8.dp)
      .testTag("kit_tool_detail_${toolType.name.lowercase()}")
  ) {
    // Top bar with Back
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically
    ) {
      IconButton(onClick = onBack) {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowBack,
          contentDescription = "Voltar",
          tint = TextPrimary
        )
      }
      Spacer(modifier = Modifier.width(8.dp))
      Text(
        text = when (toolType) {
          KitToolType.COMPASS -> "BÚSSOLA DE CAMPO"
          KitToolType.HEALTH -> "BIOMETRIA & SAÚDE"
          KitToolType.SUPPLEMENT_CALC -> "RAÇÃO & SUPLEMENTO"
          KitToolType.BAROMETER -> "BARÔMETRO & ALTÍMETRO"
          KitToolType.SUN_LIGHT -> "SOL & LUZ NATURAL"
          KitToolType.BREADCRUMB_TRAIL -> "TRILHA DE VOLTA"
        },
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        letterSpacing = 1.sp,
        color = TextPrimary
      )
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Tool interactive content
    Box(
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth()
    ) {
      when (toolType) {
        KitToolType.COMPASS -> CompassToolView()
        KitToolType.HEALTH -> HealthBiometricsView()
        KitToolType.SUPPLEMENT_CALC -> SupplementCalcView()
        KitToolType.BAROMETER -> BarometerToolView()
        KitToolType.SUN_LIGHT -> SunLightToolView()
        KitToolType.BREADCRUMB_TRAIL -> BreadcrumbToolView()
      }
    }
  }
}

// 1. Compass Tool
@Composable
private fun CompassToolView() {
  var headingDegrees by remember { mutableFloatStateOf(32f) }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState()),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Surface(
      shape = RoundedCornerShape(20.dp),
      color = CharcoalCardElevated,
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(
        modifier = Modifier.padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text(
          text = "${headingDegrees.toInt()}°",
          fontFamily = InterFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 44.sp,
          color = TextPrimary
        )
        Text(
          text = "NNE • NORTE-NORDESTE",
          fontFamily = InterFontFamily,
          fontWeight = FontWeight.SemiBold,
          fontSize = 13.sp,
          letterSpacing = 1.sp,
          color = AmberAccent
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Compass Rose Canvas
        Box(
          modifier = Modifier.size(240.dp),
          contentAlignment = Alignment.Center
        ) {
          Canvas(
            modifier = Modifier
              .size(240.dp)
              .rotate(-headingDegrees)
          ) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val radius = size.width / 2f - 12.dp.toPx()

            // Outer ring
            drawCircle(
              color = Color(0xFF26322A),
              radius = radius,
              center = center,
              style = Stroke(width = 2.dp.toPx())
            )

            // Degree tick marks (every 10 degrees)
            for (deg in 0 until 360 step 10) {
              val rad = Math.toRadians(deg.toDouble())
              val isCardinal = deg % 90 == 0
              val tickLen = if (isCardinal) 14.dp.toPx() else 6.dp.toPx()
              val outerX = center.x + (radius * sin(rad)).toFloat()
              val outerY = center.y - (radius * cos(rad)).toFloat()
              val innerX = center.x + ((radius - tickLen) * sin(rad)).toFloat()
              val innerY = center.y - ((radius - tickLen) * cos(rad)).toFloat()

              drawLine(
                color = if (isCardinal) AmberAccent else Color(0xFF3B4840),
                start = Offset(innerX, innerY),
                end = Offset(outerX, outerY),
                strokeWidth = if (isCardinal) 2.dp.toPx() else 1.dp.toPx()
              )
            }
          }

          // Fixed Pointer Needle (North Amber, South Dark)
          Canvas(modifier = Modifier.size(240.dp)) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val needleLen = size.width * 0.35f
            val halfW = 10.dp.toPx()

            // North Pointer (Amber)
            val northPath = Path().apply {
              moveTo(center.x, center.y - needleLen)
              lineTo(center.x - halfW, center.y)
              lineTo(center.x + halfW, center.y)
              close()
            }
            drawPath(path = northPath, color = AmberAccent)

            // South Pointer (Muted)
            val southPath = Path().apply {
              moveTo(center.x, center.y + needleLen)
              lineTo(center.x - halfW, center.y)
              lineTo(center.x + halfW, center.y)
              close()
            }
            drawPath(path = southPath, color = Color(0xFF38433C))

            // Center pivot
            drawCircle(color = Color(0xFF161917), radius = 6.dp.toPx(), center = center)
            drawCircle(color = AmberAccent, radius = 3.dp.toPx(), center = center)
          }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Azimuth adjustment slider
        Text(
          text = "Calibração e Rumo de Mira",
          fontFamily = InterFontFamily,
          fontWeight = FontWeight.Medium,
          fontSize = 11.sp,
          color = TextTertiary
        )
        Slider(
          value = headingDegrees,
          onValueChange = { headingDegrees = it },
          valueRange = 0f..360f,
          colors = SliderDefaults.colors(
            thumbColor = AmberAccent,
            activeTrackColor = AmberAccent,
            inactiveTrackColor = Color(0xFF26322A)
          ),
          modifier = Modifier.padding(horizontal = 16.dp)
        )
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Information card
    Surface(
      shape = RoundedCornerShape(16.dp),
      color = CharcoalCardElevated,
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Text(
          text = "DECLINAÇÃO MAGNÉTICA",
          fontFamily = InterFontFamily,
          fontWeight = FontWeight.SemiBold,
          fontSize = 10.sp,
          letterSpacing = 1.sp,
          color = TacticalOlive
        )
        Text(
          text = "No Brasil a declinação magnética varia em torno de 20° a 24° Oeste. Ao transferir rumos da bússola para a carta topográfica impressa, some o valor da declinação ao azimute lido.",
          fontFamily = InterFontFamily,
          fontWeight = FontWeight.Normal,
          fontSize = 12.sp,
          color = TextSecondary,
          lineHeight = 17.sp,
          modifier = Modifier.padding(top = 4.dp)
        )
      }
    }
  }
}

// 2. Saúde & Biometria (Calcula IMC, TMB, FC Zona 2 e Prontidão de Campo)
@Composable
private fun HealthBiometricsView() {
  var weightKg by remember { mutableIntStateOf(72) }
  var heightCm by remember { mutableIntStateOf(175) }
  var ageYears by remember { mutableIntStateOf(28) }

  // Cálculos técnicos
  val heightM = heightCm / 100f
  val imc = weightKg / (heightM * heightM)
  val imcFormatted = String.format(java.util.Locale.US, "%.1f", imc)

  // Status do IMC
  val (imcCategory, imcColor) = when {
    imc < 18.5f -> Pair("Abaixo do peso ideal para carga", AmberAccent)
    imc in 18.5f..24.9f -> Pair("Peso Ideal • Excelente para montanha", TacticalOlive)
    imc in 25.0f..29.9f -> Pair("Sobrepeso leve • Bom rendimento", AmberAccent)
    else -> Pair("Atenção • Cuidado com articulações e carga", EmergencyRed)
  }

  // Frequência Cardíaca Máxima e Zona 2 (Marcha contínua sustentável)
  val fcMax = 220 - ageYears
  val fcZona2Min = (fcMax * 0.60f).roundToInt()
  val fcZona2Max = (fcMax * 0.72f).roundToInt()

  // Taxa Metabólica Basal aproximada (Mifflin-St Jeor)
  val tmb = (10 * weightKg) + (6.25f * heightCm) - (5 * ageYears) + 5
  val waterMinBasal = (weightKg * 35f) / 1000f

  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState()),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // Painel de Resultados Técnicos
    Surface(
      shape = RoundedCornerShape(22.dp),
      color = CharcoalCardElevated,
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(
        modifier = Modifier.padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text(
          text = "ÍNDICE DE MASSA CORPORAL (IMC)",
          fontFamily = InterFontFamily,
          fontWeight = FontWeight.SemiBold,
          fontSize = 11.sp,
          letterSpacing = 1.sp,
          color = TextTertiary
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
          text = imcFormatted,
          fontFamily = InterFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 46.sp,
          color = TextPrimary
        )

        Surface(
          shape = RoundedCornerShape(10.dp),
          color = Color(0xFF1E2A20),
          modifier = Modifier.padding(top = 4.dp)
        ) {
          Text(
            text = imcCategory,
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 11.sp,
            color = imcColor,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
          )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Grid com 3 métricas técnicas vitais
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceAround
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Marcha Zona 2", fontFamily = InterFontFamily, fontSize = 11.sp, color = TextTertiary)
            Text(text = "$fcZona2Min-$fcZona2Max bpm", fontFamily = InterFontFamily, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TacticalOlive)
            Text(text = "Ritmo aeróbico", fontFamily = InterFontFamily, fontSize = 9.sp, color = TextTertiary)
          }

          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Gasto em Repouso", fontFamily = InterFontFamily, fontSize = 11.sp, color = TextTertiary)
            Text(text = "${tmb.roundToInt()} kcal", fontFamily = InterFontFamily, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = AmberAccent)
            Text(text = "TMB diária", fontFamily = InterFontFamily, fontSize = 9.sp, color = TextTertiary)
          }

          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Água Basal", fontFamily = InterFontFamily, fontSize = 11.sp, color = TextTertiary)
            Text(text = String.format(java.util.Locale.US, "%.1f L", waterMinBasal), fontFamily = InterFontFamily, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextPrimary)
            Text(text = "Sem esforço", fontFamily = InterFontFamily, fontSize = 9.sp, color = TextTertiary)
          }
        }
      }
    }

    // Controles Deslizantes Rápidos
    Surface(
      shape = RoundedCornerShape(20.dp),
      color = CharcoalCardElevated,
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(18.dp)) {
        Text(
          text = "PARÂMETROS CORPORAIS",
          fontFamily = InterFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 11.sp,
          letterSpacing = 1.sp,
          color = TacticalOlive
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Peso
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(text = "Peso", fontFamily = InterFontFamily, fontSize = 13.sp, color = TextPrimary)
          Text(text = "$weightKg kg", fontFamily = InterFontFamily, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = AmberAccent)
        }
        Slider(
          value = weightKg.toFloat(),
          onValueChange = { weightKg = it.toInt() },
          valueRange = 45f..120f,
          steps = 74,
          colors = SliderDefaults.colors(thumbColor = AmberAccent, activeTrackColor = AmberAccent, inactiveTrackColor = Color(0xFF26322A))
        )

        // Altura
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(text = "Altura", fontFamily = InterFontFamily, fontSize = 13.sp, color = TextPrimary)
          Text(text = "$heightCm cm (${heightM}m)", fontFamily = InterFontFamily, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = AmberAccent)
        }
        Slider(
          value = heightCm.toFloat(),
          onValueChange = { heightCm = it.toInt() },
          valueRange = 140f..210f,
          steps = 69,
          colors = SliderDefaults.colors(thumbColor = AmberAccent, activeTrackColor = AmberAccent, inactiveTrackColor = Color(0xFF26322A))
        )

        // Idade
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(text = "Idade", fontFamily = InterFontFamily, fontSize = 13.sp, color = TextPrimary)
          Text(text = "$ageYears anos (FC Max: $fcMax bpm)", fontFamily = InterFontFamily, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = AmberAccent)
        }
        Slider(
          value = ageYears.toFloat(),
          onValueChange = { ageYears = it.toInt() },
          valueRange = 16f..75f,
          steps = 58,
          colors = SliderDefaults.colors(thumbColor = AmberAccent, activeTrackColor = AmberAccent, inactiveTrackColor = Color(0xFF26322A))
        )
      }
    }

    // Dica de Campo Fisiológica
    Surface(
      shape = RoundedCornerShape(16.dp),
      color = Color(0xFF19241E),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(14.dp)) {
        Text(
          text = "DICA FISIOLÓGICA DE MARCHA",
          fontFamily = InterFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 10.sp,
          letterSpacing = 0.8.sp,
          color = TacticalOlive
        )
        Text(
          text = "Mantenha a frequência cardíaca na Zona 2 ($fcZona2Min a $fcZona2Max bpm) durante subidas prolongadas. Se você não conseguir conversar sem perder o fôlego, reduza o ritmo da passada para poupar glicogênio muscular e evitar exaustão precoce.",
          fontFamily = InterFontFamily,
          fontWeight = FontWeight.Normal,
          fontSize = 12.sp,
          color = Color(0xFFD4E0D8),
          lineHeight = 17.sp,
          modifier = Modifier.padding(top = 4.dp)
        )
      }
    }
  }
}

// 3. Suplemento & Ração de Campo (Útil, Intuitivo e Eficiente)
@Composable
private fun SupplementCalcView() {
  var trailDays by remember { mutableIntStateOf(3) }
  var hikingHoursDaily by remember { mutableIntStateOf(6) }
  var isSteepClimb by remember { mutableStateOf(true) }
  var isHotWeather by remember { mutableStateOf(false) }

  // Cálculos de alta utilidade prática para campo:
  val hourlyBurn = if (isSteepClimb) 420 else 280
  val dailyActiveCalories = 1650 + (hikingHoursDaily * hourlyBurn)
  val totalExpeditionCalories = dailyActiveCalories * trailDays

  // Hidratação de marcha
  val baseWaterLiters = 2.0f + (hikingHoursDaily * (if (isHotWeather) 0.35f else 0.22f))
  // Gramas de ração seca (alta densidade: ~4.8 kcal/g)
  val dryRationGramsPerDay = (dailyActiveCalories / 4.8f).roundToInt()
  val totalPackRationKg = String.format(java.util.Locale.US, "%.2f", (dryRationGramsPerDay * trailDays) / 1000f)

  // Sal / Eletrólitos necessários por dia para evitar câimbras
  val dailySodiumGrams = if (isHotWeather) 3.5f else 2.2f

  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState()),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // Resumo de Campo
    Surface(
      shape = RoundedCornerShape(22.dp),
      color = CharcoalCardElevated,
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(20.dp)) {
        Text(
          text = "RAÇÃO DIÁRIA & COMBUSTÍVEL",
          fontFamily = InterFontFamily,
          fontWeight = FontWeight.SemiBold,
          fontSize = 11.sp,
          letterSpacing = 1.sp,
          color = AmberAccent
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(text = "Calorias / Dia", fontFamily = InterFontFamily, fontSize = 11.sp, color = TextTertiary)
            Text(text = "$dailyActiveCalories kcal", fontFamily = InterFontFamily, fontWeight = FontWeight.Bold, fontSize = 24.sp, color = TextPrimary)
          }

          Column {
            Text(text = "Ração Seca", fontFamily = InterFontFamily, fontSize = 11.sp, color = TextTertiary)
            Text(text = "${dryRationGramsPerDay}g / dia", fontFamily = InterFontFamily, fontWeight = FontWeight.Bold, fontSize = 24.sp, color = AmberAccent)
          }

          Column {
            Text(text = "Água de Marcha", fontFamily = InterFontFamily, fontSize = 11.sp, color = TextTertiary)
            Text(text = String.format(java.util.Locale.US, "%.1f L", baseWaterLiters), fontFamily = InterFontFamily, fontWeight = FontWeight.Bold, fontSize = 24.sp, color = TacticalOlive)
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Surface(
          shape = RoundedCornerShape(12.dp),
          color = Color(0xFF1B231D),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Peso total de comida para $trailDays dias na mochila:",
              fontFamily = InterFontFamily,
              fontSize = 11.sp,
              color = TextSecondary
            )
            Text(
              text = "$totalPackRationKg kg",
              fontFamily = InterFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp,
              color = TacticalOlive
            )
          }
        }
      }
    }

    // Configuração Rápida
    Surface(
      shape = RoundedCornerShape(20.dp),
      color = CharcoalCardElevated,
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(18.dp)) {
        Text(
          text = "CONDIÇÕES DA EXPEDIÇÃO",
          fontFamily = InterFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 11.sp,
          letterSpacing = 1.sp,
          color = TextTertiary
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Dias
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(text = "Duração da travessia", fontFamily = InterFontFamily, fontSize = 13.sp, color = TextPrimary)
          Text(text = "$trailDays dias", fontFamily = InterFontFamily, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = AmberAccent)
        }
        Slider(
          value = trailDays.toFloat(),
          onValueChange = { trailDays = it.toInt() },
          valueRange = 1f..10f,
          steps = 8,
          colors = SliderDefaults.colors(thumbColor = AmberAccent, activeTrackColor = AmberAccent, inactiveTrackColor = Color(0xFF26322A))
        )

        // Horas de marcha
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(text = "Horas de caminhada diária", fontFamily = InterFontFamily, fontSize = 13.sp, color = TextPrimary)
          Text(text = "$hikingHoursDaily horas/dia", fontFamily = InterFontFamily, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = AmberAccent)
        }
        Slider(
          value = hikingHoursDaily.toFloat(),
          onValueChange = { hikingHoursDaily = it.toInt() },
          valueRange = 2f..10f,
          steps = 7,
          colors = SliderDefaults.colors(thumbColor = AmberAccent, activeTrackColor = AmberAccent, inactiveTrackColor = Color(0xFF26322A))
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Toggles rápidos para Subida Íngreme e Calor
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = if (isSteepClimb) Color(0xFF332714) else Color(0xFF1B221E),
            modifier = Modifier
              .weight(1f)
              .clickable { isSteepClimb = !isSteepClimb }
              .border(
                0.8.dp,
                if (isSteepClimb) AmberAccent else Color.Transparent,
                RoundedCornerShape(12.dp)
              )
          ) {
            Column(modifier = Modifier.padding(10.dp)) {
              Text(
                text = "Desnível / Subida",
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = if (isSteepClimb) AmberAccent else TextPrimary
              )
              Text(
                text = if (isSteepClimb) "+40% calorias" else "Terreno plano",
                fontFamily = InterFontFamily,
                fontSize = 10.sp,
                color = TextTertiary
              )
            }
          }

          Surface(
            shape = RoundedCornerShape(12.dp),
            color = if (isHotWeather) Color(0xFF332714) else Color(0xFF1B221E),
            modifier = Modifier
              .weight(1f)
              .clickable { isHotWeather = !isHotWeather }
              .border(
                0.8.dp,
                if (isHotWeather) AmberAccent else Color.Transparent,
                RoundedCornerShape(12.dp)
              )
          ) {
            Column(modifier = Modifier.padding(10.dp)) {
              Text(
                text = "Clima Quente / Sol",
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = if (isHotWeather) AmberAccent else TextPrimary
              )
              Text(
                text = if (isHotWeather) "+3.5g sódio" else "Clima ameno",
                fontFamily = InterFontFamily,
                fontSize = 10.sp,
                color = TextTertiary
              )
            }
          }
        }
      }
    }

    // Checklist Prático de Ração por Dia
    Surface(
      shape = RoundedCornerShape(20.dp),
      color = CharcoalCardElevated,
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(18.dp)) {
        Text(
          text = "CHECKLIST DIÁRIO DE SUPLEMENTAÇÃO",
          fontFamily = InterFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 11.sp,
          letterSpacing = 1.sp,
          color = TacticalOlive
        )

        Spacer(modifier = Modifier.height(10.dp))

        RationCheckItem(
          title = "Prevenção de Hipoglicemia (O 'Prego')",
          detail = "30g de carbo rápido/hora (fruta seca, mel, rapadura ou barra energética na subida)."
        )
        RationCheckItem(
          title = "Eletrólitos & Sódio (${dailySodiumGrams}g)",
          detail = "Adicione 1 pitada de sal ou pastilha isotônica na garrafa para evitar câimbras e hiponatremia."
        )
        RationCheckItem(
          title = "Almoço e Lanches de Marcha (~${(dryRationGramsPerDay * 0.45f).toInt()}g)",
          detail = "Oleaginosas (amendoim, castanhas), queijo curado ou charque de alta densidade calórica."
        )
        RationCheckItem(
          title = "Jantar de Recuperação (~${(dryRationGramsPerDay * 0.35f).toInt()}g)",
          detail = "Comida desidratada ou cuscuz/arroz com proteína para reconstrução muscular no repouso."
        )
      }
    }
  }
}

@Composable
private fun RationCheckItem(title: String, detail: String) {
  Row(
    modifier = Modifier.padding(vertical = 6.dp),
    verticalAlignment = Alignment.Top
  ) {
    Icon(
      imageVector = Icons.Default.Check,
      contentDescription = null,
      tint = TacticalOlive,
      modifier = Modifier
        .size(16.dp)
        .padding(top = 2.dp)
    )
    Spacer(modifier = Modifier.width(10.dp))
    Column {
      Text(
        text = title,
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        color = TextPrimary
      )
      Text(
        text = detail,
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp,
        color = TextSecondary,
        lineHeight = 15.sp
      )
    }
  }
}

// 4. Barometer Tool
@Composable
private fun BarometerToolView() {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState()),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    Surface(
      shape = RoundedCornerShape(22.dp),
      color = CharcoalCardElevated,
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(
        modifier = Modifier.padding(22.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text(
          text = "PRESSÃO ATMOSFÉRICA",
          fontFamily = InterFontFamily,
          fontWeight = FontWeight.SemiBold,
          fontSize = 11.sp,
          letterSpacing = 1.sp,
          color = TextTertiary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
          text = "1013.2",
          fontFamily = InterFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 46.sp,
          color = TextPrimary
        )
        Text(
          text = "hPa (Hectopascais) • ~1.00 ATM",
          fontFamily = InterFontFamily,
          fontWeight = FontWeight.Normal,
          fontSize = 13.sp,
          color = AmberAccent
        )

        Spacer(modifier = Modifier.height(18.dp))

        Surface(
          shape = RoundedCornerShape(12.dp),
          color = Color(0xFF1C2720)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.Check,
              contentDescription = null,
              tint = TacticalOlive,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "Tendência: ESTÁVEL (Tempo Firme nas próximas 8h)",
              fontFamily = InterFontFamily,
              fontWeight = FontWeight.SemiBold,
              fontSize = 12.sp,
              color = TacticalOlive
            )
          }
        }
      }
    }

    // Storm warning rule
    Surface(
      shape = RoundedCornerShape(18.dp),
      color = CharcoalCardElevated,
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(18.dp)) {
        Text(
          text = "REGRA BAROMÉTRICA DE TEMPESTADE",
          fontFamily = InterFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 10.sp,
          letterSpacing = 1.sp,
          color = AmberAccent
        )
        Text(
          text = "Uma queda superior a 3 hPa em um período de 3 horas indica a chegada iminente de frente fria ou temporal violento na montanha. Procure abrigo e desça de cumes expostos imediatamente.",
          fontFamily = InterFontFamily,
          fontWeight = FontWeight.Normal,
          fontSize = 12.sp,
          color = TextSecondary,
          lineHeight = 17.sp,
          modifier = Modifier.padding(top = 4.dp)
        )
      }
    }
  }
}

// 5. Sun & Natural Light Tool
@Composable
private fun SunLightToolView() {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState()),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    Surface(
      shape = RoundedCornerShape(22.dp),
      color = CharcoalCardElevated,
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(
        modifier = Modifier.padding(22.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Icon(
          imageVector = Icons.Default.WbSunny,
          contentDescription = null,
          tint = AmberAccent,
          modifier = Modifier.size(44.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
          text = "2h 45m",
          fontFamily = InterFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 42.sp,
          color = TextPrimary
        )

        Text(
          text = "Luz Natural Restante",
          fontFamily = InterFontFamily,
          fontWeight = FontWeight.SemiBold,
          fontSize = 13.sp,
          color = AmberAccent
        )

        Spacer(modifier = Modifier.height(18.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceAround
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Nascer do Sol", fontFamily = InterFontFamily, fontSize = 11.sp, color = TextTertiary)
            Text(text = "05:42", fontFamily = InterFontFamily, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextPrimary)
          }

          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Pôr do Sol (Poente)", fontFamily = InterFontFamily, fontSize = 11.sp, color = TextTertiary)
            Text(text = "18:24", fontFamily = InterFontFamily, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = AmberAccent)
          }
        }
      }
    }

    // Regra dos 4 Dedos Card
    Surface(
      shape = RoundedCornerShape(18.dp),
      color = CharcoalCardElevated,
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(18.dp)) {
        Text(
          text = "A REGRA DOS 4 DEDOS (MÉTODO PRIMITIVO)",
          fontFamily = InterFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 10.sp,
          letterSpacing = 1.sp,
          color = TacticalOlive
        )
        Text(
          text = "Estenda o braço com a mão na horizontal entre a base do sol e a linha do horizonte:\n\n• Cada dedo deitado equivale a ~15 minutos de sol.\n• Quatro dedos juntos equivalem a 1 hora exata.\n\nMonte sua barraca e acenda o fogo antes que reste menos de 2 dedos de luz.",
          fontFamily = InterFontFamily,
          fontWeight = FontWeight.Normal,
          fontSize = 12.sp,
          color = TextSecondary,
          lineHeight = 18.sp,
          modifier = Modifier.padding(top = 6.dp)
        )
      }
    }
  }
}

// 6. Breadcrumb Trail Tool
@Composable
private fun BreadcrumbToolView() {
  val breadcrumbs = remember {
    mutableStateListOf(
      BreadcrumbPoint(label = "KM 0 - Estacionamento Base", lat = -22.4295, lng = -44.9450, isBaseCamp = true),
      BreadcrumbPoint(label = "Ponto 1 - Travessia de Rio", lat = -22.4210, lng = -44.9540),
      BreadcrumbPoint(label = "Ponto 2 - Mirante da Crista", lat = -22.4178, lng = -44.9585),
      BreadcrumbPoint(label = "Ponto 3 - Posição Atual", lat = -22.4132, lng = -44.9621)
    )
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState()),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    Surface(
      shape = RoundedCornerShape(20.dp),
      color = CharcoalCardElevated,
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(18.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "RUMO DE RETORNO À BASE",
              fontFamily = InterFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 10.sp,
              letterSpacing = 1.sp,
              color = AmberAccent
            )
            Text(
              text = "214° SSW • 2.4 km",
              fontFamily = InterFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 22.sp,
              color = TextPrimary
            )
          }

          Button(
            onClick = {
              breadcrumbs.add(
                BreadcrumbPoint(
                  label = "Ponto ${breadcrumbs.size} - Waypoint",
                  lat = -22.4132,
                  lng = -44.9621
                )
              )
            },
            colors = ButtonDefaults.buttonColors(containerColor = AmberAccent),
            shape = RoundedCornerShape(12.dp)
          ) {
            Icon(imageVector = Icons.Default.AddLocation, contentDescription = null, tint = Color(0xFF1E1405), modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = "Marcar", fontFamily = InterFontFamily, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFF1E1405))
          }
        }
      }
    }

    // List of Breadcrumb Waypoints
    Surface(
      shape = RoundedCornerShape(20.dp),
      color = CharcoalCardElevated,
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(18.dp)) {
        Text(
          text = "MIGALHAS DE PÃO REGISTRADAS",
          fontFamily = InterFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 10.sp,
          letterSpacing = 1.sp,
          color = TextTertiary
        )

        Spacer(modifier = Modifier.height(10.dp))

        breadcrumbs.reversed().forEachIndexed { index, pt ->
          Row(
            modifier = Modifier.padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.Navigation,
              contentDescription = null,
              tint = if (pt.isBaseCamp) TacticalOlive else AmberAccent,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = pt.label,
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                color = TextPrimary
              )
              Text(
                text = String.format(java.util.Locale.US, "Lat: %.4f, Lng: %.4f", pt.lat, pt.lng),
                fontFamily = InterFontFamily,
                fontSize = 11.sp,
                color = TextTertiary
              )
            }
          }
        }
      }
    }
  }
}
