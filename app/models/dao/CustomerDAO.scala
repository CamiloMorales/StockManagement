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
          | INSERT INTO `Customer` (`user_name`,`first_name`,`last_name`)
          | VALUES ({userName},{firstName},{lastName});
        """.stripMargin).on(
          "userName" -> customer.userName,
          "firstName" -> customer.firstName,
          "lastName" -> customer.lastName
        ).executeInsert()
    }
  }

  def getAll(): List[Customer] = {
    DB.withConnection { implicit c =>
      val results = SQL(
        """
          | SELECT `id`,`user_name`,`first_name`,`last_name`
          | FROM `Customer`;
        """.stripMargin).apply()

      results.map { row =>
        Customer(row[Int]("id"), row[String]("user_name"),row[String]("first_name"),row[String]("last_name"))
      }.force.toList
    }
  }
}
