package io.mustelidae.smoothcoatedotter.api.sender

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

/**
 * use block kit builder
 * @ref https://app.slack.com/block-kit-builder
 */
class BlockKit {
    @JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true,
    )
    @JsonSubTypes(
        JsonSubTypes.Type(value = Context::class, name = "context"),
        JsonSubTypes.Type(value = Header::class, name = "header"),
        JsonSubTypes.Type(value = Section::class, name = "section"),
        JsonSubTypes.Type(value = Divider::class, name = "divider"),
        JsonSubTypes.Type(value = Image::class, name = "checkboxes"),
        JsonSubTypes.Type(value = Text::class, name = "plain_text"),
        JsonSubTypes.Type(value = Text::class, name = "mrkdwn"),
        JsonSubTypes.Type(value = Button::class, name = "button"),
        JsonSubTypes.Type(value = Checkbox::class, name = "checkboxes"),
        JsonSubTypes.Type(value = Image::class, name = "image"),
    )
    interface Block

    /**
     * @ref https://api.slack.com/reference/block-kit#elements
     */
    interface BlockKitElement : Block

    /**
     * https://api.slack.com/reference/block-kit#objects
     */

    interface BlockKitObject : Block

    /**
     * @ref https://api.slack.com/reference/block-kit#blocks
     */
    interface BlockKitBlock : Block

    data class Context(
        /** Only images BlockElement and text CompositionObject are allowed.*/
        val elements: List<BlockKitObject>,
        val blockId: String? = null,
    ) : BlockKitBlock {
        val type = "context"
    }

    data class Header(
        val text: Text,
    ) : BlockKitBlock {
        val type: String = "header"
    }

    data class Section(
        val text: Text? = null,
        val blockId: String? = null,
        val fields: List<Text>? = null,
        val accessory: BlockKitElement? = null,
    ) : BlockKitBlock {
        val type = "section"
    }

    class Divider : BlockKitBlock {
        val type = "divider"
    }

    data class Button(
        val text: Text,
        val actionId: String,
        val url: String? = null,
        val value: String? = null,
        val style: Style? = null,
        val confirm: ConfirmationDialog? = null,
        val accessibilityLabel: Text? = null,
    ) : BlockKitElement {
        val type = "button"

        enum class Style {
            @JsonProperty("primary")
            PRIMARY,

            @JsonProperty("danger")
            DANGER,
        }
    }

    data class Checkbox(
        val actionId: String,
        val options: List<Option>,
        val initialOptions: List<Option>? = null,
        val confirm: ConfirmationDialog? = null,
        val focusOnLoad: Boolean? = null,
    ) : BlockKitElement {
        val type = "checkboxes"
    }

    data class Image(
        val imageUrl: String,
        val altText: String,
    ) : BlockKitElement,
        BlockKitObject {
        val type = "image"
    }

    data class Text(
        val type: Type,
        val text: String,
        val emoji: Boolean? = null,
        val verbatim: Boolean? = null,
    ) : BlockKitObject {
        enum class Type {
            @JsonProperty("plain_text")
            PLAIN_TEXT,

            @JsonProperty("mrkdwn")
            MRKDWN,
        }
    }

    data class ConfirmationDialog(
        val title: Text,
        val text: Text,
        val confirm: Text,
        val deny: Text,
        val style: Style? = null,
    ) : BlockKitObject {
        enum class Style {
            @JsonProperty("primary")
            PRIMARY,

            @JsonProperty("danger")
            DANGER,
        }
    }

    data class Option(
        val text: Text,
        val value: String,
    ) : BlockKitObject

    data class OptionGroup(
        val label: Text,
        val options: List<Option>,
    ) : BlockKitObject
}
