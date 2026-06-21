package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AppHeader
import com.example.ui.theme.CoolTeal40
import com.example.ui.theme.DeepBlue40
import com.example.ui.theme.EnergeticOrange40
import com.example.ui.viewmodel.ChatMessage
import com.example.ui.viewmodel.HunterPayViewModel
import kotlinx.coroutines.launch

@Composable
fun ChatScreen(
    viewModel: HunterPayViewModel,
    onNavigateBack: () -> Unit
) {
    val messagesList by viewModel.chatMessages.collectAsState()
    val isSearchingAI by viewModel.isChatLoading.collectAsState()

    var textInput by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    // Smooth scroll to bottom when a new bubble appears
    LaunchedEffect(messagesList.size) {
        if (messagesList.isNotEmpty()) {
            scope.launch {
                listState.animateScrollToItem(messagesList.size - 1)
            }
        }
    }

    Scaffold(
        topBar = {
            AppHeader(
                title = "Asisten HunterPAI 🤖",
                onBackClick = onNavigateBack,
                actions = {
                    IconButton(onClick = { viewModel.clearChat() }) {
                        Icon(
                            imageVector = Icons.Default.DeleteSweep,
                            contentDescription = "Reset Chat",
                            tint = DeepBlue40
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Chat messages layout
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(messagesList) { msg ->
                    ChatBubble(msg = msg)
                }

                if (isSearchingAI) {
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(8.dp)
                                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
                                .padding(12.dp)
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp,
                                color = DeepBlue40
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "HunterPAI sedang merencanakan...",
                                fontSize = 12.sp,
                                color = Color.Gray,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            // Text input container
            Card(
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = textInput,
                        onValueChange = { textInput = it },
                        placeholder = { Text("Tanya rencana liburan, cuaca, budget, dll...") },
                        modifier = Modifier.weight(1f),
                        singleLine = false,
                        maxLines = 4,
                        colors = OutlinedTextFieldDefaults.colors()
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    IconButton(
                        onClick = {
                            if (textInput.trim().isNotEmpty()) {
                                viewModel.sendChatMessage(textInput)
                                textInput = ""
                            }
                        },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = DeepBlue40,
                            contentColor = Color.White
                        ),
                        modifier = Modifier.size(46.dp),
                        enabled = textInput.trim().isNotEmpty() && !isSearchingAI
                    ) {
                        Icon(imageVector = Icons.Default.Send, contentDescription = "Kirim")
                    }
                }
            }
        }
    }
}

@Composable
fun ChatBubble(msg: ChatMessage) {
    val horizontalAlign = if (msg.isUser) Alignment.End else Alignment.Start
    val containerColor = if (msg.isUser) DeepBlue40 else Color(0xFFECEFF1)
    val contentTextColor = if (msg.isUser) Color.White else Color.Black
    val corners = if (msg.isUser) {
        RoundedCornerShape(topStart = 14.dp, topEnd = 1.dp, bottomStart = 14.dp, bottomEnd = 14.dp)
    } else {
        RoundedCornerShape(topStart = 1.dp, topEnd = 14.dp, bottomStart = 14.dp, bottomEnd = 14.dp)
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = horizontalAlign
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .background(containerColor, corners)
                .padding(12.dp)
        ) {
            Column {
                if (!msg.isUser) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.SmartToy,
                            contentDescription = "Bot PAI",
                            tint = CoolTeal40,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "HunterPAI",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = CoolTeal40
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }
                Text(
                    text = msg.message,
                    fontSize = 13.sp,
                    color = contentTextColor,
                    lineHeight = 18.sp
                )
            }
        }
    }
}
