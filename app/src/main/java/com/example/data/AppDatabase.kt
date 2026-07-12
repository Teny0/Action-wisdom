package com.example.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        ActionJournal::class,
        KnowledgeBuilder::class,
        KnowHowLibrary::class,
        RealWorldObservation::class,
        DecisionImprovement::class,
        HabitTracker::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun actionJournalDao(): ActionJournalDao
    abstract fun knowledgeBuilderDao(): KnowledgeBuilderDao
    abstract fun knowHowLibraryDao(): KnowHowLibraryDao
    abstract fun realWorldObservationDao(): RealWorldObservationDao
    abstract fun decisionImprovementDao(): DecisionImprovementDao
    abstract fun habitTrackerDao(): HabitTrackerDao
}
