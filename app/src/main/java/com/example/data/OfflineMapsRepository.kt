package com.example.data

import com.example.model.MapWaypoint
import com.example.model.OfflineMapRegion
import com.example.model.WaypointType

object OfflineMapsRepository {

  val regions: List<OfflineMapRegion> = listOf(
    OfflineMapRegion(
      id = "mantiqueira",
      name = "Serra da Mantiqueira",
      subtitle = "Travessias Marins-Itaguaré e Serra Fina (SP/MG/RJ)",
      sizeMb = 142,
      isDownloaded = true,
      elevationMax = 2798,
      trailsCount = 18,
      waterPoints = 12
    ),
    OfflineMapRegion(
      id = "veadeiros",
      name = "Chapada dos Veadeiros",
      subtitle = "Parque Nacional e Vales de Cristal (GO)",
      sizeMb = 88,
      isDownloaded = false,
      elevationMax = 1650,
      trailsCount = 14,
      waterPoints = 19
    ),
    OfflineMapRegion(
      id = "serra_orgaos",
      name = "Serra dos Órgãos",
      subtitle = "Travessia Petrópolis-Teresópolis e Dedo de Deus (RJ)",
      sizeMb = 94,
      isDownloaded = true,
      elevationMax = 2263,
      trailsCount = 9,
      waterPoints = 8
    ),
    OfflineMapRegion(
      id = "canastra",
      name = "Serra da Canastra",
      subtitle = "Nascente do Rio São Francisco e Casca D'Anta (MG)",
      sizeMb = 118,
      isDownloaded = false,
      elevationMax = 1496,
      trailsCount = 16,
      waterPoints = 24
    ),
    OfflineMapRegion(
      id = "diamantina",
      name = "Chapada Diamantina",
      subtitle = "Vale do Pati, Fumaça e Morro do Pai Inácio (BA)",
      sizeMb = 156,
      isDownloaded = false,
      elevationMax = 1280,
      trailsCount = 22,
      waterPoints = 31
    )
  )

  val sampleWaypoints: List<MapWaypoint> = listOf(
    MapWaypoint(
      name = "Nascente Cristalina",
      type = WaypointType.WATER,
      lat = -22.4132,
      lng = -44.9621,
      elevationMeters = 2120,
      description = "Água límpida e potável correndo entre rochas graníticas. Vazão constante."
    ),
    MapWaypoint(
      name = "Córrego das Pedras",
      type = WaypointType.WATER,
      lat = -22.4178,
      lng = -44.9585,
      elevationMeters = 1980,
      description = "Leito com corredeiras. Recomenda-se ferver ou filtrar antes de ingerir."
    ),
    MapWaypoint(
      name = "Refúgio da Pedra Branca",
      type = WaypointType.SHELTER,
      lat = -22.4089,
      lng = -44.9688,
      elevationMeters = 2340,
      description = "Caverna natural rasa com proteção contra ventos do quadrante sul."
    ),
    MapWaypoint(
      name = "Acampamento Base da Crista",
      type = WaypointType.CAMP,
      lat = -22.4210,
      lng = -44.9540,
      elevationMeters = 2050,
      description = "Área plana para até 4 barracas protegidas por vegetação de altitude."
    ),
    MapWaypoint(
      name = "Entrada da Trilha (KM 0)",
      type = WaypointType.TRAILHEAD,
      lat = -22.4295,
      lng = -44.9450,
      elevationMeters = 1520,
      description = "Estacionamento rústico e ponto de contato com a equipe de guarda-parques."
    ),
    MapWaypoint(
      name = "Fenda Profunda / Risco de Queda",
      type = WaypointType.HAZARD,
      lat = -22.4055,
      lng = -44.9712,
      elevationMeters = 2480,
      description = "Passagem estreita com despenhadeiro de 80m. Proibido transitar com neblina densa."
    )
  )
}
