package common.slick

import slick.dbio.{DBIOAction, NoStream}
import slick.driver.MySQLDriver

import scala.concurrent.Future

case class DatabaseContainer(master: MySQLDriver.backend.Database, slave: MySQLDriver.backend.Database) {
  def forWrite: MySQLDriver.backend.Database = master
  private def forRead: MySQLDriver.backend.Database = slave
  def masterOnly = DatabaseContainer(master, master)

  // デフォルトはmasterに向けている
  final def run[R](dbio: DBIOAction[R, NoStream, Nothing]): Future[R] = forWrite.run(dbio)
}