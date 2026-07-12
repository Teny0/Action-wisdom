package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ActionJournalDao {
    @Query("SELECT * FROM action_journals ORDER BY timestamp DESC")
    fun getAllJournals(): Flow<List<ActionJournal>>

    @Query("SELECT * FROM action_journals WHERE id = :id LIMIT 1")
    suspend fun getJournalById(id: Int): ActionJournal?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJournal(journal: ActionJournal): Long

    @Update
    suspend fun updateJournal(journal: ActionJournal)

    @Delete
    suspend fun deleteJournal(journal: ActionJournal)

    @Query("SELECT * FROM action_journals WHERE title LIKE '%' || :query || '%' OR actionTaken LIKE '%' || :query || '%' OR lessonsLearned LIKE '%' || :query || '%' OR category LIKE '%' || :query || '%' OR tags LIKE '%' || :query || '%'")
    fun searchJournals(query: String): Flow<List<ActionJournal>>
}

@Dao
interface KnowledgeBuilderDao {
    @Query("SELECT * FROM knowledge_builders ORDER BY timestamp DESC")
    fun getAllKnowledge(): Flow<List<KnowledgeBuilder>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKnowledge(knowledge: KnowledgeBuilder): Long

    @Update
    suspend fun updateKnowledge(knowledge: KnowledgeBuilder)

    @Delete
    suspend fun deleteKnowledge(knowledge: KnowledgeBuilder)

    @Query("SELECT * FROM knowledge_builders WHERE title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%' OR type LIKE '%' || :query || '%' OR category LIKE '%' || :query || '%'")
    fun searchKnowledge(query: String): Flow<List<KnowledgeBuilder>>
}

@Dao
interface KnowHowLibraryDao {
    @Query("SELECT * FROM know_how_library ORDER BY timestamp DESC")
    fun getAllLibraryEntries(): Flow<List<KnowHowLibrary>>

    @Query("SELECT * FROM know_how_library WHERE topic = :topic ORDER BY timestamp DESC")
    fun getEntriesByTopic(topic: String): Flow<List<KnowHowLibrary>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: KnowHowLibrary): Long

    @Query("DELETE FROM know_how_library WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT COUNT(*) FROM know_how_library")
    suspend fun getCount(): Int
}

@Dao
interface RealWorldObservationDao {
    @Query("SELECT * FROM real_world_observations ORDER BY timestamp DESC")
    fun getAllObservations(): Flow<List<RealWorldObservation>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertObservation(observation: RealWorldObservation): Long

    @Update
    suspend fun updateObservation(observation: RealWorldObservation)

    @Delete
    suspend fun deleteObservation(observation: RealWorldObservation)

    @Query("SELECT * FROM real_world_observations WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%' OR category LIKE '%' || :query || '%'")
    fun searchObservations(query: String): Flow<List<RealWorldObservation>>
}

@Dao
interface DecisionImprovementDao {
    @Query("SELECT * FROM decision_improvements ORDER BY timestamp DESC")
    fun getAllDecisions(): Flow<List<DecisionImprovement>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDecision(decision: DecisionImprovement): Long

    @Update
    suspend fun updateDecision(decision: DecisionImprovement)

    @Delete
    suspend fun deleteDecision(decision: DecisionImprovement)
}

@Dao
interface HabitTrackerDao {
    @Query("SELECT * FROM habit_trackers ORDER BY timestamp DESC")
    fun getAllHabits(): Flow<List<HabitTracker>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: HabitTracker): Long

    @Update
    suspend fun updateHabit(habit: HabitTracker)

    @Delete
    suspend fun deleteHabit(habit: HabitTracker)
}
