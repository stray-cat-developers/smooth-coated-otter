package io.mustelidae.smoothcoatedotter.api.common

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.util.Assert
import java.util.Collections

@Schema(name = "SmootCoatedOtter.Common.Replies", description = "Http Json Response Base Format (Collection 형태의 리소스를 반환할 때 사용)")
open class Replies<T>(
    content: Iterable<T>,
) : Iterable<T> {
    @JsonIgnore
    private val collection: MutableCollection<T>?

    init {
        Assert.notNull(content, "Content must not be null!")

        this.collection = ArrayList()

        for (element in content) {
            this.collection.add(element)
        }
    }

    @JsonProperty("content")
    open fun getContent(): Collection<T> = Collections.unmodifiableCollection(collection!!)

    override fun iterator(): Iterator<T> = collection!!.iterator()

    override fun toString(): String = String.format("Resources { content: %s, %s }", getContent(), super.toString())
}

fun <T> List<T>.toReplies(): Replies<T> = Replies(this)
