package models.dao

import anorm._
import models.Customer
import play.api.db.DB
import play.api.Play.current

import models.Customer

object CustomerDAO {
  def create(customer: Customer): Option[Long] = {
    DB.withConnection { implicit c =>
      SQL(
        """
          | INSERT INTO `Customer` (`user_name`)
          | VALUES ({userName});
        """.stripMargin).on(
          "userName" -> customer.userName
        ).executeInsert()
    }
  }

  def getAll(): List[Customer] = {
    DB.withConnection { implicit c =>
      val results = SQL(
        """
          | SELECT `id`,`user_name`
          | FROM `Customer`;
        """.stripMargin).apply()

      results.map { row =>
        Customer(row[Int]("id"), row[String]("user_name"))
      }.force.toList
    }
  }

  def getByUsername(username:String): Option[Customer] = {

    val parser = {
      SqlParser.get[Int]("id") ~
        SqlParser.get[String]("user_name") map {
        case id~username => Customer(id, username)
      }
    }

    DB.withConnection { implicit c =>
      SQL(
        """
          | SELECT `id`,`user_name`
          | FROM `Customer`
          | WHERE `user_name`= {user_name};
        """.stripMargin).on(
        "user_name" -> username
      ).as(parser.singleOpt)
    }
  }

}
