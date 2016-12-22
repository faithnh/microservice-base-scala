package com.example.eventapi.domain.entities

import com.github.tototoshi.play.json.JsonNaming
import ai.x.play.json.Jsonx

case class EventEntity(id: Long, name: String, content: String)