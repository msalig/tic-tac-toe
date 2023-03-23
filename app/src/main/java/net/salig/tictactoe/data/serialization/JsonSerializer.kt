package net.salig.tictactoe.data.serialization

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import net.salig.tictactoe.data.serialization.model.Coordinates
import net.salig.tictactoe.data.serialization.model.Data
import net.salig.tictactoe.data.serialization.model.Username

class JsonSerializer : JsonContentPolymorphicSerializer<Data>(Data::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out Data> =
        when {
            "name" in element.jsonObject -> Username.serializer()
            else -> Coordinates.serializer()
        }
}