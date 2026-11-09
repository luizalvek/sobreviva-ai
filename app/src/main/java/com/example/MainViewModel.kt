package com.example

import android.app.Application
import android.content.Context
import android.hardware.camera2.CameraManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.OfflineMapsRepository
import com.example.data.SurvivalKnowledgeBase
import com.example.model.ChatMessage
import com.example.model.ChatSession
import com.example.model.OfflineMapRegion
import com.example.model.ScreenTab
import com.example.ui.components.DrawerDestination
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MainUiState(
  val currentTab: ScreenTab = ScreenTab.CHAT,
  val isEmergencyActive: Boolean = false,
  val isBatterySaverActive: Boolean = false,
  val isTorchActive: Boolean = false,
  val activeDrawerDestination: DrawerDestination = DrawerDestination.NONE,
  val selectedGuideId: String? = null,
  val userLat: Double = -22.41320,
  val userLng: Double = -44.96210,
  val elevationMeters: Int = 2120,
  val chatMessages: List<ChatMessage> = listOf(
    ChatMessage(
      content = "Saudações. Sou o seu guia de campo Vereda. Estou totalmente operacional offline para navegação, purificação de água, fogo, primeiros socorros e meteorologia. Como posso apoiar sua travessia agora?",
      isUser = false
    )
  ),
  val chatSessions: List<ChatSession> = listOf(
    ChatSession(
      title = "Travessia Marins-Itaguaré",
      lastMessage = "Pontos de água potável no cume",
      timestamp = System.currentTimeMillis() - 86400000L
    ),
    ChatSession(
      title = "Montagem de Tarp lean-to",
      lastMessage = "Ângulo de 45 graus para vento",
      timestamp = System.currentTimeMillis() - 172800000L
    )
  ),
  val mapRegions: List<OfflineMapRegion> = OfflineMapsRepository.regions
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

  private val _uiState = MutableStateFlow(MainUiState())
  val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

  fun setTab(tab: ScreenTab) {
    _uiState.update { it.copy(currentTab = tab) }
  }

  fun setEmergencyActive(active: Boolean) {
    _uiState.update { it.copy(isEmergencyActive = active) }
  }

  fun toggleBatterySaver(enabled: Boolean) {
    _uiState.update { it.copy(isBatterySaverActive = enabled) }
  }

  fun setDrawerDestination(dest: DrawerDestination) {
    _uiState.update { it.copy(activeDrawerDestination = dest) }
  }

  fun openGuide(guideId: String) {
    _uiState.update {
      it.copy(
        currentTab = ScreenTab.LIBRARY,
        selectedGuideId = guideId
      )
    }
  }

  fun clearSelectedGuide() {
    _uiState.update { it.copy(selectedGuideId = null) }
  }

  fun sendMessage(text: String, imageUri: String? = null) {
    val userMsg = ChatMessage(
      content = text,
      isUser = true,
      attachedImageUri = imageUri
    )

    // Check emergency context
    val emergencyAnalysis = SurvivalKnowledgeBase.detectEmergencyIntent(text)

    _uiState.update { state ->
      state.copy(chatMessages = state.chatMessages + userMsg)
    }

    viewModelScope.launch {
      delay(400) // Realistic responsive field intelligence delay

      val agentResponseContent = if (emergencyAnalysis.isEmergency) {
        emergencyAnalysis.responseText
      } else {
        SurvivalKnowledgeBase.generateGuideResponse(text)
      }

      val agentMsg = ChatMessage(
        content = agentResponseContent,
        isUser = false,
        isEmergencyDetected = emergencyAnalysis.isEmergency,
        emergencyTitle = emergencyAnalysis.directTitle,
        suggestedActions = if (emergencyAnalysis.isEmergency) listOf("Abrir SOS", "Ver Protocolo de Hemorragia") else emptyList()
      )

      _uiState.update { state ->
        state.copy(chatMessages = state.chatMessages + agentMsg)
      }
    }
  }

  fun downloadRegion(regionId: String) {
    viewModelScope.launch {
      _uiState.update { state ->
        val updated = state.mapRegions.map { reg ->
          if (reg.id == regionId) reg.copy(isDownloading = true, downloadProgress = 0.1f) else reg
        }
        state.copy(mapRegions = updated)
      }

      for (p in 2..10) {
        delay(150)
        _uiState.update { state ->
          val updated = state.mapRegions.map { reg ->
            if (reg.id == regionId) reg.copy(downloadProgress = p / 10f) else reg
          }
          state.copy(mapRegions = updated)
        }
      }

      _uiState.update { state ->
        val updated = state.mapRegions.map { reg ->
          if (reg.id == regionId) reg.copy(isDownloading = false, isDownloaded = true, downloadProgress = 1f) else reg
        }
        state.copy(mapRegions = updated)
      }
    }
  }

  fun toggleTorch() {
    val newState = !_uiState.value.isTorchActive
    _uiState.update { it.copy(isTorchActive = newState) }

    try {
      val context = getApplication<Application>()
      val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as? CameraManager
      val cameraId = cameraManager?.cameraIdList?.firstOrNull()
      if (cameraId != null) {
        cameraManager.setTorchMode(cameraId, newState)
      }
    } catch (_: Exception) {
      // Graceful fallback for devices/emulators without camera flash hardware
    }
  }
}
