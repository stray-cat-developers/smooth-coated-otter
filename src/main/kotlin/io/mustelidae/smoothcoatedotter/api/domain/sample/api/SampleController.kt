package io.mustelidae.smoothcoatedotter.api.domain.sample.api

import io.mustelidae.smoothcoatedotter.api.common.Reply
import io.mustelidae.smoothcoatedotter.api.common.toReply
import io.mustelidae.smoothcoatedotter.api.domain.sample.SampleFinder
import io.mustelidae.smoothcoatedotter.api.domain.sample.SampleInteraction
import io.mustelidae.smoothcoatedotter.api.lock.EnableUserLock
import io.mustelidae.smoothcoatedotter.api.permission.RoleHeader
import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/sample", "v1/gateway/sample")
class SampleController(
    private val sampleInteraction: SampleInteraction,
    private val sampleFinder: SampleFinder,
) {
    @Operation(operationId = "Boilerplate.Sample.helloWorld", description = "Hello World")
    @EnableUserLock
    @GetMapping
    fun helloWorld(
        @RequestHeader(RoleHeader.XUser.KEY) userId: Long,
    ): Reply<String> =
        "Hello World"
            .toReply()

    @Operation(operationId = "Boilerplate.Sample.Request", description = "add sample")
    @PostMapping
    fun sample(
        @RequestBody request: SampleResources.Request.Sample,
    ): Reply<Long> {
        val id = sampleInteraction.create(request.name, request.age)
        return id.toReply()
    }

    @Operation(operationId = "Boilerplate.Sample.Find", description = "find sample")
    @GetMapping("{id}")
    fun findSample(
        @PathVariable id: Long,
    ): Reply<SampleResources.Reply.Sample> {
        val sample = sampleFinder.findById(id)
        return SampleResources.Reply.Sample
            .of(sample)
            .toReply()
    }
}
