package com.example.eventapi.infrastructure.persistence

import com.example.eventapi.domain.entities.EventEntity
import com.example.eventapi.domain.repository.{EventRepository, UsingEventRepository}
import common.slick.MySQLDatabase
import slick.driver.MySQLDriver.api._
import shapeless.{::, Generic, HList, HNil}
import slickless._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class EventTable(tag: Tag) extends Table[EventEntity](tag, "event") {
  def id = column[Long]("id")
  def name = column[String]("name")
  def content = column[String]("content")

  def * = (id :: name :: content :: HNil).mappedWith(Generic[EventEntity])
}

object MySQLEventRepository extends EventRepository {
  lazy val db = MySQLDatabase.db

  override def find(eventId: Long): Option[EventEntity] = {
    lazy val event = TableQuery[EventTable]

    val eventQuery = event.filter(_.id === eventId).result.headOption

    Await.result(db.run(eventQuery), Duration.Inf)
  }
}

trait MixinMySQLEventRepository extends UsingEventRepository {
  override val eventRepository: EventRepository = MySQLEventRepository
}
