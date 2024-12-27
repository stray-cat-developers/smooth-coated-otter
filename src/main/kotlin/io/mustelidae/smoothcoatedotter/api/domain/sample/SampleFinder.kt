package io.mustelidae.smoothcoatedotter.api.domain.sample

import io.mustelidae.smoothcoatedotter.api.domain.sample.repository.SampleRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class SampleFinder(
    private val sampleRepository: SampleRepository,
) {
    fun findById(id: Long): Sample = sampleRepository.findByIdOrNull(id) ?: throw IllegalArgumentException("Sample not found")
}
