package com.example.ui

import android.content.Context
import android.media.MediaPlayer
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class ActionWisdomViewModel(private val repository: ActionWisdomRepository) : ViewModel() {

    // --- State Streams ---
    val journals: StateFlow<List<ActionJournal>> = repository.allJournals
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val principles: StateFlow<List<KnowledgeBuilder>> = repository.allKnowledge
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val libraryEntries: StateFlow<List<KnowHowLibrary>> = repository.allLibraryEntries
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val observations: StateFlow<List<RealWorldObservation>> = repository.allObservations
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val decisions: StateFlow<List<DecisionImprovement>> = repository.allDecisions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val habits: StateFlow<List<HabitTracker>> = repository.allHabits
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- Search Query State ---
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // --- Filtered States (For Search) ---
    val filteredJournals: StateFlow<List<ActionJournal>> = combine(journals, searchQuery) { list, query ->
        if (query.isBlank()) list else {
            list.filter {
                it.title.contains(query, ignoreCase = true) ||
                it.actionTaken.contains(query, ignoreCase = true) ||
                it.lessonsLearned.contains(query, ignoreCase = true) ||
                it.category.contains(query, ignoreCase = true) ||
                it.tags.contains(query, ignoreCase = true)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredPrinciples: StateFlow<List<KnowledgeBuilder>> = combine(principles, searchQuery) { list, query ->
        if (query.isBlank()) list else {
            list.filter {
                it.title.contains(query, ignoreCase = true) ||
                it.content.contains(query, ignoreCase = true) ||
                it.category.contains(query, ignoreCase = true) ||
                it.type.contains(query, ignoreCase = true)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- AI Assistant Chat State ---
    private val _chatHistory = MutableStateFlow<List<Pair<String, String>>>(
        listOf("model" to "Hello! I am Action Wisdom AI. I can analyze your choices, suggest improvements based on your journal context, summarize observations, or answer any practical question. How can I guide you today?")
    )
    val chatHistory: StateFlow<List<Pair<String, String>>> = _chatHistory.asStateFlow()

    private val _isChatLoading = MutableStateFlow(false)
    val isChatLoading: StateFlow<Boolean> = _isChatLoading.asStateFlow()

    // --- AI Path State ---
    private val _personalizedSummary = MutableStateFlow<String?>(null)
    val personalizedSummary: StateFlow<String?> = _personalizedSummary.asStateFlow()

    private val _isSummaryLoading = MutableStateFlow(false)
    val isSummaryLoading: StateFlow<Boolean> = _isSummaryLoading.asStateFlow()

    // --- TTS Playback Management ---
    private var mediaPlayer: MediaPlayer? = null
    private var nativeTts: TextToSpeech? = null
    private var isNativeTtsReady = false

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    // --- CRUD: Action Journal & Reflection ---
    fun addJournal(
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
    ) {
        viewModelScope.launch {
            val journal = ActionJournal(
                title = title,
                actionTaken = actionTaken,
                motivation = motivation,
                expectedOutcome = expectedOutcome,
                actualOutcome = actualOutcome,
                lessonsLearned = lessonsLearned,
                tags = tags,
                category = category,
                alternatives = alternatives,
                choiceReason = choiceReason,
                isVoluntary = isVoluntary,
                valuesInfluenced = valuesInfluenced,
                wouldChooseDifferently = wouldChooseDifferently,
                wouldChooseDifferentlyReason = wouldChooseDifferentlyReason
            )
            repository.actionJournalDao.insertJournal(journal)
        }
    }

    fun deleteJournal(journal: ActionJournal) {
        viewModelScope.launch {
            repository.actionJournalDao.deleteJournal(journal)
        }
    }

    // --- CRUD: Knowledge Builder ---
    fun addKnowledge(title: String, type: String, content: String, tags: String, category: String) {
        viewModelScope.launch {
            val kb = KnowledgeBuilder(
                title = title,
                type = type,
                content = content,
                tags = tags,
                category = category
            )
            repository.knowledgeBuilderDao.insertKnowledge(kb)
        }
    }

    fun deleteKnowledge(kb: KnowledgeBuilder) {
        viewModelScope.launch {
            repository.knowledgeBuilderDao.deleteKnowledge(kb)
        }
    }

    // --- CRUD: Know-How Library Custom Entries ---
    fun addLibraryEntry(
        topic: String,
        title: String,
        steps: String,
        commonMistakes: String,
        tips: String,
        examples: String,
        resources: String
    ) {
        viewModelScope.launch {
            val entry = KnowHowLibrary(
                topic = topic,
                title = title,
                processSteps = steps,
                commonMistakes = commonMistakes,
                tips = tips,
                realExamples = examples,
                resources = resources,
                isUserCreated = true
            )
            repository.knowHowLibraryDao.insertEntry(entry)
        }
    }

    fun deleteLibraryEntry(id: Int) {
        viewModelScope.launch {
            repository.knowHowLibraryDao.deleteById(id)
        }
    }

    // --- CRUD: Observations & Pattern Search ---
    private val _isAnalyzingObservation = MutableStateFlow<Int?>(null) // Observation ID currently being analyzed
    val isAnalyzingObservation: StateFlow<Int?> = _isAnalyzingObservation.asStateFlow()

    fun addObservation(title: String, description: String, causeAndEffect: String, category: String) {
        viewModelScope.launch {
            val obs = RealWorldObservation(
                title = title,
                description = description,
                causeAndEffect = causeAndEffect,
                category = category
            )
            repository.realWorldObservationDao.insertObservation(obs)
        }
    }

    fun deleteObservation(obs: RealWorldObservation) {
        viewModelScope.launch {
            repository.realWorldObservationDao.deleteObservation(obs)
        }
    }

    fun searchObservationPatterns(obs: RealWorldObservation) {
        viewModelScope.launch {
            _isAnalyzingObservation.value = obs.id
            try {
                repository.analyzeObservationPattern(obs)
            } catch (e: Exception) {
                Log.e("ViewModel", "Pattern analysis failed", e)
            } finally {
                _isAnalyzingObservation.value = null
            }
        }
    }

    // --- CRUD: Decision Recommendation Builder ---
    private val _isAnalyzingDecision = MutableStateFlow(false)
    val isAnalyzingDecision: StateFlow<Boolean> = _isAnalyzingDecision.asStateFlow()

    fun addDecision(context: String, pastDecisions: String, outcomes: String, runAiAnalysis: Boolean = true) {
        viewModelScope.launch {
            _isAnalyzingDecision.value = runAiAnalysis
            try {
                val decision = DecisionImprovement(
                    context = context,
                    pastDecisionsCompared = pastDecisions,
                    possibleFutureOutcomes = outcomes
                )
                val id = repository.decisionImprovementDao.insertDecision(decision)
                
                if (runAiAnalysis) {
                    val savedDecision = decision.copy(id = id.toInt())
                    repository.generateDecisionRecommendations(savedDecision)
                }
            } catch (e: Exception) {
                Log.e("ViewModel", "Decision recommendation failed", e)
            } finally {
                _isAnalyzingDecision.value = false
            }
        }
    }

    fun deleteDecision(decision: DecisionImprovement) {
        viewModelScope.launch {
            repository.decisionImprovementDao.deleteDecision(decision)
        }
    }

    // --- CRUD: Habits & Practices Tracker ---
    fun addHabit(title: String, category: String, targetDaysPerWeek: Int) {
        viewModelScope.launch {
            val habit = HabitTracker(
                title = title,
                category = category,
                targetDaysPerWeek = targetDaysPerWeek
            )
            repository.habitTrackerDao.insertHabit(habit)
        }
    }

    fun completeHabit(habit: HabitTracker) {
        viewModelScope.launch {
            val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            val historyList = habit.completionHistory.split(",").filter { it.isNotBlank() }.toMutableList()
            
            if (historyList.contains(todayStr)) {
                // Already completed today, do nothing to prevent double counting
                return@launch
            }

            historyList.add(todayStr)
            val newCompletions = habit.totalCompletions + 1
            
            // Check streak
            var newStreak = habit.activeStreak
            val yesterdayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000)
            )
            
            if (historyList.contains(yesterdayStr) || habit.activeStreak == 0) {
                newStreak += 1
            } else {
                newStreak = 1 // reset streak to 1 if yesterday was missed
            }

            val updated = habit.copy(
                activeStreak = newStreak,
                totalCompletions = newCompletions,
                lastCompletedTimestamp = System.currentTimeMillis(),
                completionHistory = historyList.joinToString(",")
            )
            repository.habitTrackerDao.updateHabit(updated)
        }
    }

    fun deleteHabit(habit: HabitTracker) {
        viewModelScope.launch {
            repository.habitTrackerDao.deleteHabit(habit)
        }
    }

    // --- AI Knowledge Chat ---
    fun sendChatMessage(message: String) {
        if (message.isBlank()) return
        
        viewModelScope.launch {
            val currentList = _chatHistory.value.toMutableList()
            currentList.add("user" to message)
            _chatHistory.value = currentList
            _isChatLoading.value = true

            try {
                // Grounded query incorporating current DB state
                val response = repository.askGroundedKnowledgeAssistant(message, currentList)
                currentList.add("model" to response)
                _chatHistory.value = currentList
            } catch (e: Exception) {
                currentList.add("model" to "Error: ${e.message ?: "Could not reach AI."}")
                _chatHistory.value = currentList
            } finally {
                _isChatLoading.value = false
            }
        }
    }

    fun clearChat() {
        _chatHistory.value = listOf(
            "model" to "Chat history cleared! I am ready for fresh observations or decisions context. What shall we learn next?"
        )
    }

    // --- AI Learning Path Synthesis ---
    fun generatePersonalizedSummary() {
        viewModelScope.launch {
            _isSummaryLoading.value = true
            try {
                val summary = repository.generatePersonalizedWisdomSummary()
                _personalizedSummary.value = summary
            } catch (e: Exception) {
                _personalizedSummary.value = "Failed to generate personal learning path. Error: ${e.message}"
            } finally {
                _isSummaryLoading.value = false
            }
        }
    }

    // --- Double-Layer Text-To-Speech Playback ---
    fun speakText(text: String, context: Context) {
        stopSpeaking()
        _isSpeaking.value = true

        viewModelScope.launch {
            try {
                // Try premium Gemini 3.1 Flash TTS API
                val audioBytes = repository.run { GeminiApiClient.generateSpeech(text) }
                if (audioBytes != null) {
                    playAudioBytes(audioBytes, context)
                } else {
                    // Fallback to Native TTS if offline or Gemini key missing
                    speakNativeTts(text, context)
                }
            } catch (e: Exception) {
                Log.e("ViewModel", "TTS generation failed, attempting native fallback", e)
                speakNativeTts(text, context)
            }
        }
    }

    private suspend fun playAudioBytes(bytes: ByteArray, context: Context) = withContext(Dispatchers.IO) {
        try {
            val tempFile = File.createTempFile("gemini_tts_", ".mp3", context.cacheDir)
            tempFile.deleteOnExit()
            FileOutputStream(tempFile).use { fos ->
                fos.write(bytes)
            }

            withContext(Dispatchers.Main) {
                mediaPlayer = MediaPlayer().apply {
                    setDataSource(tempFile.absolutePath)
                    prepare()
                    setOnCompletionListener {
                        stopSpeaking()
                    }
                    setOnErrorListener { _, _, _ ->
                        stopSpeaking()
                        false
                    }
                    start()
                }
            }
        } catch (e: Exception) {
            Log.e("ViewModel", "Failed to play audio bytes", e)
            withContext(Dispatchers.Main) {
                speakNativeTts(text = "Fallback text-to-speech error", context)
            }
        }
    }

    private fun speakNativeTts(text: String, context: Context) {
        if (nativeTts == null) {
            nativeTts = TextToSpeech(context) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    isNativeTtsReady = true
                    nativeTts?.language = Locale.getDefault()
                    performNativeSpeak(text)
                } else {
                    _isSpeaking.value = false
                }
            }
        } else if (isNativeTtsReady) {
            performNativeSpeak(text)
        } else {
            _isSpeaking.value = false
        }
    }

    private fun performNativeSpeak(text: String) {
        nativeTts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "aw_tts")
        // Check periodic status to set isSpeaking to false on end
        viewModelScope.launch {
            while (nativeTts?.isSpeaking == true) {
                kotlinx.coroutines.delay(100)
            }
            _isSpeaking.value = false
        }
    }

    fun stopSpeaking() {
        try {
            mediaPlayer?.let {
                if (it.isPlaying) {
                    it.stop()
                }
                it.release()
            }
            mediaPlayer = null
        } catch (e: Exception) {
            Log.e("ViewModel", "Error stopping MediaPlayer", e)
        }

        try {
            nativeTts?.stop()
        } catch (e: Exception) {
            Log.e("ViewModel", "Error stopping Native TTS", e)
        }

        _isSpeaking.value = false
    }

    override fun onCleared() {
        super.onCleared()
        stopSpeaking()
        try {
            nativeTts?.shutdown()
        } catch (e: Exception) {
            // silent fail
        }
    }

    // --- Factory provider helper ---
    companion object {
        fun provideFactory(repository: ActionWisdomRepository): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ActionWisdomViewModel(repository) as T
            }
        }
    }
}
