package io.mustelidae.smoothcoatedotter.api.domain.api

import io.mustelidae.smoothcoatedotter.api.common.Reply
import io.mustelidae.smoothcoatedotter.api.domain.sample.api.SampleController
import io.mustelidae.smoothcoatedotter.api.domain.sample.api.SampleResources
import io.mustelidae.smoothcoatedotter.api.permission.RoleHeader
import io.mustelidae.smoothcoatedotter.utils.fromJson
import io.mustelidae.smoothcoatedotter.utils.toJson
import org.springframework.hateoas.server.mvc.linkTo
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

internal class SampleControllerFlow(
    private val mockMvc: MockMvc,
) {
    fun helloWorld(): String {
        val uri = linkTo<SampleController> { helloWorld(1234) }.toUri()

        return mockMvc
            .get(uri) {
                contentType = MediaType.APPLICATION_JSON
                header(RoleHeader.XUser.KEY, 1234)
            }.andExpect {
                status { is2xxSuccessful() }
            }.andReturn()
            .response
            .contentAsString
            .fromJson<Reply<String>>()
            .content!!
    }

    fun addSample(
        name: String,
        age: Int,
    ): Long {
        val request = SampleResources.Request.Sample(name, age)
        val uri = linkTo<SampleController> { sample(request) }.toUri()

        return mockMvc
            .post(uri) {
                contentType = MediaType.APPLICATION_JSON
                header(RoleHeader.XUser.KEY, 1234)
                content = request.toJson()
            }.andExpect {
                status { is2xxSuccessful() }
            }.andReturn()
            .response
            .contentAsString
            .fromJson<Reply<Long>>()
            .content!!
    }

    fun findSample(id: Long): SampleResources.Reply.Sample {
        val uri = linkTo<SampleController> { findSample(id) }.toUri()

        return mockMvc
            .get(uri) {
                contentType = MediaType.APPLICATION_JSON
            }.andExpect {
                status { is2xxSuccessful() }
            }.andReturn()
            .response
            .contentAsString
            .fromJson<Reply<SampleResources.Reply.Sample>>()
            .content!!
    }
}
