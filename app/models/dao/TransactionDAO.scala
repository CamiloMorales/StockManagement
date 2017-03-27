package models.dao

import anorm._
import models.Transaction
import play.api.db.DB
import play.api.Play.current


import models.Product

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

  def getAllByCustomer(customer_id:Int): List[Transaction] = {
    DB.withConnection { implicit c =>
      val results = SQL(
        """
          | SELECT `id`, `customer_id`, `product_id`, `trans_type`, `quantity`, `finished`
          | FROM `Trans`
          | WHERE `customer_id`={userId}
        """.stripMargin).apply()

      results.map { row =>
        Transaction(row[Int]("id"), row[Int]("customer_id"), row[Int]("product_id"), row[String]("trans_type"), row[Int]("quantity"), row[Int]("finished"))
      }.force.toList
    }
  }
}
