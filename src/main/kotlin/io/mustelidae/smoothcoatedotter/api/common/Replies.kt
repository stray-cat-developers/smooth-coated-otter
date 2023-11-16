package io.mustelidae.smoothcoatedotter.api.common

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.xml.bind.annotation.XmlAnyElement
import jakarta.xml.bind.annotation.XmlElementWrapper
import org.springframework.util.Assert
import java.util.Collections

open class Replies<T>
constructor(content: Iterable<T>) : Iterable<T> {

    private val content: MutableCollection<T>?

    @Suppress("unused")
    constructor() : this(ArrayList<T>())

    init {
        Assert.notNull(content, "Content must not be null!")

        this.content = ArrayList()

        for (element in content) {
            this.content.add(element)
        }
    }

    @XmlAnyElement
    @XmlElementWrapper
    @JsonProperty("content")
    open fun getContent(): Collection<T> {
        return Collections.unmodifiableCollection(content!!)
    }

    override fun iterator(): Iterator<T> {
        return content!!.iterator()
    }

    override fun toString(): String {
        return String.format("Resources { content: %s, %s }", getContent(), super.toString())
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result += if (content == null) 0 else 17 * content.hashCode()

        return result
    }
}

fun <T> List<T>.toReplies(): Replies<T> = Replies(this)
