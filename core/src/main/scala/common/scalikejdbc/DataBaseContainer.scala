package common.scalikejdbc

import javax.sql.DataSource

import com.typesafe.config.ConfigFactory
import net.ceedubs.ficus.Ficus._
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import scalikejdbc._

trait DataBaseContainer {
  def master: NamedDB
  def slave: NamedDB

  def getDataSource(dbSettingKey: String) = {
    val config = ConfigFactory.load

    val url = config.as[String](s"${dbSettingKey}.url")
    val user = config.as[String](s"${dbSettingKey}.user")
    val password = config.as[String](s"${dbSettingKey}.password")

    val hikariConfig: HikariConfig = new HikariConfig
    hikariConfig.setJdbcUrl(url)
    hikariConfig.setUsername(user)
    hikariConfig.setPassword(password)
    config
      .as[Map[String, Option[String]]](s"${dbSettingKey}.hikaricp")
      .foreach(property => {
        val (key, value) = property
        hikariConfig.addDataSourceProperty(key, value)
      })
    val ds = new HikariDataSource(hikariConfig)
    val dataSourceClassName = "com.mysql.jdbc.jdbc2.optional.MysqlDataSource"
    ds.setDataSourceClassName(dataSourceClassName)
    ds
  }
}

trait UsesDatabaseContainer {
  implicit val db: DataBaseContainer
}

object MySQLDB extends DataBaseContainer {
  val Master = 'master
  val Slave = 'slave
  Class.forName("com.mysql.jdbc.Driver")
  ConnectionPool.add(Master, new DataSourceConnectionPool(this.masterDataSource))
  ConnectionPool.add(Slave, new DataSourceConnectionPool(this.slaveDataSource))

  def masterDataSource: DataSource = {
    getDataSource("db_master")
  }

  def slaveDataSource: DataSource = {
    getDataSource("db_slave")
  }

  def master = NamedDB(Master)
  def slave = NamedDB(Slave)
}

trait MixinMySQLDB extends UsesDatabaseContainer {
  implicit val db: DataBaseContainer = MySQLDB
}
