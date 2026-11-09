package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.data.OfflineMapsRepository
import com.example.model.ScreenTab
import com.example.ui.components.DrawerContent
import com.example.ui.components.DrawerDestination
import com.example.ui.components.DrawerDestinationModal
import com.example.ui.components.EmergencyScreen
import com.example.ui.components.FieldBottomNav
import com.example.ui.components.TopFieldBar
import com.example.ui.screens.chat.ChatScreen
import com.example.ui.screens.kit.KitScreen
import com.example.ui.screens.library.LibraryScreen
import com.example.ui.screens.map.MapScreen
import com.example.ui.theme.CharcoalBackground
import com.example.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

  private val viewModel: MainViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    setContent {
      val uiState by viewModel.uiState.collectAsState()

      MyApplicationTheme(batterySaver = uiState.isBatterySaverActive) {
        MainAppContent(
          uiState = uiState,
          viewModel = viewModel
        )
      }
    }
  }
}

@Composable
fun MainAppContent(
  uiState: MainUiState,
  viewModel: MainViewModel
) {
  val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
  val coroutineScope = rememberCoroutineScope()
  val snackbarHostState = remember { SnackbarHostState() }

  ModalNavigationDrawer(
    drawerState = drawerState,
    gesturesEnabled = !uiState.isEmergencyActive,
    drawerContent = {
      DrawerContent(
        batterySaverActive = uiState.isBatterySaverActive,
        onToggleBatterySaver = { enabled -> viewModel.toggleBatterySaver(enabled) },
        onSelectDestination = { dest ->
          viewModel.setDrawerDestination(dest)
        },
        onClose = {
          coroutineScope.launch { drawerState.close() }
        }
      )
    }
  ) {
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(CharcoalBackground)
    ) {
      Scaffold(
        contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp),
        topBar = {
          TopFieldBar(
            title = when (uiState.currentTab) {
              ScreenTab.CHAT -> "Vereda"
              ScreenTab.MAP -> "Mapa de Campo"
              ScreenTab.LIBRARY -> "Biblioteca Técnica"
              ScreenTab.KIT -> "Kit de Ferramentas"
            },
            subtitle = when (uiState.currentTab) {
              ScreenTab.CHAT -> "Guia I.A Offline"
              ScreenTab.MAP -> "Mantiqueira • Topo"
              ScreenTab.LIBRARY -> "6 Protocolos"
              ScreenTab.KIT -> "6 Instrumentos"
            },
            onOpenDrawer = {
              coroutineScope.launch { drawerState.open() }
            },
            onSosTriggered = {
              viewModel.setEmergencyActive(true)
            },
            onSosHint = {
              coroutineScope.launch {
                snackbarHostState.showSnackbar("Segure o botão SOS por 2 segundos para abrir o Modo de Emergência.")
              }
            }
          )
        },
        bottomBar = {
          FieldBottomNav(
            currentTab = uiState.currentTab,
            onSelectTab = { tab -> viewModel.setTab(tab) }
          )
        },
        snackbarHost = {
          SnackbarHost(hostState = snackbarHostState)
        },
        containerColor = CharcoalBackground
      ) { innerPadding ->
        Box(
          modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        ) {
          when (uiState.currentTab) {
            ScreenTab.CHAT -> {
              ChatScreen(
                messages = uiState.chatMessages,
                onSendMessage = { text, imageUri ->
                  viewModel.sendMessage(text, imageUri)
                },
                onOpenSos = {
                  viewModel.setEmergencyActive(true)
                },
                onOpenGuide = { guideId ->
                  viewModel.openGuide(guideId)
                }
              )
            }

            ScreenTab.MAP -> {
              MapScreen(
                userLat = uiState.userLat,
                userLng = uiState.userLng,
                waypoints = OfflineMapsRepository.sampleWaypoints,
                regions = uiState.mapRegions,
                onAskGuide = { query ->
                  viewModel.setTab(ScreenTab.CHAT)
                  viewModel.sendMessage(query, null)
                },
                onDownloadRegion = { regionId ->
                  viewModel.downloadRegion(regionId)
                }
              )
            }

            ScreenTab.LIBRARY -> {
              LibraryScreen(
                selectedGuideId = uiState.selectedGuideId,
                onClearSelectedGuide = {
                  viewModel.clearSelectedGuide()
                }
              )
            }

            ScreenTab.KIT -> {
              KitScreen(
                onOpenSos = {
                  viewModel.setEmergencyActive(true)
                },
                isTorchActive = uiState.isTorchActive,
                onToggleTorch = {
                  viewModel.toggleTorch()
                }
              )
            }
          }
        }
      }

      // Full Emergency Mode Overlay
      AnimatedVisibility(
        visible = uiState.isEmergencyActive,
        enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
        exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
      ) {
        EmergencyScreen(
          lat = uiState.userLat,
          lng = uiState.userLng,
          elevationMeters = uiState.elevationMeters,
          isTorchActive = uiState.isTorchActive,
          onToggleTorch = { viewModel.toggleTorch() },
          onDismiss = { viewModel.setEmergencyActive(false) }
        )
      }

      // Drawer Destination Sheet Modal
      if (uiState.activeDrawerDestination != DrawerDestination.NONE) {
        DrawerDestinationModal(
          destination = uiState.activeDrawerDestination,
          chatSessions = uiState.chatSessions,
          onSelectSession = { session ->
            viewModel.setTab(ScreenTab.CHAT)
          },
          onClearHistory = {},
          onDismiss = {
            viewModel.setDrawerDestination(DrawerDestination.NONE)
          }
        )
      }
    }
  }
}
