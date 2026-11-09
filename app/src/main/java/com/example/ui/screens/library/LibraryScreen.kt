package com.example.ui.screens.library

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Forest
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.NightShelter
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.data.SurvivalKnowledgeBase
import com.example.model.GuideCategory
import com.example.model.GuideStep
import com.example.model.SurvivalGuide
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.CharcoalBackground
import com.example.ui.theme.CharcoalCardElevated
import com.example.ui.theme.EmergencyRed
import com.example.ui.theme.InterFontFamily
import com.example.ui.theme.TacticalOlive
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary
import kotlinx.coroutines.launch

@Composable
fun LibraryScreen(
  selectedGuideId: String? = null,
  onClearSelectedGuide: () -> Unit,
  modifier: Modifier = Modifier
) {
  var searchQuery by remember { mutableStateOf("") }
  var activeGuide by remember(selectedGuideId) {
    mutableStateOf(SurvivalKnowledgeBase.guides.firstOrNull { it.id == selectedGuideId })
  }

  // Handle Android Back when viewing a step-by-step guide
  if (activeGuide != null) {
    BackHandler {
      activeGuide = null
      onClearSelectedGuide()
    }
  }

  AnimatedContent(
    targetState = activeGuide,
    label = "library_step_transition"
  ) { guide ->
    if (guide != null) {
      StepByStepGuideViewer(
        guide = guide,
        onBack = {
          activeGuide = null
          onClearSelectedGuide()
        }
      )
    } else {
      LibraryCategoriesGrid(
        searchQuery = searchQuery,
        onSearchChange = { searchQuery = it },
        onSelectGuide = { g -> activeGuide = g }
      )
    }
  }
}

/**
 * Biblioteca com Grid moderna e ícones bem intuitivos
 */
