package io.mustelidae.smoothcoatedotter.api.domain.sample.api

import io.mustelidae.smoothcoatedotter.api.domain.sample.Sample

class SampleResources {
    class Request {
        data class Sample(
            val name: String,
            val age: Int,
        )
    }

    class Reply {
        data class Sample(
            val id: Long,
            val name: String,
            val age: Int,
        ) {
            companion object {
                fun of(sample: io.mustelidae.smoothcoatedotter.api.domain.sample.Sample): Sample =
                    sample.run {
                        Sample(id!!, name, age)
                    }
            }
        }
    }
}
