package com.example.ui

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.*
import java.text.SimpleDateFormat
import java.util.*

enum class AppScreen(val title: String) {
    Dashboard("Dashboard"),
    Timeline("Timeline"),
    Principles("Principles"),
    Library("Library"),
    AiLabs("AI Labs")
}

enum class AiLabTab {
    Observations, Decisions, Chat
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionWisdomApp(viewModel: ActionWisdomViewModel) {
    val context = LocalContext.current
    var currentScreen by remember { mutableStateOf(AppScreen.Dashboard) }
    var activeLabTab by remember { mutableStateOf(AiLabTab.Chat) }
    
    // Dialog States
    var showAddJournalDialog by remember { mutableStateOf(false) }
    var showAddPrincipleDialog by remember { mutableStateOf(false) }
    var showAddGuideDialog by remember { mutableStateOf(false) }
    var showAddObservationDialog by remember { mutableStateOf(false) }
    var showAddDecisionDialog by remember { mutableStateOf(false) }
    var showAddHabitDialog by remember { mutableStateOf(false) }

    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val isSpeaking by viewModel.isSpeaking.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Psychology,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            text = "Action Wisdom",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                actions = {
                    if (currentScreen == AppScreen.Timeline || currentScreen == AppScreen.Principles) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { viewModel.updateSearchQuery(it) },
                            placeholder = { Text("Search wisdom...") },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                            trailingIcon = {
                                if (searchQuery.isNotEmpty()) {
                                    IconButton(onClick = { viewModel.updateSearchQuery("") }) {
                                        Icon(Icons.Default.Clear, contentDescription = "Clear")
                                    }
                                }
                            },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                                focusedBorderColor = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier
                                .width(200.dp)
                                .padding(end = 8.dp)
                                .height(50.dp)
                        )
                    }
                    if (isSpeaking) {
                        IconButton(onClick = { viewModel.stopSpeaking() }) {
                            Icon(
                                imageVector = Icons.Default.VolumeMute,
                                contentDescription = "Stop voice",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = currentScreen == AppScreen.Dashboard,
                    onClick = { currentScreen = AppScreen.Dashboard },
                    label = { Text("Dashboard") },
                    icon = { Icon(Icons.Default.Assessment, contentDescription = "Dashboard") },
                    modifier = Modifier.testTag("nav_dashboard")
                )
                NavigationBarItem(
                    selected = currentScreen == AppScreen.Timeline,
                    onClick = { currentScreen = AppScreen.Timeline },
                    label = { Text("Timeline") },
                    icon = { Icon(Icons.Default.History, contentDescription = "Timeline") },
                    modifier = Modifier.testTag("nav_timeline")
                )
                NavigationBarItem(
                    selected = currentScreen == AppScreen.Principles,
                    onClick = { currentScreen = AppScreen.Principles },
                    label = { Text("Principles") },
                    icon = { Icon(Icons.Default.Lightbulb, contentDescription = "Principles") },
                    modifier = Modifier.testTag("nav_principles")
                )
                NavigationBarItem(
                    selected = currentScreen == AppScreen.Library,
                    onClick = { currentScreen = AppScreen.Library },
                    label = { Text("Library") },
                    icon = { Icon(Icons.Default.AutoStories, contentDescription = "Library") },
                    modifier = Modifier.testTag("nav_library")
                )
                NavigationBarItem(
                    selected = currentScreen == AppScreen.AiLabs,
                    onClick = { currentScreen = AppScreen.AiLabs },
                    label = { Text("AI Labs") },
                    icon = { Icon(Icons.Default.Psychology, contentDescription = "AI Labs") },
                    modifier = Modifier.testTag("nav_ailabs")
                )
            }
        },
        floatingActionButton = {
            when (currentScreen) {
                AppScreen.Timeline -> {
                    ExtendedFloatingActionButton(
                        onClick = { showAddJournalDialog = true },
                        icon = { Icon(Icons.Default.Add, contentDescription = "Log Choice") },
                        text = { Text("Log Action") },
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.testTag("fab_add_journal")
                    )
                }
                AppScreen.Principles -> {
                    ExtendedFloatingActionButton(
                        onClick = { showAddPrincipleDialog = true },
                        icon = { Icon(Icons.Default.Add, contentDescription = "Build Principle") },
                        text = { Text("Build Principle") },
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.testTag("fab_add_principle")
                    )
                }
                AppScreen.Library -> {
                    ExtendedFloatingActionButton(
                        onClick = { showAddGuideDialog = true },
                        icon = { Icon(Icons.Default.Add, contentDescription = "Add Guide") },
                        text = { Text("Add Guide") },
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.testTag("fab_add_guide")
                    )
                }
                AppScreen.AiLabs -> {
                    when (activeLabTab) {
                        AiLabTab.Observations -> {
                            ExtendedFloatingActionButton(
                                onClick = { showAddObservationDialog = true },
                                icon = { Icon(Icons.Default.Add, contentDescription = "Add Observation") },
                                text = { Text("Record Observe") },
                                containerColor = MaterialTheme.colorScheme.secondary,
                                contentColor = MaterialTheme.colorScheme.onSecondary,
                                modifier = Modifier.testTag("fab_add_observation")
                            )
                        }
                        AiLabTab.Decisions -> {
                            ExtendedFloatingActionButton(
                                onClick = { showAddDecisionDialog = true },
                                icon = { Icon(Icons.Default.Add, contentDescription = "Build Strategy") },
                                text = { Text("Compare Decision") },
                                containerColor = MaterialTheme.colorScheme.secondary,
                                contentColor = MaterialTheme.colorScheme.onSecondary,
                                modifier = Modifier.testTag("fab_add_decision")
                            )
                        }
                        else -> {}
                    }
                }
                else -> {}
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                AppScreen.Dashboard -> DashboardScreen(viewModel, onAddHabitClick = { showAddHabitDialog = true })
                AppScreen.Timeline -> TimelineScreen(viewModel)
                AppScreen.Principles -> PrinciplesScreen(viewModel)
                AppScreen.Library -> LibraryScreen(viewModel)
                AppScreen.AiLabs -> AiLabsScreen(
                    viewModel = viewModel,
                    activeTab = activeLabTab,
                    onTabSelected = { activeLabTab = it }
                )
            }
        }
    }

    // --- DIALOG MODALS ---

    if (showAddJournalDialog) {
        AddJournalDialog(
            onDismiss = { showAddJournalDialog = false },
            onSave = { title, action, motivation, expected, actual, lessons, tags, cat, alt, reason, voluntary, values, diff, diffReason ->
                viewModel.addJournal(title, action, motivation, expected, actual, lessons, tags, cat, alt, reason, voluntary, values, diff, diffReason)
                showAddJournalDialog = false
            }
        )
    }

    if (showAddPrincipleDialog) {
        AddPrincipleDialog(
            onDismiss = { showAddPrincipleDialog = false },
            onSave = { title, type, content, tags, category ->
                viewModel.addKnowledge(title, type, content, tags, category)
                showAddPrincipleDialog = false
            }
        )
    }

    if (showAddGuideDialog) {
        AddGuideDialog(
            onDismiss = { showAddGuideDialog = false },
            onSave = { topic, title, steps, mistakes, tips, examples, resources ->
                viewModel.addLibraryEntry(topic, title, steps, mistakes, tips, examples, resources)
                showAddGuideDialog = false
            }
        )
    }

    if (showAddObservationDialog) {
        AddObservationDialog(
            onDismiss = { showAddObservationDialog = false },
            onSave = { title, description, causeEffect, category ->
                viewModel.addObservation(title, description, causeEffect, category)
                showAddObservationDialog = false
            }
        )
    }

    if (showAddDecisionDialog) {
        AddDecisionDialog(
            onDismiss = { showAddDecisionDialog = false },
            onSave = { contextText, pastDecisions, outcomes, runAi ->
                viewModel.addDecision(contextText, pastDecisions, outcomes, runAi)
                showAddDecisionDialog = false
            }
        )
    }

    if (showAddHabitDialog) {
        AddHabitDialog(
            onDismiss = { showAddHabitDialog = false },
            onSave = { title, category, target ->
                viewModel.addHabit(title, category, target)
                showAddHabitDialog = false
            }
        )
    }
}