@Composable
private fun LibraryCategoriesGrid(
  searchQuery: String,
  onSearchChange: (String) -> Unit,
  onSelectGuide: (SurvivalGuide) -> Unit
) {
  val allGuides = SurvivalKnowledgeBase.guides

  val filteredGuides = remember(searchQuery) {
    if (searchQuery.isBlank()) allGuides
    else allGuides.filter {
      it.title.contains(searchQuery, ignoreCase = true) ||
        it.category.displayName.contains(searchQuery, ignoreCase = true) ||
        it.summary.contains(searchQuery, ignoreCase = true)
    }
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(CharcoalBackground)
      .padding(horizontal = 16.dp)
      .testTag("library_screen")
  ) {
    Spacer(modifier = Modifier.height(12.dp))

    // Search bar at the top
    Surface(
      shape = RoundedCornerShape(20.dp),
      color = CharcoalCardElevated,
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
          onValueChange = onSearchChange,
          placeholder = {
            Text(
              text = "Buscar em manuais e protocolos...",
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
            .testTag("library_search_field")
        )

        if (searchQuery.isNotBlank()) {
          IconButton(
            onClick = { onSearchChange("") },
            modifier = Modifier.size(32.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "Limpar",
              tint = TextSecondary,
              modifier = Modifier.size(16.dp)
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    if (searchQuery.isBlank()) {
      // 2-Column Grid with highly intuitive icons
      LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 24.dp),
        modifier = Modifier.fillMaxSize()
      ) {
        item(span = { GridItemSpan(2) }) {
          Text(
            text = "PROTOCOLOS E MANUAIS ESSENCIAIS",
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 11.sp,
            letterSpacing = 1.2.sp,
            color = TextTertiary,
            modifier = Modifier.padding(start = 2.dp, bottom = 4.dp)
          )
        }

        items(GuideCategory.values()) { category ->
          val guidesInCategory = allGuides.filter { it.category == category }
          CategoryGridCard(
            category = category,
            guides = guidesInCategory,
            onSelectGuide = onSelectGuide
          )
        }
      }
    } else {
      // Search results list
      LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
      ) {
        item {
          Text(
            text = "${filteredGuides.size} resultado(s) encontrado(s)",
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 11.sp,
            color = TextTertiary,
            modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
          )
        }
        items(filteredGuides, key = { it.id }) { guide ->
          GuideItemCard(
            guide = guide,
            onClick = { onSelectGuide(guide) }
          )
        }
      }
    }
  }
}

/**
 * Retorna o ícone bem intuitivo para cada categoria da Biblioteca
 */
fun getCategoryIntuitiveIcon(category: GuideCategory): ImageVector {
  return when (category) {
    GuideCategory.FIRST_AID -> Icons.Filled.MedicalServices
    GuideCategory.WATER -> Icons.Filled.WaterDrop
    GuideCategory.FIRE -> Icons.Filled.LocalFireDepartment
    GuideCategory.SHELTER -> Icons.Filled.NightShelter
    GuideCategory.ORIENTATION -> Icons.Filled.Explore
    GuideCategory.FAUNA_FLORA -> Icons.Filled.Forest
  }
}

/**
 * Card em Grid para categoria com ícone expressivo e intuitivo
 */
@Composable
private fun CategoryGridCard(
  category: GuideCategory,
  guides: List<SurvivalGuide>,
  onSelectGuide: (SurvivalGuide) -> Unit
) {
  val primaryGuide = guides.firstOrNull()
  val icon = getCategoryIntuitiveIcon(category)

  val iconColor = when (category) {
    GuideCategory.FIRST_AID -> EmergencyRed
    GuideCategory.WATER -> TacticalOlive
    GuideCategory.FIRE -> AmberAccent
    GuideCategory.SHELTER -> AmberAccent
    GuideCategory.ORIENTATION -> TacticalOlive
    GuideCategory.FAUNA_FLORA -> TacticalOlive
  }

  val iconBgColor = when (category) {
    GuideCategory.FIRST_AID -> Color(0xFF331414)
    GuideCategory.WATER -> Color(0xFF1B2B22)
    GuideCategory.FIRE -> Color(0xFF332414)
    GuideCategory.SHELTER -> Color(0xFF2E2517)
    GuideCategory.ORIENTATION -> Color(0xFF1E2A20)
    GuideCategory.FAUNA_FLORA -> Color(0xFF1A291E)
  }

  Surface(
    shape = RoundedCornerShape(20.dp),
    color = CharcoalCardElevated,
    modifier = Modifier
      .fillMaxWidth()
      .height(160.dp)
      .clip(RoundedCornerShape(20.dp))
      .clickable {
        if (primaryGuide != null) {
          onSelectGuide(primaryGuide)
        }
      }
      .border(0.6.dp, Color(0xFF28342D), RoundedCornerShape(20.dp))
      .testTag("library_grid_${category.name.lowercase()}")
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
        // Intuitive Icon Badge
        Box(
          modifier = Modifier
            .size(46.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(iconBgColor),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = icon,
            contentDescription = category.displayName,
            tint = iconColor,
            modifier = Modifier.size(24.dp)
          )
        }

        Surface(
          shape = RoundedCornerShape(8.dp),
          color = Color(0xFF19221C)
        ) {
          Text(
            text = "${guides.size} ${if (guides.size == 1) "guia" else "guias"}",
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 10.sp,
            color = iconColor,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
          )
        }
      }

      Column {
        Text(
          text = category.displayName,
          fontFamily = InterFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 14.sp,
          color = TextPrimary,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
        Text(
          text = category.subtitle,
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

@Composable
private fun GuideItemCard(
  guide: SurvivalGuide,
  onClick: () -> Unit
) {
  val icon = getCategoryIntuitiveIcon(guide.category)

  Surface(
    shape = RoundedCornerShape(18.dp),
    color = CharcoalCardElevated,
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() }
  ) {
    Row(
      modifier = Modifier.padding(16.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(42.dp)
          .clip(CircleShape)
          .background(Color(0xFF26322A)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = AmberAccent,
          modifier = Modifier.size(20.dp)
        )
      }

      Spacer(modifier = Modifier.width(14.dp))

      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = guide.category.displayName.uppercase(),
          fontFamily = InterFontFamily,
          fontWeight = FontWeight.SemiBold,
          fontSize = 10.sp,
          letterSpacing = 1.sp,
          color = AmberAccent
        )
        Text(
          text = guide.title,
          fontFamily = InterFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 14.sp,
          color = TextPrimary,
          modifier = Modifier.padding(top = 2.dp)
        )
        Text(
          text = guide.summary,
          fontFamily = InterFontFamily,
          fontWeight = FontWeight.Normal,
          fontSize = 12.sp,
          color = TextSecondary,
          lineHeight = 16.sp,
          modifier = Modifier.padding(top = 2.dp)
        )
      }
    }
  }
}

/**
 * Tela de Guia: passos curtos, um por vez, deslizando (Swipeable Horizontal Pager)
 */
@Composable
private fun StepByStepGuideViewer(
  guide: SurvivalGuide,
  onBack: () -> Unit
) {
  val pagerState = rememberPagerState(pageCount = { guide.steps.size })
  val coroutineScope = rememberCoroutineScope()
  val icon = getCategoryIntuitiveIcon(guide.category)

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(CharcoalBackground)
      .padding(horizontal = 20.dp, vertical = 12.dp)
      .testTag("step_by_step_guide_viewer")
  ) {
    // Navigation top bar
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      IconButton(
        onClick = onBack,
        modifier = Modifier.testTag("guide_back_button")
      ) {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowBack,
          contentDescription = "Voltar à biblioteca",
          tint = TextPrimary
        )
      }

      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = AmberAccent,
          modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "Passo ${pagerState.currentPage + 1} de ${guide.steps.size}",
          fontFamily = InterFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 13.sp,
          color = TextPrimary
        )
      }

      Spacer(modifier = Modifier.size(48.dp)) // balance
    }

    Spacer(modifier = Modifier.height(8.dp))

    // Step dots indicator
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp),
      horizontalArrangement = Arrangement.Center,
      verticalAlignment = Alignment.CenterVertically
    ) {
      repeat(guide.steps.size) { index ->
        val isCurrent = pagerState.currentPage == index
        Box(
          modifier = Modifier
            .padding(horizontal = 4.dp)
            .height(4.dp)
            .width(if (isCurrent) 24.dp else 8.dp)
            .clip(CircleShape)
            .background(if (isCurrent) AmberAccent else Color(0xFF28342D))
        )
      }
    }

    // Optional Priority Warning
    if (guide.priorityWarning != null) {
      Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFF2B1616),
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 8.dp)
      ) {
        Row(
          modifier = Modifier.padding(12.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = null,
            tint = EmergencyRed,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(10.dp))
          Text(
            text = guide.priorityWarning,
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 11.sp,
            color = Color(0xFFFFCDD2),
            lineHeight = 15.sp
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Horizontal Pager: short steps, one at a time, sliding smoothly
    HorizontalPager(
      state = pagerState,
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth()
    ) { pageIndex ->
      val step = guide.steps[pageIndex]
      SingleStepSlideCard(step = step, totalSteps = guide.steps.size)
    }

    // Bottom Navigation Buttons
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 12.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      if (pagerState.currentPage > 0) {
        OutlinedButton(
          onClick = {
            coroutineScope.launch {
              pagerState.animateScrollToPage(pagerState.currentPage - 1)
            }
          },
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier.height(48.dp)
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = null,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Anterior",
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 13.sp,
            color = TextPrimary
          )
        }
      } else {
        Spacer(modifier = Modifier.size(48.dp))
      }

      if (pagerState.currentPage < guide.steps.size - 1) {
        Button(
          onClick = {
            coroutineScope.launch {
              pagerState.animateScrollToPage(pagerState.currentPage + 1)
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = AmberAccent),
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier.height(48.dp)
        ) {
          Text(
            text = "Próximo Passo",
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            color = Color(0xFF1E1405)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            tint = Color(0xFF1E1405),
            modifier = Modifier.size(16.dp)
          )
        }
      } else {
        Button(
          onClick = onBack,
          colors = ButtonDefaults.buttonColors(containerColor = TacticalOlive),
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier.height(48.dp)
        ) {
          Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = Color(0xFF131A15),
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Concluir Guia",
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = Color(0xFF131A15)
          )
        }
      }
    }
  }
}

