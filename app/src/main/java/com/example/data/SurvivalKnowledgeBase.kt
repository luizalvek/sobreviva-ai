package com.example.data

import com.example.model.GuideCategory
import com.example.model.GuideStep
import com.example.model.SurvivalGuide
import java.util.Locale

object SurvivalKnowledgeBase {

  val guides: List<SurvivalGuide> = listOf(
    // 1. Primeiros Socorros
    SurvivalGuide(
      id = "hemorragia_grave",
      title = "Contenção de Hemorragia Grave",
      category = GuideCategory.FIRST_AID,
      summary = "Controle rápido de perda massiva de sangue com compressão direta e torniquete se necessário.",
      timeEstimate = "Ação imediata (0 a 3 min)",
      priorityWarning = "Atenção: A perda rápida de sangue é a principal causa evitável de morte no campo.",
      steps = listOf(
        GuideStep(
          stepNumber = 1,
          title = "Exposição e Pressão Direta Firme",
          instruction = "Exponha o ferimento rasgando ou cortando a roupa. Com um tecido limpo, gaze ou a palma da mão, pressione com toda a força diretamente sobre o ponto de sangramento.",
          crucialTip = "Mantenha o peso do corpo sobre a compressão. Não levante o pano para 'espiar' se parou de sangrar por pelo menos 5 minutos.",
          checklistItem = "Pressionar sem interrupção"
        ),
        GuideStep(
          stepNumber = 2,
          title = "Curativo Compressivo",
          instruction = "Enfaixe firmemente o local com atadura ou faixa de tecido apertada sobre o enchimento. A bandagem deve ser justa sem paralisar totalmente a circulação distal da extremidade (a menos que seja torniquete).",
          crucialTip = "Se o sangue vazar pelo curativo, NÃO o remova; adicione mais camadas por cima e aperte mais.",
          checklistItem = "Fixar bandagem firme"
        ),
        GuideStep(
          stepNumber = 3,
          title = "Torniquete em Membros (Se a compressão falhar)",
          instruction = "Em braços ou pernas com sangramento arterial em jato: aplique uma tira larga (mínimo 4 cm) de 5 a 7 cm acima do ferimento (nunca sobre articulação). Aperte girando uma barra ou bastão resistente até o sangramento parar por completo.",
          crucialTip = "Anote a HORA EXATA em que o torniquete foi aplicado (ex: no braço com caneta ou terra). Não afrouxe até o atendimento médico definitivo.",
          checklistItem = "Marcar horário do torniquete"
        ),
        GuideStep(
          stepNumber = 4,
          title = "Prevenção de Choque e Hipotermia",
          instruction = "Deite a vítima suavemente. Mantenha-a aquecida com isolante térmico ou manta de emergência, mesmo se o dia estiver quente. O choque hipovolêmico derruba a temperatura corporal drasticamente.",
          crucialTip = "Não forneça alimentos sólidos nem grande volume de água se houver suspeita de cirurgia iminente.",
          checklistItem = "Isolar a vítima do chão frio"
        )
      )
    ),

    SurvivalGuide(
      id = "picada_peconhenta",
      title = "Acidente com Animais Peçonhentos (Cobras / Escorpiões)",
      category = GuideCategory.FIRST_AID,
      summary = "Protocolo oficial do Ministério da Saúde para picadas de serpentes (Jararaca, Cascavel, Surucucu, Coral) e escorpiões.",
      timeEstimate = "Imediato",
      priorityWarning = "NUNCA faça torniquete, cortes nem sucção no local da picada. Isso agrava a necrose e intoxicação.",
      steps = listOf(
        GuideStep(
          stepNumber = 1,
          title = "Imobilização e Repouso Absoluto",
          instruction = "A vítima deve sentar ou deitar imediatamente e manter-se o mais imóvel possível. Movimentos aceleram a absorção e circulação do veneno na corrente sanguínea.",
          crucialTip = "Retire anéis, pulseiras, relógios e botas apertadas imediatamente antes que ocorra inchaço (edema).",
          checklistItem = "Remover adornos e conter movimentos"
        ),
        GuideStep(
          stepNumber = 2,
          title = "Limpeza Suave com Água e Sabão",
          instruction = "Lave apenas a área externa com água limpa e sabão neutro se houver. Mantenha o membro acometido estendido e em posição neutra ou ligeiramente elevada em relação ao tronco.",
          crucialTip = "Não passe borra de café, folhas, álcool ou querosene sobre o ferimento.",
          checklistItem = "Lavar apenas com água e sabão"
        ),
        GuideStep(
          stepNumber = 3,
          title = "Identificação Visual sem Risco",
          instruction = "Se a serpente ou animal ainda estiver visível, fotografe à distância segura ou observe características (padrão de escamas, cauda com guizo, formato da cabeça) para ajudar a equipe médica a escolher o soro antiofídico correto.",
          crucialTip = "Nunca tente capturar ou manusear o animal mesmo que pareça morto.",
          checklistItem = "Fotografar à distância se seguro"
        ),
        GuideStep(
          stepNumber = 4,
          title = "Evacuação e Hidratação",
          instruction = "Acione o SOS no app para emitir coordenadas e ligue 192 (SAMU) ou 193 (Bombeiros). Transporte a vítima em maca improvisada sem que ela caminhe.",
          crucialTip = "Ofereça goles moderados de água à vítima consciente.",
          checklistItem = "Acionar resgate e não caminhar"
        )
      )
    ),

    SurvivalGuide(
      id = "fraturas_imobilizacao",
      title = "Imobilização de Fraturas e Entorses",
      category = GuideCategory.FIRST_AID,
      summary = "Estabilização de ossos quebrados e luxações em áreas remotas.",
      timeEstimate = "5 a 10 min",
      steps = listOf(
        GuideStep(
          stepNumber = 1,
          title = "Não Tente Realinhar Ossos Fechados",
          instruction = "Imobilize o membro na posição em que foi encontrado. Tentar forçar o osso de volta no lugar pode romper vasos sanguíneos e nervos vitais.",
          crucialTip = "Se houver osso exposto (fratura aberta), cubra com pano limpo úmido sem empurrar o osso para dentro.",
          checklistItem = "Manter posição original"
        ),
        GuideStep(
          stepNumber = 2,
          title = "Tala Rígida Acolchoada",
          instruction = "Utilize galhos retos, bastões de caminhada ou isolante térmico dobrado. O segredo da imobilização é fixar a articulação ANTERIOR e a POSTERIOR à fratura.",
          crucialTip = "Acolchoe com roupas macias entre a tala e a pele para evitar lesões por pressão.",
          checklistItem = "Travar articulação acima e abaixo"
        )
      )
    ),

    // 2. Água
    SurvivalGuide(
      id = "purificacao_agua",
      title = "Purificação e Captação de Água",
      category = GuideCategory.WATER,
      summary = "Técnicas confiáveis para tornar água de rios e vertentes 100% segura para consumo humano.",
      timeEstimate = "10 a 20 min",
      steps = listOf(
        GuideStep(
          stepNumber = 1,
          title = "Coleta Estratégica em Águas Rápidas",
          instruction = "Dê preferência a córregos em encostas rochosas com correnteza rápida e leito pedregoso. Evite água estagnada ou poças próximas a fezes de animais e pastagens.",
          crucialTip = "Cavando um buraco de 30 cm na areia a 1 metro da margem de um rio turvo, você obtém água pré-filtrada pelo lençol de cascalho.",
          checklistItem = "Buscar correnteza límpida"
        ),
        GuideStep(
          stepNumber = 2,
          title = "Pré-Filtração de Sedimentos",
          instruction = "Passe a água por uma camiseta dobrada, bandana ou filtro de camadas (areia fina, carvão vegetal triturado e pedregulhos em garrafa PET cortada).",
          crucialTip = "A pré-filtração remove partículas, turbidez e parasitas maiores, mas NÃO elimina bactérias e vírus.",
          checklistItem = "Filtrar matéria em suspensão"
        ),
        GuideStep(
          stepNumber = 3,
          title = "Fervura Robusta (Padrão Ouro)",
          instruction = "Ferva a água vigorosamente em borbulhas contínuas por no mínimo 1 minuto completo (3 minutos em altitudes superiores a 2.000m).",
          crucialTip = "Ferver mata 100% de parasitas, protozoários, bactérias e vírus conhecidos.",
          checklistItem = "Borbulhar por 1 minuto"
        ),
        GuideStep(
          stepNumber = 4,
          title = "Cloração / Pastilhas de Cloro (Alternativa)",
          instruction = "Se não houver fogo: use pastilhas de cloro/iodopovidona ou 2 a 4 gotas de hipoclorito de sódio (2,5%) por litro. Deixe repousar por 30 minutos fechado.",
          crucialTip = "Em água fria ou turva, dobre o tempo de descanso químico para 60 minutos.",
          checklistItem = "Aguardar 30 min de ação química"
        )
      )
    ),

    // 3. Fogo
    SurvivalGuide(
      id = "iniciar_fogo_pederneira",
      title = "Iniciação de Fogo com Pederneira (Ferrocério)",
      category = GuideCategory.FIRE,
      summary = "Preparação milimétrica de ninho de isca e faíscas eficientes mesmo com vento ou umidade.",
      timeEstimate = "10 min",
      steps = listOf(
        GuideStep(
          stepNumber = 1,
          title = "A Tríade dos Combustíveis",
          instruction = "Separe 3 pilhas antes de soltar a primeira faísca: 1. Isca finíssima (palha seca, casca de bétula/árvore esfarelada, algodão); 2. Gravetos da espessura de fósforo a lápis; 3. Lenha principal da espessura de punho.",
          crucialTip = "90% das falhas de fogo ocorrem por tentar jogar lenha grossa cedo demais sem cama de brasas.",
          checklistItem = "Organizar 3 tamanhos de madeira"
        ),
        GuideStep(
          stepNumber = 2,
          title = "Construção do Ninho Térmico",
          instruction = "Crie um pequeno ninho no formato de pássaro com as fibras secas. Isole o ninho da umidade do solo com uma base de casca de árvore ou pedaços de madeira seca.",
          crucialTip = "O ninho precisa de passagem de oxigênio; não amasse as fibras com força excessiva.",
          checklistItem = "Isolar a isca do chão úmido"
        ),
        GuideStep(
          stepNumber = 3,
          title = "Técnica de Puxar a Barra (Não o raspador)",
          instruction = "Apoie o raspador fixo a 2 cm do ninho e puxe a barra de ferrocério para trás. Isso lança faíscas concentradas a 3.000°C sem bater na isca nem desmanchar o ninho.",
          crucialTip = "Assim que a fumaça engrossar, sopre lenta e continuamente no coração da brasa até virar chama aberta.",
          checklistItem = "Puxar a barra com sopro suave"
        )
      )
    ),

    // 4. Abrigo
    SurvivalGuide(
      id = "abrigo_detritos_tarp",
      title = "Abrigo de Emergência e Isolamento do Chão",
      category = GuideCategory.SHELTER,
      summary = "Proteção contra chuva, vento e perda de calor condutiva contra o solo frio.",
      timeEstimate = "30 a 45 min",
      steps = listOf(
        GuideStep(
          stepNumber = 1,
          title = "Escolha do Local Seguro (Regra dos 4 Ws)",
          instruction = "Verifique: Wind (vento dominante), Water (fora de leitos secos sujeitos a tromba d'água), Widowmakers (galhos secos suspensos nas árvores), Wood (lenha abundante).",
          crucialTip = "Nunca monte acampamento no ponto mais baixo de uma ravina (acúmulo de ar congelante) nem em cumes expostos a raios.",
          checklistItem = "Inspecionar galhos no topo e leito de água"
        ),
        GuideStep(
          stepNumber = 2,
          title = "Cama de Detritos Isolante (Prioridade Vital)",
          instruction = "O solo frio rouba calor corporal 30 vezes mais rápido que o ar. Amontoe pelo menos 20 a 30 cm de folhas secas, musgo, samambaias e ramos de pinheiro compactados para deitar em cima.",
          crucialTip = "Um bom isolamento do chão é mais importante para sobrevivência que o próprio teto.",
          checklistItem = "Criar colchão de 25cm de vegetação seca"
        ),
        GuideStep(
          stepNumber = 3,
          title = "Cobertura Inclinada (Lean-to / Lona)",
          instruction = "Apoie uma viga central robusta entre duas árvores a 1 metro de altura. Incline galhos a 45 graus voltados contra o vento e cubra com camadas espessas de folhagem ou lona.",
          crucialTip = "O ângulo de 45° faz a água da chuva escorrer sem penetrar nas frestas.",
          checklistItem = "Fixar inclinação de 45 graus"
        )
      )
    ),

    // 5. Orientação
    SurvivalGuide(
      id = "orientacao_sol_astros",
      title = "Navegação por Sol, Sombra e Terreno",
      category = GuideCategory.ORIENTATION,
      summary = "Encontrar o Norte e linhas cardeais sem GPS usando o método da vara e sombra.",
      timeEstimate = "15 min",
      steps = listOf(
        GuideStep(
          stepNumber = 1,
          title = "Método da Vara e Sombra (Bússola Solar)",
          instruction = "Finque uma vara reta de 1 metro no chão nivelado. Marque a ponta da primeira sombra com uma pedrinha (Ponto 1 = OESTE).",
          crucialTip = "Aguarde 15 minutos enquanto a sombra se desloca.",
          checklistItem = "Marcar primeira ponta de sombra"
        ),
        GuideStep(
          stepNumber = 2,
          title = "Segunda Marcação e Linha Leste-Oeste",
          instruction = "Marque a nova posição da ponta da sombra (Ponto 2 = LESTE). Trace uma linha reta entre as duas pedrinhas: você tem a linha Leste-Oeste.",
          crucialTip = "Coloque o pé esquerdo na primeira marca e o direito na segunda: você estará olhando exatamente para o NORTE.",
          checklistItem = "Traçar eixo e apontar para o Norte"
        ),
        GuideStep(
          stepNumber = 3,
          title = "Regra do Cruzeiro do Sul (Hemisfério Sul)",
          instruction = "À noite, localize a constelação do Cruzeiro do Sul. Prolongue o braço maior da cruz 4,5 vezes em linha reta na mesma direção. Baixe uma vertical até o horizonte: ali está o SUL verdadeiro.",
          crucialTip = "As duas estrelas 'Apontadoras' (Alfa e Beta Centauri) confirmam que você está olhando a cruz verdadeira e não a falsa.",
          checklistItem = "Projetar 4,5x o eixo da cruz ao solo"
        )
      )
    ),

    // 6. Fauna e Flora
    SurvivalGuide(
      id = "plantas_animais_perigosos",
      title = "Identificação de Riscos: Fauna e Flora de Campo",
      category = GuideCategory.FAUNA_FLORA,
      summary = "Regras universais para evitar intoxicação botânica e encontros com animais perigosos.",
      timeEstimate = "Leitura preventiva",
      steps = listOf(
        GuideStep(
          stepNumber = 1,
          title = "Regras Universais de Plantas Tóxicas",
          instruction = "EVITE plantas que possuam: seiva leitosa/branca, pelos urticantes nas folhas, cheiro de amêndoa amarga, folhas agrupadas em três (como hera venenosa), bagas brancas ou amarelas.",
          crucialTip = "Se não tiver certeza absoluta da espécie, NUNCA consuma. A desidratação por diarreia mata mais rápido que a fome.",
          checklistItem = "Evitar seivas leitosas e frutos desconhecidos"
        ),
        GuideStep(
          stepNumber = 2,
          title = "Prevenção de Serpentes e Aracnídeos no Acampamento",
          instruction = "Mantenha a barraca e mochilas sempre 100% ziperadas. Antes de calçar botas, vire-as e bata firmemente contra o chão. Não enfie as mãos em troncos ocos ou fendas de rocha.",
          crucialTip = "À noite, caminhe sempre com bota de cano alto e lanterna focada nos próximos 3 metros de trilha.",
          checklistItem = "Bater as botas e checar fendas com graveto"
        )
      )
    )
  )

