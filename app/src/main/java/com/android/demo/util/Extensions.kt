package com.android.demo.util

import android.content.Context
import android.provider.Settings
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.google.gson.JsonObject
import java.text.DecimalFormat

fun Float.toFormat(): String = DecimalFormat("###.00").format(this)

fun Int.to2Digit(): String = (if (this < 10) "0$this" else this.toString())

fun Any.jsonString(): String = if (this == JsonNull.INSTANCE) "" else (this as JsonElement).asString

fun JsonObject.jsonString(key: String): String = if (this.get(key) != null && JsonNull.INSTANCE != this.get(key)) this.get(key).asString else ""

fun JsonObject.jsonInt(key: String): Int = if (this.get(key) != null && JsonNull.INSTANCE != this.get(key)) this.get(key).asInt else 0

fun JsonObject.jsonFloat(key: String): Float = if (this.get(key) != null && JsonNull.INSTANCE != this.get(key)) this.get(key).asFloat else 0.0f

fun JsonElement.jsonArray(key: String): JsonArray = if (this is JsonObject && this.asJsonObject.get(key) != null && this.asJsonObject.get(key) != JsonNull.INSTANCE) this.asJsonObject.get(key).asJsonArray else JsonArray()

fun JsonElement.jsonString(key: String): String = if (this is JsonObject && this.asJsonObject.get(key) != null && this.asJsonObject.get(key) != JsonNull.INSTANCE) this.asJsonObject.get(key).asString else ""

fun JsonElement.jsonString(obj: String, key: String): String = if (this is JsonObject && this.asJsonObject.get(obj) != null && this.asJsonObject.get(obj) != JsonNull.INSTANCE) this.asJsonObject.get(obj).jsonString(key) else ""

fun JsonElement.jsonInt(obj: String, key: String): Int = if (this is JsonObject && this.asJsonObject.get(obj) != null && this.asJsonObject.get(obj) != JsonNull.INSTANCE) this.asJsonObject.get(obj).jsonInt(key) else 0

fun JsonElement.jsonFloat(obj: String, key: String): Float = if (this is JsonObject && this.asJsonObject.get(obj) != null && this.asJsonObject.get(obj) != JsonNull.INSTANCE) this.asJsonObject.get(obj).jsonFloat(key) else 0F

fun JsonElement.jsonInt(key: String): Int = if (this is JsonObject && this.asJsonObject.get(key) != null && this.asJsonObject.get(key) != JsonNull.INSTANCE && this.asJsonObject.get(key).jsonString().isNotEmpty()) this.asJsonObject.get(key).asInt else 0

fun JsonElement.jsonFloat(key: String): Float = if (this is JsonObject && this.asJsonObject.get(key) != null && this.asJsonObject.get(key) != JsonNull.INSTANCE && this.asJsonObject.get(key).jsonString().isNotEmpty()) this.asJsonObject.get(key).asFloat else 0f

fun Context.getDeviceId() = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID)
