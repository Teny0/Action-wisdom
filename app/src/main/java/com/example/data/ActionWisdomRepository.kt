package com.example.data

import android.content.Context
import android.util.Log
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ActionWisdomRepository(private val context: Context) {
    private val db: AppDatabase = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        "action_wisdom_db"
    ).build()

    val actionJournalDao = db.actionJournalDao()
    val knowledgeBuilderDao = db.knowledgeBuilderDao()
    val knowHowLibraryDao = db.knowHowLibraryDao()
    val realWorldObservationDao = db.realWorldObservationDao()
    val decisionImprovementDao = db.decisionImprovementDao()
    val habitTrackerDao = db.habitTrackerDao()

    // Read all flows
    val allJournals: Flow<List<ActionJournal>> = actionJournalDao.getAllJournals()
    val allKnowledge: Flow<List<KnowledgeBuilder>> = knowledgeBuilderDao.getAllKnowledge()
    val allLibraryEntries: Flow<List<KnowHowLibrary>> = knowHowLibraryDao.getAllLibraryEntries()
    val allObservations: Flow<List<RealWorldObservation>> = realWorldObservationDao.getAllObservations()
    val allDecisions: Flow<List<DecisionImprovement>> = decisionImprovementDao.getAllDecisions()
    val allHabits: Flow<List<HabitTracker>> = habitTrackerDao.getAllHabits()

    init {
        // Prepopulate the Know-How Library if empty
        CoroutineScope(Dispatchers.IO).launch {
            try {
                if (knowHowLibraryDao.getCount() == 0) {
                    prepopulateLibrary()
                }
            } catch (e: Exception) {
                Log.e("Repository", "Failed to check or prepopulate library", e)
            }
        }
    }

    private suspend fun prepopulateLibrary() {
        val defaultEntries = listOf(
            KnowHowLibrary(
                topic = "Communication",
                title = "Active Listening Mastery",
                processSteps = "1. Maintain open posture & eye contact\n2. Minimize internal monologue & focus completely on speaker\n3. Use vocal tracking ('Uh-huh', 'I see')\n4. Mirror back their core point ('It sounds like you feel...')\n5. Ask clarifying, open-ended questions",
                commonMistakes = "- Listening with the intent to reply rather than understand\n- Interrupting to share personal anecdotes\n- Dismissing feelings by immediately jumping to solutions\n- Looking at phone or shifting eyes away",
                tips = "- Keep your mouth closed while they speak\n- Pause for 2 seconds before responding to show reflection\n- Look for micro-expressions (eyebrow raise, tight lips) for emotional clues",
                realExamples = "Scenario: A teammate is frustrated with a deadline. Instead of saying 'Well, we have no choice', you mirror: 'It sounds like you're feeling overwhelmed by the scope and lack of support. What part can we deprioritize?'",
                resources = "Book: 'Never Split the Difference' by Chris Voss\nArticle: 'Active Listening' on Harvard Business Review",
                isUserCreated = false
            ),
            KnowHowLibrary(
                topic = "Problem Solving",
                title = "First Principles Thinking",
                processSteps = "1. Identify and define your current assumptions ('We need X to do Y')\n2. Breakdown the problem into its fundamental truths ('What is absolutely factual here?')\n3. Reconstruct a solution from scratch based on fundamental facts\n4. Validate the feasibility of the new solution\n5. Formulate a prototype or pilot test",
                commonMistakes = "- Reasoning by analogy ('It's how everyone else does it')\n- Overcomplicating basic inputs\n- Accepting legacy constraints as unalterable truths",
                tips = "- Keep asking 'Why?' until you reach raw physical or structural facts\n- Focus on cost of materials rather than cost of finished products\n- Sketch layouts to visualize core functional relations",
                realExamples = "Example: Elon Musk reducing rocket costs. Instead of buying an expensive $65M completed rocket, he bought raw aerospace-grade materials for 2% of the price and built SpaceX's rocket from scratch.",
                resources = "Article: 'First Principles' by Farnam Street\nVideo: Elon Musk interview on Physics reasoning",
                isUserCreated = false
            ),
            KnowHowLibrary(
                topic = "Productivity",
                title = "Deep Work Protocol",
                processSteps = "1. Define your high-concentration tasks ahead of time\n2. Select a dedicated workspace with zero distractions\n3. Lock your phone in another room or use a blocking app\n4. Set a timer (90-minute blocks work best for human focus)\n5. Conduct a clean shutdown ritual (empty inbox, plan tomorrow)",
                commonMistakes = "- Checking emails/chat in the middle of deep blocks\n- Expecting to do 8 hours of deep work (4 hours is the elite limit)\n- Starting without a specific output target",
                tips = "- Use noise-canceling headphones with repetitive instrumental music\n- Work first thing in the morning when willpower is peak\n- Track hours on a calendar to build streak satisfaction",
                realExamples = "A software developer writes core algorithms between 8 AM and 10 AM, keeping chat apps completely closed, resulting in 3x cleaner code.",
                resources = "Book: 'Deep Work' by Cal Newport\nPodcast: 'Huberman Lab' on Focus Protocols",
                isUserCreated = false
            ),
            KnowHowLibrary(
                topic = "Business",
                title = "Lean Customer Discovery",
                processSteps = "1. Define hypotheses about customer pain points\n2. Prepare open-ended, past-behavior questions\n3. Interview 10-15 prospects face-to-face or on video\n4. Ask about how they currently solve the problem\n5. Synthesize findings to decide on pivot or proceed",
                commonMistakes = "- Pitching your product instead of listening to their pain\n- Asking hypothetical future questions ('Would you buy this?')\n- Ignoring negative feedback because it hurts your ego",
                tips = "- If you are talking more than 20% of the interview, you are failing\n- Look for emotional intensity (frustration, anger) in their voice\n- Ask: 'What does this problem cost you in time or money?'",
                realExamples = "Instead of building a complex $10K meal prep app, the founder asked 15 professionals how they choose lunch. They discovered the issue wasn't recipes, but speed of delivery, so they pivoted to a local delivery list.",
                resources = "Book: 'The Mom Test' by Rob Fitzpatrick\nGuide: 'Lean Startup' by Eric Ries",
                isUserCreated = false
            ),
            KnowHowLibrary(
                topic = "Relationships",
                title = "Nonviolent Communication (NVC)",
                processSteps = "1. State the objective observation without judgment ('When I see...')\n2. Express your raw feeling clearly ('I feel...')\n3. State the universal human need that is unfulfilled ('Because I need...')\n4. Make a concrete, actionable request ('Would you be willing to...?')",
                commonMistakes = "- Mixing evaluation/judgment into observation ('You are lazy')\n- Expressing thoughts instead of feelings ('I feel like you don't care' is a thought, not a feeling)\n- Making demands instead of requests",
                tips = "- A request is only a request if the other person can safely say 'No'\n- Focus on 'I' statements rather than accusatory 'You' statements",
                realExamples = "Instead of 'You are always late and don't respect my time', say: 'When I wait for 20 minutes (Observation), I feel anxious and unvalued (Feeling), because I need reliability and connection (Need). Would you be willing to text me if you're running late? (Request)'",
                resources = "Book: 'Nonviolent Communication' by Marshall Rosenberg",
                isUserCreated = false
            ),
            KnowHowLibrary(
                topic = "Finance",
                title = "Dollar-Cost Averaging (DCA)",
                processSteps = "1. Determine a comfortable monthly saving amount\n2. Select a low-cost, broad-market index fund (e.g., S&P 500 ETF)\n3. Set up an automatic monthly recurring buy on a reliable broker\n4. Commit to never pausing or editing the buy based on market news\n5. Rebalance portfolio once a year",
                commonMistakes = "- Trying to time the market bottoms and tops\n- Panic selling when the market crashes\n- Checking portfolio value multiple times a day",
                tips = "- Treat the automatic investment as a non-negotiable tax on yourself\n- Market drops are actually 'sales' where your fixed dollar buys more shares",
                realExamples = "An investor buys $200 of SPY on the 1st of every month. Whether the market is high or low, their average cost over 10 years beats 90% of active traders.",
                resources = "Article: 'Dollar Cost Averaging' on Investopedia\nBook: 'The Little Book of Common Sense Investing' by John Bogle",
                isUserCreated = false
            )
        )

        for (entry in defaultEntries) {
            knowHowLibraryDao.insertEntry(entry)
        }
        Log.i("Repository", "Prepopulated library with ${defaultEntries.size} entries successfully.")
    }

    // --- AI Helper Features ---

    /**
     * AI Analysis of a Real World Observation to identify patterns and cause-effect relationships.
     */
    suspend fun analyzeObservationPattern(observation: RealWorldObservation): String = withContext(Dispatchers.IO) {
        val prompt = """
            Please analyze this real-world observation and identify hidden patterns, cause-and-effect mechanics, and potential behavioral/system insights.
            
            Title: ${observation.title}
            Category: ${observation.category}
            Description/Observation: ${observation.description}
            User's perceived Cause and Effect: ${observation.causeAndEffect}
            
            Provide a highly structured, objective summary (under 250 words) outlining:
            1. Underlying Pattern Detected (e.g., psychological bias, market cycle, habit loop, systemic feedback)
            2. Deeper Cause-and-Effect Analysis
            3. Actionable Rule of Thumb or Principle the user can apply in their own life.
        """.trimIndent()

        val systemInstruction = "You are a wisdom-focused mental model expert and social/behavioral scientist. Your analysis must be concise, deeply insightful, avoiding generic fluff, and grounded in real-world logic."
        
        val result = GeminiApiClient.generateContent(prompt, systemInstruction, useSearch = false)
        
        // Update the database with the AI's summary
        val updatedObs = observation.copy(aiPatternSummary = result)
        realWorldObservationDao.updateObservation(updatedObs)
        
        return@withContext result
    }

    /**
     * AI Review of a Decision to generate recommendations based on past choices and possible outcomes.
     */
    suspend fun generateDecisionRecommendations(decision: DecisionImprovement): String = withContext(Dispatchers.IO) {
        val prompt = """
            Analyze the following decision situation and provide practical, objective recommendations to optimize decision quality.
            
            Current Situation / Context: ${decision.context}
            Related Past Decisions: ${decision.pastDecisionsCompared}
            Possible Future Outcomes/Risks: ${decision.possibleFutureOutcomes}
            
            Deliver a concise evaluation outlining:
            - Key cognitive blindspots or biases that might be influencing this choice.
            - 2-3 Actionable suggestions to mitigate future risks.
            - A quantitative rating/index recommendation of the decision strategy (e.g., high or low risk, structured or impulsive).
        """.trimIndent()

        val systemInstruction = "You are a professional strategist, decision analyst, and game theorist. Deliver objective, sharp, and highly practical recommendations without filler words."
        
        val result = GeminiApiClient.generateContent(prompt, systemInstruction, useSearch = false)
        
        val updatedDecision = decision.copy(aiRecommendations = result)
        decisionImprovementDao.updateDecision(updatedDecision)
        
        return@withContext result
    }

    /**
     * Interactive Chat Assistant grounded in the user's stored database.
     * Incorporates Google Search Grounding to keep answers accurate and up-to-date!
     */
    suspend fun askGroundedKnowledgeAssistant(
        question: String,
        chatHistory: List<Pair<String, String>>
    ): String = withContext(Dispatchers.IO) {
        // Retrieve context data from database to build the ground context
        val journals = allJournals.first().take(15)
        val principles = allKnowledge.first().take(15)
        val observations = allObservations.first().take(15)

        val contextBuilder = StringBuilder()
        contextBuilder.append("USER'S STORED PERSONAL KNOWLEDGE BASE CONTEXT:\n\n")

        if (journals.isNotEmpty()) {
            contextBuilder.append("--- RECENT ACTION JOURNALS & REFLECTIONS ---\n")
            journals.forEach { j ->
                contextBuilder.append("- Action: ${j.title} (${j.category}). Action taken: ${j.actionTaken}. Outcome: ${j.actualOutcome}. Lessons: ${j.lessonsLearned}. Alternatives considered: ${j.alternatives}. Would change: ${j.wouldChooseDifferently ?: "N/A"}.\n")
            }
            contextBuilder.append("\n")
        }

        if (principles.isNotEmpty()) {
            contextBuilder.append("--- PERSONAL PRINCIPLES & RULES OF THUMB ---\n")
            principles.forEach { p ->
                contextBuilder.append("- ${p.title} (${p.type} - ${p.category}): ${p.content}\n")
            }
            contextBuilder.append("\n")
        }

        if (observations.isNotEmpty()) {
            contextBuilder.append("--- REAL WORLD OBSERVATIONS & BEHAVIOR ---\n")
            observations.forEach { o ->
                contextBuilder.append("- Observation: ${o.title} (${o.category}). Description: ${o.description}. Cause/Effect: ${o.causeAndEffect}. Pattern Summary: ${o.aiPatternSummary ?: "None"}\n")
            }
            contextBuilder.append("\n")
        }

        val formattedHistory = StringBuilder()
        chatHistory.forEach { (sender, msg) ->
            formattedHistory.append("$sender: $msg\n")
        }

        val prompt = """
            $contextBuilder
            
            CHAT HISTORY:
            $formattedHistory
            
            USER'S QUESTION:
            $question
            
            Based on the user's personal context above, answer their question. If they ask about their own habits, journals, or principles, draw heavily from their stored database context.
            If they ask about general concepts, how to improve communication, business, technology, or need general wisdom, combine their stored context with your extensive knowledge base.
            You should use the Google Search grounding tool (enabled) to search for any specific facts, books, or current methodologies mentioned, ensuring the advice remains cutting edge and highly accurate.
            
            Keep your response structured, encouraging, and highly practical. Use bold headers where appropriate, and end with 1-2 recommended next steps or custom action items.
        """.trimIndent()

        val systemInstruction = "You are 'Action Wisdom AI', the user's dedicated personal growth advisor, cognitive coach, and personal knowledge archivist. You help them synthesize their own life observations, extract deep principles, and adopt optimal habits."

        return@withContext GeminiApiClient.generateContent(
            prompt = prompt,
            systemInstruction = systemInstruction,
            useSearch = true // ENABLE SEARCH GROUNDING!
        )
    }

    /**
     * Synthesizes user journals and observations to generate a personalized learning path or summary of lessons.
     */
    suspend fun generatePersonalizedWisdomSummary(): String = withContext(Dispatchers.IO) {
        val journals = allJournals.first().take(20)
        val principles = allKnowledge.first().take(20)

        if (journals.isEmpty() && principles.isEmpty()) {
            return@withContext "You haven't recorded enough actions or principles yet! Add some action journals, choice reflections, or rules of thumb, and Action Wisdom AI will generate a personalized synthesis of your decision patterns."
        }

        val contextBuilder = StringBuilder()
        contextBuilder.append("SUMMARY OF USER DATA:\n")
        journals.forEach { j ->
            contextBuilder.append("- Action: ${j.title}. Motivation: ${j.motivation}. Outcome: ${j.actualOutcome}. Lesson: ${j.lessonsLearned}. Would change choice: ${j.wouldChooseDifferently ?: "N/A"}\n")
        }
        principles.forEach { p ->
            contextBuilder.append("- Principle: ${p.title} (${p.type}): ${p.content}\n")
        }

        val prompt = """
            Analyze the user's action history, choice patterns, and principles saved:
            
            $contextBuilder
            
            Provide a personalized wisdom audit (under 350 words). Outline:
            1. Core Growth Areas: What topics or categories does the user focus on?
            2. Recurring Decision Patterns: Are they voluntary? Do they regret choices, or learn well?
            3. Synthesized Life Principles: Group 2-3 of their principles into a cohesive personal framework.
            4. Recommended Learning Path: 3 custom actionable habits or choices to make in the next 14 days to improve their decision-making.
        """.trimIndent()

        val systemInstruction = "You are a senior behavioral coach and patterns analyst. Synthesize this data with absolute clarity, highlighting strength areas and key vulnerabilities in the user's decision strategies."

        return@withContext GeminiApiClient.generateContent(prompt, systemInstruction, useSearch = false)
    }
}
