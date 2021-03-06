package models.dao

import anorm._
import models.Product
import play.api.db.DB
import play.api.Play.current

object ProductDAO {
  def create(product: Product): Option[Long] = {
    DB.withConnection { implicit c =>
      SQL(
        """
          | INSERT INTO `product` (`name`, `quantity`)
          | VALUES ({productName}, {currentQuantity});
        """.stripMargin).on(
          "productName" -> product.productName,
          "currentQuantity" -> product.currentQuantity
        ).executeInsert()
    }
  }

  def getAll(): List[Product] = {
    DB.withConnection { implicit c =>
      val results = SQL(
        """
          | SELECT `id`, `name`, `quantity`
          | FROM `Product`;
        """.stripMargin).apply()

      results.map { row =>
        Product(row[Int]("id"), row[String]("name"), row[Int]("quantity"))
      }.force.toList
    }
  }

  def updateQuantity(prod_id:Int, quantity:Int): Int = {
    DB.withConnection { implicit c =>
      SQL(
        """
          | UPDATE `Product`
          | SET `quantity` = `quantity` + {quant}
          | WHERE `id`= {prod_id};
        """.stripMargin).on(
        "prod_id" -> prod_id,
        "quant" -> quantity
        ).executeUpdate()
    }
  }

  def getProductByName(name:String): Option[Product] = {

    val parser = {
      SqlParser.get[Int]("id") ~
        SqlParser.get[String]("name") ~
        SqlParser.get[Int]("quantity") map {
        case id~username~quantity => Product(id, username, quantity)
      }
    }

    DB.withConnection { implicit c =>
      SQL(
        """
          | SELECT `id`,`name`,`quantity`
          | FROM `Product`
          | WHERE `name`= {name};
        """.stripMargin).on(
        "name" -> name
      ).as(parser.singleOpt)
    }
  }
}
