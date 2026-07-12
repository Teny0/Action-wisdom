package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "action_journals")
data class ActionJournal(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val actionTaken: String,
    val motivation: String,
    val expectedOutcome: String,
    val actualOutcome: String,
    val lessonsLearned: String,
    val tags: String, // Comma-separated tags
    val category: String, // e.g. Communication, Business, Tech, Health, Productivity, Finance, Relationships, Problem Solving
    val timestamp: Long = System.currentTimeMillis(),
    
    // Freedom of Choice Reflection
    val alternatives: String,
    val choiceReason: String,
    val isVoluntary: Boolean,
    val valuesInfluenced: String,
    val wouldChooseDifferently: Boolean?, // null, true, false
    val wouldChooseDifferentlyReason: String
)

@Entity(tableName = "knowledge_builders")
data class KnowledgeBuilder(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val type: String, // "PRINCIPLE", "RULE_OF_THUMB", "BEST_PRACTICE", "MISTAKE_TO_AVOID", "FRAMEWORK", "CHECKLIST"
    val content: String,
    val tags: String,
    val category: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "know_how_library")
data class KnowHowLibrary(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val topic: String, // e.g., Communication, Business, Technology, Health, Productivity, Finance, Relationships, Problem Solving
    val title: String,
    val processSteps: String, // Raw text or JSON
    val commonMistakes: String,
    val tips: String,
    val realExamples: String,
    val resources: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isUserCreated: Boolean = false
)

@Entity(tableName = "real_world_observations")
data class RealWorldObservation(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val causeAndEffect: String,
    val category: String, // Human Behavior, Market Trends, Social, Nature, Cause & Effect
    val aiPatternSummary: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "decision_improvements")
data class DecisionImprovement(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val context: String,
    val pastDecisionsCompared: String,
    val possibleFutureOutcomes: String,
    val aiRecommendations: String? = null,
    val rating: Int = 0, // 0 to 5 score
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "habit_trackers")
data class HabitTracker(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val category: String,
    val targetDaysPerWeek: Int = 5,
    val activeStreak: Int = 0,
    val totalCompletions: Int = 0,
    val lastCompletedTimestamp: Long = 0,
    val completionHistory: String = "", // Comma-separated list of "YYYY-MM-DD"
    val timestamp: Long = System.currentTimeMillis()
)
