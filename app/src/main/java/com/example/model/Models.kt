package com.example.model

import java.util.UUID

enum class ScreenTab {
  CHAT,
  MAP,
  LIBRARY,
  KIT
}

enum class GuideCategory(val displayName: String, val subtitle: String) {
  FIRST_AID("Primeiros socorros", "Protocolos vitais para traumas, hemorragias e picadas"),
  WATER("Água", "Captação, purificação e fontes seguras"),
  FIRE("Fogo", "Métodos de ignição primitiva, manutenção e segurança"),
  SHELTER("Abrigo", "Construção de abrigos de emergência e isolamento térmico"),
  ORIENTATION("Orientação", "Navegação por astros, terreno e bússola de campo"),
  FAUNA_FLORA("Fauna e flora", "Reconhecimento de perigos, animais peçonhentos e plantas")
}

data class GuideStep(
  val stepNumber: Int,
  val title: String,
  val instruction: String,
  val crucialTip: String? = null,
  val checklistItem: String? = null
)

data class SurvivalGuide(
  val id: String,
  val title: String,
  val category: GuideCategory,
  val summary: String,
  val timeEstimate: String,
  val steps: List<GuideStep>,
  val priorityWarning: String? = null
)

data class ChatMessage(
  val id: String = UUID.randomUUID().toString(),
  val content: String,
  val isUser: Boolean,
  val timestamp: Long = System.currentTimeMillis(),
  val attachedImageUri: String? = null,
  val isEmergencyDetected: Boolean = false,
  val emergencyTitle: String? = null,
  val suggestedActions: List<String> = emptyList()
)

data class ChatSession(
  val id: String = UUID.randomUUID().toString(),
  val title: String,
  val lastMessage: String,
  val timestamp: Long = System.currentTimeMillis()
)

data class OfflineMapRegion(
  val id: String,
  val name: String,
  val subtitle: String,
  val sizeMb: Int,
  val isDownloaded: Boolean = false,
  val isDownloading: Boolean = false,
  val downloadProgress: Float = 0f,
  val elevationMax: Int = 0,
  val trailsCount: Int = 0,
  val waterPoints: Int = 0
)

enum class WaypointType {
  WATER,
  SHELTER,
  CAMP,
  TRAILHEAD,
  HAZARD
}

data class MapWaypoint(
  val id: String = UUID.randomUUID().toString(),
  val name: String,
  val type: WaypointType,
  val lat: Double,
  val lng: Double,
  val elevationMeters: Int,
  val description: String
)

data class BreadcrumbPoint(
  val id: String = UUID.randomUUID().toString(),
  val label: String,
  val lat: Double,
  val lng: Double,
  val timestamp: Long = System.currentTimeMillis(),
  val isBaseCamp: Boolean = false
)

enum class MascotState {
  CALM,
  ALERT,
  HELPING
}