// ==========================================
// 1. DASHBOARD & ANALYTICS SCREEN
// ==========================================
@Composable
fun DashboardScreen(viewModel: ActionWisdomViewModel, onAddHabitClick: () -> Unit) {
    val journals by viewModel.journals.collectAsStateWithLifecycle()
    val habits by viewModel.habits.collectAsStateWithLifecycle()
    val observations by viewModel.observations.collectAsStateWithLifecycle()
    val decisions by viewModel.decisions.collectAsStateWithLifecycle()
    val personalizedSummary by viewModel.personalizedSummary.collectAsStateWithLifecycle()
    val isSummaryLoading by viewModel.isSummaryLoading.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // Calculations
    val totalActions = journals.size
    val voluntaryCount = journals.count { it.isVoluntary }
    val voluntarinessRate = if (totalActions > 0) (voluntaryCount.toFloat() / totalActions * 100).toInt() else 0
    val regretCount = journals.count { it.wouldChooseDifferently == true }
    val regretIndex = if (totalActions > 0) (regretCount.toFloat() / totalActions * 100).toInt() else 0
    val averageDecisionQuality = if (decisions.isNotEmpty()) decisions.map { it.rating }.average() else 0.0

    val categoryCounts = journals.groupBy { it.category }.mapValues { it.value.size }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // Welcome Hero Card
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Wisdom Analytics",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Analyze your experiences, choices, and habits. Harness patterns to make optimal decisions.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .background(MaterialTheme.colorScheme.primary, shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.TrendingUp,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
        }

        item {
            // Metrics grid
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                MetricCard(
                    title = "Total Actions",
                    value = "$totalActions",
                    subtitle = "Logged in journal",
                    icon = Icons.Default.History,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "Choice Autonomy",
                    value = "$voluntarinessRate%",
                    subtitle = "Voluntary actions",
                    icon = Icons.Default.AccessibilityNew,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                MetricCard(
                    title = "Regret Index",
                    value = "$regretIndex%",
                    subtitle = "Would change choice",
                    icon = Icons.Default.Warning,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "Strategy Score",
                    value = String.format(Locale.getDefault(), "%.1f/5.0", averageDecisionQuality),
                    subtitle = "Avg decision quality",
                    icon = Icons.Default.ThumbUp,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Custom Donut Chart for Categories
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Experience Categories",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    CategoryDonutChart(categoryCounts)
                }
            }
        }

        // Habits List & Chart
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Habits & Daily Practices",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        IconButton(onClick = onAddHabitClick) {
                            Icon(Icons.Default.AddCircle, contentDescription = "Add Habit", tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    if (habits.isEmpty()) {
                        Text(
                            text = "No habits tracked yet. Record routine daily practices (e.g., Deep Work, Meditation) to measure progress.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(vertical = 12.dp)
                        )
                    } else {
                        HabitsCompletionsBarChart(habits)
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            habits.forEach { habit ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(MaterialTheme.colorScheme.background, RoundedCornerShape(8.dp))
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(habit.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                                        Text("Streak: ${habit.activeStreak} days | Total: ${habit.totalCompletions}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                    Button(
                                        onClick = { viewModel.completeHabit(habit) },
                                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                        modifier = Modifier.testTag("complete_habit_${habit.id}")
                                    ) {
                                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Done", fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // AI Personalized Learning Path Section
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Psychology, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(28.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "AI Wisdom Path Generator",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Generates a personalized synthesis audit and 14-day growth path by studying your decision-making regret indicators and recorded principles.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    if (isSummaryLoading) {
                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                        }
                    } else if (personalizedSummary != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                                .padding(16.dp)
                        ) {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("PERSONAL ROADMAP", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, fontSize = 12.sp)
                                    IconButton(
                                        onClick = { viewModel.speakText(personalizedSummary!!, context) },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(Icons.Default.VolumeUp, contentDescription = "Listen path", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = personalizedSummary!!,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    Button(
                        onClick = { viewModel.generatePersonalizedSummary() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("generate_path_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Synthesize My Personal Wisdom Path")
                    }
                }
            }
        }
    }
}

@Composable
fun MetricCard(
    title: String,
    value: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(title, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = color)
            Spacer(modifier = Modifier.height(4.dp))
            Text(subtitle, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun CategoryDonutChart(data: Map<String, Int>) {
    if (data.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("No category distributions logged yet. Log actions with custom categories.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        return
    }

    val total = data.values.sum().toFloat()
    val colors = listOf(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.secondary,
        MaterialTheme.colorScheme.tertiary,
        MaterialTheme.colorScheme.error,
        Color(0xFFEAB308),
        Color(0xFFEC4899),
        Color(0xFF3B82F6),
        Color(0xFF8B5CF6)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(100.dp), contentAlignment = Alignment.Center) {
            Canvas(modifier = Modifier.size(80.dp)) {
                var startAngle = 0f
                data.entries.forEachIndexed { index, entry ->
                    val sweepAngle = (entry.value / total) * 360f
                    val color = colors[index % colors.size]
                    drawArc(
                        color = color,
                        startAngle = startAngle,
                        sweepAngle = sweepAngle,
                        useCenter = false,
                        style = Stroke(width = 16f)
                    )
                    startAngle += sweepAngle
                }
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("${total.toInt()}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text("Total", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            data.entries.forEachIndexed { index, entry ->
                val color = colors[index % colors.size]
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier
                        .size(10.dp)
                        .background(color, shape = CircleShape))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${entry.key} (${entry.value})",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
fun HabitsCompletionsBarChart(habits: List<HabitTracker>) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val barColor = MaterialTheme.colorScheme.secondaryContainer
    val labelColor = MaterialTheme.colorScheme.onSurface

    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
        Canvas(modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
        ) {
            val barWidth = 32.dp.toPx()
            val spacing = 24.dp.toPx()
            val maxCompletions = habits.maxOf { it.totalCompletions }.toFloat().coerceAtLeast(1f)

            habits.forEachIndexed { index, habit ->
                val barHeight = (habit.totalCompletions / maxCompletions) * size.height * 0.8f
                val x = index * (barWidth + spacing) + spacing
                val y = size.height - barHeight

                // Draw background bar
                drawRect(
                    color = barColor,
                    topLeft = Offset(x, 0f),
                    size = Size(barWidth, size.height)
                )

                // Draw solid completion bar
                drawRect(
                    color = primaryColor,
                    topLeft = Offset(x, y),
                    size = Size(barWidth, barHeight)
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            habits.forEach { habit ->
                Text(
                    text = if (habit.title.length > 8) habit.title.take(6) + ".." else habit.title,
                    style = MaterialTheme.typography.labelSmall,
                    color = labelColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.width(60.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

// ==========================================
// 2. ACTION JOURNAL TIMELINE SCREEN
// ==========================================
@Composable
fun TimelineScreen(viewModel: ActionWisdomViewModel) {
    val journals by viewModel.filteredJournals.collectAsStateWithLifecycle()
    var selectedCategory by remember { mutableStateOf("All") }
    val categories = listOf("All", "Communication", "Business", "Technology", "Health", "Productivity", "Finance", "Relationships", "Problem Solving")

    val displayedJournals = if (selectedCategory == "All") journals else journals.filter { it.category == selectedCategory }

    Column(modifier = Modifier.fillMaxSize()) {
        // Categories Horizontal scroll
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.forEach { cat ->
                FilterChip(
                    selected = selectedCategory == cat,
                    onClick = { selectedCategory = cat },
                    label = { Text(cat) }
                )
            }
        }

        if (displayedJournals.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(32.dp)) {
                    Icon(Icons.Default.History, contentDescription = null, modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.outlineVariant)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("No actions logged yet", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Click 'Log Action' below to record a voluntary action, its outcome, and run a freedom of choice reflection.",
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(displayedJournals) { journal ->
                    ActionJournalTimelineCard(journal, onDelete = { viewModel.deleteJournal(journal) })
                }
            }
        }
    }
}

@Composable
fun ActionJournalTimelineCard(journal: ActionJournal, onDelete: () -> Unit) {
    val dateStr = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(journal.timestamp))
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(
                                color = if (journal.isVoluntary) MaterialTheme.colorScheme.primary else Color.Gray,
                                shape = CircleShape
                            )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = journal.category,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Text(dateStr, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(journal.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Action: ${journal.actionTaken}",
                style = MaterialTheme.typography.bodyMedium,
                maxLines = if (expanded) Int.MAX_VALUE else 2,
                overflow = TextOverflow.Ellipsis
            )

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    Divider(color = MaterialTheme.colorScheme.outlineVariant)
                    Spacer(modifier = Modifier.height(12.dp))

                    Text("JOURNAL ANALYSIS", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Motivation:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                    Text(journal.motivation, style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Expected Outcome:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                    Text(journal.expectedOutcome, style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Actual Outcome:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                    Text(journal.actualOutcome, style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Lessons Learned:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                    Text(journal.lessonsLearned, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)

                    Spacer(modifier = Modifier.height(16.dp))

                    // Reflection Section
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Column {
                            Text(
                                text = "FREEDOM OF CHOICE REFLECTION",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("What other alternatives existed?", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                            Text(journal.alternatives.ifEmpty { "None recorded" }, style = MaterialTheme.typography.bodyMedium)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Was this choice made voluntary?", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                            Text(if (journal.isVoluntary) "Yes, completed with full autonomy." else "No, influenced heavily by external conditions.", style = MaterialTheme.typography.bodyMedium)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Why did you make this choice?", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                            Text(journal.choiceReason.ifEmpty { "Not detailed" }, style = MaterialTheme.typography.bodyMedium)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Values that influenced this choice:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                            Text(journal.valuesInfluenced.ifEmpty { "Not detailed" }, style = MaterialTheme.typography.bodyMedium)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Would you make this choice differently today?", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                            val choiceDiff = when (journal.wouldChooseDifferently) {
                                true -> "Yes. Lesson: ${journal.wouldChooseDifferentlyReason}"
                                false -> "No, I would choose the exact same. Justification: ${journal.wouldChooseDifferentlyReason}"
                                null -> "Not sure/Still validating."
                            }
                            Text(choiceDiff, style = MaterialTheme.typography.bodyMedium)
                        }
                    }

                    if (journal.tags.isNotBlank()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.horizontalScroll(rememberScrollState())) {
                            journal.tags.split(",").forEach { tag ->
                                Box(
                                    modifier = Modifier
                                        .background(MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(4.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(tag.trim(), style = MaterialTheme.typography.labelSmall)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        TextButton(
                            onClick = onDelete,
                            colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Delete Log")
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 3. KNOWLEDGE BUILDER SCREEN
// ==========================================
@Composable
fun PrinciplesScreen(viewModel: ActionWisdomViewModel) {
    val principles by viewModel.filteredPrinciples.collectAsStateWithLifecycle()
    var selectedType by remember { mutableStateOf("All") }
    val types = listOf("All", "PRINCIPLE", "RULE_OF_THUMB", "BEST_PRACTICE", "MISTAKE_TO_AVOID", "FRAMEWORK", "CHECKLIST")

    val displayedPrinciples = if (selectedType == "All") principles else principles.filter { it.type == selectedType }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            types.forEach { t ->
                FilterChip(
                    selected = selectedType == t,
                    onClick = { selectedType = t },
                    label = { Text(t.replace("_", " ")) }
                )
            }
        }

        if (displayedPrinciples.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(32.dp)) {
                    Icon(Icons.Default.Lightbulb, contentDescription = null, modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.outlineVariant)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("No principles built yet", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Synthesize principles and lessons from your real-world experiments. Group them as principles, best practices, or checklists.",
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(1),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(displayedPrinciples) { kb ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(4.dp))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = kb.type.replace("_", " "),
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                IconButton(onClick = { viewModel.deleteKnowledge(kb) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(18.dp))
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(kb.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(kb.content, style = MaterialTheme.typography.bodyMedium)
                            
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                Text("Category: ${kb.category}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                if (kb.tags.isNotBlank()) {
                                    Text("Tags: ${kb.tags}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 4. KNOW-HOW LIBRARY SCREEN
// ==========================================
@Composable
fun LibraryScreen(viewModel: ActionWisdomViewModel) {
    val libraryEntries by viewModel.libraryEntries.collectAsStateWithLifecycle()
    var selectedTopic by remember { mutableStateOf("Communication") }
    val topics = listOf("Communication", "Business", "Technology", "Health", "Productivity", "Finance", "Relationships", "Problem Solving")

    val displayedEntries = libraryEntries.filter { it.topic == selectedTopic }

    Row(modifier = Modifier.fillMaxSize()) {
        // Vertical Topic navigation
        Column(
            modifier = Modifier
                .width(110.dp)
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.surface)
                .verticalScroll(rememberScrollState())
                .padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            topics.forEach { topic ->
                val isSelected = selectedTopic == topic
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedTopic = topic }
                        .background(if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent)
                        .padding(vertical = 12.dp, horizontal = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = topic,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        VerticalDivider(color = MaterialTheme.colorScheme.outlineVariant)

        // Contents
        if (displayedEntries.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "No custom guides added for $selectedTopic. Add one!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(displayedEntries) { entry ->
                    var showFull by remember { mutableStateOf(false) }
                    val context = LocalContext.current

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(entry.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                    Text("Topic: ${entry.topic}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                Row {
                                    IconButton(onClick = { viewModel.speakText(entry.title + ". Process: " + entry.processSteps, context) }) {
                                        Icon(Icons.Default.VolumeUp, contentDescription = "Listen guide")
                                    }
                                    if (entry.isUserCreated) {
                                        IconButton(onClick = { viewModel.deleteLibraryEntry(entry.id) }) {
                                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                                        }
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Process & Steps:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                            Text(entry.processSteps, style = MaterialTheme.typography.bodyMedium)

                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = { showFull = !showFull },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer, contentColor = MaterialTheme.colorScheme.onPrimaryContainer)
                            ) {
                                Text(if (showFull) "Collapse Details" else "View Common Mistakes & Tips")
                            }

                            AnimatedVisibility(visible = showFull) {
                                Column(modifier = Modifier.padding(top = 12.dp)) {
                                    Divider(color = MaterialTheme.colorScheme.outlineVariant)
                                    Spacer(modifier = Modifier.height(12.dp))

                                    Text("Common Mistakes:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
                                    Text(entry.commonMistakes, style = MaterialTheme.typography.bodyMedium)
                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text("Pro Tips:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                                    Text(entry.tips, style = MaterialTheme.typography.bodyMedium)
                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text("Real World Examples:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                                    Text(entry.realExamples, style = MaterialTheme.typography.bodyMedium)
                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text("Resources / Books:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                                    Text(entry.resources, style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 5. AI LABS SCREEN (OBSERVATIONS, DECISIONS, CHAT)
// ==========================================
@Composable
fun AiLabsScreen(
    viewModel: ActionWisdomViewModel,
    activeTab: AiLabTab,
    onTabSelected: (AiLabTab) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = activeTab.ordinal) {
            Tab(selected = activeTab == AiLabTab.Chat, onClick = { onTabSelected(AiLabTab.Chat) }) {
                Text("AI Chat Assistant", modifier = Modifier.padding(12.dp), fontWeight = FontWeight.Bold)
            }
            Tab(selected = activeTab == AiLabTab.Observations, onClick = { onTabSelected(AiLabTab.Observations) }) {
                Text("Observations", modifier = Modifier.padding(12.dp), fontWeight = FontWeight.Bold)
            }
            Tab(selected = activeTab == AiLabTab.Decisions, onClick = { onTabSelected(AiLabTab.Decisions) }) {
                Text("Decisions Strategy", modifier = Modifier.padding(12.dp), fontWeight = FontWeight.Bold)
            }
        }

        when (activeTab) {
            AiLabTab.Chat -> AiChatTab(viewModel)
            AiLabTab.Observations -> ObservationsTab(viewModel)
            AiLabTab.Decisions -> DecisionsTab(viewModel)
        }
    }
}

@Composable
fun AiChatTab(viewModel: ActionWisdomViewModel) {
    val chatHistory by viewModel.chatHistory.collectAsStateWithLifecycle()
    val isChatLoading by viewModel.isChatLoading.collectAsStateWithLifecycle()
    var messageText by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Chat messages
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(chatHistory) { (sender, text) ->
                val isModel = sender == "model"
                val alignment = if (isModel) Alignment.Start else Alignment.End
                val containerColor = if (isModel) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.primaryContainer
                val textColor = if (isModel) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onPrimaryContainer

                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = alignment) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = if (isModel) Arrangement.Start else Arrangement.End
                    ) {
                        if (isModel) {
                            Icon(Icons.Default.Psychology, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(end = 4.dp).size(20.dp))
                        }
                        Text(
                            text = if (isModel) "Action Wisdom AI" else "Me",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(
                                topStart = 16.dp,
                                topEnd = 16.dp,
                                bottomStart = if (isModel) 0.dp else 16.dp,
                                bottomEnd = if (isModel) 16.dp else 0.dp
                            ))
                            .background(containerColor)
                            .padding(16.dp)
                            .widthIn(max = 280.dp)
                    ) {
                        Column {
                            Text(text = text, style = MaterialTheme.typography.bodyMedium, color = textColor)
                            if (isModel) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                                    IconButton(
                                        onClick = { viewModel.speakText(text, context) },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(Icons.Default.VolumeUp, contentDescription = "Listen answer", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (isChatLoading) {
                item {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Message input row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = messageText,
                onValueChange = { messageText = it },
                placeholder = { Text("Ask about your journals, principles, or rules...") },
                modifier = Modifier
                    .weight(1f)
                    .testTag("chat_input_field"),
                shape = RoundedCornerShape(24.dp)
            )
            IconButton(
                onClick = {
                    viewModel.sendChatMessage(messageText)
                    messageText = ""
                },
                modifier = Modifier
                    .size(48.dp)
                    .background(MaterialTheme.colorScheme.primary, shape = CircleShape)
                    .testTag("chat_send_button")
            ) {
                Icon(Icons.Default.Send, contentDescription = "Send", tint = MaterialTheme.colorScheme.onPrimary)
            }
            IconButton(
                onClick = { viewModel.clearChat() },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(Icons.Default.Refresh, contentDescription = "Clear History", tint = MaterialTheme.colorScheme.outline)
            }
        }
    }
}

@Composable
fun ObservationsTab(viewModel: ActionWisdomViewModel) {
    val observations by viewModel.observations.collectAsStateWithLifecycle()
    val isAnalyzingObservation by viewModel.isAnalyzingObservation.collectAsStateWithLifecycle()
    val context = LocalContext.current

    if (observations.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(32.dp)) {
                Icon(Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.outlineVariant)
                Spacer(modifier = Modifier.height(16.dp))
                Text("No observations recorded yet", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Record events occurring around you, human behavior patterns, or market changes. Use Gemini to find hidden system models.",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(observations) { obs ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(4.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(obs.category, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSecondaryContainer)
                            }
                            IconButton(onClick = { viewModel.deleteObservation(obs) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(obs.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Observation Details:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                        Text(obs.description, style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Perceived Cause and Effect:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                        Text(obs.causeAndEffect, style = MaterialTheme.typography.bodyMedium)

                        if (obs.aiPatternSummary != null) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                                    .padding(12.dp)
                            ) {
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.Psychology, contentDescription = null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("AI PATTERN ANALYSIS", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
                                        }
                                        IconButton(
                                            onClick = { viewModel.speakText(obs.aiPatternSummary, context) },
                                            modifier = Modifier.size(24.dp)
                                        ) {
                                            Icon(Icons.Default.VolumeUp, contentDescription = "Listen analysis", tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(18.dp))
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(obs.aiPatternSummary, style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = { viewModel.searchObservationPatterns(obs) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("analyze_pattern_${obs.id}"),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                            enabled = isAnalyzingObservation != obs.id
                        ) {
                            if (isAnalyzingObservation == obs.id) {
                                CircularProgressIndicator(color = MaterialTheme.colorScheme.onSecondary, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Analyzing Cause & Effect...")
                            } else {
                                Icon(Icons.Default.Refresh, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(if (obs.aiPatternSummary != null) "Refresh AI Pattern Search" else "AI Pattern Search")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DecisionsTab(viewModel: ActionWisdomViewModel) {
    val decisions by viewModel.decisions.collectAsStateWithLifecycle()
    val isAnalyzingDecision by viewModel.isAnalyzingDecision.collectAsStateWithLifecycle()
    val context = LocalContext.current

    if (decisions.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(32.dp)) {
                Icon(Icons.Default.Psychology, contentDescription = null, modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.outlineVariant)
                Spacer(modifier = Modifier.height(16.dp))
                Text("No decisions strategies compared yet", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Click 'Compare Decision' to outline a current decision context, note past mistakes, and receive structured AI suggestions.",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (isAnalyzingDecision) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f))
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.secondary)
                            Spacer(modifier = Modifier.width(16.dp))
                            Text("Consulting game theorist AI & mitigating risks...", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }

            items(decisions) { decision ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Strategy Builder", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                            IconButton(onClick = { viewModel.deleteDecision(decision) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Decision Context:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                        Text(decision.context, style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Related Past Decisions / Lessons:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                        Text(decision.pastDecisionsCompared, style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Future Outcomes & Risks:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                        Text(decision.possibleFutureOutcomes, style = MaterialTheme.typography.bodyMedium)

                        if (decision.aiRecommendations != null) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                                    .padding(12.dp)
                            ) {
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.Psychology, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("AI RECOMMENDATIONS", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                                        }
                                        IconButton(
                                            onClick = { viewModel.speakText(decision.aiRecommendations, context) },
                                            modifier = Modifier.size(24.dp)
                                        ) {
                                            Icon(Icons.Default.VolumeUp, contentDescription = "Listen decisions", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(decision.aiRecommendations, style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 6. DETAILED FORM DIALOG MODALS
// ==========================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddJournalDialog(
    onDismiss: () -> Unit,
    onSave: (
        title: String,
        actionTaken: String,
        motivation: String,
        expectedOutcome: String,
        actualOutcome: String,
        lessonsLearned: String,
        tags: String,
        category: String,
        alternatives: String,
        choiceReason: String,
        isVoluntary: Boolean,
        valuesInfluenced: String,
        wouldChooseDifferently: Boolean?,
        wouldChooseDifferentlyReason: String
    ) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var actionTaken by remember { mutableStateOf("") }
    var motivation by remember { mutableStateOf("") }
    var expectedOutcome by remember { mutableStateOf("") }
    var actualOutcome by remember { mutableStateOf("") }
    var lessonsLearned by remember { mutableStateOf("") }
    var tags by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Communication") }

    // Reflections
    var alternatives by remember { mutableStateOf("") }
    var choiceReason by remember { mutableStateOf("") }
    var isVoluntary by remember { mutableStateOf(true) }
    var valuesInfluenced by remember { mutableStateOf("") }
    var wouldChooseDifferently by remember { mutableStateOf<Boolean?>(null) }
    var wouldChooseDifferentlyReason by remember { mutableStateOf("") }

    val categories = listOf("Communication", "Business", "Technology", "Health", "Productivity", "Finance", "Relationships", "Problem Solving")

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                TopAppBar(
                    title = { Text("Log Choice & Reflection") },
                    navigationIcon = {
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        TextButton(
                            onClick = {
                                if (title.isNotBlank() && actionTaken.isNotBlank()) {
                                    onSave(
                                        title, actionTaken, motivation, expectedOutcome, actualOutcome,
                                        lessonsLearned, tags, category, alternatives, choiceReason,
                                        isVoluntary, valuesInfluenced, wouldChooseDifferently, wouldChooseDifferentlyReason
                                    )
                                }
                            },
                            modifier = Modifier.testTag("submit_journal_button")
                        ) {
                            Text("Save", fontWeight = FontWeight.Bold)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("CORE ACTION DETAILS", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.titleSmall)

                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Short Action Title (e.g., Renegotiated Tech Contract)") },
                        modifier = Modifier.fillMaxWidth().testTag("journal_title_input"),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = actionTaken,
                        onValueChange = { actionTaken = it },
                        label = { Text("What action did you take?") },
                        modifier = Modifier.fillMaxWidth().height(100.dp).testTag("journal_action_input"),
                        maxLines = 4
                    )

                    // Category dropdown selector
                    Text("Category:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        categories.forEach { cat ->
                            FilterChip(
                                selected = category == cat,
                                onClick = { category = cat },
                                label = { Text(cat) }
                            )
                        }
                    }

                    OutlinedTextField(
                        value = motivation,
                        onValueChange = { motivation = it },
                        label = { Text("Motivation (Why did you choose to act?)") },
                        modifier = Modifier.fillMaxWidth().height(80.dp),
                        maxLines = 3
                    )

                    OutlinedTextField(
                        value = expectedOutcome,
                        onValueChange = { expectedOutcome = it },
                        label = { Text("Expected Outcome") },
                        modifier = Modifier.fillMaxWidth().height(80.dp),
                        maxLines = 3
                    )

                    OutlinedTextField(
                        value = actualOutcome,
                        onValueChange = { actualOutcome = it },
                        label = { Text("Actual Outcome") },
                        modifier = Modifier.fillMaxWidth().height(80.dp).testTag("journal_actual_outcome_input"),
                        maxLines = 3
                    )

                    OutlinedTextField(
                        value = lessonsLearned,
                        onValueChange = { lessonsLearned = it },
                        label = { Text("Lessons Learned") },
                        modifier = Modifier.fillMaxWidth().height(100.dp).testTag("journal_lessons_input"),
                        maxLines = 4
                    )

                    OutlinedTextField(
                        value = tags,
                        onValueChange = { tags = it },
                        label = { Text("Tags (Comma separated: strategy, career, focus)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Text("FREEDOM OF CHOICE REFLECTION", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.titleSmall)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Was this choice completely voluntary?", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                            Text("Unvoluntary choices are forced by external environments.", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                        }
                        Switch(checked = isVoluntary, onCheckedChange = { isVoluntary = it })
                    }

                    OutlinedTextField(
                        value = alternatives,
                        onValueChange = { alternatives = it },
                        label = { Text("What alternative choices existed?") },
                        modifier = Modifier.fillMaxWidth().height(80.dp),
                        maxLines = 3
                    )

                    OutlinedTextField(
                        value = choiceReason,
                        onValueChange = { choiceReason = it },
                        label = { Text("Why did you pick this choice over alternatives?") },
                        modifier = Modifier.fillMaxWidth().height(80.dp),
                        maxLines = 3
                    )

                    OutlinedTextField(
                        value = valuesInfluenced,
                        onValueChange = { valuesInfluenced = it },
                        label = { Text("What universal values influenced this (trust, speed, peace?)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Text("Would you choose differently now?", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = { wouldChooseDifferently = true },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (wouldChooseDifferently == true) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.surfaceVariant,
                                contentColor = if (wouldChooseDifferently == true) MaterialTheme.colorScheme.onError else MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Yes")
                        }
                        Button(
                            onClick = { wouldChooseDifferently = false },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (wouldChooseDifferently == false) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                contentColor = if (wouldChooseDifferently == false) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("No")
                        }
                        Button(
                            onClick = { wouldChooseDifferently = null },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (wouldChooseDifferently == null) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.surfaceVariant,
                                contentColor = if (wouldChooseDifferently == null) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Unsure")
                        }
                    }

                    OutlinedTextField(
                        value = wouldChooseDifferentlyReason,
                        onValueChange = { wouldChooseDifferentlyReason = it },
                        label = { Text("Justification (Why or why not?)") },
                        modifier = Modifier.fillMaxWidth().height(80.dp),
                        maxLines = 3
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPrincipleDialog(
    onDismiss: () -> Unit,
    onSave: (title: String, type: String, content: String, tags: String, category: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("PRINCIPLE") }
    var content by remember { mutableStateOf("") }
    var tags by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Communication") }

    val types = listOf("PRINCIPLE", "RULE_OF_THUMB", "BEST_PRACTICE", "MISTAKE_TO_AVOID", "FRAMEWORK", "CHECKLIST")
    val categories = listOf("Communication", "Business", "Technology", "Health", "Productivity", "Finance", "Relationships", "Problem Solving")

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Build Personal Principle", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title (e.g., Fast Feedback Loops)") },
                    modifier = Modifier.fillMaxWidth().testTag("principle_title_input"),
                    singleLine = true
                )

                // Type selector
                Text("Type:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    types.forEach { t ->
                        FilterChip(
                            selected = type == t,
                            onClick = { type = t },
                            label = { Text(t.replace("_", " ")) }
                        )
                    }
                }

                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Core Principle description / rules...") },
                    modifier = Modifier.fillMaxWidth().height(100.dp).testTag("principle_content_input"),
                    maxLines = 4
                )

                // Category selector
                Text("Category:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    categories.forEach { cat ->
                        FilterChip(
                            selected = category == cat,
                            onClick = { category = cat },
                            label = { Text(cat) }
                        )
                    }
                }

                OutlinedTextField(
                    value = tags,
                    onValueChange = { tags = it },
                    label = { Text("Tags (comma separated)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (title.isNotBlank() && content.isNotBlank()) {
                                onSave(title, type, content, tags, category)
                            }
                        },
                        modifier = Modifier.testTag("submit_principle_button")
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddGuideDialog(
    onDismiss: () -> Unit,
    onSave: (topic: String, title: String, steps: String, mistakes: String, tips: String, examples: String, resources: String) -> Unit
) {
    var topic by remember { mutableStateOf("Communication") }
    var title by remember { mutableStateOf("") }
    var steps by remember { mutableStateOf("") }
    var mistakes by remember { mutableStateOf("") }
    var tips by remember { mutableStateOf("") }
    var examples by remember { mutableStateOf("") }
    var resources by remember { mutableStateOf("") }

    val topics = listOf("Communication", "Business", "Technology", "Health", "Productivity", "Finance", "Relationships", "Problem Solving")

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                TopAppBar(
                    title = { Text("Create Custom Know-How Guide") },
                    navigationIcon = {
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        TextButton(
                            onClick = {
                                if (title.isNotBlank() && steps.isNotBlank()) {
                                    onSave(topic, title, steps, mistakes, tips, examples, resources)
                                }
                            },
                            modifier = Modifier.testTag("submit_guide_button")
                        ) {
                            Text("Create", fontWeight = FontWeight.Bold)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("Topic:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        topics.forEach { t ->
                            FilterChip(
                                selected = topic == t,
                                onClick = { topic = t },
                                label = { Text(t) }
                            )
                        }
                    }

                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Guide Title (e.g., Active Listening for Negotiations)") },
                        modifier = Modifier.fillMaxWidth().testTag("guide_title_input"),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = steps,
                        onValueChange = { steps = it },
                        label = { Text("Process Steps (numbered line by line)") },
                        modifier = Modifier.fillMaxWidth().height(120.dp).testTag("guide_steps_input"),
                        maxLines = 6
                    )

                    OutlinedTextField(
                        value = mistakes,
                        onValueChange = { mistakes = it },
                        label = { Text("Common Mistakes (bulleted list)") },
                        modifier = Modifier.fillMaxWidth().height(100.dp),
                        maxLines = 4
                    )

                    OutlinedTextField(
                        value = tips,
                        onValueChange = { tips = it },
                        label = { Text("Pro Tips (bulleted list)") },
                        modifier = Modifier.fillMaxWidth().height(100.dp),
                        maxLines = 4
                    )

                    OutlinedTextField(
                        value = examples,
                        onValueChange = { examples = it },
                        label = { Text("Real World Examples") },
                        modifier = Modifier.fillMaxWidth().height(80.dp),
                        maxLines = 3
                    )

                    OutlinedTextField(
                        value = resources,
                        onValueChange = { resources = it },
                        label = { Text("Books or Article Resources") },
                        modifier = Modifier.fillMaxWidth().height(80.dp),
                        maxLines = 3
                    )
                }
            }
        }
    }
}

@Composable
fun AddObservationDialog(
    onDismiss: () -> Unit,
    onSave: (title: String, description: String, causeEffect: String, category: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var causeEffect by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Human Behavior") }

    val categories = listOf("Human Behavior", "Market Trends", "Social", "Nature", "Cause & Effect")

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Record Real-World Observation", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Observation Title (e.g., Commuters on Subway)") },
                    modifier = Modifier.fillMaxWidth().testTag("observation_title_input"),
                    singleLine = true
                )

                Text("Category:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    categories.forEach { cat ->
                        FilterChip(
                            selected = category == cat,
                            onClick = { category = cat },
                            label = { Text(cat) }
                        )
                    }
                }

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("What did you observe? Describe behavior or trends...") },
                    modifier = Modifier.fillMaxWidth().height(100.dp).testTag("observation_desc_input"),
                    maxLines = 5
                )

                OutlinedTextField(
                    value = causeEffect,
                    onValueChange = { causeEffect = it },
                    label = { Text("Perceived Cause and Effect mechanics") },
                    modifier = Modifier.fillMaxWidth().height(80.dp),
                    maxLines = 4
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (title.isNotBlank() && description.isNotBlank()) {
                                onSave(title, description, causeEffect, category)
                            }
                        },
                        modifier = Modifier.testTag("submit_observation_button")
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
}

@Composable
fun AddDecisionDialog(
    onDismiss: () -> Unit,
    onSave: (contextText: String, pastDecisions: String, outcomes: String, runAi: Boolean) -> Unit
) {
    var contextText by remember { mutableStateOf("") }
    var pastDecisions by remember { mutableStateOf("") }
    var outcomes by remember { mutableStateOf("") }
    var runAi by remember { mutableStateOf(true) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Compare Decision Strategy", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

                OutlinedTextField(
                    value = contextText,
                    onValueChange = { contextText = it },
                    label = { Text("Current Decision Context (e.g., Buying house vs renting)") },
                    modifier = Modifier.fillMaxWidth().testTag("decision_context_input"),
                    maxLines = 4
                )

                OutlinedTextField(
                    value = pastDecisions,
                    onValueChange = { pastDecisions = it },
                    label = { Text("What past decisions relate to this? Lessons?") },
                    modifier = Modifier.fillMaxWidth().height(80.dp).testTag("decision_past_input"),
                    maxLines = 3
                )

                OutlinedTextField(
                    value = outcomes,
                    onValueChange = { outcomes = it },
                    label = { Text("Possible future outcomes and associated risks") },
                    modifier = Modifier.fillMaxWidth().height(80.dp),
                    maxLines = 3
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Consult Gemini Decision Analyst", style = MaterialTheme.typography.bodyMedium)
                    Switch(checked = runAi, onCheckedChange = { runAi = it })
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (contextText.isNotBlank()) {
                                onSave(contextText, pastDecisions, outcomes, runAi)
                            }
                        },
                        modifier = Modifier.testTag("submit_decision_button")
                    ) {
                        Text("Evaluate Strategy")
                    }
                }
            }
        }
    }
}

@Composable
fun AddHabitDialog(
    onDismiss: () -> Unit,
    onSave: (title: String, category: String, targetDaysPerWeek: Int) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Productivity") }
    var targetDays by remember { mutableStateOf(5) }

    val categories = listOf("Communication", "Business", "Technology", "Health", "Productivity", "Finance", "Relationships", "Problem Solving")

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Track Habit or Practice", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Habit Title (e.g., Deep Work block, HIIT)") },
                    modifier = Modifier.fillMaxWidth().testTag("habit_title_input"),
                    singleLine = true
                )

                Text("Category:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    categories.forEach { cat ->
                        FilterChip(
                            selected = category == cat,
                            onClick = { category = cat },
                            label = { Text(cat) }
                        )
                    }
                }

                Text("Target completions per week: $targetDays", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                Slider(
                    value = targetDays.toFloat(),
                    onValueChange = { targetDays = it.toInt() },
                    valueRange = 1f..7f,
                    steps = 5
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (title.isNotBlank()) {
                                onSave(title, category, targetDays)
                            }
                        },
                        modifier = Modifier.testTag("submit_habit_button")
                    ) {
                        Text("Add Tracker")
                    }
                }
            }
        }
    }
}
