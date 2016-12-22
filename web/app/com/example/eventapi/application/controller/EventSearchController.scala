package com.example.eventapi.application.controller

import com.example.eventapi.domain.service.EventSearchService
import play.api.mvc._

class EventSearchController extends Controller {
  lazy val eventSearchService = EventSearchService

  def index() = Action { implicit request =>
    Ok("OK")
  }

  def search(eventId: Long) = Action { implicit request =>
    Ok(eventSearchService.search(eventId))
  }
}
