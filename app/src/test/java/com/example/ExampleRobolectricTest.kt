package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.ActionJournal
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun readStringFromContext() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Action Wisdom", appName)
  }

  @Test
  fun verifyActionJournalModelCreation() {
    val journal = ActionJournal(
        title = "Strategic Planning Session",
        actionTaken = "Outlined Q3 product roadmap with division heads",
        motivation = "Align team goals and milestones",
        expectedOutcome = "Unified timeline and scope definition",
        actualOutcome = "Approved roadmap with clear ownership",
        lessonsLearned = "Face-to-face feedback accelerates resolution loops",
        tags = "strategy, management",
        category = "Business",
        alternatives = "Email proposals (slower resolution)",
        choiceReason = "Direct collaboration mitigates misalignment risks",
        isVoluntary = true,
        valuesInfluenced = "alignment, velocity",
        wouldChooseDifferently = false,
        wouldChooseDifferentlyReason = "Highly effective resolution speed"
    )
    assertNotNull(journal)
    assertEquals("Strategic Planning Session", journal.title)
    assertEquals("Business", journal.category)
    assertEquals(true, journal.isVoluntary)
  }
}
