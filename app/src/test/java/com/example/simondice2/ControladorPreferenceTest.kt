
package com.example.simondice2

import android.content.Context
import android.content.SharedPreferences
import com.example.simondice2.data.Record
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class ControladorPreferenceTest {

    private val mockPrefs = mockk<SharedPreferences>(relaxed = true)
    private val mockEditor = mockk<SharedPreferences.Editor>(relaxed = true)
    private val mockContext = mockk<Context>(relaxed = true)

    @Before
    fun setUp() {
        every { mockContext.getSharedPreferences(any(), any()) } returns mockPrefs
        every { mockPrefs.edit() } returns mockEditor
    }

    @Test
    fun `obtenerRecord devuelve null si no hay record guardado`() {
        // Given
        every { mockPrefs.getInt(any(), any()) } returns 0 // Simula que no hay record

        // When
        val record = ControladorPreference.obtenerRecord(mockContext)

        // Then
        assertNull(record)
    }

    @Test
    fun `actualizar y obtenerRecord funciona correctamente`() {
        // Given
        val timestamp = System.currentTimeMillis()
        val nuevoRecord = Record(timestampMillis = timestamp, maxRound = 10)

        // When: se actualiza el récord
        ControladorPreference.actualizarRecord(mockContext, nuevoRecord)

        // Then: verificamos que se llamó a putInt y putLong con los valores correctos
        verify { mockEditor.putInt("record_round", 10) }
        verify { mockEditor.putLong("record_timestamp", timestamp) }
        verify { mockEditor.apply() }

        // Now, let's verify obtaining the record
        // Given
        every { mockPrefs.getInt("record_round", 0) } returns 10
        every { mockPrefs.getLong("record_timestamp", 0L) } returns timestamp

        // When
        val recordObtenido = ControladorPreference.obtenerRecord(mockContext)

        // Then
        assertNotNull(recordObtenido)
        assertEquals(10, recordObtenido?.maxRound)
        assertEquals(timestamp, recordObtenido?.timestampMillis)
    }

    @Test
    fun `borrarRecord elimina las preferencias`() {
        // When
        ControladorPreference.borrarRecord(mockContext)

        // Then
        verify { mockEditor.clear() }
        verify { mockEditor.apply() }
    }
}
