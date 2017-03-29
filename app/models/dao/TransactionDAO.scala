package models.dao

import anorm._
import models.{CustomTransaction, Product, Transaction}
import play.api.db.DB
import play.api.Play.current

object TransactionDAO {
  def create(transaction: Transaction): Option[Long] = {
    DB.withConnection { implicit c =>
      SQL(
        """
          | INSERT INTO `Trans` (`customer_id`, `product_id`, `trans_type`, `quantity`, `finished`)
          | VALUES ({customer_id}, {product_id}, {trans_type}, {quantity}, {finished});
        """.stripMargin).on(
        "customer_id" -> transaction.customer_id,
        "product_id" -> transaction.product_id,
        "trans_type" -> transaction.trans_type,
        "quantity" -> transaction.quantity,
        "finished" -> transaction.finished
      ).executeInsert()
    }
  }

  def getAll(): List[Transaction] = {
    DB.withConnection { implicit c =>
      val results = SQL(
        """
          | SELECT `id`, `customer_id`, `product_id`, `trans_type`, `quantity`, `finished`
          | FROM `Trans`;
        """.stripMargin).apply()

      results.map { row =>
        Transaction(row[Int]("id"), row[Int]("customer_id"), row[Int]("product_id"), row[String]("trans_type"), row[Int]("quantity"), row[Int]("finished"))
      }.force.toList
    }
  }

  def getAllByCustomer(username:String): List[CustomTransaction] = {
    DB.withConnection { implicit c =>
      val results = SQL(
        """
          | SELECT t.`id`, c.`user_name`, p.`name`, t.`trans_type`, t.`quantity`, t.`finished`
          | FROM `trans` t JOIN `customer` c ON t.`customer_id` = c.`id`
          |                JOIN `product` p ON t.`product_id` = p.`id`
          | WHERE c.`user_name`={username}
        """.stripMargin).on(
        "username" -> username
      ).apply()

      results.map { row =>
        CustomTransaction(row[Int]("trans.id"), row[String]("customer.user_name"), row[String]("product.name"), row[String]("trans.trans_type"), row[Int]("trans.quantity"), row[Int]("trans.finished"))
      }.force.toList
    }
  }

  def updateFinished(transaction_id:Int): Int = {
    DB.withConnection { implicit c =>
      SQL(
        """
          | UPDATE `trans`
          | SET `finished` = 1
          | WHERE `id`= {transaction_id};
        """.stripMargin).on(
        "transaction_id" -> transaction_id
      ).executeUpdate()
    }
  }
}
