# Action Wisdom 🧠🌱

**Action Wisdom** is a modern, offline-first Android application designed to help individuals transform their everyday actions, experiences, and free choices into practical knowledge, wisdom, and real-world skills. 

Using structured reflection models, local database tracking, and state-of-the-art server-side Gemini API analysis, the app encourages students, entrepreneurs, and self-learners to consciously choose actions, reflect on outcomes, identify patterns, and construct a personalized knowledge base that improves strategic decision-making over time.

---

## 🚀 Key Features

### 1. Action Journal & Freedom of Choice Reflection
- **Continuous Timeline of Learning:** Record decisions with their underlying motivation, expected outcomes, actual outcomes, lessons learned, and category tags.
- **Autonomy Reflection:** Analyze alternative choices, voluntariness of action, and core values that influenced decisions. Evaluates regrets dynamically to support strategic growth.

### 2. Knowledge Builder & Principles Grid
- **Personal Knowledge base:** Catalog personal heuristics, rules of thumb, frameworks, best practices, checklists, and mistakes to avoid.
- **Searchable Database:** Filter, categorize, and query principles using a high-performance offline search index.

### 3. Interactive Know-How Library
- **Prepopulated Guides:** Access curated step-by-step corporate, personal development, and academic processes (e.g., Active Listening, High-Stakes Negotiation, Strategic Cost Auditing).
- **Custom Playbook Recorder:** Record and expand personalized steps, common pitfalls, and tips.

### 4. Real-World Observation Feed & Pattern Detector
- **System Models:** Record environmental, market, or human behavior observations.
- **Gemini Pattern Analysis:** Consult **Gemini 1.5 Flash** to analyze cause-and-effect patterns, reveal hidden blindspots, and trace systemic models.

### 5. Decision Strategy Builder
- **Strategic Game Theory:** Input high-stakes decision contexts (e.g., job changes, financial investments), outline related past experiences, and evaluate future risk boundaries.
- **AI Recommendation Engine:** Generates strategic feedback, game-theoretic options, and mitigation procedures.

### 6. Habits & Practices Tracker
- **Routine Practices:** Register and check off daily exercises (e.g., Deep Work, HIIT).
- **Custom Canvas Visualization:** Features native Android `Canvas` donut charts for experience distributions and vertical bar charts for habit streak completions.

### 7. AI Grounded Knowledge Assistant Chat
- **Deep Conversational Context:** Chat with an AI assistant that has grounded visibility of your entire offline journal logs, principles, and database records.

---

## 🛠️ Tech Stack & Architecture

- **Language:** 100% Kotlin
- **UI Framework:** Jetpack Compose (Material Design 3)
- **Local Database:** Room Database with KSP (Kotlin Symbol Processing) code generation
- **Architecture:** MVVM (Model-View-ViewModel) with Clean Repository pattern
- **Asynchronous Operations:** Kotlin Coroutines and flow-based reactive state-sharing
- **AI Engine:** Gemini REST API Client
  - **Text Generation & Google Search Grounding:** `gemini-3.5-flash` with Google Search tool grounding
  - **Premium Text-To-Speech (TTS):** `gemini-3.1-flash-tts-preview`

---

## 🧠 Dual-Layer Text-To-Speech (TTS) Engine

To deliver a premium, resilient auditory experience, Action Wisdom implements a custom **double-layer fallback TTS engine**:
1. **Primary Layer (Gemini Premium TTS):** Calls the `gemini-3.1-flash-tts-preview` REST API endpoint to generate high-fidelity MP3 voice responses and plays them natively using Android's `MediaPlayer`.
2. **Secondary Layer (Native Android Fallback):** If the device is offline or the Gemini API key is not configured, the app automatically falls back to Android's native offline `android.speech.tts.TextToSpeech` engine seamlessly.

---

## 📂 Project Structure

```
/app/src/main/
├── AndroidManifest.xml
├── java/com/example/
│   ├── ActionWisdomApplication.kt          # Application class, registers Repository singleton
│   ├── MainActivity.kt                      # Main Activity, entry point loading the app
│   ├── data/
│   │   ├── Entities.kt                      # Room database entities
│   │   ├── Daos.kt                          # Room DAOs with reactive flow queries
│   │   ├── AppDatabase.kt                   # Room database declaration
│   │   ├── GeminiApiClient.kt               # REST API client with TTS & Grounding
│   │   └── ActionWisdomRepository.kt        # Repository bridging room and Gemini
│   └── ui/
│       ├── ActionWisdomApp.kt               # Main application layout, scaffolds, dialogs
│       ├── ActionWisdomViewModel.kt         # Master state controller & calculations
│       └── theme/
│           ├── Color.kt                     # Teal Slate & Indigo color palette
│           ├── Theme.kt                     # Material 3 theme configurations
│           └── Type.kt                      # Material typography
```

---

## 💻 Build & Run Instructions

### 1. Add API Keys
The application reads the Gemini API Key from the local secure environment using `BuildConfig`. Enter your Gemini API key inside the AI Studio Secrets panel.

### 2. Gradle Command-Line Actions
Run the following commands inside the repository root:
- **Build APK:** `gradle assembleDebug`
- **Run Tests:** `gradle :app:testDebugUnitTest`
- **Verify Screenshot Tests:** `gradle :app:verifyRoborazziDebug`
