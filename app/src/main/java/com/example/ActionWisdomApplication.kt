package com.example

import android.app.Application
import com.example.data.ActionWisdomRepository

class ActionWisdomApplication : Application() {
    lateinit var repository: ActionWisdomRepository
        private set

    override fun onCreate() {
        super.onCreate()
        repository = ActionWisdomRepository(this)
    }
}