@Composable
private fun SingleStepSlideCard(
  step: GuideStep,
  totalSteps: Int
) {
  Surface(
    shape = RoundedCornerShape(24.dp),
    color = CharcoalCardElevated,
    modifier = Modifier
      .fillMaxSize()
      .padding(vertical = 4.dp)
      .border(0.8.dp, Color(0xFF28342D), RoundedCornerShape(24.dp))
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(24.dp),
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      Column {
        // Step number badge
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = Color(0xFF2C2417)
        ) {
          Text(
            text = "PASSO ${step.stepNumber} DE $totalSteps",
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            letterSpacing = 1.sp,
            color = AmberAccent,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
          )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
          text = step.title,
          fontFamily = InterFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 20.sp,
          color = TextPrimary,
          lineHeight = 26.sp
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(
          text = step.instruction,
          fontFamily = InterFontFamily,
          fontWeight = FontWeight.Normal,
          fontSize = 15.sp,
          color = TextSecondary,
          lineHeight = 23.sp
        )
      }

      Column {
        if (step.crucialTip != null) {
          Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFF1B231E),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              Text(
                text = "DICA CRUCIAL DE CAMPO",
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                letterSpacing = 0.8.sp,
                color = TacticalOlive
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = step.crucialTip,
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                color = Color(0xFFD6DFD9),
                lineHeight = 17.sp
              )
            }
          }
          Spacer(modifier = Modifier.height(12.dp))
        }

        if (step.checklistItem != null) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 4.dp)
          ) {
            Icon(
              imageVector = Icons.Default.CheckCircle,
              contentDescription = null,
              tint = TacticalOlive,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = step.checklistItem,
              fontFamily = InterFontFamily,
              fontWeight = FontWeight.Medium,
              fontSize = 12.sp,
              color = TextPrimary
            )
          }
        }
      }
    }
  }
}
