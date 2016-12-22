package common.slick

import slick.driver.MySQLDriver

/**
  * 以下のtraitを継承すれば、特定のDBを使用することができるようになる
  * usage:
  * class SampleDAO extends Database {
  * …
  * val userQuery = sql"select user_id, name from user".as[(Int, String)]
  * // dbが定義されるので、それを利用することができる
  * Await.result(db.run(userQuery), Duration.Inf)
  * }
 */
trait Database {
  implicit val db = MySQLDatabase.db
}

object MySQLDatabase {
  val master: MySQLDriver.backend.Database = MySQLDriver.api.Database.forConfig("db_master")
  val slave: MySQLDriver.backend.Database = MySQLDriver.api.Database.forConfig("db_slave")
  val db = DatabaseContainer(master, slave)
}