package io.mustelidae.otter.sumatrana.api.domain

import io.kotest.matchers.shouldBe
import io.mustelidae.otter.sumatrana.api.config.FlowTestSupport
import org.junit.jupiter.api.Test

internal class SampleControllerTest : FlowTestSupport() {

    @Test
    fun helloWorld() {
        // Given
        val sampleControllerFlow = SampleControllerFlow(mockMvc)
        // When
        val reply = sampleControllerFlow.helloWorld()
        // Then
        reply shouldBe "Hello World"
    }
}