  // Emergency intent classification
  data class EmergencyAnalysis(
    val isEmergency: Boolean,
    val triggerWord: String = "",
    val detectedType: String = "",
    val directTitle: String = "",
    val immediateGuide: SurvivalGuide? = null,
    val responseText: String = ""
  )

  fun detectEmergencyIntent(prompt: String): EmergencyAnalysis {
    val clean = prompt.lowercase(Locale.ROOT)

    // 1. Bleeding / Hemorrhage
    if (clean.contains("sangrando") || clean.contains("sangue") || clean.contains("hemorragia") || clean.contains("corte profundo") || clean.contains("esfaque")) {
      val guide = guides.firstOrNull { it.id == "hemorragia_grave" }
      return EmergencyAnalysis(
        isEmergency = true,
        triggerWord = "sangrando",
        detectedType = "HEMORRAGIA",
        directTitle = "URGÊNCIA: Hemorragia Detectada",
        immediateGuide = guide,
        responseText = "ALERTA DE PRIMEIROS SOCORROS: Detectei suspeita de sangramento grave. Ação prioritária imediata: aplique pressão direta firme sobre o ferimento agora mesmo com pano limpo e mantenha o membro apoiado. Se necessário, acione o botão SOS no topo para transmitir suas coordenadas geográficas exatas aos serviços de emergência (192 / 193)."
      )
    }

    // 2. Snake / Venomous Bite
    if (clean.contains("picado") || clean.contains("picada") || clean.contains("cobra") || clean.contains("serpente") || clean.contains("escorpião") || clean.contains("aranha") || clean.contains("peçonhento")) {
      val guide = guides.firstOrNull { it.id == "picada_peconhenta" }
      return EmergencyAnalysis(
        isEmergency = true,
        triggerWord = "picada",
        detectedType = "PEÇONHENTO",
        directTitle = "URGÊNCIA: Acidente com Peçonhento",
        immediateGuide = guide,
        responseText = "ALERTA DE PRIMEIROS SOCORROS: Acidente com animal peçonhento detectado. NÃO faça torniquete, NÃO corte e NÃO sugue a lesão. Mantenha repouso absoluto para evitar que o veneno circule. Retire relógios e sapatos antes do inchaço. Se puder, fotografe o animal de longe para o soro antiofídico no hospital e acione o SOS."
      )
    }

    // 3. Fractures / Severe Trauma
    if (clean.contains("quebrei") || clean.contains("fratura") || clean.contains("osso") || clean.contains("luxação") || clean.contains("caí do barranco")) {
      val guide = guides.firstOrNull { it.id == "fraturas_imobilizacao" }
      return EmergencyAnalysis(
        isEmergency = true,
        triggerWord = "fratura",
        detectedType = "FRATURA",
        directTitle = "URGÊNCIA: Suspeita de Fratura",
        immediateGuide = guide,
        responseText = "ALERTA MÉDICO DE CAMPO: Suspeita de fratura ou trauma ósseo. Não tente puxar ou alinhar o membro. Imobilize na posição encontrada utilizando galhos retos ou bastões acolchoados com peças de roupa, travando a articulação antes e depois da lesão."
      )
    }

    // 4. Lost / Hypothermia
    if (clean.contains("perdido") || clean.contains("perdi a trilha") || clean.contains("desaparecido") || clean.contains("não sei onde estou")) {
      return EmergencyAnalysis(
        isEmergency = true,
        triggerWord = "perdido",
        detectedType = "DESORIENTAÇÃO",
        directTitle = "ALERTA: Perda de Trilha",
        responseText = "PARE IMEDIATAMENTE (Regra S.T.O.P. - Sente, Pense, Observe, Planeje). Não continue descendo ravinas às cegas. Abra a tela de Mapa ou Kit para verificar seu azimute de bússola e ponto de partida. Se a noite estiver caindo, priorize montar abrigo e fogueira antes da escuridão em vez de vagar no escuro."
      )
    }

    return EmergencyAnalysis(isEmergency = false)
  }

