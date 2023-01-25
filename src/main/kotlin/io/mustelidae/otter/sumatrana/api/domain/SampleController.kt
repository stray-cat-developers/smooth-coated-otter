package io.mustelidae.otter.sumatrana.api.domain

import io.mustelidae.otter.sumatrana.api.common.Reply
import io.mustelidae.otter.sumatrana.api.common.toReply
import io.mustelidae.otter.sumatrana.api.lock.EnableUserLock
import io.mustelidae.otter.sumatrana.api.permission.RoleHeader
import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/sample", "v1/gateway/sample")
class SampleController {

    @Operation(operationId = "Boilerplate.Sample.helloWorld", description = "Hello World")
    @EnableUserLock
    @GetMapping
    fun helloWorld(
        @RequestHeader(RoleHeader.XUser.KEY) userId: Long
    ): Reply<String> {
        return "Hello World"
            .toReply()
    }
}
