package io.mustelidae.smoothcoatedotter.api.domain.sample.repository

import io.mustelidae.smoothcoatedotter.api.domain.sample.Sample
import org.springframework.data.jpa.repository.JpaRepository

interface SampleRepository : JpaRepository<Sample, Long> {
    fun findByName(name: String): Sample?
}
