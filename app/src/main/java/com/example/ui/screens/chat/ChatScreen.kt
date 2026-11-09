package com.example.ui.screens.chat

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.ChatMessage
import com.example.model.MascotState
import com.example.ui.components.FieldMascot
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

@Composable
fun ChatScreen(
  messages: List<ChatMessage>,
  onSendMessage: (text: String, imageUri: String?) -> Unit,
  onOpenSos: () -> Unit,
  onOpenGuide: (guideId: String) -> Unit,
  modifier: Modifier = Modifier
) {
  var inputText by remember { mutableStateOf("") }
  var attachedImageUri by remember { mutableStateOf<String?>(null) }
  val listState = rememberLazyListState()

  // Photo picker launcher (using Android Photo Picker - zero permission compliant)
  val photoPickerLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.PickVisualMedia()
  ) { uri: Uri? ->
    if (uri != null) {
      attachedImageUri = uri.toString()
    }
  }

  // Scroll to bottom when new messages arrive
  LaunchedEffect(messages.size) {
    if (messages.isNotEmpty()) {
      listState.animateScrollToItem(messages.size - 1)
    }
  }

  val quickPrompts = listOf(
    "Onde achar água?",
    "Como fazer fogo?",
    "estou sangrando",
    "Sinais de tempestade",
    "Fui picado por cobra"
  )

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(CharcoalBackground)
      .testTag("chat_screen")
  ) {
    // Message stream
    LazyColumn(
      state = listState,
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
      contentPadding = PaddingValues(top = 12.dp, bottom = 12.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // Header guide banner
      item {
        ChatHeaderCard()
      }

      items(messages, key = { it.id }) { msg ->
        if (msg.isUser) {
          UserMessageRow(message = msg)
        } else {
          AgentMessageRow(
            message = msg,
            onOpenSos = onOpenSos,
            onOpenGuide = onOpenGuide
          )
        }
      }
    }

    // Quick prompt pills
    LazyRow(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 6.dp),
      contentPadding = PaddingValues(horizontal = 16.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      items(quickPrompts) { prompt ->
        val isEmergencyPrompt = prompt.contains("sangrando") || prompt.contains("cobra")
        Surface(
          shape = RoundedCornerShape(20.dp),
          color = if (isEmergencyPrompt) Color(0xFF281414) else CharcoalCardElevated,
          modifier = Modifier
            .clickable {
              onSendMessage(prompt, null)
            }
            .border(
              width = 0.8.dp,
              color = if (isEmergencyPrompt) EmergencyRed.copy(alpha = 0.5f) else Color(0xFF2C3830),
              shape = RoundedCornerShape(20.dp)
            )
        ) {
          Text(
            text = prompt,
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            color = if (isEmergencyPrompt) EmergencyRed else TextSecondary,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
          )
        }
      }
    }

    // Attached Image Preview or Empty State Box (if selected)
    AnimatedVisibility(
      visible = attachedImageUri != null,
      enter = expandVertically(),
      exit = shrinkVertically()
    ) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 6.dp)
          .clip(RoundedCornerShape(14.dp))
          .background(CharcoalCardElevated)
          .padding(8.dp)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.fillMaxWidth()
        ) {
          if (attachedImageUri != null) {
            AsyncImage(
              model = attachedImageUri,
              contentDescription = "Foto anexada",
              contentScale = ContentScale.Crop,
              modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(8.dp))
            )
          } else {
            // Empty state illustration placeholder as per prompt requirement
            Box(
              modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF1B231F)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Image,
                contentDescription = null,
                tint = TextTertiary,
                modifier = Modifier.size(22.dp)
              )
            }
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "Imagem para análise de campo",
              fontFamily = InterFontFamily,
              fontWeight = FontWeight.Medium,
              fontSize = 12.sp,
              color = TextPrimary
            )
            Text(
              text = "Plantas, pegadas, relevo ou lesões",
              fontFamily = InterFontFamily,
              fontWeight = FontWeight.Normal,
              fontSize = 10.sp,
              color = TextTertiary
            )
          }
          IconButton(
            onClick = { attachedImageUri = null },
            modifier = Modifier.size(32.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "Remover foto",
              tint = TextSecondary,
              modifier = Modifier.size(18.dp)
            )
          }
        }
      }
    }

    // Input Bar (Minimalist ChatGPT style)
    Surface(
      color = CharcoalCard,
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 12.dp, vertical = 8.dp)
        .clip(RoundedCornerShape(24.dp))
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Photo attachment button
        IconButton(
          onClick = {
            photoPickerLauncher.launch(
              PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
          },
          modifier = Modifier
            .size(40.dp)
            .testTag("attach_photo_button")
        ) {
          Icon(
            imageVector = Icons.Default.AddPhotoAlternate,
            contentDescription = "Anexar foto",
            tint = TextSecondary,
            modifier = Modifier.size(22.dp)
          )
        }

        OutlinedTextField(
          value = inputText,
          onValueChange = { inputText = it },
          placeholder = {
            Text(
              text = "Pergunte ao guia de campo...",
              fontFamily = InterFontFamily,
              fontWeight = FontWeight.Normal,
              fontSize = 14.sp,
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
          keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
          keyboardActions = KeyboardActions(onSend = {
            if (inputText.isNotBlank() || attachedImageUri != null) {
              onSendMessage(inputText.trim(), attachedImageUri)
              inputText = ""
              attachedImageUri = null
            }
          }),
          singleLine = true,
          modifier = Modifier
            .weight(1f)
            .testTag("chat_input_field")
        )

        // Send Button
        IconButton(
          onClick = {
            if (inputText.isNotBlank() || attachedImageUri != null) {
              onSendMessage(inputText.trim(), attachedImageUri)
              inputText = ""
              attachedImageUri = null
            }
          },
          enabled = inputText.isNotBlank() || attachedImageUri != null,
          modifier = Modifier
            .size(40.dp)
            .testTag("chat_send_button")
        ) {
          Box(
            modifier = Modifier
              .size(34.dp)
              .clip(CircleShape)
              .background(
                if (inputText.isNotBlank() || attachedImageUri != null) AmberAccent else Color(0xFF263029)
              ),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.Send,
              contentDescription = "Enviar",
              tint = if (inputText.isNotBlank() || attachedImageUri != null) Color(0xFF1B1405) else TextTertiary,
              modifier = Modifier.size(16.dp)
            )
          }
        }
      }
    }
  }
}

