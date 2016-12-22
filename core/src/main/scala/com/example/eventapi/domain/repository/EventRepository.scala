package com.example.eventapi.domain.repository

import com.example.eventapi.domain.entities.EventEntity

trait EventRepository {
  def find(eventId: Long): Option[EventEntity]
}

trait UsingEventRepository {
  val eventRepository: EventRepository
}
