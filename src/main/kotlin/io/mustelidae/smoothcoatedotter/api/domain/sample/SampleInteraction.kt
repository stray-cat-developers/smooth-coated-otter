package io.mustelidae.smoothcoatedotter.api.domain.sample

import io.mustelidae.smoothcoatedotter.api.domain.sample.repository.SampleRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class SampleInteraction(
    private val sampleRepository: SampleRepository,
) {
    fun create(
        name: String,
        age: Int,
    ): Long = sampleRepository.save(Sample(name = name, age = age)).id!!
}
