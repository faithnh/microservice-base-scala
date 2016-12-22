package com.example.eventapi.domain.service

import ai.x.play.json.Jsonx
import com.example.eventapi.domain.entities.EventEntity
import com.example.eventapi.domain.repository.UsingEventRepository
import com.example.eventapi.infrastructure.persistence.MixinMySQLEventRepository
import com.github.tototoshi.play.json.JsonNaming
import play.api.libs.json.Json

object EventSearchService extends EventSearchService with MixinMySQLEventRepository

trait EventSearchService extends UsingEventRepository {
  implicit def eventWrites = JsonNaming.snakecase[EventEntity](Jsonx.formatCaseClass[EventEntity])

  def search(eventId: Long) = {
    val eventEntity = eventRepository.find(eventId)

    Json.obj("result" -> eventEntity)
  }
}
