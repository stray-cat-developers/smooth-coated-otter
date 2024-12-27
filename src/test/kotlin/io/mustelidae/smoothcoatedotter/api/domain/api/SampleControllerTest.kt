package io.mustelidae.smoothcoatedotter.api.domain.api

import io.kotest.matchers.longs.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.mustelidae.smoothcoatedotter.api.config.FlowTestSupport
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

    @Test
    fun create() {
        // Given
        val sampleControllerFlow = SampleControllerFlow(mockMvc)
        // When
        val reply = sampleControllerFlow.addSample("name", 1)
        // Then
        reply shouldBeGreaterThan 0

        val sample = sampleControllerFlow.findSample(reply)

        sample.id shouldBe reply
        sample.name shouldBe "name"
        sample.age shouldBe 1
    }
}