@Composable
private fun ChatHeaderCard() {
  Surface(
    shape = RoundedCornerShape(18.dp),
    color = CharcoalCardElevated,
    modifier = Modifier.fillMaxWidth()
  ) {
    Row(
      modifier = Modifier.padding(14.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      FieldMascot(size = 48.dp, state = MascotState.CALM)
      Spacer(modifier = Modifier.width(14.dp))
      Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = "Guia de Campo",
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = TextPrimary
          )
          Spacer(modifier = Modifier.width(8.dp))
          Surface(
            shape = RoundedCornerShape(6.dp),
            color = Color(0xFF233026)
          ) {
            Text(
              text = "OFFLINE ATIVO",
              fontFamily = InterFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 9.sp,
              color = TacticalOlive,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
          }
        }
        Text(
          text = "Respostas táticas para navegação, água, fogo e emergência médica.",
          fontFamily = InterFontFamily,
          fontWeight = FontWeight.Normal,
          fontSize = 11.sp,
          color = TextSecondary,
          lineHeight = 15.sp,
          modifier = Modifier.padding(top = 2.dp)
        )
      }
    }
  }
}

@Composable
private fun UserMessageRow(message: ChatMessage) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.End
  ) {
    Column(
      horizontalAlignment = Alignment.End,
      modifier = Modifier.fillMaxWidth(0.85f)
    ) {
      if (message.attachedImageUri != null) {
        AsyncImage(
          model = message.attachedImageUri,
          contentDescription = "Foto enviada",
          contentScale = ContentScale.Crop,
          modifier = Modifier
            .size(140.dp)
            .clip(RoundedCornerShape(16.dp))
            .padding(bottom = 6.dp)
        )
      }

      Surface(
        shape = RoundedCornerShape(topStart = 18.dp, topEnd = 4.dp, bottomStart = 18.dp, bottomEnd = 18.dp),
        color = Color(0xFF2B362F)
      ) {
        Text(
          text = message.content,
          fontFamily = InterFontFamily,
          fontWeight = FontWeight.Normal,
          fontSize = 14.sp,
          color = TextPrimary,
          lineHeight = 20.sp,
          modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
        )
      }
    }
  }
}

@Composable
private fun AgentMessageRow(
  message: ChatMessage,
  onOpenSos: () -> Unit,
  onOpenGuide: (guideId: String) -> Unit
) {
  val isEmergency = message.isEmergencyDetected

  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.Start
  ) {
    FieldMascot(
      size = 32.dp,
      state = if (isEmergency) MascotState.ALERT else MascotState.CALM,
      modifier = Modifier.padding(top = 4.dp)
    )

    Spacer(modifier = Modifier.width(10.dp))

    Column(modifier = Modifier.fillMaxWidth(0.9f)) {
      // Emergency Context Banner if triggered
      if (isEmergency) {
        Surface(
          shape = RoundedCornerShape(16.dp),
          color = Color(0xFF2E1212),
          modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .border(1.dp, EmergencyRed, RoundedCornerShape(16.dp))
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Emergency,
                contentDescription = null,
                tint = EmergencyRed,
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = message.emergencyTitle ?: "PROTOCOLO DE URGÊNCIA ATIVADO",
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = EmergencyRed,
                letterSpacing = 0.5.sp
              )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Button(
                onClick = onOpenSos,
                colors = ButtonDefaults.buttonColors(containerColor = EmergencyRed),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.weight(1f).height(36.dp)
              ) {
                Text(
                  text = "Abrir Modo SOS",
                  fontFamily = InterFontFamily,
                  fontWeight = FontWeight.Bold,
                  fontSize = 11.sp,
                  color = Color.White
                )
              }

              Button(
                onClick = { onOpenGuide("hemorragia_grave") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF451919)),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.weight(1f).height(36.dp)
              ) {
                Text(
                  text = "Ver Passos",
                  fontFamily = InterFontFamily,
                  fontWeight = FontWeight.SemiBold,
                  fontSize = 11.sp,
                  color = Color(0xFFFFCDD2)
                )
              }
            }
          }
        }
      }

      Surface(
        shape = RoundedCornerShape(topStart = 4.dp, topEnd = 18.dp, bottomStart = 18.dp, bottomEnd = 18.dp),
        color = CharcoalCardElevated
      ) {
        Text(
          text = message.content,
          fontFamily = InterFontFamily,
          fontWeight = FontWeight.Normal,
          fontSize = 14.sp,
          color = TextPrimary,
          lineHeight = 21.sp,
          modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
        )
      }
    }
  }
}
