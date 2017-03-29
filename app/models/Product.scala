package models

import models.dao.ProductDAO
import play.api.libs.concurrent.Akka
import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.Play.current

object Product {
  implicit val ProductWrites: Writes[Product] = (
    (JsPath \ "id").write[Int] and
    (JsPath \ "productName").write[String] and
    (JsPath \ "currentQuantity").write[Int]
    )(unlift(Product.unapply))

  def add(productName: String, currentQuantity: Int): Option[Long] = {
    val new_product = Product(-1, productName, currentQuantity)
    ProductDAO.create(new_product)
  }

  def exists(name: String): Boolean = {
    ProductDAO.getProductByName(name) match {
      case Some(product) => true
      case _ => false
    }
  }

  def getAll(): List[Product] =
    ProductDAO.getAll()

  def refill(prod_id:Int, quantity:Int): Int =
    ProductDAO.updateQuantity(prod_id, quantity)

  def buyProduct(username:String, prod_id: Int, quantity: Int): Boolean = {
    Transaction.add(Customer.getByUsername(username).get.id, prod_id, "BUY", quantity, 1) match {
      case Some(rows) =>
        ProductDAO.updateQuantity(prod_id, -quantity) compare 1 match {
        case 0 => true
        case _ => false
      }
      case _ => false
    }
  }

  def reserveProduct(username:String, prod_id: Int, quantity: Int): Boolean = {
    Transaction.add(Customer.getByUsername(username).get.id, prod_id, "RESERVE", quantity, 0) match {
      case Some(rows) =>
        ProductDAO.updateQuantity(prod_id, -quantity) compare 1 match {
          case 0 => true
          case _ => false
        }
      case _ => false
    }
  }
}

case class Product(id:Int, productName: String, currentQuantity: Int)