  fun generateGuideResponse(prompt: String): String {
    val clean = prompt.lowercase(Locale.ROOT)

    if (clean.contains("água") || clean.contains("agua") || clean.contains("sede") || clean.contains("filtrar")) {
      return "Para água segura no campo:\n\n1. Busque correntezas rápidas em encostas rochosas, evitando poças paradas.\n2. Faça pré-filtração de sedimentos com uma bandana ou camiseta limpa.\n3. Ferva em borbulha forte por pelo menos 1 minuto (padrão ouro para eliminar bactérias, vírus e protozoários).\n\nConsulte a seção 'Água' na Biblioteca para detalhes do filtro de carvão e decantação."
    }

    if (clean.contains("fogo") || clean.contains("fogueira") || clean.contains("pederneira") || clean.contains("faísca")) {
      return "Dicas para ignição eficiente:\n\n• O segredo é a preparação: tenha o ninho de isca seca (palha fina esfarelada), gravetos finos e lenha média organizados antes da primeira faísca.\n• Com a pederneira: apoie o raspador fixo e puxe a barra de ferrocério para trás para não desmanchar o ninho.\n• Sopre com calma no coração da brasa até abrir a chama."
    }

    if (clean.contains("abrigo") || clean.contains("frio") || clean.contains("chuva") || clean.contains("vento")) {
      return "Princípios de abrigo de sobrevivência:\n\n• Isole o chão com pelo menos 25 cm de folhas secas e ramos (o chão frio rouba calor corporal muito rápido).\n• Evite fundos de vale úmidos e cumes expostos a raios.\n• Use ângulo de 45° na lona ou vigas de galhos para a chuva escorrer."
    }

    if (clean.contains("onde há água") || clean.contains("água perto")) {
      return "Análise Topográfica de Campo:\n\n• Siga as linhas de declive natural do terreno e ravina: a água se acumula no fundo das dobras geográficas.\n• No mapa interativo, há 2 fontes mapeadas nas proximidades (Córrego das Pedras a 420m a Nordeste e Nascente da Gruta a 890m).\n• Procure manchas de vegetação mais verde e exuberante (samambaias e bananeiras-do-mato indicam lençol freático superficial)."
    }

    return "Entendido. Como seu guia de campo, recomendo manter a calma e avaliar seus recursos fundamentais (Água, Fogo, Abrigo, Orientação). Você pode consultar os protocolos na Biblioteca, verificar a bússola no Kit ou navegar pelo mapa offline. Se houver emergência iminente, segure o botão SOS no topo por 2 segundos."
  }
}
