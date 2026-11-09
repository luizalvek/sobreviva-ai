package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Vereda", appName)
  }

  @Test
  fun `verify emergency intent detection on bleeding`() {
    val analysis = com.example.data.SurvivalKnowledgeBase.detectEmergencyIntent("estou sangrando muito no braço")
    assertEquals(true, analysis.isEmergency)
    assertEquals("HEMORRAGIA", analysis.detectedType)
  }

  @Test
  fun `verify normal guide prompt does not trigger emergency`() {
    val analysis = com.example.data.SurvivalKnowledgeBase.detectEmergencyIntent("Como filtrar água de rio?")
    assertEquals(false, analysis.isEmergency)
  }
}
