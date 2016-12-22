import common.slick.Database
import slick.codegen.SourceCodeGenerator
import slick.driver.JdbcProfile

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

object CodeGenMySQL extends Database {

  def main(args: Array[String]) {
    val driver: JdbcProfile = slick.driver.MySQLDriver
    val modelAction = driver.createModel(Some(driver.defaultTables))
    val model = Await.result(db.master.run(modelAction), Duration.Inf)

    val codeGenFuture = new SourceCodeGenerator(model) {
      override def code =
        "import com.github.tototoshi.slick.MySQLJodaSupport._\n" + "import org.joda.time.DateTime\n" + super.code

      override def Table = new Table(_) {
        override def Column = new Column(_) {
          override def rawType = model.tpe match {
            case "java.sql.Timestamp" => "DateTime" // kill j.s.Timestamp
            case "java.sql.Date" => "DateTime" // kill j.s.Timestamp
            case _ => {
              super.rawType
            }
          }
        }
      }
    }

    codeGenFuture.writeToFile(
      "slick.driver.MySQLDriver",
      "core/src/main/scala/",
      "com.example.eventapi.infrastructure.persistence",
      "MySQLTablesForSlick",
      "MySQLTablesForSlick.scala"
    )
  }
}
